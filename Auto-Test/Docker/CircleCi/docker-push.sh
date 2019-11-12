#!/bin/bash
#Script for pushing docker image to personal docker hub repository
docker build -t ryanfrohar/broken-arm:v1.0 .
docker login
docker push ryanfrohar/broken-arm:v1.0
