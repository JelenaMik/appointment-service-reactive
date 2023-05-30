package com.example.appointmentservicereactivegradle.mapper;

import com.example.appointmentservicereactivegradle.domain.AppointmentDetails;
import com.example.appointmentservicereactivegradle.dto.AppointmentDetailDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AppointmentDetailMapper {
    AppointmentDetailDto entityToDto(AppointmentDetails appointmentDetails);
    AppointmentDetails dtoToEntity(AppointmentDetailDto appointmentDetailDto);
}
