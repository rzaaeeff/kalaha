#!/bin/bash

set -e;

docker-compose down --volumes --rmi local;
rm -rf mongo-db/volume/
