package com.dev.job.controller;

import com.dev.job.dto.ApiResponse;
import com.dev.job.entity.communication.Message;
import com.dev.job.repository.Communication.MessageRepository;
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

@RestController
@RequestMapping("/messages")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MessageController {


    MessageRepository messageRepository;
    @GetMapping("/mine")
    public ResponseEntity<ApiResponse<List<Message>>> getAllMessageOfCurrentUser(Principal principal){
        List<Message> messages;
        if(principal == null){
            messages = List.of();
        }
        else{
            messages = messageRepository.findAllMessageByUser(principal.getName());
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
    public ResponseEntity<ApiResponse<List<Message>>> getMessageWithUser(@PathVariable String otherUserId, Authentication authentication){
        System.out.println("BETWEEN: "+otherUserId);
        System.out.println("AND: "+authentication.getName());
        List<Message> messages = messageRepository.findChatBetweenUsers(authentication.getName(), otherUserId);
        System.out.println(messages);
        return ResponseEntity.ok(
                ApiResponse
                        .<List<Message>>builder()
                        .success(true)
                        .data(messages)
                        .build()
        );
    }

}
