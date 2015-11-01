package de.shutterstock.android.shutterstock.net;

import de.shutterstock.android.shutterstock.content.model.ShutterStockAccessToken;
import de.shutterstock.android.shutterstock.content.model.User;
import retrofit.http.Body;
import retrofit.http.POST;
import retrofit.http.Url;
import rx.Observable;

/**
 * Created by emanuele on 25.10.15.
 */
public interface RestDescriptor {
    @POST("user")
    Observable<String> registerUserCommand(@Body User user);

    @POST("user")
    Observable<String> loginUserCommand(@Body User user);

    @POST
    Observable<ShutterStockAccessToken> getAccessToken(@Url String url, @Body ShutterStockAccessToken.AccessTokenRequest filters);

}
