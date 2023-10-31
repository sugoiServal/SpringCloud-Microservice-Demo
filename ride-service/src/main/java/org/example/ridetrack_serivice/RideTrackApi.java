package org.example.ridetrack_serivice;

import com.example.RideProto.RideProto;
import com.example.RideProto.TripServiceGrpc;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import org.example.ridetrack_serivice.application.model.RideData;
import org.example.ridetrack_serivice.application.model.TripSummary;
import org.example.ridetrack_serivice.application.repository.TripRepository;
import org.example.ridetrack_serivice.application.service.TripSummaryService;

@RequiredArgsConstructor
@GrpcService
@Slf4j
public class RideTrackApi extends TripServiceGrpc.TripServiceImplBase {
    public static final int ESTIMATED = 1;
    public static final int FINAL = 2;

    private final TripRepository tripRepository;
    private final TripSummaryService tripSummaryService;

    @Override
    public StreamObserver<RideProto.TripDataRequest> sendTripData(StreamObserver<RideProto.TripSummaryResponse> responseObserver) {
        return new StreamObserver<RideProto.TripDataRequest>() {
            private String rideId;

            @Override
            public void onNext(RideProto.TripDataRequest request) {

                tripRepository.saveTripData(
                        new RideData(
                                request.getRideType().getDriverId(),
                                request.getRideType().getRideId(),
                                request.getLatitude(),
                                request.getLongitude()));

                // Send a response to the client after every call
                this.rideId = request.getRideType().getRideId();
                TripSummary tripSummary = tripSummaryService.getTripSummary(rideId);
                responseObserver.onNext(
                        RideProto.TripSummaryResponse.newBuilder()
                                .setDistance(tripSummary.getDistance())
                                .setCharge(tripSummary.getCharge())
                                .setTime((int) tripSummary.getTime())
                                .setStatus(ESTIMATED)
                                .build());

            }

            @Override
            public void onError(Throwable t) {
                log.error("Error while processing request ");
            }

            @Override
            public void onCompleted() {
                // Once Trip is completed then generate Trip summary
                var tripSummary = tripSummaryService.getTripSummary(rideId);
                responseObserver.onNext(
                        RideProto.TripSummaryResponse.newBuilder()
                                .setDistance(tripSummary.getDistance())
                                .setCharge(tripSummary.getCharge())
                                .setTime((int) tripSummary.getTime())
                                .setStatus(FINAL)
                                .build());
                responseObserver.onCompleted();
                log.info("Request completed");
            }
        };
    }
}
