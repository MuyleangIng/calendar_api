#!/bin/bash

# Build the project
./gradlew clean build

# Docker setup for deployment
docker build -t my-api .

# Run the container, mapping ports or updating server configurations
docker run -d --name my-api -p 8080:8080 my-api

# Additional configurations as needed
