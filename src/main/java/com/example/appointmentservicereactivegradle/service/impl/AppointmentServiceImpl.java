package com.example.appointmentservicereactivegradle.service.impl;

import com.example.appointmentservicereactivegradle.domain.Appointment;
import com.example.appointmentservicereactivegradle.dto.AppointmentDto;
import com.example.appointmentservicereactivegradle.dto.AppointmentRequest;
import com.example.appointmentservicereactivegradle.exception.AppointmentHasAlreadyBookedException;
import com.example.appointmentservicereactivegradle.exception.AppointmentNotFoundException;
import com.example.appointmentservicereactivegradle.exception.BookingTimeOverlappingException;
import com.example.appointmentservicereactivegradle.mapper.AppointmentMapper;
import com.example.appointmentservicereactivegradle.mapper.RequestToAppointmentMapper;
import com.example.appointmentservicereactivegradle.repository.AppointmentRepository;
import com.example.appointmentservicereactivegradle.service.AppointmentDetailService;
import com.example.appointmentservicereactivegradle.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppointmentServiceImpl implements AppointmentService {

    private final RequestToAppointmentMapper requestToAppointmentMapper;
    private final AppointmentMapper appointmentMapper;
    private final AppointmentRepository appointmentRepository;
    private final AppointmentDetailService appointmentDetailService;

    @Override
    public Mono<Appointment> createAppointment(AppointmentRequest request){
        Appointment appointment = requestToAppointmentMapper.requestToAppointment(request);
        Mono<Appointment> appointmentMono = appointmentRepository.findByProviderIdAndStartTime(appointment.getProviderId(), appointment.getStartTime());
        return appointmentMono.switchIfEmpty(
                appointmentRepository.save(appointment)
                        .flatMap(appointment1 -> {
                            return appointmentDetailService.createAppointmentDetails(appointment1.getAppointmentId())
                                    .thenReturn(appointment1).log();

//
//            return appointmentRepository.findByProviderIdAndStartTime(appointment.getProviderId(), appointment.getStartTime())
//                    .flatMap(foundApp -> Mono.error(new BookingTimeOverlappingException()));

    })
    }

    @Override
    public Mono<List<AppointmentDto>> getAllAppointments(){
        return appointmentRepository.findAll()
                .map(appointmentMapper::entityToDto)
                .collectList();
    }

    @Override
    public Mono<AppointmentDto> findAppointment(String appointmentId){
        return appointmentRepository.findById(appointmentId)
                .switchIfEmpty(Mono.error(new AppointmentNotFoundException()))
                .map(appointmentMapper::entityToDto);
    }

    @Override
    public Boolean checkIfAppointmentSlotCanBeBooked(AppointmentDto appointmentDto, String clientId){
//        if (appointmentRepository.findByClientIdAndStartTime(clientId, appointmentDto.getStartTime())) throw new BookingTimeOverlappingException();
        if (StringUtils.hasText(appointmentDto.getClientId())) throw new AppointmentHasAlreadyBookedException();
        else return true;
    }

    @Override
    public Mono<AppointmentDto> bookAppointment(AppointmentDto appointmentDto){
        Appointment appointment = appointmentMapper.dtoToEntity(appointmentDto);
        return appointmentRepository.save(appointment)
                .map(appointmentMapper::entityToDto);
    }
}
