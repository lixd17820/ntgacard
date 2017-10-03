package com.ntga.card;

import android.app.Application;

import io.objectbox.BoxStore;

/**
 * Created by lixiaodong on 2017/10/1.
 */

public class App extends Application {

    private BoxStore boxStore;

    @Override
    public void onCreate() {
        super.onCreate();
        boxStore = MyObjectBox.builder().androidContext(App.this).build();
    }

    public BoxStore getBoxStore() {
        return boxStore;
    }
}
