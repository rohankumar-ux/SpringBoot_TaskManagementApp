package com.example.TaskManagement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddCommentRequestDto {
    @NotNull(message = "Author ID is required")
    private UUID authorId;

    @NotBlank(message = "Comment text is required")
    private String text;
}
