package com.ashish.parseapp;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseInstallation;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("MB3scQ1YDkdhKNacnsiMIjuUqIh1uzP0qSH0vllI")
                // if desired
                .clientKey("rS7PNJtZThLNam0GPIQ1yQ1NyDLtx6CUdlk7PqbF")
                .server("https://parseapi.back4app.com/")
                .build()
        );
        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        installation.put("GCMSenderId", "524358019182");
        installation.saveInBackground();
    }
}
