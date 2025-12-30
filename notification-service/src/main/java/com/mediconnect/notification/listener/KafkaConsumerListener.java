package com.mediconnect.notification.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mediconnect.notification.model.Appointment;
import com.mediconnect.notification.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
@Component
public class KafkaConsumerListener {

	private static final Logger logger = LoggerFactory.getLogger(KafkaConsumerListener.class);

	private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

	// Service for triggering email notifications
	@Autowired
	private EmailService emailService;

	@KafkaListener(topics = "#{'${spring.kafka.topic.name}'}")
	public void listen(String message) {
		try {

			logger.info("Listening on topic: {}", "${spring.kafka.listener.topic}");

			Appointment appointment = objectMapper.readValue(message, Appointment.class);
			logger.info("Received Appointment | rawMessage: {}, parsedJson: {}", message, appointment);
			emailService.triggerEmailNotification(appointment);
			logger.info("Email notification sent for Appointment ID: {}", appointment.getId());
		} catch (JsonProcessingException e) {
			logger.error("Error deserializing message: {}", message, e);
		} catch (Exception ex) {
			logger.error("Unexpected error while processing message: {}", message, ex);
		}
	}
}
