package com.example.appointmentservicereactivegradle.service;

import com.example.appointmentservicereactivegradle.domain.Appointment;
import com.example.appointmentservicereactivegradle.dto.AppointmentDto;
import com.example.appointmentservicereactivegradle.dto.AppointmentRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;

@Service
public interface AppointmentService {
    Mono<Appointment> createAppointment(AppointmentRequest request);

    Mono<List<AppointmentDto>> getAllAppointments();

    Mono<AppointmentDto> findAppointment(String appointmentId);

    Boolean checkIfAppointmentSlotCanBeBooked(AppointmentDto appointmentDto, String clientId);

    Mono<AppointmentDto> bookAppointment(AppointmentDto appointmentDto);
}
