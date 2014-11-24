package com.example.fracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class Splash extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		
		 /** set time to splash out */
        final int welcomeScreenDisplay = 6000;
        /** create a thread to show splash up to splash time */
        Thread welcomeThread = new Thread() {
 
            int wait = 0;
             
 
            @Override
            public void run() {
                try {
                    super.run();
                   
                    /**
                     * use while to get the splash time. Use sleep() to increase
                     * the wait variable for every 100L.
                     */
                    while (wait < welcomeScreenDisplay) {
                        sleep(100);
                        wait += 100;
                    }
                } catch (Exception e) {
                    while (wait < welcomeScreenDisplay) {
                        try {
                            sleep(100);
                        } catch (InterruptedException e1) {
                            // TODO Auto-generated catch block
                            e1.printStackTrace();
                        }
                        wait += 100;
                    }
                    System.out.println("EXc=" + e);
                } finally {
                    /**
                     * Called after splash times up. Do some action after splash
                     * times up. Here we moved to another main activity class
                     */
                    startActivity(new Intent(Splash.this,
                            MapActivity.class));
                    finish();
                }
            }
        };
        welcomeThread.start();
 
    }
     
	}

