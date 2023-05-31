package com.example.appointmentservicereactivegradle.handler;

import com.example.appointmentservicereactivegradle.dto.AppointmentDto;
import com.example.appointmentservicereactivegradle.dto.AppointmentRequest;
import com.example.appointmentservicereactivegradle.exception.AppointmentNotFoundException;
import com.example.appointmentservicereactivegradle.service.AppointmentDetailService;
import com.example.appointmentservicereactivegradle.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.EAN;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;

@Component
@Slf4j
@RequiredArgsConstructor
public class AppointmentHandler {
    private final AppointmentService appointmentService;
    private final AppointmentDetailService appointmentDetailService;

        public Mono<ServerResponse> createAppointment(ServerRequest serverRequest){
            Mono<AppointmentRequest> appointmentRequestMono = serverRequest.bodyToMono(AppointmentRequest.class);
            return appointmentRequestMono.flatMap(appointmentService::createAppointment)
                    .flatMap(appointment -> ServerResponse.ok().bodyValue(appointment));
        }


    public Mono<ServerResponse> getAllAppointments(ServerRequest serverRequest) {
            return ServerResponse.ok().body(appointmentService.getAllAppointments(), List.class).log();
    }

    public Mono<ServerResponse> getAppointment(ServerRequest request) {
        String appointmentId = request.pathVariable("id");
        return ServerResponse.ok().body(appointmentService.findAppointment(appointmentId), AppointmentDto.class).log();
    }

    public Mono<ServerResponse> bookAppointment(ServerRequest request){
            String details = request.queryParam("details").orElse(null);
            String clientId = request.queryParam("clientId").orElse(null);
            String appointmentId = request.queryParam("appointmentId").orElse(null);

        return ServerResponse.ok().body(appointmentService.bookAppointment(clientId, appointmentId, details), AppointmentDto.class).log();

    }

    public Mono<ServerResponse> changeAppointmentType(ServerRequest request){
        String appointmentType = request.queryParam("appointmentType").orElse(null);
        String appointmentId = request.queryParam("appointmentId").orElse(null);
        return ServerResponse.ok().body(appointmentService.changeAppointmentType(appointmentType, appointmentId), AppointmentDto.class).log();
    }

    public Mono<ServerResponse> cancelAppointmentFromProviderSide(ServerRequest request){
        String appointmentId = request.pathVariable("id");
        appointmentService.cancelAppointmentFromProviderSide(appointmentId).log();
        return ServerResponse.noContent().build();
    }

    public Mono<ServerResponse> cancelAppointmentFromClientSide(ServerRequest request){
        String appointmentId = request.pathVariable("id");
            return ServerResponse.ok().body(appointmentService.cancelAppointmentFromClientSide(appointmentId), AppointmentDto.class).log();
    }

    public Mono<ServerResponse> getAppointmentsOfCurrentWeek(ServerRequest request){
//        Integer weekOfTheYear = Integer.parseInt(request.pathVariable("week"));
        String role = request.queryParam("role").orElseThrow(AppointmentNotFoundException::new);
        String userId = request.pathVariable("userId");

        return ServerResponse.ok().body(appointmentService.getAppointmentsOfCurrentWeek( role, userId), Flux.class).log();
    }


}
