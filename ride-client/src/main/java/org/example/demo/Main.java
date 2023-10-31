package org.example.demo;

import org.example.ridetrack_client.RideClient;

public class Main {
    public static void main(String[] args) {
        RideClient rideClient = new RideClient("localhost", 9090);
        rideClient.callServer();
    }
}