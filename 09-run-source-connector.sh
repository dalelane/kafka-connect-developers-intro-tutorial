#!/bin/bash

if [ -z "$KAFKA_HOME" ]; then
    KAFKA_HOME="$(pwd)/kafka_home"
fi
echo "KAFKA_HOME is set to $KAFKA_HOME"

cp my-connector-project/target/my-first-connector-0.0.1-jar-with-dependencies.jar connector-jars/.

$KAFKA_HOME/bin/connect-standalone.sh connect.properties source-connector.properties
