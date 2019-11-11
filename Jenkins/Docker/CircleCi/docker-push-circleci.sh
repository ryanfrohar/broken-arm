#!/bin/bash
#Pushing docker image to circle ci docker hub repository for continous integration
docker build -t circleci/broken-arm:v1.0 
docker login
docker push circleci/broken-arm:v1.0
