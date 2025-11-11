package com.dev.job.controller;

import com.dev.job.dto.ApiResponse;
import com.dev.job.dto.response.User.UserResponse;
import com.dev.job.entity.communication.Message;
import com.dev.job.repository.Communication.MessageRepository;
import com.dev.job.repository.User.UserRepository;
import com.dev.job.service.MessageService;
import static com.dev.job.utils.ResponseHelper.*;

import com.dev.job.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/messages")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MessageController {

    UserService userService;
    MessageRepository messageRepository;
    MessageService messageService;

    @GetMapping("/mine")
    public ResponseEntity<ApiResponse<List<Message>>> getAllMessageOfCurrentUser(Authentication authentication){
        List<Message> messages;
        if(authentication == null){
            messages = List.of();
        }
        else{
            messages = messageRepository.findAllMessageByUser(UUID.fromString(authentication.getName()));
        }
        return ResponseEntity.ok(
          ApiResponse
                  .<List<Message>>builder()
                  .success(true)
                  .data(messages)
                  .build()
        );
    }

    @GetMapping("/with/{otherUserId}")
    public ResponseEntity<ApiResponse<List<Message>>> getMessageWithUser(@PathVariable UUID otherUserId, Authentication authentication){
        System.out.println("BETWEEN: "+otherUserId);
        System.out.println("AND: "+authentication.getName());
        List<Message> messages = messageRepository.findChatBetweenUsers(UUID.fromString(authentication.getName()), otherUserId);
        System.out.println(messages);
        return ResponseEntity.ok(
                ApiResponse
                        .<List<Message>>builder()
                        .success(true)
                        .data(messages)
                        .build()
        );
    }

    @GetMapping("/partners")
    ResponseEntity<ApiResponse<List<UserResponse>>> findPartners(Authentication authentication){
        return ok(messageService.findPartners(UUID.fromString(authentication.getName())));
    }

    // TEST MESSAGES
    @GetMapping("/users")
    ResponseEntity<ApiResponse<List<UserResponse>>> findAllUsers(){
        return ok(userService.getAllUsers());
    }
}
