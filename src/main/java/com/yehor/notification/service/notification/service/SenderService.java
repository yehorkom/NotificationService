package com.yehor.notification.service.notification.service;

import com.yehor.notification.service.notification.dto.NotificationRequest;

public interface SenderService {
	void send(NotificationRequest request, String body);
}
