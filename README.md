# broken-arm
3rd year Computer Systems Project at Carleton University to draw text on a white board!

## **Android App Initialization**

1) Before downloading the Application a *Lending Hand* onto an Android Phone configure the UDP class to have the correct IP address of the Raspberry Pi that will be receiving the commands

2) Once Downloaded onto an phone click the *Insert* button that will open the file explorer. This allows the user to select a text file saved on the phone to have it read by the application

3) Once the file is selected the user will be able to see the contents of the selected text file on screen, the user will be able to choose from a drop down what font they would like to have it displayed as

4) Once satisfied with the selected text file click the *send* button that will send the message in bytes to the designated Raspberry Pi with its selected font

## **Automated Testing Initialization**

Once a JUnit test is ready to be integrated, the user should add the script to the /Jenkins/Tests directory. Run the interactive testcase.sh script in the Jenkins directory to add the script to the testcases database (an example of a testcase is already in the database). If you have configured it correctly, Jenkins will automatically include you JUnit test the next time it builds!

## **Hardware Assembly and Software Set Up**

1.	Turn on the raspberry pi with the electrical switch on the left-hand side where the mains power plug is connected
1.	Execute [cnc-computer.jar](https://github.com/ryanfrohar/broken-arm/blob/master/Java/out/artifacts/cnc_computer_jar/cnc-computer.jar) from the raspberry PI. The application will automatically connect to GRBL and can be checked for success in the command line output.
1.	Mark corner points of a square on the drawing surface of 80cm x 80 cm. This value is set in mm in the “dist.x” and “dist.y” entry of [config.properties](https://github.com/ryanfrohar/broken-arm/blob/master/Java/resources/config.properties)
1.	Stick the two suction cup assemblies to the top left and right corners of the marked corners where the hook on the assembly is positioned directly over the marked point.
1.	Un spool the strings from the motor assembly and hook the strings onto the suction cup assembly where the string coming from motor marked X hooks onto the top left hook and the string coming from motor marked Y hooks onto the top right hook.
1.	Attach a marker to the tool head assembly with the tip of the marker aligned with the edge of the assembly while making sure that the tip does not contact the drawing surface.
1.	Hook the ends of the strings onto the tool head assembly.
1.	Rotate the unpowered motors such that the tool head’s pen lines up with the bottom left corner of the marked square.
1.	Flip the right-hand switch to power the motors on and hold the tool head in position. 

The machine can now be sent requests to draw text using the android application. 
The command line output can also be used to access different operations such as 
`dev argument` to send the argument directly into GRBL or `text font message` to start a drawing of the message. Typing in help will list all available commands in the CLI. 

See [here](https://github.com/ryanfrohar/broken-arm/tree/master/Python/F-Engrave-1.68_src/fonts) for the names of avialabel fonts.

The machine also has physical buttons that can pause (blue), resume (green), stop (yellow), and emergency stop (red) the current operation.

## **Code Road Map**

* [Android](https://github.com/ryanfrohar/broken-arm/tree/master/Android): Android application code
* [Java](https://github.com/ryanfrohar/broken-arm/tree/master/Java): cnc computer code
* [Jenkins](https://github.com/ryanfrohar/broken-arm/tree/master/Jenkins): Automated testing code
* [Python](https://github.com/ryanfrohar/broken-arm/tree/master/Python/F-Engrave-1.68_src): 3ed party text to gcode script from [scorchworks](http://www.scorchworks.com/Fengrave/fengrave.html)

The arduino is running version [1.1h](https://github.com/gnea/grbl/releases/tag/v1.1h.20190825) of [grbl](https://github.com/grbl/grbl) for stepper motor control
