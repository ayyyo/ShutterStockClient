package de.shutterstock.android.shutterstock.net;


import de.shutterstock.android.shutterstock.content.model.Category;
import de.shutterstock.android.shutterstock.content.model.PagedResponse;
import de.shutterstock.android.shutterstock.content.model.ShutterStockAccessToken;
import de.shutterstock.android.shutterstock.content.model.User;
import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;
import retrofit.http.Url;
import rx.Observable;

/**
 * Created by emanuele on 25.10.15.
 */
public interface RestDescriptor {

    @GET("images/categories")
    Observable<PagedResponse<Category>> getCategories();


    @POST("user")
    Observable<String> registerUserCommand(@Body User user);

    @POST
    Observable<ShutterStockAccessToken> getAccessToken(@Url String url, @Body ShutterStockAccessToken.AccessTokenRequest filters);

    @GET("user")
    Observable<User> getUser();

    @POST
    Call<ShutterStockAccessToken> refreshAccessToken(@Url String url, @Body ShutterStockAccessToken.RefreshTokenRequest filters);


}
