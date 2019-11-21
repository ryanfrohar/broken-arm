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
    String fonts[] = {"courier","something","something2"};
    ArrayAdapter<String> adapter;

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

        dropDown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){

                    case 0:
                        fontSelected = "courier";
                        break;

                    case 1:
                        fontSelected = "something";
                        break;

                    case 2:
                        fontSelected = "something2";
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // .This configs my insert button

                file = new Intent(Intent.ACTION_GET_CONTENT);
                file.setType("*/*");
                startActivityForResult(file, 10);

            }

        });

        send.setOnClickListener(new View.OnClickListener() {// This configs my send button, it will send what ever is currently in Source over UDP
            @Override
            public void onClick(View v) {
                //get it to send source
                //String SendingInfo = source.toString();
                String command = "TEXT " + fontSelected + " " + message;
                new UDP().execute(command);
                

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
                message = info;

            }

        }
    }
/*
    public void UDP (String command) {

        DatagramSocket socket = null;
        try {
            // Convert the arguments first, to ensure that they are valid
            final InetAddress host = InetAddress.getByName("127.0.0.1");
            final int port = 8080;
            final int numSend = 1;
            socket = new DatagramSocket();

            while (true) {
                //if (.length() == 0) break;// Here i will insert what ever i wanna send

                byte[] what = command.getBytes();
                //byte[] fontUsed = font.getBytes();
                //byte[] data = text.getBytes();
                //byte[] message = new byte[what.length + fontUsed.length + data.length];

                //System.arraycopy(what,0,message,0,what.length);
                //System.arraycopy(fontUsed,0,message,what.length,fontUsed.length);
                //System.arraycopy(data,0,message,fontUsed.length,data.length);



                for (int i = 0; i < numSend; i++) {
                    DatagramPacket packet1 = new DatagramPacket(what, what.length, host, port);
                    socket.send(packet1);

                }
            }
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            if (socket != null)
                socket.close();
        }
    }
*/


}

