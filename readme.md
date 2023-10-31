# Ridetrack

- simple gRPC-SpringBoot bi-directional stream demo with a ride tracking app
- tech used:
    - gRPC
    - [grpc-spring-boot](https://github.com/grpc-ecosystem/grpc-spring)

# Usage

- generate .proto and install the generated library as module

```bash
cd ./ride-proto
mvn clean install
```

- compile and run server

```bash
cd ./ride-track
mvn clean package
mvn spring-boot:run
```

- complie client and run demo

```bash
cd ./ride-client
mvn clean package
java -cp ./target/ride-client-1.0-SNAPSHOT.jar com.example.demo.Main
```
