package com.example.arm;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
    TextView source;
    Intent file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Spinner dropDown = (Spinner) findViewById(R.id.spinner); //initialize spinner

        ArrayAdapter<String> list = new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.Fonts));//create a list with the value we put in strings.xml

        list.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);//configures our list into a drop down
        dropDown.setAdapter(list);// set this drop-down into the spinner

        button = (Button)findViewById(R.id.button);
        source = (TextView) findViewById((R.id.source)) ;


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                file=new Intent(Intent.ACTION_GET_CONTENT);
                file.setType("*/*");
                startActivityForResult(file, 10);



            }

        });



    }




    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
            String info = null;
            if(data != null){
                Uri uri = data.getData();
                String path = uri.getPath();
                path = path.substring(path.indexOf(":") + 1);
                BufferedReader br = null;

                try {
                    br = new BufferedReader(new InputStreamReader(Objects.requireNonNull(getContentResolver().openInputStream(uri))));
                    info = (br.readLine());

                } catch (IOException e) {
                    e.printStackTrace();
                }
                /*
                if(path.contains("emulated")){
                    path = path.substring(path.indexOf("0") + 1);
                }*/
                Toast.makeText(this, "" + path, Toast.LENGTH_SHORT).show();
                source.setText(info);

            }

    }

}
