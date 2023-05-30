package com.example.appointmentservicereactivegradle.mapper;

import com.example.appointmentservicereactivegradle.domain.Appointment;
import com.example.appointmentservicereactivegradle.dto.AppointmentRequest;
import com.example.appointmentservicereactivegradle.enums.AppointmentType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
@Configuration
public class RequestToAppointmentMapper {

    public Appointment requestToAppointment(AppointmentRequest request){
        int hour = Integer.parseInt( request.getStartHour() );

        LocalDateTime startDate = LocalDateTime.of(
                LocalDate.parse(request.getStartDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                LocalTime.of(hour,0,0)
        );

        return Appointment.builder()
                .providerId(request.getProviderId())
                .startTime(startDate)
                .appointmentType(AppointmentType.valueOf(request.getAppointmentType().toUpperCase()))
                .build();
    }


}
