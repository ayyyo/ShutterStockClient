package de.shutterstock.android.shutterstock.net;

import android.util.Base64;
import android.util.Log;

import com.google.gson.Gson;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import de.shutterstock.android.shutterstock.content.model.ShutterStockAccessToken;
import de.shutterstock.android.shutterstock.content.model.ShutterStockError;
import de.shutterstock.android.shutterstock.utilities.RxJavaUtils;
import de.shutterstock.android.shutterstock.utilities.SSSharedPreferences;
import retrofit.Converter;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;

/**
 * Created by emanuele on 25.10.15.
 */
public class RestClient {


    public static class BasicAuthInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            final Request original = chain.request();
            final String authorizationString = SSSharedPreferences.getOAUTHToken() == null
                    ? "Basic " + BASIC_AUTH_ENCODED
                    : "Bearer " + SSSharedPreferences.getOAUTHToken();
            final Request request = original.newBuilder()
                    .addHeader("Authorization", authorizationString)
                    .method(original.method(), original.body()).build();
            return chain.proceed(request);
        }
    }

    public static class OAUTHInterceptor implements Interceptor {

        private static final String LOG_TAG = OAUTHInterceptor.class.getSimpleName();
        private AtomicBoolean isRefreshed = new AtomicBoolean(false);
        private Semaphore mSemaphore = new Semaphore(1);
        private AtomicInteger mCounter = new AtomicInteger();

        @Override
        public Response intercept(Chain chain) throws IOException {
            final Request original = chain.request();
            final Response response = chain.proceed(chain.request());
            if (response.code() == 401) {
                Log.w(LOG_TAG, "response code: " + response.code());
                mCounter.incrementAndGet();
                while (!isRefreshed.get()) {
                    try {
                        Log.w(LOG_TAG, "401, entering: " + Thread.currentThread().getName() + " original " + original);
                        mSemaphore.acquire();
                        if (isRefreshed.get()) {
                            Log.w(LOG_TAG, "refreshed: " + Thread.currentThread().getName() + " continues ");
                            continue;
                        }
                        isRefreshed.set(refreshOAUTHToken(chain));
                        if (!isRefreshed.get()) {
                            return null;
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        if (!isRefreshed.get()) {
                            return null;
                        }
                    } finally {
                        mSemaphore.release();
                        if (mCounter.getAndDecrement() <= 0 && isRefreshed.get()) {
                            isRefreshed.set(false);
                        }
                        Log.w(LOG_TAG, "finally. Threads still waiting: " + mCounter.intValue());
                    }
                }
                final Request request = new Request.Builder()
                        .url(original.url())
                        .addHeader("Authorization", "Bearer " + SSSharedPreferences.getOAUTHToken())
                        .method(original.method(), original.body()).build();
                Log.w(LOG_TAG, "401, executing next request: " + request);
                return chain.proceed(request);
            }
            return response;
        }

        private boolean refreshOAUTHToken(Chain chain) throws IOException {
            if (RxJavaUtils.isUiThread()) {
                Log.w(LOG_TAG, "chain " + chain + " called on ui thread");
                //return false;
            }
            Log.w(LOG_TAG, "401 " + Thread.currentThread().getName() + " is refreshing the oauth token " + (SSSharedPreferences.getRefreshToken()));
            ShutterStockAccessToken.RefreshTokenRequest refresh
                    = new ShutterStockAccessToken.RefreshTokenRequest(SSSharedPreferences.getRefreshToken());
            final Gson gson = new Gson();
            Request request = new Request.Builder()
                    .post(RequestBody.create(MediaType.parse("application/json"), gson.toJson(refresh)))
                    .url(ACCESS_TOKEN_URL).build();
            Response tokenResponse = chain.proceed(request);
            if (tokenResponse == null) {
                return false;
            }
            ShutterStockAccessToken token = gson.fromJson(tokenResponse.body().string(), ShutterStockAccessToken.class);
            if (token == null) {
                return false;
            }
            Log.w(LOG_TAG, "401 " + token.access_token + " new token");
            SSSharedPreferences.setRefreshToken(token.refresh_token);
            SSSharedPreferences.setOAUTHToken(token.access_token);
            return true;
        }
    }


    public static final String REDIRECT_URI = "http://www.blackbelt.com/";
    public static final String CLIENT_ID = "37de3ef873a3e1fedff6";
    public static final String CLIENT_SECRET = "3754c6886581be2419f709b3fd50867a2cd55d71";
    public static final String BASE_URL = "https://api.shutterstock.com/v2/";
    public static final String OAUTH_URL = "https://accounts.shutterstock.com/oauth/authorize";
    public static final String ACCESS_TOKEN_URL = "https://accounts.shutterstock.com/oauth/access_token";
    private static RestClient mApiClient;

    public static final String BASIC_AUTH_ENCODED;

    static {
        BASIC_AUTH_ENCODED = Base64.encodeToString((CLIENT_ID + ":" + CLIENT_SECRET).getBytes(), Base64.NO_WRAP);
    }

    private final Retrofit mRestAdapter;
    private RestDescriptor mApiDescriptor;
    private Converter<ResponseBody, ShutterStockError> mErrorConverter;

    private RestClient() {
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.interceptors().add(new BasicAuthInterceptor());
        okHttpClient.interceptors().add(new OAUTHInterceptor());

        mRestAdapter = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mApiDescriptor = mRestAdapter.create(RestDescriptor.class);
    }

    public synchronized static RestClient getRestClient() {
        if (mApiClient == null) {
            mApiClient = new RestClient();
        }
        return mApiClient;
    }

    public synchronized static RestDescriptor getApiDescriptor() {
        if (mApiClient == null) {
            mApiClient = new RestClient();
        }
        return mApiClient.mApiDescriptor;
    }

    public synchronized Converter<ResponseBody, ShutterStockError> getErrorResponseConverter() {
        if (mErrorConverter == null) {
            mErrorConverter =
                    mRestAdapter.responseConverter(ShutterStockError.class, new Annotation[0]);
        }
        return mErrorConverter;
    }

    public ShutterStockError convertError(ResponseBody errorBody) throws IOException {
        if (mErrorConverter == null) {
            mErrorConverter =
                    mRestAdapter.responseConverter(ShutterStockError.class, new Annotation[0]);
        }
        return mErrorConverter.convert(errorBody);
    }
}
