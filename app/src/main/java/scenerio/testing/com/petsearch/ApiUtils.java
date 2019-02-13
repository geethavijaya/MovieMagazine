package scenerio.testing.com.petsearch;

import android.content.Context;
import android.content.res.Configuration;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by harik on 12-02-2019.
 */

public class ApiUtils {
    public static Retrofit getRestAdapterBuilder(int versionNumber) {
        Retrofit retrofit= null;
        OkHttpClient client = null;
        OkHttpClient.Builder okhttpBuilder = new OkHttpClient.Builder();
        // handling all versions of retrofit
        switch (versionNumber) {
            // we can use different versions based on your api structure
            // ex:- some api want encrypted data like that
            case 1:
                okhttpBuilder.addInterceptor(new NetworkInterceptor());
                okhttpBuilder.retryOnConnectionFailure(true);
                client = okhttpBuilder.connectTimeout(180, TimeUnit.SECONDS)
                        .readTimeout(180, TimeUnit.SECONDS)
                        .writeTimeout(180, TimeUnit.SECONDS).build();
                retrofit = new Retrofit.Builder()
                        .baseUrl(MainActivity.TMDB_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(client)
                        .build();
                break;
            case 2:
                okhttpBuilder.addInterceptor(new NetworkInterceptor());
                okhttpBuilder.retryOnConnectionFailure(true);
                client = okhttpBuilder.connectTimeout(180, TimeUnit.SECONDS)
                        .readTimeout(180, TimeUnit.SECONDS)
                        .writeTimeout(180, TimeUnit.SECONDS).build();
                retrofit = new Retrofit.Builder()
                        .baseUrl(MainActivity.TMDB_URL)
                        .client(client)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                break;
        }
        return retrofit;
    }

    public static  Retrofit getRestAdapter(int versionNumber) {
        Retrofit builder = getRestAdapterBuilder(versionNumber);
        return builder;
    }

    public static Retrofit getRestAdapter() {
        return getRestAdapter(1);
    }
    public static Retrofit getRestAdapterV3() {
        return getRestAdapterV3(3);
    }




    public static Retrofit getRestAdapterV3(int versionNumber) {
        Retrofit restAdapter = getRestAdapterBuilder(versionNumber);
        return restAdapter;
    }


}
