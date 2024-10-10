package com.yehor.notification.service.email.service;

import com.yehor.notification.service.email.dto.Email;
import com.yehor.notification.service.email.sender.EmailSender;
import com.yehor.notification.service.exception.EmailSendException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class EmailService {
    private final EmailSender emailSender;

	public void sendEmail(Email email) {

		try {
			emailSender.send(email);
		} catch (Exception e) {
			throw new EmailSendException("Failed to send email", e);
		}
	}
}
