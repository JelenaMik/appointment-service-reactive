package com.example.appointmentservicereactivegradle.domain;

import com.example.appointmentservicereactivegradle.enums.AppointmentType;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import reactor.util.annotation.Nullable;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
@Builder
public class Appointment {
    @Id
    private String appointmentId;

    @Nullable
    private String clientId;
    @NotNull
    private String providerId;
    @FutureOrPresent
    private LocalDateTime startTime;
    private AppointmentType appointmentType;
    @Size(max = 50)
    private String details;

}
