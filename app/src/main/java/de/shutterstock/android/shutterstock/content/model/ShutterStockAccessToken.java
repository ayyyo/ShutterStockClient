package de.shutterstock.android.shutterstock.content.model;

import de.shutterstock.android.shutterstock.net.RestClient;

/**
 * Created by emanuele on 30.10.15.
 */
public class ShutterStockAccessToken {

    public static class AccessTokenRequest {
        public final String client_id = RestClient.CLIENT_ID;
        public final String client_secret = RestClient.CLIENT_SECRET;
        public final String grant_type = "authorization_code";
        public final String redirect_uri = RestClient.REDIRECT_URI;
        public final String code;

        public AccessTokenRequest(final String code) {
            this.code = code;
        }
    }

    public String access_token;
    public int expires_in;
    public String refresh_token;
    public String token_type;

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("access_token: ")
                .append(access_token != null ? access_token : "")
                .append(", ")
                .append("expires_in: ")
                .append(String.valueOf(expires_in))
                .append(", ")
                .append("refresh_token: ")
                .append(refresh_token != null ? refresh_token : "")
                .append(", ")
                .append("token_type: ")
                .append(token_type != null ? token_type : "");
        return builder.toString();
    }
}
