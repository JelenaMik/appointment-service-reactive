package com.example.appointmentservicereactivegradle.service;

import com.example.appointmentservicereactivegradle.dto.AppointmentDto;
import com.example.appointmentservicereactivegradle.dto.AppointmentRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public interface AppointmentService {
    Mono<Object> createAppointment(AppointmentRequest request);

    Mono<AppointmentDto> bookAppointment(String clientId, String appointmentId, String details);

    Mono<List<AppointmentDto>> getAllAppointments();

    Mono<AppointmentDto> findAppointment(String appointmentId);

    Mono<AppointmentDto> changeAppointmentType(String appointmentType, String appointmentId);

    Mono<Void> cancelAppointmentFromProviderSide(String appointmentId);

    Mono<AppointmentDto> cancelAppointmentFromClientSide(String appointmentId);

    Boolean isBeforeNow(String appointmentId);

    Flux<AppointmentDto> getAppointmentsOfCurrentWeek( String role, String userId);

//    Boolean checkIfAppointmentSlotCanBeBooked(AppointmentDto appointmentDto, String clientId);

}
