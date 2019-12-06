#!/bin/bash
#Script that runs testcases from database
export TCID=$1
export NOW=$(date)
export RESULTS="$($2)"

#Publish the TCID, NOW and RESULTS variables to the testresults database
python testResult.py
