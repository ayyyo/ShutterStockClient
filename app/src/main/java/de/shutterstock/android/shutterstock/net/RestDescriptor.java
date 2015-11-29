package de.shutterstock.android.shutterstock.net;


import java.util.Map;

import de.shutterstock.android.shutterstock.content.model.Category;
import de.shutterstock.android.shutterstock.content.model.Contributor;
import de.shutterstock.android.shutterstock.content.model.Image;
import de.shutterstock.android.shutterstock.content.model.PagedResponse;
import de.shutterstock.android.shutterstock.content.model.ShutterStockAccessToken;
import de.shutterstock.android.shutterstock.content.model.User;
import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.QueryMap;
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

    @GET("images/search")
    Observable<PagedResponse<Image>> getImages(@QueryMap Map<String, String> queryMap);

    @GET("contributors/{contributorId}")
    Observable<Contributor> getContributor(@Path("contributorId") final String contributorId);

    @POST
    Call<ShutterStockAccessToken> refreshAccessToken(@Url String url, @Body ShutterStockAccessToken.RefreshTokenRequest filters);
}
