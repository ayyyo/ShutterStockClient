package de.shutterstock.android.shutterstock.utilities;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import de.shutterstock.android.shutterstock.ShutterStockApplication;

/**
 * Created by emanuele on 31.10.15.
 */
public class SSSharedPreferences {

    private static final String SHUTTER_STOCK_PREFS_NAME = "SHUTTER_STOCK_PREFS_NAME";
    private static final String SSC_OAUTH_TOKEN_KEY = "SSC_OAUTH_TOKEN_KEY";
    private static final String SSC_REFRESH_TOKEN_KEY = "SSC_REFRESH_TOKEN_KEY";
    private static SharedPreferences sSharedPreferences;
    private static SharedPreferences.Editor sEditor;

    static {
        new SSSharedPreferences();
    }

    private SSSharedPreferences() {
        final Context context = ShutterStockApplication.getContext();
        sSharedPreferences = context.getSharedPreferences(SHUTTER_STOCK_PREFS_NAME, Activity.MODE_PRIVATE);
        sEditor = sSharedPreferences.edit();
    }

    public static String getOAUTHToken() {
        return sSharedPreferences.getString(SSC_OAUTH_TOKEN_KEY, null);
    }

    public static void setOAUTHToken(final String token) {
        sEditor.putString(SSC_OAUTH_TOKEN_KEY, token).apply();
    }

    public static String getRefreshToken() {
        return sSharedPreferences.getString(SSC_REFRESH_TOKEN_KEY, null);
    }

    public static void setRefreshToken(final String token) {
        sEditor.putString(SSC_REFRESH_TOKEN_KEY, token).apply();
    }
}
