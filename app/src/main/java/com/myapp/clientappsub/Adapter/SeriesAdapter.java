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
import com.myapp.clientappsub.GoogleAdsMob;
import com.myapp.clientappsub.MainActivity;
import com.myapp.clientappsub.R;
import com.myapp.clientappsub.SeriesDetFragment;
import com.myapp.clientappsub.SeriesFragment;
import com.myapp.clientappsub.models.Series;

import java.util.ArrayList;

public class SeriesAdapter extends RecyclerView.Adapter<SeriesAdapter.SeriesHolder> {
    ArrayList<Series> series = new ArrayList<>();
    Context context;
    FragmentManager fm;

    private InterstitialAd interstitialAd;

    public SeriesAdapter(ArrayList<Series> series, Context context, FragmentManager fm) {
        this.series = series;
        this.context = context;
        this.fm = fm;
        GoogleAdsMob adsMob = new GoogleAdsMob();
        interstitialAd = adsMob.loadInterAds(context);
    }

    @NonNull
    @Override
    public SeriesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View myView = LayoutInflater.from(parent.getContext()).inflate(R.layout.series_item, parent, false);

        return new SeriesHolder(myView);
    }

    @Override
    public void onBindViewHolder(@NonNull SeriesHolder holder, final int position) {
        Glide.with(context)
                .load(series.get(position).getImageUrl())
                .into(holder.image);

        holder.tvSeriesName.setText(series.get(position).getTitle());

        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SeriesDetFragment detFragment = new SeriesDetFragment();
                detFragment.myModel = series.get(position);
                setFragment(detFragment, position);

                if (interstitialAd.isLoaded()) {
                    interstitialAd.show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return series.size();
    }

    public class SeriesHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView tvSeriesName;

        public SeriesHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image_view);
            tvSeriesName = itemView.findViewById(R.id.tv_series_name);
        }
    }

    public void setFragment(Fragment f, int position) {
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.content_fragment, f);
        ft.commit();
        MainActivity.series = series.get(position);

        if (MainActivity.currentFrag.equals(context.getString(R.string.home_frag))) {
            MainActivity.prevFrag = context.getString(R.string.home_frag);
        }
        else if (MainActivity.currentFrag.equals(context.getString(R.string.series_frag))) {
            MainActivity.prevFrag = context.getString(R.string.series_frag);
        }
        else if (MainActivity.currentFrag.equals(context.getString(R.string.search_frag))) {
            MainActivity.prevFrag = context.getString(R.string.search_frag);
        }

        MainActivity.currentFrag = context.getString(R.string.series_det_frag);
    }
}
