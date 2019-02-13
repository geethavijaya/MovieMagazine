package scenerio.testing.com.petsearch;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

/**
 * Created by harik on 13-02-2019.
 */

public class movieListAdapter  extends RecyclerView.Adapter<movieListAdapter.MovieData> {


    private List<movieListModelClass> mListOfElements;
    private Activity mActivity;

    public movieListAdapter(Activity activity, List<movieListModelClass> elementLists) {
        mListOfElements = elementLists;
        mActivity = activity;

    }
    @NonNull
    @Override
    public movieListAdapter.MovieData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview, parent, false);
        MovieData data = new MovieData(v);
        return data;
    }

    @Override
    public void onBindViewHolder(@NonNull movieListAdapter.MovieData holder,final int position) {
        //holder.imageview
        holder.moviename.setText(mListOfElements.get(position).getTitle());
        holder.description.setText(mListOfElements.get(position).getOverview());
        holder.rating.setText(mListOfElements.get(position).getVote_average());
        holder.date.setText(mListOfElements.get(position).getRelease_date());
        holder.language.setText(mListOfElements.get(position).getOriginal_language());
        String imgUrl = "https://image.tmdb.org/t/p/w400"+mListOfElements.get(position).getPoster_path();
        Glide.with(mActivity).load(imgUrl)
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.imageview);
        holder.mRootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, MovieInfo.class);
                intent.putExtra("ElementId", mListOfElements.get(position).getId());
                mActivity.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return mListOfElements.size();
    }

    public class MovieData extends RecyclerView.ViewHolder {

        CardView cardView;
        RelativeLayout relativelayouttop,bottomlayoutdown;
        ImageView imageview,proceed;
        TextView moviename,description,rating,date,language;
        View mRootView;


        MovieData(View itemView) {
            super(itemView);
            mRootView = itemView;
            cardView = (CardView) itemView.findViewById(R.id.card_view);
            relativelayouttop = (RelativeLayout) itemView.findViewById(R.id.relativelayout);
            bottomlayoutdown = (RelativeLayout) itemView.findViewById(R.id.bottomlayout);
            imageview = (ImageView)itemView.findViewById(R.id.imageview);
            moviename = (TextView) itemView.findViewById(R.id.moviename);
            description = (TextView)itemView.findViewById(R.id.description);
            rating = (TextView)itemView.findViewById(R.id.rating);
            date = (TextView)  itemView.findViewById(R.id.date) ;
            language = (TextView)  itemView.findViewById(R.id.language) ;
            proceed = (ImageView)itemView.findViewById(R.id.proceed);

        }
    }
}
