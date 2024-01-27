# kafka-connect-dev-tutorial

Skeleton project to help a Java developer create their first Kafka Connect connector.

## Setup a dev environment

```shell
./00-check-prereqs.sh
```

Checks you have Java and Maven needed for the rest of the instructions

```shell
./01-download-kafka.sh
```

Downloads and unzips Kafka.

```shell
./02-run-kafka.sh
```

## Try an existing connector

```shell
./03-consume-file-topic.sh
```

Start listening to the topic

```shell
./04-run-file-connector.sh
```

Run the source connector that puts the contents of `input-file.txt` to the Kafka topic.

See the file contents show up in the `03-consume-file-topic.sh` output.

Add additional lines to the file to confirm the connector is still running.

## Run a custom sink connector

```shell
./05-build-connectors.sh
```

Compile the custom connector.

Edit `sink-connector.properties` with an API key and channel ID from Slack

```shell
./06-run-sink-connector.sh
```

Run the connector.

```shell
./07-produce-my-topic.sh
```

Produce messages to the Kafka topic.

See the messages show up in your Slack channel.

## Run a custom source connector

```shell
./05-build-connectors.sh
```

Compile the custom connector.

Edit `source-connector.properties` with an API key from weatherapi.com

```shell
./08-consume-my-topic.sh
```

Start listening to the topic

```shell
./09-run-source-connector.sh
```

Run the connector.

See weather events show up on the Kafka topic.

