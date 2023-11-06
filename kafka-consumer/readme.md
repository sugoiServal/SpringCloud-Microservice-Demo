```bash
docker-compose up -d
docker exec --interactive --tty kafka_cluster-kafka-0-1 /bin/bash   # start interactive in node-0

# node-list
  # kafka_cluster-kafka-0-1
  # kafka_cluster-kafka-1-1
  # kafka_cluster-kafka-2-1

# network
  # kafka_cluster_default
  
# volume 
 # kafka_cluster_kafka_0_data
 # kafka_cluster_kafka_0_data
 # kafka_cluster_kafka_0_data
```