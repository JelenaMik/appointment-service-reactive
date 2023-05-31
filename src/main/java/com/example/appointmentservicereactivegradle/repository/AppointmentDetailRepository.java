package com.example.appointmentservicereactivegradle.repository;

import com.example.appointmentservicereactivegradle.domain.AppointmentDetails;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface AppointmentDetailRepository extends ReactiveMongoRepository<AppointmentDetails, String> {
    Mono<AppointmentDetails> findByAppointmentId(String appointmentId);

}
