package de.shutterstock.android.shutterstock.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.concurrent.TimeUnit;

import de.shutterstock.android.shutterstock.R;
import de.shutterstock.android.shutterstock.utilities.SSSharedPreferences;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by emanuele on 31.10.15.
 */
public class SSSplashScreen extends AppCompatActivity {

    private Subscription mSubscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        if (SSSharedPreferences.getOAUTHToken() != null) {
            startActivity(new Intent(SSSplashScreen.this, ShutterStockActivity.class));
            finish();
            return;
        }
        mSubscription = Observable.timer(2, TimeUnit.SECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Long>() {
                               @Override
                               public void call(Long aLong) {
                                   startActivity(new Intent(SSSplashScreen.this, LoginActivity.class));
                                   finish();
                               }
                           }
                );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSubscription != null) {
            mSubscription.unsubscribe();
        }
    }
}
