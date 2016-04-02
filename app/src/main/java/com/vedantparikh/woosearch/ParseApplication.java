package com.vedantparikh.woosearch;

/* Code Referenced from www.parse.com */
import com.parse.Parse;

public class ParseApplication extends android.app.Application {
    static final String APP_KEY = "";
    static final String CLIENT_KEY = "";

    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(this, APP_KEY, CLIENT_KEY);
    }
}
/* Code Reference Over */