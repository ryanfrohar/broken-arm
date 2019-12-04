#!/bin/bash
#Script that runs testcases from database
export TCID=$1
export NOW=$(date)
export RESULTS="$($2)"
python testResult.py &
