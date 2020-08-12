package com.myapp.clientappsub.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.myapp.clientappsub.GoogleAdsMob;
import com.myapp.clientappsub.MainActivity;
import com.myapp.clientappsub.R;
import com.myapp.clientappsub.VideoDetFragment;
import com.myapp.clientappsub.models.Movie;

import java.util.ArrayList;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieHolder>{
    ArrayList<Movie> movies = new ArrayList<>();
    Context context;
    FragmentManager fm;

    private InterstitialAd interstitialAd;
    private RewardedVideoAd rewardedVideoAd;

    public MovieAdapter(ArrayList<Movie> movies, Context context, FragmentManager fm) {
        this.movies = movies;
        this.context = context;
        this.fm = fm;
        GoogleAdsMob adsMob = new GoogleAdsMob();
        interstitialAd = adsMob.loadInterAds(context);
        rewardedVideoAd = adsMob.loadRewarded(context);
    }

    @NonNull
    @Override
    public MovieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View myView = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item, parent, false);

        return new MovieHolder(myView);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieHolder holder, final int position) {
        Glide.with(context)
                .load(movies.get(position).getImageUrl())
                .into(holder.image);

        holder.tvMovieName.setText(movies.get(position).getTitle());

        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                VideoDetFragment fragment = new VideoDetFragment();
                fragment.movie = movies.get(position);
                setFragment(fragment);

                if (interstitialAd.isLoaded()) {
                    interstitialAd.show();
                }

                if (rewardedVideoAd.isLoaded()) {
                    rewardedVideoAd.show();
                }

                if (MainActivity.currentFrag.equals(context.getString(R.string.home_frag))) {
                    MainActivity.prevFrag = context.getString(R.string.home_frag);
                }
                else if (MainActivity.currentFrag.equals(context.getString(R.string.movie_frag))) {
                    MainActivity.prevFrag = context.getString(R.string.movie_frag);
                }
                else if (MainActivity.currentFrag.equals(context.getString(R.string.search_frag))) {
                    MainActivity.prevFrag = context.getString(R.string.search_frag);
                }
                MainActivity.currentFrag = context.getString(R.string.video_det_frag);
            }
        });

    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class MovieHolder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView tvMovieName;

        public MovieHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image_view);
            tvMovieName = itemView.findViewById(R.id.tv_movie_name);
        }
    }

    public void setFragment(Fragment f) {
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.content_fragment, f);
        ft.commit();

    }
}
