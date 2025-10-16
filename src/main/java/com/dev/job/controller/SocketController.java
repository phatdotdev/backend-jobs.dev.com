package com.dev.job.controller;

import com.dev.job.entity.communication.Message;
import com.dev.job.repository.Communication.MessageRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class SocketController {

    SimpMessagingTemplate messagingTemplate;
    MessageRepository messageRepository;

    @MessageMapping("/send")
    public void sendMessage(@Payload Message message, Principal principal) {
        if (principal == null) return;
        message.setTimestamp(LocalDateTime.now());
        Message newMessage = Message.builder()
                .senderId(message.getSenderId())
                .receiverId(message.getReceiverId())
                .content(message.getContent())
                .timestamp(LocalDateTime.now())
                .build();
        messageRepository.save(newMessage);
        messagingTemplate.convertAndSendToUser(
                message.getReceiverId(),
                "/queue/messages",
                message
        );
    }
}
