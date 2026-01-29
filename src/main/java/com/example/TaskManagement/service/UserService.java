package com.example.TaskManagement.service;

import com.example.TaskManagement.converter.UserConverter;
import com.example.TaskManagement.dto.CreateUserRequestDto;
import com.example.TaskManagement.dto.UpdateUserRequestDto;
import com.example.TaskManagement.dto.UserResponseDto;
import com.example.TaskManagement.enums.Role;
import com.example.TaskManagement.exception.DuplicateEmailException;
import com.example.TaskManagement.exception.ResourceNotFoundException;
import com.example.TaskManagement.exception.RuleViolationException;
import com.example.TaskManagement.model.User;
import com.example.TaskManagement.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public UserResponseDto createUser(CreateUserRequestDto request){
        if(userRepository.existsByEmail(request.getEmail())){
            throw new DuplicateEmailException("Email already exists : " + request.getEmail());
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setRole(request.getRole());
        user.setActive(true);

        User createdUser = userRepository.save(user);
        return UserConverter.toUserResponse(createdUser);
    }

    @Transactional
    public UserResponseDto getUserById(UUID id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not Found!"));

        return UserConverter.toUserResponse(user);
    }

    @Transactional
    public List<UserResponseDto> getAllUsers(String sortBy , Role role , Boolean activeOnly){
        Sort sort = Sort.by(sortBy != null ? sortBy : "name");
        List<User> users = userRepository.findAll(sort);

        if(role != null){
            users = users.stream()
                    .filter(u -> u.getRole().equals(role))
                    .collect(Collectors.toList());
        }

        if(activeOnly != null && activeOnly){
            users = users.stream()
                    .filter(User::getActive)
                    .collect(Collectors.toList());
        }

        return users.stream()
                .map(UserConverter::toUserResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public UserResponseDto updateUser(UUID id , UpdateUserRequestDto request){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not Found!"));

        user.setName(request.getName());
        user.setRole(request.getRole());

        User updatedUser = userRepository.save(user);
        return UserConverter.toUserResponse(updatedUser);
    }

    @Transactional
    public void deleteUser(UUID id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not Found!"));

        if(userRepository.hasActiveTasks(id)){
            throw new RuleViolationException(
                    "Cannot delete user with active assigned tasks. User ID: " + id);
        }

        user.setActive(false);
        userRepository.save(user);
    }
}
