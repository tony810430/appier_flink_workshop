# Appier Flink Workshop

## 事前準備

### Part 1

- [Setup for Labs](https://training.ververica.com/devEnvSetup.html)
- [Lab 1](https://training.ververica.com/intro/rideCleansing.html)

### Part 2 (給沒參加過上次 flink workshop 的閱讀功課)

- [What is Apache Flink](https://flink.apache.org/flink-architecture.html)
- [Flink’s Dataflow Programming Model](https://ci.apache.org/projects/flink/flink-docs-release-1.7/concepts/programming-model.html)
- [Flink’s Distributed Runtime Environment](https://ci.apache.org/projects/flink/flink-docs-release-1.7/concepts/runtime.html)
- [Data Streaming Fault Tolerance](https://ci.apache.org/projects/flink/flink-docs-release-1.7/internals/stream_checkpointing.html)
- [Event Time and Watermarks](https://ci.apache.org/projects/flink/flink-docs-release-1.7/dev/event_time.html#event-time-and-watermarks) (會有點難懂，有問題請找我討論)
  ([Reference1](https://conferences.oreilly.com/strata/strata-eu-2016/public/schedule/detail/49605))
  ([Reference2](https://www.youtube.com/watch?v=TWxSLmkWPm4))


## Workshop 1: Transforming Data

### 課前閱讀

- [Basic API Overview](https://ci.apache.org/projects/flink/flink-docs-release-1.7/dev/api_concepts.html)
  (讀到 Specifying Transformation Functions)
- [Operators Overview](https://ci.apache.org/projects/flink/flink-docs-release-1.7/dev/stream/operators/)
- [Using Managed Keyed State](https://ci.apache.org/projects/flink/flink-docs-release-1.7/dev/stream/state/state.html#using-managed-keyed-state)
   (State TTL 和之後的不用讀)

### 課程素材

- Slides: [DataStream API basic](https://www.slideshare.net/dataArtisans/apache-flink-datastream-api-basics)
- Slides: [Working with State](https://www.slideshare.net/dataArtisans/apache-flink-training-working-with-state)
- [Lab 2](https://training.ververica.com/exercises/rideEnrichment-flatmap.html)


## Workshop 2: Time and Analytics

### 課前閱讀

- [Windows](https://ci.apache.org/projects/flink/flink-docs-release-1.7/dev/stream/operators/windows.html)
  (略過 Triggers, Evictors)
- [Generating Timestamps/Watermarks](https://ci.apache.org/projects/flink/flink-docs-release-1.7/dev/event_timestamps_watermarks.html)
  (Optional)

### 課程素材

- Slides: [Windows](https://www.slideshare.net/dataArtisans/apache-flink-training-datastream-api-windows-time)
- Slides: [Time and Watermarks](https://www.slideshare.net/dataArtisans/apache-flink-training-time-and-watermarks)
- [Lab 3](https://training.ververica.com/exercises/hourlyTips.html)


## Workshop 3: Event-driven Apps

### 課前閱讀

- [Process Function](https://ci.apache.org/projects/flink/flink-docs-release-1.7/dev/stream/operators/process_function.html)
- [Side Outputs](https://ci.apache.org/projects/flink/flink-docs-release-1.7/dev/stream/side_output.html)

### 課程素材

- Slides: [ProcessFunction](https://www.slideshare.net/dataArtisans/apache-flink-training-datastream-api-processfunction)
- Slides: [Low-level Stream Operations](https://www.slideshare.net/dataArtisans/apache-flink-training-lowlevel-stream-operations)
- [Lab 4](https://training.ververica.com/exercises/rideEnrichment-processfunction.html)


## Workshop 4: Fault Tolerance & Programming Model Sum-up

### 課前閱讀

- 無

### 課程素材

- Slides: [Fault tolerance](https://www.slideshare.net/dataArtisans/apache-flink-training-datastream-api-state-failure-recovery)
- [State Backends](https://training.ververica.com/state-backends.html)
- [State Backends (flink docs)](https://ci.apache.org/projects/flink/flink-docs-release-1.7/ops/state/state_backends.html)
- [Production Readiness Checklist](https://ci.apache.org/projects/flink/flink-docs-release-1.7/ops/production_ready.html)
- [From API to JobGraph](https://zhuanlan.zhihu.com/p/22736103)


## Workshop 5: Operations & Monitoring (checkpoint, savepoint, metrics)

### 課前閱讀

- [Savepoints](https://ci.apache.org/projects/flink/flink-docs-release-1.7/ops/state/savepoints.html)
- [Kafka Connector](https://ci.apache.org/projects/flink/flink-docs-release-1.7/dev/connectors/kafka.html) (Optional)

### 課程素材

- Slides: [Connectors](https://www.slideshare.net/dataArtisans/apache-flink-training-datastream-api-connectors)
- Slides: [Metrics & Monitoring](https://www.slideshare.net/dataArtisans/apache-flink-training-metrics-monitoring)
- [Checkpoints](https://ci.apache.org/projects/flink/flink-docs-release-1.7/ops/state/checkpoints.html)
- [CLI](https://ci.apache.org/projects/flink/flink-docs-release-1.7/ops/cli.html)
- [REST API](https://ci.apache.org/projects/flink/flink-docs-release-1.7/monitoring/rest_api.html)
- [Metrics](https://ci.apache.org/projects/flink/flink-docs-release-1.7/monitoring/metrics.html)


## Workshop 6: Testing - Basic & Advanced

### 課前閱讀

- wip

### 課程素材

- [Testing](https://ci.apache.org/projects/flink/flink-docs-release-1.7/dev/stream/testing.html) (Basic)
- Testing (Advanced) - wip


## Workshop ??: Exactly-Once Function

### 課前閱讀

- wip

### 課程素材

- wip
- Custom Source/Sink
- [Non-keyed State](https://ci.apache.org/projects/flink/flink-docs-release-1.7/dev/stream/state/state.html#using-managed-operator-state)


## Workshop ??: Connectors & Async I/O

### 課前閱讀

- wip

### 課程素材

- wip
- [Connectors](https://ci.apache.org/projects/flink/flink-docs-release-1.7/dev/connectors/)
- [Async I/O](https://ci.apache.org/projects/flink/flink-docs-release-1.7/dev/stream/operators/asyncio.html)


## Follow-up Topics

- [State Migration](https://ci.apache.org/projects/flink/flink-docs-release-1.7/ops/upgrading.html#application-state-compatibility) & [Schema Evolution](https://ci.apache.org/projects/flink/flink-docs-release-1.7/dev/stream/state/schema_evolution.html)
- [Table API & SQL](https://ci.apache.org/projects/flink/flink-docs-release-1.7/dev/table/)
