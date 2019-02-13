package scenerio.testing.com.petsearch;

import com.google.gson.JsonElement;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by harik on 12-02-2019.
 */

public interface ApiService {
    // write your all api calls
    @GET("discover/movie?api_key=12ad0f96b3e29f0bd52f579a4dd9e034&sort_by=popularity.desc")
    public Call<JsonElement> fetchmovieList();


    @GET("movie/{id}?api_key=12ad0f96b3e29f0bd52f579a4dd9e034")
    public Call<JsonElement> fetchMovieDetails(@Path("id") String token);
}
