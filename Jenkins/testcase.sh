#!/bin/bash
#Interactive shell script that allows developpers to input a row into the testcases table 
echo -n "Which test suite is this case apart of?"
read SUITE
export SUITE
echo -n "Where is the location of the script that runs the JUnit test in the file directory?"
read LOCATION
export LOCATION
#Passing varaibles using environment variables because parameter passing was extremely buggy
#This is okay because it does not permanently add the environment variable to the system but only to this particular shell session 

#Create the tables if not created, and add the testcase with the path 
python testCaseInit.py 
