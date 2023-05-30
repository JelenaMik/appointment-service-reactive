package com.example.appointmentservicereactivegradle.handler;

import com.example.appointmentservicereactivegradle.dto.AppointmentDto;
import com.example.appointmentservicereactivegradle.dto.AppointmentRequest;
import com.example.appointmentservicereactivegradle.service.AppointmentDetailService;
import com.example.appointmentservicereactivegradle.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.EAN;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class AppointmentHandler {
    private final AppointmentService appointmentService;
    private final AppointmentDetailService appointmentDetailService;

        public Mono<ServerResponse> createAppointment(ServerRequest serverRequest){
            Mono<AppointmentRequest> appointmentRequestMono = serverRequest.bodyToMono(AppointmentRequest.class);
            return appointmentRequestMono.flatMap( appointmentRequest ->
                    appointmentService.createAppointment(appointmentRequest)
                            .flatMap(appointment ->{
                                String id = appointment.getAppointmentId();
                                appointmentDetailService.createAppointmentDetails(id);
                                return ServerResponse.ok().bodyValue(appointment);
                            })
            );
        }


    public Mono<ServerResponse> getAllAppointments(ServerRequest serverRequest) {
            return ServerResponse.ok().body(appointmentService.getAllAppointments(), List.class).log();
    }

    public Mono<ServerResponse> bookAppointment(ServerRequest request){
            String details = request.queryParam("details").orElse(null);
            String clientId = request.queryParam("clientId").orElse(null);
            String appointmentId = request.queryParam("appointmentId").orElse(null);

        return appointmentService.findAppointment(appointmentId)
                .flatMap( appointmentDto -> {
                    if(appointmentService.checkIfAppointmentSlotCanBeBooked(appointmentDto, clientId)){
                        appointmentDto.setDetails(details);
                        appointmentDto.setClientId(clientId);
                        return appointmentService.bookAppointment(appointmentDto)
                                .flatMap(savedAppointment ->{
                                    return ServerResponse.ok().bodyValue(savedAppointment);
                                });
                    }
                    else {
                        return ServerResponse.badRequest().build();
                    }
                } );
    }
}
