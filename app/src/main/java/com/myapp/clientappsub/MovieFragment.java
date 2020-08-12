package com.myapp.clientappsub;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.myapp.clientappsub.Adapter.MovieAdapter;
import com.myapp.clientappsub.models.Movie;

import java.util.ArrayList;
import java.util.Random;

import recycler.coverflow.RecyclerCoverFlow;


/**
 * A simple {@link Fragment} subclass.
 */
public class MovieFragment extends Fragment {

    public MovieFragment() {
        // Required empty public constructor
    }

    FirebaseFirestore db;
    CollectionReference movieRef;
    RecyclerView rcvMovies;
    RecyclerCoverFlow rcvTenMovies;
    TextView tvMovie;

    View myView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myView = inflater.inflate(R.layout.fragment_movie, container, false);

        GoogleAdsMob adsMob = new GoogleAdsMob();
        adsMob.loadBanner(myView, getContext(), getActivity());

        db = FirebaseFirestore.getInstance();
        movieRef = db.collection(getString(R.string.movie_ref));
        rcvMovies = myView.findViewById(R.id.rcv_movie);
        rcvTenMovies = myView.findViewById(R.id.rcv_ten_movies);
        tvMovie = myView.findViewById(R.id.tv_movie);

        movieRef.orderBy("createdAt", Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                ArrayList<Movie> movies = new ArrayList<>();
                ArrayList<Movie> tenMovies = new ArrayList<>();

                for (DocumentSnapshot snapshot: queryDocumentSnapshots) {
                    movies.add(snapshot.toObject(Movie.class));
                }
                if (movies.size() >= 10) {
                    Random random = new Random();
                    for (int i=0; i < 10; i++) {
                        int index = random.nextInt(movies.size());
                        tenMovies.add(movies.get(index));
                    }
                    rcvMovies.setAdapter(new MovieAdapter(movies, getContext(), getFragmentManager()));
                    rcvMovies.setLayoutManager(new GridLayoutManager(getContext(), 3, RecyclerView.VERTICAL, false));
                    rcvTenMovies.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
                    rcvTenMovies.scrollToPosition(tenMovies.size()/2);
                }
                else {
                    MovieAdapter adapter = new MovieAdapter(movies, getContext(), getFragmentManager());
                    rcvTenMovies.setAdapter(adapter);
                    rcvMovies.setAdapter(adapter);
                    rcvMovies.setLayoutManager(new GridLayoutManager(getContext(), 3, RecyclerView.VERTICAL, false));
                    rcvTenMovies.scrollToPosition(movies.size()/2);
                }

                tvMovie.setText("All Movies (" + movies.size() + ")");

            }
        });

        return myView;
    }
}
