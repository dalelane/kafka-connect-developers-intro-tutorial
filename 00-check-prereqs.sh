#!/bin/bash

if ! command -v java &> /dev/null; then
    echo "Java is NOT installed"
    exit 1
fi
if ! command -v javac &> /dev/null; then
    echo "Java compiler/sdk is NOT installed"
    exit 1
fi
if ! command -v mvn &> /dev/null; then
    echo "Maven is NOT installed"
    exit 1
fi

echo "Java and Maven are available"
