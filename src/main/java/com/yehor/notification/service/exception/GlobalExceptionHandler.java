package com.yehor.notification.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
class GlobalExceptionHandler {

	@ExceptionHandler(EmailSendException.class)
	public ResponseEntity<String> handleEmailSendException(EmailSendException ex) {
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
		String errorMessages = ex.getBindingResult().getFieldErrors().stream()
			.map(fieldError -> fieldError.getField() + " - " + fieldError.getDefaultMessage())
			.reduce("", (subtotal, element) -> subtotal + element + "\n");

		return new ResponseEntity<>(errorMessages + ex.getStatusCode() , HttpStatus.BAD_REQUEST);
	}
}
