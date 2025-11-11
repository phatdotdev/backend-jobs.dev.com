package com.dev.job.service;

import com.dev.job.dto.response.User.UserResponse;
import com.dev.job.repository.Communication.MessageRepository;
import com.dev.job.repository.User.UserRepository;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MessageService {
    UserRepository userRepository;
    MessageRepository messageRepository;
    UserService userService;
    public  List<UserResponse> findPartners(UUID userId){
        return userService.getUserList(messageRepository.findDistinctPartnersByUserId(userId));
    }

}
