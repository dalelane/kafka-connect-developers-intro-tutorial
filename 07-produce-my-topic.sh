#!/bin/bash

if [ -z "$KAFKA_HOME" ]; then
    KAFKA_HOME="$(pwd)/kafka_home"
fi
echo "KAFKA_HOME is set to $KAFKA_HOME"

$KAFKA_HOME/bin/kafka-console-producer.sh \
    --bootstrap-server localhost:9092 \
    --topic SLACK.DEMO
