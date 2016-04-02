package com.vedantparikh.woosearch;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;


public class DisplayActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        /* Code Referenced From www.stackoverflow.com */
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                Intent DisplayHome = new Intent(DisplayActivity.this, MainActivity.class);
                DisplayActivity.this.startActivity(DisplayHome);
                DisplayActivity.this.finish();
            }
        }, 1000);
    /* Code Referenced Over */
    }
}
