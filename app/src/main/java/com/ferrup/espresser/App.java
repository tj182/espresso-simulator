package com.ferrup.espresser;

import android.app.Application;

import com.ferrup.espresser.model.Simulator;

public class App extends Application {
    private static App sInstance;

    private Simulator simulator;

    public static App getInstance() {
        if (sInstance == null)
            sInstance = new App();
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        sInstance = this;
        simulator = new Simulator(this);
    }

    public Simulator getSimulator() {
        return simulator;
    }

}
