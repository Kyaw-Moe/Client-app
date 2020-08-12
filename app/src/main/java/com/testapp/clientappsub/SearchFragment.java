package com.testapp.clientappsub;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.testapp.clientappsub.Adapter.MovieAdapter;
import com.testapp.clientappsub.Adapter.SeriesAdapter;
import com.testapp.clientappsub.models.Movie;
import com.testapp.clientappsub.models.Series;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {

    public SearchFragment() {
        // Required empty public constructor
    }

    TextView tvMovie, tvSeries;
    EditText search;
    RecyclerView rcvMovie, rcvSeries;
    View myView;
    FirebaseFirestore db;
    CollectionReference movieRef;
    CollectionReference seriesRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myView = inflater.inflate(R.layout.fragment_search, container, false);

        GoogleAdsMob adsMob = new GoogleAdsMob();
        adsMob.loadBanner(myView, getContext(), getActivity());

        search = myView.findViewById(R.id.search);
        tvMovie = myView.findViewById(R.id.tv_movie);
        tvSeries = myView.findViewById(R.id.tv_series);
        rcvMovie = myView.findViewById(R.id.rcv_movie);
        rcvSeries = myView.findViewById(R.id.rcv_series);
        db = FirebaseFirestore.getInstance();
        movieRef = db.collection(getString(R.string.movie_ref));
        seriesRef = db.collection(getString(R.string.series_ref));
        loadData();

        search.addTextChangedListener(new TextWatcher() {
            private AlertDialog.Builder recyclerView;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (search.getText().toString().trim().equals("")) {
                    loadData();
                }
                else {
                    movieRef.orderBy("title")
                            .startAt(search.getText().toString().trim())
                            .endAt(search.getText().toString().trim()+"\uf8ff")
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
                                    tvMovie.setText(search.getText().toString().trim() + " Movies (" + movies.size() + ")");
                                }
                            });
                    seriesRef.orderBy("title")
                            .startAt(search.getText().toString().trim())
                            .endAt(search.getText().toString().trim()+"\uf8ff")
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
                                    tvSeries.setText(search.getText().toString().trim() + " Series (" + series.size() + ")");
                                }
                            });
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return myView;
    }
    public void loadData() {
        movieRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
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
        seriesRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
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
    }
}
