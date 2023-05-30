package com.example.appointmentservicereactivegradle;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@SpringBootApplication
//@ComponentScan(excludeFilters =
//		{@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = AppointmentRoute.class)})
public class AppointmentServiceReactiveGradleApplication {

	public static void main(String[] args) {
		SpringApplication.run(AppointmentServiceReactiveGradleApplication.class, args);
	}

}
