package com.yehor.notification.service.email.service;

import com.yehor.notification.service.email.dto.Email;
import com.yehor.notification.service.notification.dto.NotificationRequest;
import com.yehor.notification.service.notification.service.SenderService;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Qualifier("emailSenderService")
@RequiredArgsConstructor
public class EmailSenderService implements SenderService {

	private final EmailService emailService;

	@Override
	public void send(NotificationRequest request, String body) {
		Email email = Email.builder()
			.recipientEmail(request.getRecipientEmail())
			.subject(request.getSubject())
			.body(body)
			.build();
		emailService.sendEmail(email);
	}
}
