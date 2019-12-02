package com.example.arm;

import android.net.Uri;

import org.junit.Test;

import static org.junit.Assert.*;

public class MainActivityTest {

    @Test
    public void onCreate() {

    }

    @Test
    public void onActivityResult() {


    }


    @Test
    public void fileContents(){

        MainActivity main = new MainActivity();
        String expectedOutput = "hi";
        String output = main.fileContents(main.testUri);
        assertEquals(expectedOutput,output);


    }
}