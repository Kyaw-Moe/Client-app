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

import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.myapp.clientappsub.GoogleAdsMob;
import com.myapp.clientappsub.MainActivity;
import com.myapp.clientappsub.R;
import com.myapp.clientappsub.VideoDetFragment;
import com.myapp.clientappsub.models.Episode;

import java.util.ArrayList;

public class EpisodeAdapter extends RecyclerView.Adapter<EpisodeAdapter.EpisodeHolder> {
    ArrayList<Episode> episodes = new ArrayList<>();
    Context context;
    FragmentManager fm;
    private InterstitialAd interstitialAd;
    private RewardedVideoAd rewardedVideoAd;

    public EpisodeAdapter(ArrayList<Episode> episodes, Context context, FragmentManager fm) {
        this.episodes = episodes;
        this.context = context;
        this.fm = fm;
        GoogleAdsMob adsMob = new GoogleAdsMob();
        interstitialAd = adsMob.loadInterAds(context);
        rewardedVideoAd = adsMob.loadRewarded(context);
    }

    @NonNull
    @Override
    public EpisodeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View myView = LayoutInflater.from(parent.getContext()).inflate(R.layout.episode_item, parent, false);
        return new EpisodeHolder(myView);
    }

    @Override
    public void onBindViewHolder(@NonNull EpisodeHolder holder, final int position) {

        holder.tvSr.setText(position + 1 + "");
        holder.tvName.setText(episodes.get(position).getName());
        holder.play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                VideoDetFragment fragment = new VideoDetFragment();
                fragment.episode = episodes.get(position);
                setFragment(fragment);

                if (interstitialAd.isLoaded()) {
                    interstitialAd.show();
                }

                if (rewardedVideoAd.isLoaded()) {
                    rewardedVideoAd.show();
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return episodes.size();
    }

    public class EpisodeHolder extends RecyclerView.ViewHolder {

        TextView tvSr, tvName;
        ImageView play;

        public EpisodeHolder(@NonNull View itemView) {
            super(itemView);

            tvSr = itemView.findViewById(R.id.sr);
            tvName = itemView.findViewById(R.id.ep_name);
            play = itemView.findViewById(R.id.play_btn);

        }
    }

    public void setFragment(Fragment f) {
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.content_fragment, f);
        ft.commit();
        MainActivity.mediumFrag = MainActivity.prevFrag;
        MainActivity.prevFrag = context.getString(R.string.series_det_frag);
        MainActivity.currentFrag = context.getString(R.string.video_det_frag);
    }
}
