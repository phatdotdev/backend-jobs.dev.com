package com.dev.job.controller;

import com.dev.job.dto.ApiResponse;
import com.dev.job.dto.request.Communication.CreateNotificationRequest;
import com.dev.job.dto.response.Communication.NotificationResponse;
import com.dev.job.entity.communication.Notification;
import static com.dev.job.utils.ResponseHelper.*;

import com.dev.job.service.NotificationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notifications")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class NotificationController {

    NotificationService notificationService;

    @GetMapping("/mine")
    ResponseEntity<ApiResponse<List<NotificationResponse>>> getAllMyNotifications(Authentication authentication){
        return ok(notificationService.getAllMyNotifications(UUID.fromString(authentication.getName())));
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<?> markAsRead(
            @PathVariable UUID id,
            Authentication authentication
    ) {
        notificationService.markNotificationAsRead(id, UUID.fromString(authentication.getName()));
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<?> createNotification(
            @RequestBody CreateNotificationRequest request,
            Authentication authentication
            ){
        notificationService.createNotificationToUsers(request, UUID.fromString(authentication.getName()));
        return ok("send notification.");
    }

}
