#!/bin/bash

if [ -z "$KAFKA_HOME" ]; then
    KAFKA_HOME="$(pwd)/kafka_home"
fi
echo "KAFKA_HOME is set to $KAFKA_HOME"

# Function to kill both scripts
kill_scripts() {
    echo "Terminating zookeeper and kafka..."
    pkill -P $$  # Kill all child processes
    exit 0
}

# Trap the termination signal
trap "kill_scripts" SIGINT SIGTERM

# Run zookeeper in the background
$KAFKA_HOME/bin/zookeeper-server-start.sh $KAFKA_HOME/config/zookeeper.properties &

# give it a moment to start up
sleep 30

# Run kafka in the background
$KAFKA_HOME/bin/kafka-server-start.sh $KAFKA_HOME/config/server.properties &

# block until ctrl-c
wait
