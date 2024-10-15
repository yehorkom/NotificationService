package com.yehor.notification.service;

import com.yehor.notification.service.email.service.EmailSenderService;
import com.yehor.notification.service.email.service.SmsSenderService;
import com.yehor.notification.service.notification.entity.NotificationChannel;
import com.yehor.notification.service.notification.service.SenderService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class AppConfig {

	@Bean
	public Map<NotificationChannel, SenderService> senderServicesMap(EmailSenderService emailSenderService,
																	 SmsSenderService smsSenderService) {
		Map<NotificationChannel, SenderService> senderServices = new HashMap<>();
		senderServices.put(NotificationChannel.EMAIL, emailSenderService);
		senderServices.put(NotificationChannel.SMS, smsSenderService);

		return senderServices;
	}
}
