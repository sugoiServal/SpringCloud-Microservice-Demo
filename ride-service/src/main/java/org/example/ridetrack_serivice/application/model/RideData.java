package org.example.ridetrack_serivice.application.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@AllArgsConstructor
@ToString
public class RideData {
  String driverId;
  String rideId;
  double latitude;
  double longitude;
}
