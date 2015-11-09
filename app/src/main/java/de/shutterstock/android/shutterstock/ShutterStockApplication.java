package de.shutterstock.android.shutterstock;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.backends.okhttp.OkHttpImagePipelineConfigFactory;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.squareup.okhttp.OkHttpClient;

import de.shutterstock.android.shutterstock.net.RestClient;

/**
 * Created by emanuele on 31.10.15.
 */
public class ShutterStockApplication extends Application {

    private static ShutterStockApplication sShutterStockApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        sShutterStockApplication = this;
        setupFresco();
        //SSSharedPreferences.setOAUTHToken("1/eyJjbGllbnRfaWQiOiIzN2RlM2VmODczYTNlMWZlZGZmNiIsInJlYWxtIjoiY3VzdG9tZXIiLCJzY29wZSI6InVzZXIudmlldyIsInVzZXJuYW1lIjoiYmJsYWNrYmVsdCIsInVzZXJfaWQiOjE0Mzg1ODA3Miwib3JnYW5pemF0aW9uX2lkIjpudWxsLCJjdXN0b21lcl9pZCI6Mzg2NzY1MDYsImV4cCI6MTQ0Njk4NjQxOH0.BPGqHc6kBdY-4K_6KVmQGQGRoQjaDr4DE3-BwF3b6df7iHZsgf38vDYyn8kB1sHUabMOmf2yWwJrV9S1rGJ7Bg");
    }

    private void setupFresco() {
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.interceptors().add(new RestClient.BasicAuthInterceptor());
        okHttpClient.interceptors().add(new RestClient.OAUTHInterceptor());
        ImagePipelineConfig config = OkHttpImagePipelineConfigFactory
                .newBuilder(ShutterStockApplication.getContext(), okHttpClient).build();
        Fresco.initialize(ShutterStockApplication.getContext(), config);
    }

    public static ShutterStockApplication getContext() {
        return sShutterStockApplication;
    }
}
