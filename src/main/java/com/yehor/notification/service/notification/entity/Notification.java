package com.yehor.notification.service.notification.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "notifications")
public class Notification {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 255)
	private String recipientEmail;

	@Column(length = 255)
	private String recipientPhone;

	@Column(nullable = false, length = 255)
	private String subject;

	@Column(nullable = false, columnDefinition = "TEXT")
	private String body;

	@Column
	private String orderId;

	@Column
	private String orderStatus;

	@Enumerated(EnumType.STRING)
	private NotificationType type;

	@Column(nullable = false)
	private LocalDateTime createdAt = LocalDateTime.now();
}
