package org.example.ridetrack_serivice;

import org.example.ridetrack_serivice.application.repository.TripRepository;
import org.example.ridetrack_serivice.application.service.TripSummaryService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class RideTrackApplication {

    public static void main(String[] args) {
        SpringApplication.run(RideTrackApplication.class, args);
    }
    @Bean
    public TripRepository tripRepository() {
        return new TripRepository();
    }

    @Bean
    public TripSummaryService tripSummaryService() {
        return new TripSummaryService();
    }
}
