#!/bin/bash
echo -n "Which test suite is this case apart of?"
read SUITE
export SUITE
echo -n "Where is the location of this testcase in the file directory?"
read LOCATION
export LOCATION
python test.py 
