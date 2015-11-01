package de.shutterstock.android.shutterstock.activities;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import de.shutterstock.android.shutterstock.R;
import de.shutterstock.android.shutterstock.content.model.ShutterStockAccessToken;
import de.shutterstock.android.shutterstock.net.RestClient;
import de.shutterstock.android.shutterstock.utilities.SSSharedPreferences;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by emanuele on 25.10.15.
 */
public class LoginActivity extends AppCompatActivity implements Observer<ShutterStockAccessToken> {

    private class OauthWebViewClient extends WebViewClient {

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            showProgressDialog();
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            hideProgressDialog();
            super.onPageFinished(view, url);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.e(getClass().getSimpleName(), " url " + url);
            Uri uri = Uri.parse(url);
            if (uri.getQueryParameter("code") != null) {
                requestAccessToken(uri.getQueryParameter("code"));
                Log.e(getClass().getSimpleName(), "uri " + uri.getQueryParameter("code"));
            }
            return super.shouldOverrideUrlLoading(view, url);
        }
    }

    private Subscription mSubscription;
    private ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        WebView webView = (WebView) findViewById(R.id.oauth_webview);
        webView.setWebViewClient(new OauthWebViewClient());
        Uri.Builder builder = Uri.parse(RestClient.OAUTH_URL)
                .buildUpon()
                .appendQueryParameter("client_id", RestClient.CLIENT_ID)
                .appendQueryParameter("redirect_uri", RestClient.REDIRECT_URI)
                .appendQueryParameter("state", "code");
        webView.loadUrl(builder.toString());
    }

    private void requestAccessToken(String code) {
        showProgressDialog();
        mSubscription = RestClient.getApiDescriptor().getAccessToken(RestClient.ACCESS_TOKEN_URL,
                new ShutterStockAccessToken.AccessTokenRequest(code))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this);
    }


    @Override
    public void onCompleted() {
        hideProgressDialog();
    }

    @Override
    public void onError(Throwable e) {
        Snackbar.make(findViewById(android.R.id.content),
                e.getMessage() != null ? e.getMessage() : getString(android.R.string.httpErrorBadUrl),
                Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onNext(ShutterStockAccessToken shutterStockAccessToken) {
        Log.d(getClass().getSimpleName(), "  response  " + shutterStockAccessToken);
        SSSharedPreferences.setOAUTHToken(shutterStockAccessToken.access_token);
        SSSharedPreferences.setRefreshToken(shutterStockAccessToken.refresh_token);
    }

    private void hideProgressDialog() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

    private void showProgressDialog() {
        if (mDialog == null || !mDialog.isShowing()) {
            mDialog = ProgressDialog.show(this, null, null);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSubscription != null) {
            mSubscription.unsubscribe();
        }
    }
}