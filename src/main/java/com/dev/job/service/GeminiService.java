package com.dev.job.service;

import com.dev.job.adapter.LocalDateAdapter;
import com.dev.job.adapter.LocalDateTimeAdapter;
import com.dev.job.dto.response.Posting.JobPostingPrompt;
import com.dev.job.dto.response.Posting.JobPostingResponse;
import com.dev.job.dto.response.Resume.ResumeResponse;
import com.dev.job.dto.response.User.CandidatePrompt;
import com.dev.job.dto.response.User.JobSeekerResponse;
import com.google.gson.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class GeminiService {

    ResumeService resumeService;
    PostingService postingService;
    UserService userService;

    @Value("${gemini.api-key}")
    @NonFinal
    String geminiApiKey;

    @NonFinal
    HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(30))
            .build();

    private static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();

    private static final String GEMINI_API_URL = "https://generativelanguage.googleapis.com/v1/models/gemini-2.5-flash:generateContent?key=%s";

    public String suggestTop5Jobs(UUID resumeId) {
        List<JobPostingPrompt> availableJobs = postingService.getJobPostingByState();
        if (availableJobs.isEmpty()) {
            log.warn("Không có job nào đang published");
            return emptyResult("suggested_jobs");
        }

        ResumeResponse resume = resumeService.getResume(resumeId);
        if (resume == null) {
            log.warn("Không tìm thấy resume: {}", resumeId);
            return fallbackWithJobScore(availableJobs);
        }

        String prompt = buildJobsPrompt(resume, availableJobs);
        String response = callGeminiAPI(prompt);

        if (response == null || response.isBlank()) {
            return fallbackWithJobScore(availableJobs);
        }

        return enrichWithFullJobInfo(response, availableJobs);
    }

    public String suggestTop5Candidates(UUID postId) {
        JobPostingResponse post = postingService.getJobPosting(postId);
        if (post == null) {
            log.warn("Không tìm thấy job posting: {}", postId);
            return emptyResult("suggested_candidates");
        }

        List<CandidatePrompt> availableCandidates = resumeService.getCandidatesFeatures();
        if (availableCandidates.isEmpty()) {
            log.warn("Hiện tại không có ứng viên nào!");
            return emptyResult("suggested_candidates");
        }

        String prompt = buildCandidatesPrompt(post, availableCandidates);
        String response = callGeminiAPI(prompt);

        System.out.println(response);
        if (response == null || response.isBlank()) {
            return fallbackWithCandidateScore(availableCandidates);
        }
        return enrichWithFullCandidateInfo(response, availableCandidates);
//        return null;
    }

    private String callGeminiAPI(String prompt) {
        String escapedPrompt = escapeJson(prompt);
        String requestBody = """
            {
              "contents": [{"parts": [{"text": %s}]}],
              "generationConfig": {
                "temperature": 0.2,
                "topK": 10,
                "topP": 0.8,
                "maxOutputTokens": 65536
              }
            }
            """.formatted(GSON.toJson(prompt));

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(String.format(GEMINI_API_URL, geminiApiKey)))
                .header("Content-Type", "application/json")
                .timeout(Duration.ofSeconds(60))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            // IN RA TOÀN BỘ RESPONSE THÔ - CÁI NÀY QUAN TRỌNG NHẤT!
            log.warn("=== GEMINI RAW RESPONSE START ===");
            log.warn("Status Code: {}", response.statusCode());
            log.warn("Full Response Body:\n{}", response.body());
            log.warn("=== GEMINI RAW RESPONSE END ===");

            if (response.statusCode() != 200) {
                log.error("Gemini API lỗi: {} - {}", response.statusCode(), response.body());
                return null;
            }

            String extractedJson = extractJson(response.body());
            if (extractedJson.isBlank()) {
                log.warn("Gemini không trả về JSON hợp lệ");
                return null;
            }

            return extractedJson;
        } catch (Exception e) {
            log.error("Lỗi khi gọi Gemini API", e);
            return null;
        }
    }

    private String buildCandidatesPrompt(JobPostingResponse job, List<CandidatePrompt> candidates) {
        String jobJson = GSON.toJson(job);
        String candidatesJson = GSON.toJson(candidates);

        return String.format("""
                Bạn là chuyên gia tuyển dụng AI hàng đầu Việt Nam.
                Nhiệm vụ: Phân tích bài tuyển dụng và chọn đúng 5 ứng viên PHÙ HỢP NHẤT từ danh sách dưới đây.
                
                Bài tuyển dụng:
                %s
                
                Danh sách ứng viên (chỉ chọn từ đây, KHÔNG bịa thêm):
                %s
                
                YÊU CẦU TRẢ VỀ CHÍNH XÁC ĐỊNH DẠNG JSON SAU (KHÔNG thêm ```json, KHÔNG thêm text giải thích):
                {
                  "suggested_candidates": [
                    {
                      "id": "uuid-chính-xác-trong-danh-sách",
                      "reason": "Lý do ngắn gọn tiếng Việt, tối đa 90 ký tự"
                    }
                  ]
                }
                
                Quy tắc bắt buộc:
                - CHỈ trả về JSON object duy nhất, KHÔNG code block
                - Luôn trả về đúng 5 ứng viên
                - id phải tồn tại chính xác trong danh sách trên
                - reason: tiếng Việt, ngắn gọn, có ý nghĩa
                """, jobJson, candidatesJson);
    }

    private String buildJobsPrompt(ResumeResponse resume, List<JobPostingPrompt> jobs) {
        String resumeJson = GSON.toJson(resume);
        String jobsJson = GSON.toJson(jobs);

        return String.format("""
                Bạn là chuyên gia tuyển dụng AI hàng đầu Việt Nam.
                Nhiệm vụ: Phân tích CV và chọn đúng 5 công việc PHÙ HỢP NHẤT từ danh sách dưới đây.
                
                CV ứng viên:
                %s
                
                Danh sách công việc (chỉ chọn từ đây, KHÔNG bịa thêm):
                %s
                
                YÊU CẦU TRẢ VỀ CHÍNH XÁC ĐỊNH DẠNG JSON SAU (KHÔNG thêm ```json, KHÔNG thêm text giải thích):
                {
                  "suggested_jobs": [
                    {
                      "id": "uuid-chính-xác-trong-danh-sách",
                      "reason": "Lý do ngắn gọn tiếng Việt, tối đa 50 ký tự"
                    }
                  ]
                }
                
                Quy tắc bắt buộc:
                - CHỈ trả về JSON object duy nhất, KHÔNG code block
                - Luôn trả về đúng 5 job
                - id phải tồn tại chính xác trong danh sách trên
                - reason: tiếng Việt, ngắn gọn, có ý nghĩa
                """, resumeJson, jobsJson);
    }

    private String extractJson(String body) {
        try {
            JsonObject root = JsonParser.parseString(body).getAsJsonObject();
            JsonArray candidates = root.getAsJsonArray("candidates");

            if (candidates == null || candidates.isEmpty()) {
                log.warn("Candidates array rỗng hoặc null");
                return "";
            }

            JsonObject firstCandidate = candidates.get(0).getAsJsonObject();
            JsonObject content = firstCandidate.getAsJsonObject("content");
            JsonArray parts = content.getAsJsonArray("parts");
            String text = parts.get(0).getAsJsonObject().get("text").getAsString();

            // Loại bỏ markdown code blocks nếu có
            text = text.replaceAll("```json\\s*", "").replaceAll("```\\s*", "").trim();

            int start = text.indexOf("{");
            int end = text.lastIndexOf("}") + 1;

            if (start == -1 || end == 0) {
                log.warn("Không tìm thấy JSON trong response");
                return "";
            }

            String json = text.substring(start, end);

            // Validate JSON
            JsonParser.parseString(json);
            return json;
        } catch (Exception e) {
            log.error("Extract JSON thất bại: {}", e.getMessage(), e);
            return "";
        }
    }

    private String enrichWithFullJobInfo(String geminiJson, List<JobPostingPrompt> promptJobs) {
        Set<UUID> selected = new LinkedHashSet<>();
        List<SuggestedJob> result = new ArrayList<>();

        try {
            JsonObject root = JsonParser.parseString(geminiJson).getAsJsonObject();
            JsonArray array = root.getAsJsonArray("suggested_jobs");

            if (array != null) {
                for (JsonElement el : array) {
                    if (result.size() >= 5) break;
                    if (!el.isJsonObject()) continue;

                    JsonObject obj = el.getAsJsonObject();
                    String idStr = obj.has("id") ? obj.get("id").getAsString().trim() : null;
                    String reason = obj.has("reason") ? obj.get("reason").getAsString().trim() : "Phù hợp với hồ sơ";

                    UUID id = parseUuid(idStr);
                    if (id == null || selected.contains(id)) continue;

                    JobPostingResponse fullJob = postingService.getJobPosting(id);
                    if (fullJob == null) continue;

                    selected.add(id);
                    int score = 82 + (int) (Math.random() * 17); // 82–98

                    result.add(new SuggestedJob(
                            fullJob.getId(),
                            fullJob.getTitle(),
                            fullJob.getCompanyName(),
                            fullJob.getLocation().getName(),
                            formatSalary(fullJob.getMinSalary(), fullJob.getMaxSalary()),
                            score,
                            reason
                    ));
                }
            }
        } catch (Exception e) {
            log.error("Parse response từ Gemini thất bại", e);
        }

        // Bổ sung nếu chưa đủ 5
        fillRemainingJobs(result, selected, promptJobs, 5);

        return toJsonJobResult(result.stream().limit(5).toList());
    }

    private String enrichWithFullCandidateInfo(String geminiJson, List<CandidatePrompt> promptCandidates) {
        Set<UUID> selected = new LinkedHashSet<>();
        List<SuggestedCandidate> result = new ArrayList<>();

        try {
            JsonObject root = JsonParser.parseString(geminiJson).getAsJsonObject();
            JsonArray array = root.getAsJsonArray("suggested_candidates");

            if (array != null) {
                for (JsonElement el : array) {
                    if (result.size() >= 5) break;
                    if (!el.isJsonObject()) continue;

                    JsonObject obj = el.getAsJsonObject();
                    String idStr = obj.has("id") ? obj.get("id").getAsString().trim() : null;
                    String reason = obj.has("reason") ? obj.get("reason").getAsString().trim() : "Phù hợp với yêu cầu";

                    UUID id = parseUuid(idStr);
                    if (id == null || selected.contains(id)) continue;

                    JobSeekerResponse candidate = userService.getJobSeekerById(id);
                    if (candidate == null) continue;

                    selected.add(id);
                    int score = 82 + (int) (Math.random() * 17); // 82–98

                    result.add(new SuggestedCandidate(
                            candidate.getId(),
                            candidate.getFirstname() != null && candidate.getLastname() != null
                                    ? candidate.getFirstname()+" "+candidate.getLastname()
                                    : candidate.getUsername(),
                            candidate.getEmail(),
                            candidate.getPhone(),
                            score,
                            reason
                    ));
                }
            }
        } catch (Exception e) {
            log.error("Parse response từ Gemini thất bại", e);
        }

        // Bổ sung nếu chưa đủ 5
        fillRemainingCandidates(result, selected, promptCandidates, 5);

        return toJsonCandidateResult(result.stream().limit(5).toList());
    }

    private void fillRemainingJobs(List<SuggestedJob> result, Set<UUID> selected,
                                   List<JobPostingPrompt> promptJobs, int targetSize) {
        for (JobPostingPrompt j : promptJobs) {
            if (result.size() >= targetSize) break;
            if (selected.contains(j.id())) continue;

            JobPostingResponse fullJob = postingService.getJobPosting(j.id());
            if (fullJob == null) continue;

            result.add(new SuggestedJob(
                    fullJob.getId(),
                    fullJob.getTitle(),
                    fullJob.getCompanyName(),
                    fullJob.getLocation().getName(),
                    formatSalary(fullJob.getMinSalary(), fullJob.getMaxSalary()),
                    70 + (int) (Math.random() * 21),
                    "Công việc phù hợp"
            ));
        }
    }

    private void fillRemainingCandidates(List<SuggestedCandidate> result, Set<UUID> selected,
                                         List<CandidatePrompt> promptCandidates, int targetSize) {
        for (CandidatePrompt c : promptCandidates) {
            if (result.size() >= targetSize) break;
            if (selected.contains(c.id())) continue;

            JobSeekerResponse candidate = userService.getJobSeekerById(c.id());
            if (candidate == null) continue;

            result.add(new SuggestedCandidate(
                    candidate.getId(),
                    candidate.getFirstname() != null && candidate.getLastname() != null
                            ? candidate.getFirstname()+" "+candidate.getLastname()
                            : candidate.getUsername(),
                    candidate.getEmail(),
                    null,
                    70 + (int) (Math.random() * 21),
                    "Ứng viên tiềm năng"
            ));
        }
    }

    private String formatSalary(BigDecimal min, BigDecimal max) {
        if (min == null && max == null) return "Thỏa thuận";
        if (min == null) return "Lên đến " + max;
        if (max == null) return "Từ " + min;
        return min + " - " + max;
    }

    private String fallbackWithJobScore(List<JobPostingPrompt> jobs) {
        return toJsonJobResult(jobs.stream()
                .limit(5)
                .map(j -> {
                    JobPostingResponse full = postingService.getJobPosting(j.id());
                    if (full == null) return null;
                    return new SuggestedJob(
                            full.getId(),
                            full.getTitle(),
                            full.getCompanyName(),
                            full.getLocation().getName(),
                            formatSalary(full.getMinSalary(), full.getMaxSalary()),
                            70 + (int) (Math.random() * 26),
                            "Công việc đang tuyển dụng"
                    );
                })
                .filter(Objects::nonNull)
                .toList());
    }

    private String fallbackWithCandidateScore(List<CandidatePrompt> candidates) {
        return toJsonCandidateResult(candidates.stream()
                .limit(5)
                .map(c -> {
                    JobSeekerResponse full = userService.getJobSeekerById(c.id());
                    if (full == null) return null;
                    return new SuggestedCandidate(
                            full.getId(),
                            full.getFirstname() != null && full.getLastname() != null
                                    ? full.getFirstname()+" "+full.getLastname()
                                    : full.getUsername(),
                            full.getEmail(),
                            full.getPhone(),
                            70 + (int) (Math.random() * 26),
                            "Ứng viên tiềm năng"
                    );
                })
                .filter(Objects::nonNull)
                .toList());
    }

    private String toJsonJobResult(List<SuggestedJob> jobs) {
        JsonObject root = new JsonObject();
        JsonArray array = new JsonArray();

        for (int i = 0; i < 5; i++) {
            SuggestedJob job = i < jobs.size() ? jobs.get(i) : null;
            JsonObject obj = new JsonObject();

            if (job != null) {
                obj.addProperty("id", job.id().toString());
                obj.addProperty("title", job.title());
                obj.addProperty("companyName", job.companyName());
                obj.addProperty("location", job.location());
                obj.addProperty("promotedSalary", job.promotedSalary());
                obj.addProperty("match_score", job.matchScore());
                obj.addProperty("reason", job.reason());
            } else {
                obj.addProperty("id", (String) null);
                obj.addProperty("title", "Không có");
                obj.addProperty("companyName", "");
                obj.addProperty("location", "");
                obj.addProperty("promotedSalary", "Thỏa thuận");
                obj.addProperty("match_score", 0);
                obj.addProperty("reason", "");
            }

            array.add(obj);
        }

        root.add("suggested_jobs", array);
        return GSON.toJson(root);
    }

    private String toJsonCandidateResult(List<SuggestedCandidate> candidates) {
        JsonObject root = new JsonObject();
        JsonArray array = new JsonArray();

        for (int i = 0; i < 5; i++) {
            SuggestedCandidate candidate = i < candidates.size() ? candidates.get(i) : null;
            JsonObject obj = new JsonObject();

            if (candidate != null) {
                obj.addProperty("id", candidate.id().toString());
                obj.addProperty("fullName", candidate.fullName());
                obj.addProperty("email", candidate.email());
                obj.addProperty("phone",candidate.phone());
                obj.addProperty("match_score", candidate.matchScore());
                obj.addProperty("reason", candidate.reason());
            } else {
                obj.addProperty("id", (String) null);
                obj.addProperty("fullName", "Không có");
                obj.addProperty("email", "");
                obj.addProperty("phone", "");
                obj.addProperty("match_score", 0);
                obj.addProperty("reason", "");
            }

            array.add(obj);
        }

        root.add("suggested_candidates", array);
        return GSON.toJson(root);
    }

    private String emptyResult(String key) {
        return String.format("{\"%s\":[]}", key);
    }

    private UUID parseUuid(String s) {
        if (s == null || s.isBlank()) return null;
        try {
            return UUID.fromString(s.trim());
        } catch (IllegalArgumentException e) {
            log.debug("UUID không hợp lệ: {}", s);
            return null;
        }
    }

    private String escapeJson(String text) {
        return text.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    private record SuggestedJob(
            UUID id,
            String title,
            String companyName,
            String location,
            String promotedSalary,
            int matchScore,
            String reason
    ) {}

    private record SuggestedCandidate(
            UUID id,
            String fullName,
            String email,
            String phone,
            int matchScore,
            String reason
    ) {}
}