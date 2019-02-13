package scenerio.testing.com.petsearch;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class MainActivity extends Activity {
    private RecyclerView movieListRecyclerView;
    public static String TMDB_URL = "https://api.themoviedb.org/3/";
    public ProgressDialog mpgLoadingProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        movieListRecyclerView = (RecyclerView)findViewById(R.id.recylerview);
        mpgLoadingProgress = new ProgressDialog(this);
        fetchMovieList();
    }

    private void fetchMovieList() {
        try{
            if(isNetworkAvailable(MainActivity.this)){

                mpgLoadingProgress.setMessage(getString(R.string.loading_text));
                mpgLoadingProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                mpgLoadingProgress.show();

                Call<JsonElement> call = APIMediator.getInstance().fetchMovieList();
                if (call != null) {
                    call.enqueue(new Callback<JsonElement>() {
                        @Override
                        public void onResponse(Call<JsonElement> call, retrofit2.Response<JsonElement> response) {
                            if (response != null) {
                                try {

                                    Log.d("Response>>>", response.body().toString());
                                    JSONObject json = new JSONObject(response.body().toString());
                                    JSONArray resultsArray = json.getJSONArray("results");
                                    List<movieListModelClass> moduleProgressList
                                            = new ArrayList<movieListModelClass>();
                                    for (int index = 0; index < resultsArray.length(); index++) {
                                        movieListModelClass moduleProgress =
                                                new Gson().fromJson(resultsArray.getJSONObject(index).toString(),
                                                        movieListModelClass.class);
                                        //Add module progress to arrayList
                                        moduleProgressList.add(moduleProgress);
                                    }
                                    // sorting
                                    try {
                                        Collections.sort(moduleProgressList, new Comparator<movieListModelClass>() {
                                            @Override
                                            public int compare(movieListModelClass lhs, movieListModelClass rhs) {
                                                return rhs.getVote_average().compareToIgnoreCase(lhs.getVote_average());
                                            }
                                        });
                                    }catch (Exception e){}

                                    populateViews(moduleProgressList);
                                    if(mpgLoadingProgress.isShowing()){
                                        mpgLoadingProgress.dismiss();
                                    }
                                } catch (Exception ex) {
                                    if(mpgLoadingProgress.isShowing()){
                                        mpgLoadingProgress.dismiss();
                                        Toast.makeText(MainActivity.this,"Oops Something went wrong. Please try again later!!",Toast.LENGTH_LONG).show();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<JsonElement> call, Throwable t) {
                            if(mpgLoadingProgress.isShowing()){
                                mpgLoadingProgress.dismiss();
                            }
                            Toast.makeText(MainActivity.this,"Oops Something went wrong. Please try again later!!",Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }else{
                // show toast or message to user for check internet
                Toast.makeText(MainActivity.this,"Please check your Internet Connection",Toast.LENGTH_LONG).show();
            }
        }catch (Exception e){
        }

    }

    public static boolean isNetworkAvailable(Context ctx) {
        // for checking internet connectivity
        ConnectivityManager connectivityManager = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    public void populateViews(List<movieListModelClass> moduleProgressList) {
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        movieListRecyclerView.setLayoutManager(llm);
        movieListAdapter pageAdapter = new movieListAdapter(this, moduleProgressList);
        movieListRecyclerView.setAdapter(pageAdapter);
    }
}
