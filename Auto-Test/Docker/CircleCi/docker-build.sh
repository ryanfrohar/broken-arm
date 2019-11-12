#!/bin/bash
#Script to build docker image locally 
docker login
docker build -t broken-arm:v1.0 .
