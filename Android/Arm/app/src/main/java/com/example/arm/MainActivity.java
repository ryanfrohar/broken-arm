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

public class MainActivity extends AppCompatActivity {


    Button button;
    Button send;
    ImageButton upArrow;
    ImageButton downArrow;
    ImageButton rightArrow;
    ImageButton leftArrow;
    TextView source;
    Intent file;
    Intent udp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Spinner dropDown = (Spinner) findViewById(R.id.spinner); //initialize spinner

        ArrayAdapter<String> list = new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.Fonts));//create a list with the value we put in strings.xml

        list.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);//configures our list into a drop down
        dropDown.setAdapter(list);// set this drop-down into the spinner

        button = (Button)findViewById(R.id.button);// This variable is what works the insert button on the interface
        source = (TextView) findViewById((R.id.source));// This variable will output what was in the text file
        send = (Button) findViewById((R.id.send));// This variable will be the button that will send the text info over UDP to the Raspberri Pi
        upArrow = (ImageButton)findViewById(R.id.upArrow); //This variable will control the up arrow
        downArrow = (ImageButton)findViewById(R.id.downArrow); //This varible will control the down arrow
        rightArrow = (ImageButton)findViewById(R.id.rightArrow);// This variable wil control the right arrow
        leftArrow = (ImageButton)findViewById(R.id.leftArrow);// This varible will control the left arrow



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // .This configs my insert button

                file=new Intent(Intent.ACTION_GET_CONTENT);
                file.setType("*/*");
                startActivityForResult(file, 10);

            }

        });

        send.setOnClickListener(new View.OnClickListener() {// This configs my send button, it will send what ever is currently in Source over UDP
            @Override
            public void onClick(View v) {
                //get it to send source
                source.setText("Send");
            }
        });


        upArrow.setOnClickListener(new View.OnClickListener() {// upArrow will send a string saying to go up
            @Override
            public void onClick(View v) {
                //Get it to send a up string
                source.setText("Up");
            }
        });

        downArrow.setOnClickListener(new View.OnClickListener() {// upArrow will send a string saying to go up
            @Override
            public void onClick(View v) {
                //Get it to send a up string
                source.setText("Down");
            }
        });


        rightArrow.setOnClickListener(new View.OnClickListener() {// upArrow will send a string saying to go up
            @Override
            public void onClick(View v) {
                //Get it to send a up string
                source.setText("Right");
            }
        });

        leftArrow.setOnClickListener(new View.OnClickListener() {// upArrow will send a string saying to go up
            @Override
            public void onClick(View v) {
                //Get it to send a up string
                source.setText("Left");
            }
        });


    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String info = null;

        if (requestCode == 10) {


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

            }

        }
    }

}
