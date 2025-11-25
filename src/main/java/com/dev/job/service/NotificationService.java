package com.dev.job.service;

import com.dev.job.dto.response.Communication.NotificationResponse;
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

        messagingTemplate.convertAndSendToUser(notification.getRecipientId().toString(), "/queue/notifications", this.notificationToResponse(notification));
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

    public List<NotificationResponse> getAllMyNotifications(UUID id){
        return notificationRepository.findByRecipientId(id)
                .stream().map(this::notificationToResponse).toList();
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

    public NotificationResponse notificationToResponse(Notification notification){
        return NotificationResponse
                .builder()
                .id(notification.getId())
                .senderId(notification.getSenderId())
                .recipientId(notification.getRecipientId())
                .title(notification.getTitle())
                .content(notification.getContent())
                .isRead(notification.isRead())
                .timestamp(notification.getTimestamp())
                .type(notification.getType().toString())
                .applicationId(notification.getApplication() != null ? notification.getApplication().getId() : null)
                .requestId(notification.getFeedbackRequest() != null ? notification.getFeedbackRequest().getId() : null)
                .postId(notification.getJobPosting() != null ? notification.getJobPosting().getId() : null)
                .objectTitle(resolveObjectTitle(notification))
                .build();
    }

    private String resolveObjectTitle(Notification notification) {
        if (notification.getApplication() != null) {
            return notification.getApplication().getJobPosting().getTitle();
        } else if (notification.getFeedbackRequest() != null) {
            return notification.getFeedbackRequest().getResume().getTitle();
        } else if (notification.getJobPosting() != null) {
            return notification.getJobPosting().getTitle();
        }
        return null;
    }


    public static String getMessageTemplate(NotificationType type) {
        return switch (type) {
            case APPLICATION_STATUS_CHANGED -> "Trạng thái đơn ứng tuyển đã thay đổi.";
            case APPLICATION_ACTIVITY -> "Có hoạt động trên bài tuyển dụng của bạn.";
            case REVIEW_RECEIVED -> "Kết quả đánh giá hồ sơ đã có.";
            case SYSTEM_ANNOUNCEMENT -> "Thông báo từ hệ thống.";
        };
    }
}
