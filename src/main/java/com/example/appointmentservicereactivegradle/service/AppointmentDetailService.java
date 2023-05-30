package com.example.appointmentservicereactivegradle.service;

import com.example.appointmentservicereactivegradle.dto.AppointmentDetailDto;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
@Service
public interface AppointmentDetailService {
    Mono<AppointmentDetailDto> createAppointmentDetails(String appointmentId);
}
