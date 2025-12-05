package com.dev.job.service;

import com.dev.job.dto.request.Communication.CreateNotificationRequest;
import com.dev.job.dto.response.Communication.NotificationResponse;
import com.dev.job.entity.communication.Notification;
import com.dev.job.entity.communication.NotificationType;
import com.dev.job.entity.posting.JobPosting;
import com.dev.job.exceptions.BadRequestException;
import com.dev.job.exceptions.ResourceNotFoundException;
import com.dev.job.repository.Communication.NotificationRepository;
import com.dev.job.repository.Posting.JobPostingRepository;
import com.dev.job.repository.User.UserRepository;
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

    UserRepository userRepository;
    SimpMessagingTemplate messagingTemplate;
    NotificationRepository notificationRepository;
    JobPostingRepository jobPostingRepository;

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
        LocalDateTime now = LocalDateTime.now();
        for (UUID recipientId : recipientIds) {
            Notification userNotification = Notification.builder()
                    .recipientId(recipientId)
                    .type(sourceNotification.getType())
                    .content(sourceNotification.getContent())
                    .timestamp(now)
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

    @Transactional
    public void createNotificationToUsers(CreateNotificationRequest request, UUID userId){
        List<UUID> ids = userRepository.findAllUserIds();
        Notification notification = Notification.builder()
                .type(NotificationType.SYSTEM_ANNOUNCEMENT)
                .title(request.getTitle())
                .content(request.getContent())
                .build();
        sendUserNotificationsToUsers(ids, notification, userId);
    }

    public void sendInvitationToUser(UUID senderId, UUID postId,  UUID receiverId){
        JobPosting jobPosting = jobPostingRepository.findById(postId)
                .orElseThrow(() -> new BadRequestException("Job Posting Not Found."));
        Notification notification = Notification.builder()
                .type(NotificationType.JOB_INVITATION)
                .senderId(senderId)
                .recipientId(receiverId)
                .title("Có lời mời công việc!")
                .content("Bạn có lời mời từ công việc "+jobPosting.getTitle())
                .jobPosting(jobPosting)
                .build();
        sendUserNotification(notification, senderId);
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
                .resumeId(notification.getFeedbackRequest() != null ? notification.getFeedbackRequest().getResume().getId() : null)
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

}
