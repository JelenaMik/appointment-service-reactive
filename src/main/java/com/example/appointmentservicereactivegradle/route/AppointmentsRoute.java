package com.example.appointmentservicereactivegradle.route;

import com.example.appointmentservicereactivegradle.handler.AppointmentHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.path;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class AppointmentsRoute {
    @Bean
    public RouterFunction<ServerResponse> appointmentRoute(AppointmentHandler handler){
        return route()
                .nest(path("api/v1/appointments"), builder ->
                                builder
                                        .POST("", handler::createAppointment)
                                        .GET("", handler::getAllAppointments)
                                        .GET("/{id}", handler::getAppointment)
                                        .PUT("/book", handler::bookAppointment)
                                        .PUT("/change-type", handler::changeAppointmentType)
                                        .DELETE("/{id}", handler::cancelAppointmentFromProviderSide)
                                        .PUT("/{id}", handler::cancelAppointmentFromClientSide)
                                        .GET("/all/{userId}", handler::getAppointmentsOfCurrentWeek)
                        ).build();
    }
}
