package com.yehor.notification.service.notification.dto;


import com.yehor.notification.service.notification.entity.NotificationChannel;
import com.yehor.notification.service.notification.entity.NotificationType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class NotificationRequest {

	@NotBlank(message = "Recipient email is required")
	@Email(message = "Invalid email format")
	private String recipientEmail;

	@Size(max = 15, message = "Recipient phone must not exceed 15 characters")
	@NotBlank(message = "Recipient phone number is required")
//	@Pattern(regexp = "\\d+", message = "Phone number must contain only digits")
	private String recipientPhone;

	@NotBlank(message = "Subject is required")
	@Size(max = 255, message = "Subject must not exceed 255 characters")
	private String subject;

	@NotBlank(message = "Message is required")
	private String message;

	@NotNull(message = "Notification type is required")
	private NotificationType type;

	private String channel;

	private String orderId;
	private String orderStatus;
}
