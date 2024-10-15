package com.yehor.notification.service.notification.service;

import com.yehor.notification.service.notification.entity.Notification;
import com.yehor.notification.service.notification.dto.NotificationRequest;
import com.yehor.notification.service.notification.entity.NotificationChannel;
import com.yehor.notification.service.notification.entity.NotificationType;
import com.yehor.notification.service.notification.repository.NotificationRepository;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class NotificationService {
	private final NotificationRepository notificationRepository;
	private final Map<NotificationChannel, SenderService> senderServices;

	@Transactional
	public void sendNotification(NotificationRequest request) {
		String body = createMessageBody(request);

		Notification notification = Notification.builder()
			.id(null)
			.recipientEmail(request.getRecipientEmail())
			.recipientPhone(request.getRecipientPhone())
			.subject(request.getSubject())
			.body(body)
			.orderId(request.getOrderId())
			.orderStatus(request.getOrderStatus())
			.type(request.getType())
			.createdAt(LocalDateTime.now())
			.build();

		notificationRepository.save(notification);

		NotificationChannel channel = NotificationChannel.valueOf(request.getChannel());
		senderServices.getOrDefault(channel, senderServices.get(NotificationChannel.EMAIL)).send(request, body);
	}

	private String createMessageBody(NotificationRequest request) {

		return switch (request.getType()) {
			case ORDER_MESSAGE ->
				"Your order " + request.getOrderId() + " is now " + request.getOrderStatus() + ". " + request.getMessage();
			case USER_SIGNUP ->
				"Welcome, " + request.getRecipientEmail() + "! " + request.getMessage();
			case UNUSUAL_ACTIVITY ->
				"We detected unusual activity: " + request.getMessage();
			case NEW_PROMOTION ->
				"New Promotion: " + request.getMessage();
			default ->
				throw new IllegalArgumentException("Unknown notification type: " + request.getType());
		};
	}

	public Page<Notification> getNotifications(Optional<String> recipientEmail,
											   Optional<List<NotificationType>> notificationTypes,
											   Optional<LocalDateTime> createdAfter,
											   Optional<LocalDateTime> createdBefore,
											   Pageable pageable) {

		return notificationRepository.findAll((root, query, criteriaBuilder) -> {
			var predicates = criteriaBuilder.conjunction();

			recipientEmail.ifPresent(email -> predicates.getExpressions().add(
				criteriaBuilder.equal(root.get("recipientEmail"), email)
			));

			notificationTypes.ifPresent(types -> predicates.getExpressions().add(
				root.get("type").in(types)
			));

			createdAfter.ifPresent(after -> predicates.getExpressions().add(
				criteriaBuilder.greaterThanOrEqualTo(root.get("createdAt"), after)
			));

			createdBefore.ifPresent(before -> predicates.getExpressions().add(
				criteriaBuilder.lessThanOrEqualTo(root.get("createdAt"), before)
			));

			return predicates;
		}, pageable);
	}
}
