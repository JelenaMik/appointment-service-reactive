package com.example.appointmentservicereactivegradle.mapper;

import com.example.appointmentservicereactivegradle.domain.Appointment;
import com.example.appointmentservicereactivegradle.dto.AppointmentDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AppointmentMapper {
    AppointmentDto entityToDto(Appointment appointment);
    Appointment dtoToEntity(AppointmentDto appointmentDto);
}
