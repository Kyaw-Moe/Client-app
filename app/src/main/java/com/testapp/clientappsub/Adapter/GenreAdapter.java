package com.testapp.clientappsub.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.testapp.clientappsub.HomeFragment;
import com.testapp.clientappsub.R;
import com.testapp.clientappsub.models.Genre;
import com.testapp.clientappsub.models.Movie;
import com.testapp.clientappsub.models.Series;

import java.util.ArrayList;

import project.aamir.sheikh.circletextview.CircleTextView;

public class GenreAdapter extends RecyclerView.Adapter<GenreAdapter.MyItems> {

    Context context;
    ArrayList<Genre> mArrayList;
    FragmentManager fm;

    public GenreAdapter(ArrayList<Genre> mArrayList, Context context, FragmentManager fm) {

        this.mArrayList = mArrayList;
        this.context = context;
        this.fm = fm;
    }


    @Override
    public MyItems onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item, parent, false);
        MyItems items = new MyItems(v);
        return items;
    }

    @Override
    public void onBindViewHolder(MyItems holder, final int position) {
        holder.mTextView.setText(mArrayList.get(position).getName());
        holder.mCircleTextView.setCustomText(mArrayList.get(position).getName());
        holder.mCircleTextView.setSolidColor(position);
        holder.mCircleTextView.setTextColor(Color.WHITE);
        holder.mCircleTextView.setCustomTextSize(18);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final CollectionReference refMovie = db.collection("movies");
        final CollectionReference refSeries = db.collection("series");

        holder.mCircleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mArrayList.get(position).getName().equals("All")) {
                    refMovie.addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                            ArrayList<Movie> movies = new ArrayList<>();

                            for (DocumentSnapshot snapshot: queryDocumentSnapshots) {
                                movies.add(snapshot.toObject(Movie.class));
                            }
                            MovieAdapter adapter = new MovieAdapter(movies, context, fm);
                            HomeFragment.rcvMovie.setAdapter(adapter);
                            HomeFragment.rcvMovie.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
                            HomeFragment.tvMovie.setText("All Movies (" + movies.size() + ")");

                        }
                    });
                    refSeries.addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                            ArrayList<Series> series = new ArrayList<>();

                            for (DocumentSnapshot snapshot: queryDocumentSnapshots ) {
                                series.add(snapshot.toObject(Series.class));
                            }
                            SeriesAdapter adapter = new SeriesAdapter(series, context, fm);
                            HomeFragment.rcvSeries.setAdapter(adapter);
                            HomeFragment.rcvSeries.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
                            HomeFragment.tvSeries.setText("All Series (" + series.size() + ")");

                        }
                    });
                }
                else {
                    refMovie.whereEqualTo("genreName", mArrayList.get(position).getName())
                            .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                            ArrayList<Movie> movies = new ArrayList<>();

                            for (DocumentSnapshot snapshot: queryDocumentSnapshots) {
                                movies.add(snapshot.toObject(Movie.class));
                            }
                            MovieAdapter adapter = new MovieAdapter(movies, context, fm);
                            HomeFragment.rcvMovie.setAdapter(adapter);
                            HomeFragment.rcvMovie.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
                            HomeFragment.tvMovie.setText(mArrayList.get(position).getName() + "(" + movies.size() + ")");

                        }
                    });
                    refSeries.whereEqualTo("genreName", mArrayList.get(position).getName())
                            .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                            ArrayList<Series> series = new ArrayList<>();

                            for (DocumentSnapshot snapshot: queryDocumentSnapshots ) {
                                series.add(snapshot.toObject(Series.class));
                            }
                            SeriesAdapter adapter = new SeriesAdapter(series, context, fm);
                            HomeFragment.rcvSeries.setAdapter(adapter);
                            HomeFragment.rcvSeries.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
                            HomeFragment.tvSeries.setText(mArrayList.get(position).getName() + "(" + series.size() + ")");

                        }
                    });
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return mArrayList.size();
    }

    public class MyItems extends RecyclerView.ViewHolder {
        TextView mTextView;
        CircleTextView mCircleTextView;

        public MyItems(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.tv);
            mCircleTextView = (CircleTextView) itemView.findViewById(R.id.ctv);

        }
    }
}
