package com.dev.job.service;

import com.dev.job.entity.communication.Notification;
import com.dev.job.entity.communication.NotificationType;
import com.dev.job.exceptions.BadRequestException;
import com.dev.job.exceptions.ResourceNotFoundException;
import com.dev.job.repository.Communication.NotificationRepository;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class NotificationService {
    SimpMessagingTemplate messagingTemplate;
    NotificationRepository notificationRepository;

    public void sendUserNotification(Notification notification, UUID userId){
        notification.setRead(false);
        notification.setSenderId(userId);
        notification.setTimestamp(LocalDateTime.now());
        notificationRepository.save(notification);
        messagingTemplate.convertAndSendToUser(notification.getRecipientId().toString(), "/queue/notifications", notification);
        System.out.println("Sent notification to user: " + notification.getRecipientId() + " Type: " + notification.getType());
    }

    public void sendUserNotificationsToUsers(List<UUID> recipientIds, Notification sourceNotification, UUID userId){
        if (recipientIds == null || recipientIds.isEmpty()) {
            System.out.println("No recipient IDs provided for notification.");
            return;
        }

        System.out.println("Processing notification send for " + recipientIds.size() + " users.");

        for (UUID recipientId : recipientIds) {
            Notification userNotification = Notification.builder()
                    .recipientId(recipientId)
                    .type(sourceNotification.getType())
                    .content(sourceNotification.getContent())
                    .build();
            sendUserNotification(userNotification, userId);
        }
    }

    public List<Notification> getAllMyNotifications(UUID id){
        return notificationRepository.findByRecipientId(id);
    }

    @Transactional
    public void markNotificationAsRead(UUID notificationId, UUID userId) {
        Notification noti = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found."));

        if (!noti.getRecipientId().equals(userId)) {
            throw new BadRequestException("You do not have this permission.");
        }

        notificationRepository.markAsRead(notificationId);
    }


    // PRIVATE METHOD

    public static String getMessageTemplate(NotificationType type) {
        return switch (type) {
            case NEW_JOB_POSTING -> "Có bài đăng mới vừa được tạo.";
            case APPLICATION_RECEIVED -> "Bạn vừa nhận được một đơn ứng tuyển.";
            case APPLICATION_STATUS_CHANGED -> "Trạng thái đơn ứng tuyển đã thay đổi.";
            case INTERVIEW_SCHEDULED -> "Lịch phỏng vấn đã được lên.";
            case INTERVIEW_RESULT -> "Kết quả phỏng vấn đã có.";
            case JOB_OFFER -> "Bạn vừa nhận được một thư mời nhận việc.";
            case SYSTEM_ANNOUNCEMENT -> "Thông báo từ hệ thống.";
        };
    }
}
