package org.example.ridetrack_serivice.application.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TripSummary {
  double distance;
  double charge;
  long time;
}
