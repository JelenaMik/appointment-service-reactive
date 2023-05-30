package com.example.appointmentservicereactivegradle.repository;

import com.example.appointmentservicereactivegradle.domain.AppointmentDetails;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppointmentDetailRepository extends ReactiveMongoRepository<AppointmentDetails, String> {

}
