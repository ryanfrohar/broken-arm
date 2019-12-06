#!/bin/bash
#java compilation command below have jar files relative to the broken-arm directory therefore cd into it
cd /home/pi/git-projects/broken-arm/

#Run JUnit test 
java -cp .:Java/out/artifacts/cnc_computer_jar/cnc-computer.jar:Java/lib/hamcrest-all-1.3.jar:Java/lib/junit-4.12.jar:Java/lib/hamcrest-core-1.3.jar org.junit.runner.JUnitCore test.ca.carleton.sysc.util.PacketDataSupportTest

