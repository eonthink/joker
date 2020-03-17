package com.demo.joker.ui;

import android.app.Application;

import com.demo.libnetwork.ApiService;

public class JokerApplication  extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ApiService.init("http://123.56.232.18:8080/serverdemo", null);
    }
}
