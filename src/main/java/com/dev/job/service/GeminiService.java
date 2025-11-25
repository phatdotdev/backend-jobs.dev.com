package com.dev.job.service;

import com.dev.job.entity.posting.JobPosting;
import com.dev.job.entity.posting.PostState;
import com.dev.job.repository.Posting.JobPostingRepository;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GeminiService {

    JobPostingRepository jobPostingRepository;

    @Value("${gemini.api-key}")
    @NonFinal
    String geminiApiKey;

    private final HttpClient httpClient = HttpClient.newHttpClient();

    public String suggestTop5Jobs(String resumeText) {

        List<JobPosting> allJobs = jobPostingRepository.findByState(PostState.PUBLISHED);

        if (allJobs == null || allJobs.isEmpty() || resumeText == null || resumeText.isBlank()) {
            return "{\"suggested_jobs\":[]}";
        }

        StringBuilder jobsList = new StringBuilder();
        for (int i = 0; i < allJobs.size(); i++) {
            JobPosting job = allJobs.get(i);
            String title = job.getTitle() != null ? job.getTitle().trim() : "Không có tiêu đề";
            String id = job.getId() != null ? job.getId().toString() : "unknown";
            jobsList.append(String.format("%d. [ID: %s] %s\n", i + 1, id, title));
        }

        String prompt = """
                Bạn là chuyên gia tuyển dụng AI hàng đầu.
                Nhiệm vụ: Chọn đúng 5 công việc PHÙ HỢP NHẤT với hồ sơ ứng viên từ danh sách dưới đây.
                
                Hồ sơ ứng viên:
                %s
                
                Danh sách công việc (chỉ được chọn từ đây):
                %s
                
                Trả về đúng định dạng JSON sau, không thêm bất kỳ text nào khác:
                {
                  "suggested_jobs": [
                    {"id": "uuid-1", "title": "Tên công việc 1"},
                    {"id": "uuid-2", "title": "Tên công việc 2"},
                    {"id": "uuid-3", "title": "Tên công việc 3"},
                    {"id": "uuid-4", "title": "Tên công việc 4"},
                    {"id": "uuid-5", "title": "Tên công việc 5"}
                  ]
                }
                Quan trọng: Giữ nguyên đúng ID và title từ danh sách!
                """.formatted(resumeText.trim(), jobsList.toString());

        String requestBody = """
                {
                  "contents": [{"parts": [{"text": "%s"}]}],
                  "generationConfig": {
                    "temperature": 0.3,
                    "topK": 40,
                    "topP": 0.95,
                    "maxOutputTokens": 1024
                  },
                  "safetySettings": [
                    {"category": "HARM_CATEGORY_DANGEROUS_CONTENT", "threshold": "BLOCK_NONE"}
                  ]
                }
                """.formatted(prompt.replace("\"", "\\\""));

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://generativelanguage.googleapis.com/v1/models/gemini-1.5-flash:generateContent?key=" + geminiApiKey))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                return fallbackTop5(allJobs);
            }

            String jsonText = extractJson(response.body());
            if (jsonText.isBlank()) {
                return fallbackTop5(allJobs);
            }

            JsonObject root = JsonParser.parseString(jsonText).getAsJsonObject();
            JsonArray suggestedArray = root.has("suggested_jobs")
                    ? root.getAsJsonArray("suggested_jobs")
                    : new JsonArray();

            List<SuggestedJob> result = new ArrayList<>();

            for (JsonElement el : suggestedArray) {
                if (!el.isJsonObject()) continue;
                JsonObject obj = el.getAsJsonObject();

                String idStr = obj.has("id") ? obj.get("id").getAsString().trim() : null;
                String title = obj.has("title") ? obj.get("title").getAsString().trim() : null;

                if (idStr != null) {
                    UUID uuid = parseUuid(idStr);
                    if (uuid != null) {
                        JobPosting matched = allJobs.stream()
                                .filter(j -> uuid.equals(j.getId()))
                                .findFirst()
                                .orElse(null);

                        if (matched != null) {
                            result.add(new SuggestedJob(matched.getId(), matched.getTitle()));
                        }
                    }
                }
            }

            // Bổ sung nếu thiếu
            for (JobPosting job : allJobs) {
                if (result.size() >= 5) break;
                if (result.stream().noneMatch(j -> j.id.equals(job.getId()))) {
                    result.add(new SuggestedJob(job.getId(), job.getTitle()));
                }
            }

            return toJson(result);

        } catch (Exception e) {
            e.printStackTrace();
            return fallbackTop5(allJobs);
        }
    }

    private UUID parseUuid(String id) {
        try {
            return UUID.fromString(id);
        } catch (Exception e) {
            return null;
        }
    }

    private String extractJson(String text) {
        if (text == null) return "";
        int start = text.indexOf("{");
        int end = text.lastIndexOf("}") + 1;
        if (start == -1 || end <= start) return "";
        String candidate = text.substring(start, end);
        try {
            JsonParser.parseString(candidate);
            return candidate;
        } catch (Exception e) {
            return "";
        }
    }

    private String fallbackTop5(List<JobPosting> jobs) {
        List<SuggestedJob> top5 = jobs.stream()
                .limit(5)
                .map(j -> new SuggestedJob(j.getId(), j.getTitle()))
                .toList();
        return toJson(top5);
    }

    private String toJson(List<SuggestedJob> jobs) {
        StringBuilder sb = new StringBuilder("{\n  \"suggested_jobs\": [\n");
        for (int i = 0; i < 5; i++) {
            if (i < jobs.size()) {
                SuggestedJob j = jobs.get(i);
                String id = j.id != null ? "\"" + j.id + "\"" : "null";
                String title = j.title != null ? j.title.replace("\"", "\\\"") : "";
                sb.append(String.format("    {\"id\": %s, \"title\": \"%s\"}", id, title));
            } else {
                sb.append("    {\"id\": null, \"title\": \"\"}");
            }
            if (i < 4) sb.append(",");
            sb.append("\n");
        }
        sb.append("  ]\n}");
        return sb.toString();
    }

    // Record để gọn
    private record SuggestedJob(UUID id, String title) {}
}