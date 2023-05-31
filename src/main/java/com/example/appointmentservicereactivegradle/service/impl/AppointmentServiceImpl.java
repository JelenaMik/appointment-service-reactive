package com.example.appointmentservicereactivegradle.service.impl;

import com.example.appointmentservicereactivegradle.domain.Appointment;
import com.example.appointmentservicereactivegradle.dto.AppointmentDto;
import com.example.appointmentservicereactivegradle.dto.AppointmentRequest;
import com.example.appointmentservicereactivegradle.enums.AppointmentType;
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
import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
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
    public Mono<Object> createAppointment(AppointmentRequest request){
        Appointment appointment = requestToAppointmentMapper.requestToAppointment(request);

        return appointmentRepository.findByProviderIdAndStartTime(appointment.getProviderId(), appointment.getStartTime())
                .flatMap(existingApp -> Mono.error(new BookingTimeOverlappingException()))
                .switchIfEmpty(
                        appointmentRepository.save(appointment)
                                .flatMap(appointment1 -> appointmentDetailService.createAppointmentDetails(appointment1.getAppointmentId())
                                .thenReturn(appointment1).log())
                );
    }

    @Override
    public Mono<AppointmentDto> bookAppointment(String clientId, String appointmentId, String details){

        return appointmentRepository.findById(appointmentId)
                        .switchIfEmpty(Mono.error(AppointmentNotFoundException::new))
                .flatMap(appointment -> {
                    if(StringUtils.hasText(appointment.getClientId())) return Mono.error(AppointmentHasAlreadyBookedException::new);
                    appointmentRepository.findByClientIdAndStartTime(clientId, appointment.getStartTime()).flatMap(
                            existingApp -> Mono.error(new BookingTimeOverlappingException()));
                    return Mono.just(appointment);
                })

                                .flatMap(appointment ->
                                        {
                                            appointment.setDetails(details);
                                            appointment.setClientId(clientId);
                                            return Mono.just(appointment);
                                        })
                .flatMap(appointmentRepository::save)
                .map(appointmentMapper::entityToDto);
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

//    @Override
//    public Boolean checkIfAppointmentSlotCanBeBooked(AppointmentDto appointmentDto, String clientId){
//        if (appointmentRepository.findByClientIdAndStartTime(clientId, appointmentDto.getStartTime())) throw new BookingTimeOverlappingException();
//        if (StringUtils.hasText(appointmentDto.getClientId())) throw new AppointmentHasAlreadyBookedException();
//        else return true;
//    }

    @Override
    public Mono<AppointmentDto> changeAppointmentType(String appointmentType, String appointmentId){
        return this.findAppointment(appointmentId)
                .flatMap(appointment -> {
                    appointment.setAppointmentType(AppointmentType.valueOf(appointmentType.toUpperCase()));
                    return Mono.just(appointment).log();
                })
                .flatMap(appointmentDto -> {
                    return Mono.just(appointmentMapper.dtoToEntity(appointmentDto));
                }).flatMap(appointmentRepository::save)
                .map(appointmentMapper::entityToDto);
//                    return Mono.just(appointmentMapper.entityToDto(appointment)).log();

    }

    @Override
    public Mono<Void> cancelAppointmentFromProviderSide(String appointmentId){
       appointmentDetailService.deleteByAppointmentId(appointmentId);
        return Mono.from(appointmentRepository.deleteById(appointmentId));
    }
    @Override
    public Mono<AppointmentDto> cancelAppointmentFromClientSide(String appointmentId){
        return this.findAppointment(appointmentId)
                .flatMap(
                        appointmentDto -> {
                            appointmentDto.setClientId(null);
                            if(StringUtils.hasText(appointmentDto.getDetails())) appointmentDto.setDetails(null);
                            return Mono.just(appointmentDto).log();
                        }
                )
                .map(appointmentMapper::dtoToEntity)
                .flatMap(appointmentRepository::save)
                .map(appointmentMapper::entityToDto);
    }

    @Override
    public Boolean isBeforeNow(String appointmentId){
        Mono<Appointment> appointment = appointmentRepository.findById(appointmentId)
                .switchIfEmpty(Mono.error(new AppointmentNotFoundException()));
        LocalDateTime startDate = LocalDateTime.parse(appointment.map(app->app.getStartTime()).toString());
        return startDate.isBefore(LocalDateTime.now()) || startDate.isEqual(LocalDateTime.now());
    }

    @Override
    public Flux<AppointmentDto> getAppointmentsOfCurrentWeek( String role, String userId){
//        LocalDate startDate = new LocalDate().withWeekOfWeekyear(weekOfTheYear).withDayOfWeek(DateTimeConstants.MONDAY);
//        LocalDateTime fromDate = LocalDateTime.of(java.time.LocalDate.parse(startDate.toString()),
//                LocalTime.of(0,0,0));
//        LocalDateTime toDate = fromDate.plusDays(7).minusSeconds(1);

        if(role.equals("client")) return
            appointmentRepository.findAllByClientId(userId).log()
//                    findAllByClientIdAndStartTimeGreaterThanEqualAndStartTimeLessThanEqualOrderByStartTimeAsc(userId, fromDate, toDate)
                    .map(appointmentMapper::entityToDto);
        if (role.equals("provider")) return
            appointmentRepository.findAllByProviderId(userId)
//                    findAllByProviderIdAndStartTimeGreaterThanEqualAndStartTimeLessThanEqualOrderByStartTimeAsc(userId, fromDate, toDate)
                    .map(appointmentMapper::entityToDto);
        else return Flux.error(AppointmentNotFoundException::new);
    }

}
