package scenerio.testing.com.petsearch;

import android.app.Activity;
import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class MovieInfo extends Activity {
    private String movieId;
    private ProgressDialog mpgLoadingProgress;
    private ImageView imageview,back;
    private TextView description,time,date,rating,genere,language,budget,revenue,moviename;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_info);
        mpgLoadingProgress = new ProgressDialog(this);
        imageview = (ImageView)findViewById(R.id.image);
        description = (TextView)findViewById(R.id.description);
        time = (TextView)findViewById(R.id.duration);
        date = (TextView)findViewById(R.id.date);
        rating = (TextView)findViewById(R.id.rate);
        genere = (TextView)findViewById(R.id.theme);
        language = (TextView)findViewById(R.id.lan);
        budget = (TextView)findViewById(R.id.budgetvalue);

        revenue = (TextView)findViewById(R.id.revenuevalue);
        moviename = (TextView)findViewById(R.id.moviename);
        back = (ImageView)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



        try{
            if(getIntent().getStringExtra("ElementId")!=null){
                movieId = getIntent().getStringExtra("ElementId");
                fetchmovieDetailsWithId(movieId);
            }
        }catch (Exception e){
            Toast.makeText(MovieInfo.this,"Oops Something went wrong. Please try again later!!",Toast.LENGTH_LONG).show();
        }
    }

    private void fetchmovieDetailsWithId(String movieId) {
        try{
            // you can call this method in separate java class instead of puttin in Main Activity
            if(MainActivity.isNetworkAvailable(MovieInfo.this)){
                // make a retrofit call
                // we are sorting through api itself Like this
                //discover/movie?api_key=12ad0f96b3e29f0bd52f579a4dd9e034&language=en-US&sort_by=popularity.desc

                mpgLoadingProgress.setMessage(getString(R.string.loading_text));
                mpgLoadingProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                mpgLoadingProgress.show();

                Call<JsonElement> call = APIMediator.getInstance().fetchMovieDetails(movieId);
                if (call != null) {
                    call.enqueue(new Callback<JsonElement>() {
                        @Override
                        public void onResponse(Call<JsonElement> call, retrofit2.Response<JsonElement> response) {
                            if (response != null) {
                                try {

                                    Log.d("Response>>>", response.body().toString());
                                    JSONObject jsonObject = new JSONObject(response.body().toString());
                                    description.setText(jsonObject.getString("overview"));
                                    String timeset = jsonObject.getString("runtime")+" minutes";
                                    time.setText(timeset);
                                    rating.setText(jsonObject.getString("vote_average"));
                                    language.setText(jsonObject.getString("original_language")+"");
                                    budget.setText(jsonObject.getInt("budget")+"");
                                    revenue.setText(jsonObject.getInt("revenue")+"");
                                    moviename.setText(jsonObject.getString("title"));
                                    JSONArray genericArray = jsonObject.getJSONArray("genres");
                                    String generic = null;
                                    List<genericClass> moduleProgressList
                                            = new ArrayList<genericClass>();
                                    for (int index = 0; index < genericArray.length(); index++) {
                                        genericClass moduleProgress =
                                                new Gson().fromJson(genericArray.getJSONObject(index).toString(),
                                                        genericClass.class);
                                        //Add module progress to arrayList
                                        moduleProgressList.add(moduleProgress);
                                    }
                                    for (int i=0;i<moduleProgressList.size();i++){
                                        if(i==0){
                                            generic = moduleProgressList.get(i).getName();
                                        }else {
                                            generic = generic + ", " + moduleProgressList.get(i).getName();
                                        }
                                    }
                                    Log.d("names",generic);
                                    genere.setText(generic);

                                    String imgUrl = "https://image.tmdb.org/t/p/w400"+jsonObject.getString("backdrop_path");
                                    Glide.with(MovieInfo.this).load(imgUrl)
                                            .thumbnail(0.5f)
                                            .crossFade()
                                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                                            .into(imageview);





                                    if(mpgLoadingProgress.isShowing()){
                                        mpgLoadingProgress.dismiss();
                                    }
                                } catch (Exception ex) {
                                    if(mpgLoadingProgress.isShowing()){
                                        mpgLoadingProgress.dismiss();
                                        Toast.makeText(MovieInfo.this,"Oops Something went wrong. Please try again later!!",Toast.LENGTH_LONG).show();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<JsonElement> call, Throwable t) {
                            if(mpgLoadingProgress.isShowing()){
                                mpgLoadingProgress.dismiss();
                            }
                            Toast.makeText(MovieInfo.this,"Oops Something went wrong. Please try again later!!",Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }else{
                // show toast or message to user for check internet
                Toast.makeText(MovieInfo.this,"Please check your Internet Connection",Toast.LENGTH_LONG).show();
            }
        }catch (Exception e){
        }
    }


}
