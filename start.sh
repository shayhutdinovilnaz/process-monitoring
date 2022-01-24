#!/usr/bin/env bash

set -eu

cd deployment

docker-compose up -d

echo "Service is starting... Please wait few minutes until DB start"
