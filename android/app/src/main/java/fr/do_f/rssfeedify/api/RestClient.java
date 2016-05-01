package fr.do_f.rssfeedify.api;

import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by do_f on 03/04/16.
 */
public class RestClient {

    private static String       token;
    private static ApiService   mRestClient;
    private static ApiService   mTokenRestClient;

    private final static String WEBSERVICE_HOST = "http://romain-zanchi.com:4498/api/";

    static {
        setupRestClient();
    }

    private RestClient()
    {

    }

    public static ApiService get()
    {
        return mRestClient;
    }

    public static ApiService get(String _token)
    {
        if (token == null
                || !token.equals(_token))
            setupRestClientToken(_token);
        return mTokenRestClient;
    }

    private static void setupRestClient()
    {
        Log.d("RestClient", "setupRestClient");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(WEBSERVICE_HOST)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mRestClient = retrofit.create(ApiService.class);
    }

    private static void setupRestClientToken(final String _token)
    {
        Log.d("RestClient", "setupRestClientToken");
        token = _token;
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();

                // Customize the request
                Request request = original.newBuilder()
                        .header("Content-Type", "application/json")
                        .header("User-token", _token)
                        .method(original.method(), original.body())
                        .build();

                Response response = chain.proceed(request);

                // Customize or return the response
                return response;
            }
        });

        OkHttpClient client = httpClient.build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(WEBSERVICE_HOST)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        mTokenRestClient = retrofit.create(ApiService.class);
    }

}
