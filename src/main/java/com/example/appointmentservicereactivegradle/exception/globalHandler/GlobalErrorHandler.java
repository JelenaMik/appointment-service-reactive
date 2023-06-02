package com.example.appointmentservicereactivegradle.exception.globalHandler;

import com.example.appointmentservicereactivegradle.exception.AppointmentDetailNotFoundException;
import com.example.appointmentservicereactivegradle.exception.AppointmentHasAlreadyBookedException;
import com.example.appointmentservicereactivegradle.exception.AppointmentNotFoundException;
import com.example.appointmentservicereactivegradle.exception.BookingTimeOverlappingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class GlobalErrorHandler implements ErrorWebExceptionHandler {

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {

        DataBufferFactory bufferFactory = exchange.getResponse().bufferFactory();
        var errorMessage = bufferFactory.wrap(ex.getClass().toString().getBytes());
        if(ex instanceof AppointmentDetailNotFoundException){
            exchange.getResponse().setStatusCode(HttpStatus.NOT_FOUND);
            return exchange.getResponse().writeWith(Mono.just(errorMessage));
        }

        if(ex instanceof AppointmentHasAlreadyBookedException){
            exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
            return exchange.getResponse().writeWith(Mono.just(errorMessage));
        }

        if(ex instanceof AppointmentNotFoundException){
            exchange.getResponse().setStatusCode(HttpStatus.NOT_FOUND);
            return exchange.getResponse().writeWith(Mono.just(errorMessage));
        }

        if(ex instanceof BookingTimeOverlappingException){
            exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
            return exchange.getResponse().writeWith(Mono.just(errorMessage));
        }

        exchange.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        return exchange.getResponse().writeWith(Mono.just(errorMessage));
    }
}
