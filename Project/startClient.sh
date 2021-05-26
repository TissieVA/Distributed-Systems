#!/bin/bash

killall java

rm Client/replicates/*

mvn clean
mvn package

cd Client
java -jar target/Client-1.0-SNAPSHOT.jar

