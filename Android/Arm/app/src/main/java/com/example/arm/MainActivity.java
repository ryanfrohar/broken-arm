package com.example.arm;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Objects;
import java.net.*;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {


    Button button;
    Button send;
    ImageButton upArrow;
    ImageButton downArrow;
    ImageButton rightArrow;
    ImageButton leftArrow;
    TextView source;
    Intent file;
    String message;
    Spinner dropDown;
    String fontSelected; // what font was selected in dropdown
    String fonts[] = {"courier","greek_ol","iso8859-11", "unicode", "kochimincho"};
    ArrayAdapter<String> adapter;
    Uri testUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        button = (Button) findViewById(R.id.button);// This variable is what works the insert button on the interface
        source = (TextView) findViewById((R.id.source));// This variable will output what was in the text file
        send = (Button) findViewById((R.id.send));// This variable will be the button that will send the text info over UDP to the Raspberri Pi
        upArrow = (ImageButton) findViewById(R.id.upArrow); //This variable will control the up arrow
        downArrow = (ImageButton) findViewById(R.id.downArrow); //This varible will control the down arrow
        rightArrow = (ImageButton) findViewById(R.id.rightArrow);// This variable wil control the right arrow
        leftArrow = (ImageButton) findViewById(R.id.leftArrow);// This varible will control the left arrow
        dropDown = (Spinner)findViewById(R.id.spinner);// This varible will hold all drop down variables
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,fonts);//Setting my array of fonts in the dropdown
        dropDown.setAdapter(adapter);


        // This is what you can select from the drop down
        dropDown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){

                    case 0:
                        fontSelected = "courier";
                        break;

                    case 1:
                        fontSelected = "greek_ol";
                        break;

                    case 2:
                        fontSelected = "iso8859-11";
                        break;

                    case 3:
                        fontSelected = "unicode";
                        break;
                    case 4:
                        fontSelected = "kochimincho";
                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //Below is the configerations of most buttons on the interface
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                file = new Intent(Intent.ACTION_GET_CONTENT);
                file.setType("*/*");
                startActivityForResult(file, 10);

            }

        });

        send.setOnClickListener(new View.OnClickListener() {// This configs my send button, it will send what ever is currently in Source over UDP
            @Override
            public void onClick(View v) {
                String command = "TEXT " + fontSelected + " " + message + " ";
                new UDP().execute(command);// UDP is a class that takes care of sending the UDP message to the Raspberry Pi
                

            }
        });


        upArrow.setOnClickListener(new View.OnClickListener() {// upArrow will send a string saying to go up
            @Override
            public void onClick(View v) {
                String command = "MOVE UP";
                new UDP().execute(command);
            }
        });

        downArrow.setOnClickListener(new View.OnClickListener() {// upArrow will send a string saying to go up
            @Override
            public void onClick(View v) {
                String command = "MOVE DOWN";
                new UDP().execute(command);
            }
        });


        rightArrow.setOnClickListener(new View.OnClickListener() {// upArrow will send a string saying to go up
            @Override
            public void onClick(View v) {
                String command = "MOVE RIGHT";
                new UDP().execute(command);
            }
        });

        leftArrow.setOnClickListener(new View.OnClickListener() {// upArrow will send a string saying to go up
            @Override
            public void onClick(View v) {
                String command = "MOVE LEFT";
                new UDP().execute(command);
            }
        });


    }
    // Below takes care of opening the phones files when INSERT is selected and reads the file
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String info = null;

        if (requestCode == 10) {// if insert button is pressed


            if (data != null) {
                Uri uri = data.getData();
                String path = uri.getPath();
                path = path.substring(path.indexOf(":") + 1);
                BufferedReader br = null;

                try {
                    br = new BufferedReader(new InputStreamReader(Objects.requireNonNull(getContentResolver().openInputStream(uri))));
                    info = (br.readLine());
                    String temp = info;

                    while (temp != null) {
                        temp = br.readLine();
                        if (temp == null) {
                            //Do noething
                        } else {
                            info = info + "\n";
                            info = info + temp;
                        }

                    }
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Toast.makeText(this, "" + path, Toast.LENGTH_SHORT).show();
                source.setText(info);
                message = info;
                testUri = uri;

            }

        }
    }

//This is for my testing but not finished
@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public String fileContents(Uri uri) {
    BufferedReader br = null;
    String contents = null;


    try {
        br = new BufferedReader(new InputStreamReader(Objects.requireNonNull(getContentResolver().openInputStream(uri))));
        contents = (br.readLine());
        String temp = contents;

        while (temp != null) {
            temp = br.readLine();
            if (temp == null) {
                //Do noething
            } else {
                contents = contents + "\n";
                contents = contents + temp;
            }

        }
        br.close();
    } catch (IOException e) {
        e.printStackTrace();
    }
    return contents;
}






}

