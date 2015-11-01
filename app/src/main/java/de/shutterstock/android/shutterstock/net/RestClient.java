package de.shutterstock.android.shutterstock.net;

import android.util.Base64;
import android.util.Log;

import com.squareup.okhttp.Authenticator;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.Proxy;

import de.shutterstock.android.shutterstock.content.model.ShutterStockError;
import retrofit.Converter;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;

/**
 * Created by emanuele on 25.10.15.
 */
public class RestClient {


    private static class BasicAuthInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            final Request original = chain.request();
            final Request.Builder requestBuilder = original.newBuilder()
                    .header("Authorization", "Basic " + BASIC_AUTH_ENCODED)
            .method(original.method(), original.body());
            return chain.proceed(requestBuilder.build());
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
