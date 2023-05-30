package com.example.appointmentservicereactivegradle.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AppointmentRequest {
    private String providerId;
    private String startDate;
    private String startHour;
    private String appointmentType;

}
