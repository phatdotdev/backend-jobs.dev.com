package com.dev.job.dto.request.Expertise;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateExpertiseRequest {
    @NotBlank(message = "Tiêu đề không được để trống")
    @Size(max = 100, message = "Tiêu đề tối đa 100 ký tự")
    String title;

    @NotBlank(message = "Lĩnh vực không được để trống")
    @Size(max = 100, message = "Lĩnh vực tối đa 100 ký tự")
    String field;

    @Size(max = 1000, message = "Mô tả tối đa 1000 ký tự")
    String description;

    @Min(value = 0, message = "Số năm kinh nghiệm phải >= 0")
    @Max(value = 50, message = "Số năm kinh nghiệm không vượt quá 50")
    int yearsOfExperience;
}
