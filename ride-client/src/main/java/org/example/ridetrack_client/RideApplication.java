package org.example.ridetrack_client;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EurekaDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RestController;

import java.net.URL;

// test EurekaClient naming resolve and loadbalancing
@SpringBootApplication
@RestController
@Slf4j
public class RideApplication {
    public static void main(String[] args) {
        SpringApplication.run(RideApplication.class, args);
    }

    @Autowired
    private EurekaClient eurekaClient;
    @Bean
    public ManagedChannel grpcChannel(){
        InstanceInfo instance = eurekaClient.getNextServerFromEureka("ride-service", false);
        try {
            // parse connection string to Authority URI
            URL url = new URL(instance.getHomePageUrl());
            return ManagedChannelBuilder.forTarget(url.getAuthority())
                    .usePlaintext()
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
