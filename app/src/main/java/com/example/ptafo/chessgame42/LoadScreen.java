package com.example.ptafo.chessgame42;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;


public class LoadScreen extends Activity {
    protected void onCreate(Bundle chessgame42) {
        super.onCreate(chessgame42);
        setContentView(R.layout.load_screen);

        Thread timer = new Thread() {
            public void run() {
                try {
                    sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    Intent chessintent = new Intent("com.apps.chessGame42");
                    startActivity(chessintent);

                }
            }

        };
    timer.start();
    }
}