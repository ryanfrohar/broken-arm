#!/bin/bash
echo -n "Which test suite is this case apart of?"
read SUITE
export SUITE
echo -n "Where is the location of the script that runs the JUnit test in the file directory?"
read LOCATION
export LOCATION
python testCaseInit.py 
