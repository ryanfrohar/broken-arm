# broken-arm
3rd year Computer Systems Project at Carleton University
**Android App Initialization**
1) Before downloading the Application a *Lending Hand* onto an Android Phone configure the UDP class to have the correct IP address of the Raspberry Pi that will be receiving the commands

2) Once Downloaded onto an phone click the *Insert* button that will open the file explorer. This allows the user to select a text file saved on the phone to have it read by the application

3)Once the file is selected the user will be able to see the contents of the selected text file on screen, the user will be able to choose from a drop down what font they would like to have it displayed as

4)Once satisfied with the selected text file click the *send* button that will send the message in bytes to the designated Raspberry Pi with its selected font

**Automated Testing Initialization**

Once a JUnit test is ready to be integrated, the user should add the script to the /Jenkins/Tests directory. Run the interactive testcase.sh script in the Jenkins directory to add the script to the testcases database (an example of a testcase is already in the database). If you have configured it correctly, Jenkins will automatically include you JUnit test the next time it builds!
