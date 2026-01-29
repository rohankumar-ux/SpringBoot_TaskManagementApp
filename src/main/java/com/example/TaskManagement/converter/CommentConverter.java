package com.example.TaskManagement.converter;

import com.example.TaskManagement.dto.CommentResponseDto;
import com.example.TaskManagement.model.Comment;

public class CommentConverter {

    public static CommentResponseDto toCommentResponse(Comment comment){
        return new CommentResponseDto(
                comment.getId(),
                UserConverter.toUserSummary(comment.getAuthor()),
                comment.getText(),
                comment.getTimestamp()
        );
    }
}
