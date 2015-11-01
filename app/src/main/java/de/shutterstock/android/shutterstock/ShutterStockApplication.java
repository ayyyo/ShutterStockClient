package de.shutterstock.android.shutterstock;

import android.app.Application;

/**
 * Created by emanuele on 31.10.15.
 */
public class ShutterStockApplication extends Application {

    private static ShutterStockApplication sShutterStockApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        sShutterStockApplication = this;
    }

    public static ShutterStockApplication getContext() {
        return sShutterStockApplication;
    }
}
