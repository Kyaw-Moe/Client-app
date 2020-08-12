package com.myapp.clientappsub;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.myapp.clientappsub.Adapter.GenreAdapter;
import com.myapp.clientappsub.Adapter.MovieAdapter;
import com.myapp.clientappsub.Adapter.SeriesAdapter;
import com.myapp.clientappsub.models.Genre;
import com.myapp.clientappsub.models.Movie;
import com.myapp.clientappsub.models.Series;
import com.myapp.clientappsub.models.SlideModel;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    public static TextView tvGenre, tvMovie, tvSeries;
    public static RecyclerView rcvGenre, rcvMovie, rcvSeries;

    public HomeFragment() {
        // Required empty public constructor
    }

    public  static CarouselView carouselView;

    View myView;

    ArrayList<String> sampleImages = new ArrayList<>();

    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
            Glide.with(getContext())
                    .load(sampleImages.get(position))
                    .into(imageView);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myView = inflater.inflate(R.layout.fragment_home, container, false);

        GoogleAdsMob adsMob = new GoogleAdsMob();
        adsMob.loadBanner(myView, getContext(), getActivity());

        carouselView = myView.findViewById(R.id.carouselView);
        carouselView.setPageCount(0);
        carouselView.setImageListener(imageListener);

        tvGenre = myView.findViewById(R.id.tv_genre);
        tvMovie = myView.findViewById(R.id.tv_movie);
        tvSeries = myView.findViewById(R.id.tv_series);
        rcvGenre = myView.findViewById(R.id.rcv_genre);
        rcvMovie = myView.findViewById(R.id.rcv_movie);
        rcvSeries = myView.findViewById(R.id.rcv_series);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference slideRef = db.collection(getString(R.string.slide_ref));
        CollectionReference genreRef = db.collection(getString(R.string.genre_ref));
        CollectionReference movieRef = db.collection(getString(R.string.movie_ref));
        CollectionReference seriesRef = db.collection(getString(R.string.series_ref));

        slideRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                ArrayList<SlideModel> slideModels = new ArrayList<>();
                for (DocumentSnapshot s: queryDocumentSnapshots) {
                    slideModels.add(s.toObject(SlideModel.class));
                }
                sampleImages.add(slideModels.get(0).s1);
                sampleImages.add(slideModels.get(0).s2);
                sampleImages.add(slideModels.get(0).s3);
                sampleImages.add(slideModels.get(0).s4);
                sampleImages.add(slideModels.get(0).s5);

                carouselView.setPageCount(sampleImages.size());

                carouselView.setImageListener(imageListener);
            }
        });
        genreRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                ArrayList<Genre> genres = new ArrayList<>();

                Genre all = new Genre();
                all.setName("All");
                genres.add(all);

                for (DocumentSnapshot snapshot: queryDocumentSnapshots) {
                    genres.add(snapshot.toObject(Genre.class));
                }
                GenreAdapter adapter = new GenreAdapter(genres, getContext(), getFragmentManager());
                tvGenre.setText("All Genre ("+genres.size()+")");
                rcvGenre.setAdapter(adapter);
                rcvGenre.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
            }
        });
        movieRef.orderBy("createdAt", Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                ArrayList<Movie> movies = new ArrayList<>();
                for (DocumentSnapshot snapshot: queryDocumentSnapshots) {
                    movies.add(snapshot.toObject(Movie.class));
                }
                MovieAdapter adapter = new MovieAdapter(movies, getContext(), getFragmentManager());
                LinearLayoutManager lm = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
                rcvMovie.setAdapter(adapter);
                rcvMovie.setLayoutManager(lm);

                tvMovie.setText("All Movies (" + movies.size() + ")");
            }
        });
        seriesRef.orderBy("createdAt", Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                ArrayList<Series> series = new ArrayList<>();
                for (DocumentSnapshot snapshot: queryDocumentSnapshots) {
                    series.add(snapshot.toObject(Series.class));
                }
                SeriesAdapter adapter = new SeriesAdapter(series, getContext(), getFragmentManager());
                LinearLayoutManager lm = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
                rcvSeries.setAdapter(adapter);
                rcvSeries.setLayoutManager(lm);

                tvSeries.setText("All Series (" + series.size() + ")");

            }
        });

        return myView;
    }
}
