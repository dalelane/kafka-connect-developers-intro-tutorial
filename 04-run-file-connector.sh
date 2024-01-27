#!/bin/bash

if [ -z "$KAFKA_HOME" ]; then
    KAFKA_HOME="$(pwd)/kafka_home"
fi
echo "KAFKA_HOME is set to $KAFKA_HOME"

cp $KAFKA_HOME/libs/connect-file*.jar connector-jars/.

$KAFKA_HOME/bin/connect-standalone.sh connect.properties file-connector.properties
