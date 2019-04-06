# Simple Commands

## Makefile

* Create containers
```bash
make up
```

* Shutdown & Clean up containers
```bash
make down
```

* Scale # of taskManagers
```bash
make scale NUM=${NUMBER}
```

## Docker commands

* Show existing containers
```bash
docker ps
```

* Run command on existing container
```bash
docker exec -it ${container_id_or_name} ${command}
```

## Kafka commands

* List topics
```bash
/opt/kafka/bin/kafka-topics.sh --list --zookeeper zookeeper:2181
```

* Consume topic
```bash
/opt/kafka/bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 \
  --topic ${TOPIC} \
  [--from-beginning] \
  [--isolation-level read_committed]
```

## Flink commands

* Trigger savepoint
```bash
/opt/flink/bin/flink savepoint :jobId [:targetDirectory]
# `targetDirectory` please use this prefix: `file:///savepoints`
```

* Cancel job with savepoint
```bash
/opt/bin/flink cancel -s [:targetDirectory] :jobId
# `targetDirectory` please use this prefix: `file:///savepoints`
```