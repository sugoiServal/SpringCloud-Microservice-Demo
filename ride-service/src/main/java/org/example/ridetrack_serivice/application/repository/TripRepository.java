package org.example.ridetrack_serivice.application.repository;

import org.example.ridetrack_serivice.application.model.RideData;
import lombok.extern.slf4j.Slf4j;

@Slf4j

public class TripRepository {
  public void saveTripData(RideData rideData) {
    log.info("Storing Trip Data {}", rideData);
  }
}
