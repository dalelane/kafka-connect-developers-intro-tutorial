#!/bin/bash

curl -o kafka.tgz -L https://downloads.apache.org/kafka/3.6.1/kafka_2.13-3.6.1.tgz
tar -xf kafka.tgz
mv kafka_2.13-* kafka_home
rm kafka.tgz

echo "KAFKA_HOME=$(pwd)/kafka_home"
