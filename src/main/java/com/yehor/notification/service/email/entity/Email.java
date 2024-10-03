package com.yehor.notification.service.email.entity;

import lombok.Data;
import lombok.Builder;

@Data
@Builder
public class Email {
	private String recipientEmail;
	private String subject;
	private String body;
}
