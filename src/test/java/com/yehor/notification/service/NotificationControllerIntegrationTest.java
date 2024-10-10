package com.yehor.notification.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yehor.notification.service.notification.entity.Notification;
import com.yehor.notification.service.notification.dto.NotificationRequest;
import com.yehor.notification.service.notification.entity.NotificationType;
import com.yehor.notification.service.notification.repository.NotificationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@AutoConfigureMockMvc
	class NotificationControllerIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	private NotificationRequest notificationRequest;
	@Autowired
	private NotificationRepository notificationRepository;

	@BeforeEach
	public void setUp() {
		notificationRepository.deleteAll();

		notificationRequest = new NotificationRequest();
		notificationRequest.setRecipientEmail("user@example.com");
		notificationRequest.setRecipientPhone("1234567890");
		notificationRequest.setSubject("Test Notification");
		notificationRequest.setMessage("This is a test notification.");
		notificationRequest.setNotificationType(NotificationType.USER_SIGNUP);

		Notification notification = new Notification();
		notification.setRecipientEmail("user@example.com");
		notification.setRecipientPhone("1234567890");
		notification.setSubject("Test Notification");
		notification.setBody("This is a test notification.");
		notification.setType(NotificationType.USER_SIGNUP);
		notification.setCreatedAt(LocalDateTime.now());

		notificationRepository.save(notification);
	}

	@Test
	void testSendNotification_Success() throws Exception {
		String requestBody = objectMapper.writeValueAsString(notificationRequest);

		mockMvc.perform(post("/notifications/send")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody))
			.andExpect(status().isOk());
	}

	@Test
	void testSendNotification_InvalidEmail() throws Exception {
		notificationRequest.setRecipientEmail("invalid-email");

		String requestBody = objectMapper.writeValueAsString(notificationRequest);

		mockMvc.perform(post("/notifications/send")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody))
			.andExpect(status().isBadRequest());
	}

	@Test
	void testSendNotification_EmptyEmail() throws Exception {
		notificationRequest.setRecipientEmail("");

		String requestBody = objectMapper.writeValueAsString(notificationRequest);

		mockMvc.perform(post("/notifications/send")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody))
			.andExpect(status().isBadRequest());
	}

	@Test
	void testSendNotification_PhoneNumberTooLong() throws Exception {
		notificationRequest.setRecipientPhone("12345678910111213");

		String requestBody = objectMapper.writeValueAsString(notificationRequest);

		mockMvc.perform(post("/notifications/send")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody))
			.andExpect(status().isBadRequest());
	}

	@Test
	void testSendNotification_EmptyPhoneNumber() throws Exception {
		notificationRequest.setRecipientPhone("");

		String requestBody = objectMapper.writeValueAsString(notificationRequest);

		mockMvc.perform(post("/notifications/send")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody))
			.andExpect(status().isBadRequest());
	}

	@Test
	void testSendNotification_MissingNotificationType() throws Exception {
		notificationRequest.setNotificationType(null);

		String requestBody = objectMapper.writeValueAsString(notificationRequest);

		mockMvc.perform(post("/notifications/send")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody))
			.andExpect(status().isBadRequest());
	}

	@Test
	void testSendNotification_SubjectTooLong() throws Exception {
		notificationRequest.setSubject("This is a very long subject that exceeds the limit of 255 characters. " +
			"Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. " +
			"Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.");

		String requestBody = objectMapper.writeValueAsString(notificationRequest);

		mockMvc.perform(post("/notifications/send")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody))
			.andExpect(status().isBadRequest());
	}

	@Test
	void testSendNotification_EmptyMessage() throws Exception {
		notificationRequest.setMessage("");

		String requestBody = objectMapper.writeValueAsString(notificationRequest);

		mockMvc.perform(post("/notifications/send")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody))
			.andExpect(status().isBadRequest());
	}

	@Test
	void testGetNotifications_FilterByRecipientEmail() throws Exception {
		mockMvc.perform(get("/notifications")
				.param("recipientEmail", "user@example.com")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.content").isArray())
			.andExpect(jsonPath("$.content[0].recipientEmail").value("user@example.com"));
	}

	@Test
	void testGetNotifications_FilterByNotificationType() throws Exception {
		mockMvc.perform(get("/notifications")
				.param("notificationTypes", "USER_SIGNUP")
				.param("notificationTypes", "ORDER_MESSAGE")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.content").isArray())
			.andExpect(jsonPath("$.content[0].type").value("USER_SIGNUP"));
	}

	@Test
	void testGetNotifications_FilterByCreatedAfter() throws Exception {
		mockMvc.perform(get("/notifications")
				.param("createdAfter", "2023-01-01T00:00:00")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.content").isArray());
	}

	@Test
	void testGetNotifications_FilterByCreatedBefore() throws Exception {
		mockMvc.perform(get("/notifications")
				.param("createdBefore", "2023-12-31T23:59:59")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.content").isArray());
	}

	@Test
	void testGetNotifications_SortByRecipientEmail() throws Exception {
		mockMvc.perform(get("/notifications")
				.param("sort", "recipientEmail,asc")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.content").isArray());
	}

	@Test
	void testGetNotifications_SortByCreatedAt() throws Exception {
		mockMvc.perform(get("/notifications")
				.param("sort", "createdAt,desc")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.content").isArray());
	}

	@Test
	void testGetNotifications_Pagination() throws Exception {
		mockMvc.perform(get("/notifications")
				.param("page", "0")
				.param("size", "5")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.content").isArray())
			.andExpect(jsonPath("$.size").value(5));
	}
}
