package com.example.TaskManagement.converter;

import com.example.TaskManagement.dto.UserResponseDto;
import com.example.TaskManagement.dto.UserSummaryDto;
import com.example.TaskManagement.model.User;

public class UserConverter {

    public static UserResponseDto toUserResponse(User user){
        return new UserResponseDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.getActive()
        );
    }

    public static UserSummaryDto toUserSummary(User user){
        return new UserSummaryDto(
                user.getId() ,
                user.getName(),
                user.getEmail(),
                user.getRole()
        );
    }
}
