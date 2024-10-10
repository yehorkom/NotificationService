package com.yehor.notification.service.notification.controller;

import com.yehor.notification.service.notification.entity.Notification;
import com.yehor.notification.service.notification.dto.NotificationRequest;
import com.yehor.notification.service.notification.entity.NotificationType;
import com.yehor.notification.service.notification.service.NotificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/notifications")
public class NotificationController {
    private final NotificationService notificationService;

    @PostMapping("/send")
    public ResponseEntity<Object> sendEmailNotification(@Valid @RequestBody NotificationRequest notificationRequest) {

		notificationService.sendNotification(notificationRequest);
		return ResponseEntity.ok("Notification sent and saved successfully");
    }

	@GetMapping
	public Page<Notification> getNotifications(
		@RequestParam Optional<String> recipientEmail,
		@RequestParam Optional<List<NotificationType>> notificationTypes,
		@RequestParam Optional<LocalDateTime> createdAfter,
		@RequestParam Optional<LocalDateTime> createdBefore,
		@PageableDefault(size = 10, sort = "createdAt") Pageable pageable
	) {
		return notificationService.getNotifications(recipientEmail, notificationTypes, createdAfter, createdBefore, pageable);
	}
}
