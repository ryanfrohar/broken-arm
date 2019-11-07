package com.example.arm;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {



    Button button;
    TextView textView2;
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

        button=(Button)findViewById(R.id.button);
        //textView2=(textView2)findViewById((R.id.textView2));


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                file=new Intent(Intent.ACTION_GET_CONTENT);
                file.setType("*/*");
                startActivityForResult(file, 10);



            }

        });



    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode){
            case 10:
                if(requestCode==RESULT_OK){
                    String path = data.getData().getPath();
                    textView2.setText(path);

                }
                break;
        }
    }
}
