#!/bin/bash

set -e;

cd ms-kalaha-api; ./gradlew test build; cd ..;

docker-compose up;