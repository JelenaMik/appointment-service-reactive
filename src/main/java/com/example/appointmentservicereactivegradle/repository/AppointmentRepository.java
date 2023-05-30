package com.example.appointmentservicereactivegradle.repository;

import com.example.appointmentservicereactivegradle.domain.Appointment;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Repository
public interface AppointmentRepository extends ReactiveMongoRepository<Appointment, String> {
    Mono<Appointment> findByProviderIdAndStartTime(String providerId, LocalDateTime startTime);
    Mono<Appointment> findByClientIdAndStartTime(String clientId, LocalDateTime startTime);
}
