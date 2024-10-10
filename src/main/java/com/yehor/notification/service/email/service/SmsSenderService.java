package com.yehor.notification.service.email.service;

import com.yehor.notification.service.notification.dto.NotificationRequest;
import com.yehor.notification.service.notification.service.SenderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;


// EXAMPLE

@Service
@RequiredArgsConstructor
@Qualifier("smsSenderService")
public class SmsSenderService implements SenderService {

//	private final SmsService smsService;

	@Override
	public void send(NotificationRequest request, String body) {
//		smsService.sendSms(request.getRecipientPhone(), body);
	}
}
