package org.example.ridetrack_client;

import com.example.RideProto.RideProto;
import com.example.RideProto.TripServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.text.DecimalFormat;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

@Slf4j
public class RideClient {
    private final String host;
    private final int port;
    public RideClient(String host, int port) {
        this.host = host;
        this.port = port;
    }
    @SneakyThrows
    public void callServer() {

        log.info("Calling Server..");
        ManagedChannel managedChannel = ManagedChannelBuilder
                .forAddress(this.host, this.port)
                .usePlaintext()
                .build();
        TripServiceGrpc.TripServiceStub tripServiceStub = TripServiceGrpc.newStub(managedChannel);

        StreamObserver<RideProto.TripDataRequest> tripDataRequestStreamObserver = tripServiceStub
                .sendTripData(new StreamObserver<RideProto.TripSummaryResponse>() {

                    @Override
                    public void onNext(RideProto.TripSummaryResponse tripSummaryResponse) {
                        DecimalFormat df = new DecimalFormat("0.00");
                        log.info(
                                "Trip Summary : distance {}, charge {}, time remaining {} minutes",
                                df.format(tripSummaryResponse.getDistance()),
                                df.format(tripSummaryResponse.getCharge()),
                                df.format((double) tripSummaryResponse.getTime() / 60));
                    }

                    @Override
                    public void onError(Throwable cause) {
                        log.error("Error occurred, cause {}", cause.getMessage());
                    }

                    @Override
                    public void onCompleted() {
                        log.info("Stream completed");
                    }

                });

        // Dummy input: Create stream of random 1000 calls with random lat and long,
        // with delay of 1 sec
        // This should has be generate in real-time from sensor

        IntStream.range(0, 600)
                .mapToObj(
                        n -> {
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                            return RideProto.TripDataRequest.newBuilder()
                                    .setRideType(
                                            RideProto.RideType.newBuilder()
                                                    .setDriverId("Driver_1")
                                                    .setRideId("Ride_1")
                                                    .build())
                                    .setLatitude(ThreadLocalRandom.current().nextDouble(-90, 90))
                                    .setLongitude(ThreadLocalRandom.current().nextDouble(-180, 180))
                                    .build();
                        })
                .forEach(tripDataRequestStreamObserver::onNext);
        log.info("Calling complete..");
        tripDataRequestStreamObserver.onCompleted();
        Thread.sleep(30000);
    }
}
