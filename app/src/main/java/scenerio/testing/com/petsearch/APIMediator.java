package scenerio.testing.com.petsearch;

import com.google.gson.JsonElement;

import org.json.JSONObject;

import retrofit2.Call;

/**
 * Created by harik on 12-02-2019.
 */

public class APIMediator {
    private static APIMediator singletonObj;
    private ApiService mSMUDEApiService;

    private APIMediator() {
        mSMUDEApiService = ApiUtils.getRestAdapter().create(ApiService.class);
    }
    public static APIMediator getInstance() {
        if (singletonObj == null) {
            singletonObj = new APIMediator();
            return singletonObj;
        }
        return singletonObj;
    }

    public Call<JsonElement> fetchMovieList() {
        try {
            // YOU can add body parameters like this
//            JSONObject bodyObj = new JSONObject();
//            bodyObj.put("loginname", username);
//            bodyObj.put(ApiConstants.ksmsFIELD_SOURCETYPE, ApiConstants.ksmsFIELD_SOURCETYPE_VALUE);
//            Call<JsonElement> call = mSMUDEApiService.userLogout(bodyObj);

            Call<JsonElement> call = mSMUDEApiService.fetchmovieList();
            return call;
        } catch (Exception ex) {

        }
        return null;
    }

    public Call<JsonElement> fetchMovieDetails(String id) {
        try {
            Call<JsonElement> call = mSMUDEApiService.fetchMovieDetails(id);
            return call;
        } catch (Exception ex) {

        }
        return null;
    }
}
