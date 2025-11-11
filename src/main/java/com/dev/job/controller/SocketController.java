package com.dev.job.controller;

import com.dev.job.dto.request.Communication.ReadMessageRequest;
import com.dev.job.dto.response.Communication.MessageResponse;
import com.dev.job.entity.communication.Message;
import com.dev.job.repository.Communication.MessageRepository;
import com.dev.job.service.UserService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

import javax.management.Notification;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class SocketController {

    SimpMessagingTemplate messagingTemplate;
    UserService userService;
    MessageRepository messageRepository;

    @MessageMapping("/send")
    public void sendMessage(@Payload Message message, Authentication authentication) {
        message.setTimestamp(LocalDateTime.now());
        Message newMessage = Message.builder()
                .senderId(message.getSenderId())
                .isRead(false)
                .receiverId(message.getReceiverId())
                .content(message.getContent())
                .timestamp(LocalDateTime.now())
                .build();
        messageRepository.save(newMessage);

        messagingTemplate.convertAndSendToUser(
                message.getReceiverId().toString(),
                "/queue/messages",
                this.messageToMessageResponse(newMessage)
        );
    }

    @Transactional
    @MessageMapping("/read")
    public void markMessagesAsRead(@Payload ReadMessageRequest request, Authentication authentication) {
        UUID receiverId = UUID.fromString(authentication.getName());
        UUID senderId = request.getSenderId();

        List<Message> unreadMessages = messageRepository.findBySenderIdAndReceiverIdAndIsReadFalse(senderId, receiverId);

        for (Message msg : unreadMessages) {
            msg.setRead(true);
        }

        messageRepository.saveAll(unreadMessages);
    }


    public MessageResponse messageToMessageResponse(Message message) {

        String receiverUsername = userService.getUsernameById(message.getReceiverId());
        String senderUsername = userService.getUsernameById(message.getSenderId());

        return MessageResponse.builder()
                .id(message.getId())
                .isRead(message.isRead())
                .senderId(message.getSenderId().toString())
                .senderUsername(senderUsername)
                .receiverId(message.getReceiverId().toString())
                .receiverUsername(receiverUsername)
                .content(message.getContent())
                .timestamp(message.getTimestamp())
                .build();
    }

}
