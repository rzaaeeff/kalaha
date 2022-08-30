#!/bin/bash

set -e;

# cd kalaha-core; ./gradlew test; cd ..;
cd ms-kalaha-api; ./gradlew test build; cd ..;

docker-compose up;