# SpringCloud-Microservice-Demo


- a collection of common web middlewares implemented as microservices to demonstrate their usage
  - [Elasticsearch Java API Client ](https://www.elastic.co/guide/en/elasticsearch/client/java-api-client/current/index.html): document search/analyze
  - [Spring Cloud Netflix Eureka](https://cloud.spring.io/spring-cloud-netflix/reference/html/): service discovery
  - [Spring Cloud Gateway](https://spring.io/projects/spring-cloud-gateway): API Gateway
  - [Spring Data Redis, Jedis](https://spring.io/projects/spring-data-redis): in-memory key-value storage
  - [Kafka Client](https://kafka.apache.org/documentation/): distributed event streaming platform
  - [Spring AMQP (RabbitMQ)](https://www.rabbitmq.com/getstarted.html): message queue
  - [grpc-spring-boot](https://github.com/grpc-ecosystem/grpc-spring): gRPC Spring Boot


# Usage
### Grpc
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
### TBD
