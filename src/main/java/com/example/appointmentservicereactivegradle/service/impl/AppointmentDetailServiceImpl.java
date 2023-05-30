package com.example.appointmentservicereactivegradle.service.impl;

import com.example.appointmentservicereactivegradle.domain.AppointmentDetails;
import com.example.appointmentservicereactivegradle.dto.AppointmentDetailDto;
import com.example.appointmentservicereactivegradle.enums.AppointmentStatus;
import com.example.appointmentservicereactivegradle.mapper.AppointmentDetailMapper;
import com.example.appointmentservicereactivegradle.repository.AppointmentDetailRepository;
import com.example.appointmentservicereactivegradle.repository.AppointmentRepository;
import com.example.appointmentservicereactivegradle.service.AppointmentDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AppointmentDetailServiceImpl implements AppointmentDetailService {
    private final AppointmentRepository appointmentRepository;
    private final AppointmentDetailRepository appointmentDetailRepository;
    private final AppointmentDetailMapper appointmentDetailMapper;
    @Override
    public Mono<AppointmentDetailDto> createAppointmentDetails(String appointmentId){
        AppointmentDetails details =
                AppointmentDetails.builder()
                        .appointmentId(appointmentId)
                        .status(AppointmentStatus.PENDING)
                        .created(LocalDateTime.now())
                        .build();

        return appointmentDetailRepository.save(details)
                .map(appointmentDetailMapper::entityToDto);

    }
}
