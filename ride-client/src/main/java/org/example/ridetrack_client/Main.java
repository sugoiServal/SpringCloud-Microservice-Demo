package org.example.ridetrack_client;

// test gRPC communication

public class Main {
    public static void main(String[] args) {
        RideClientService rideClientService = new RideClientService("localhost", 9090);
        rideClientService.callServer();
    }
}
