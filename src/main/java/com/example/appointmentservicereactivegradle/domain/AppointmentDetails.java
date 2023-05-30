package com.example.appointmentservicereactivegradle.domain;

import com.example.appointmentservicereactivegradle.enums.AppointmentStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
@Builder
public class AppointmentDetails {
    @Id
    private String appointmentDetailId;
    @NotNull
    private String appointmentId;

    private AppointmentStatus status;

    @CreatedDate
    private LocalDateTime created;
}
