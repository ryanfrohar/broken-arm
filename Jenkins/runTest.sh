#!/bin/sh
#Script that runs testcases from database

export TCID=$1
export LOCATION=$2
export NOW=$(date)



python testResult.py &