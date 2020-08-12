package com.testapp.clientappsub;

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
import com.google.firebase.firestore.QuerySnapshot;
import com.testapp.clientappsub.Adapter.EpisodeAdapter;
import com.testapp.clientappsub.models.Episode;
import com.testapp.clientappsub.models.Series;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class SeriesDetFragment extends Fragment {

    public Series myModel;

    public SeriesDetFragment() {
        // Required empty public constructor
    }

    View myView;
    ImageView image;
    TextView tvName;
    FirebaseFirestore db;
    CollectionReference ref;
    RecyclerView rcEpList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myView = inflater.inflate(R.layout.fragment_series_det, container, false);

        GoogleAdsMob adsMob = new GoogleAdsMob();
        adsMob.loadBanner(myView, getContext(), getActivity());

        image = myView.findViewById(R.id.image);
        tvName = myView.findViewById(R.id.series_name);
        rcEpList = myView.findViewById(R.id.epi_list);

        if (myModel != null) {
            getActivity().setTitle(myModel.getTitle());
        }

        if (myModel != null) {

            Glide.with(getContext())
                    .load(myModel.getImageUrl())
                    .into(image);
            tvName.setText(myModel.getTitle());

            db = FirebaseFirestore.getInstance();
            ref = db.collection(getString(R.string.episodes_ref));
            ref.whereEqualTo("seriesTitle", myModel.getTitle())
                    .orderBy("createdAt")
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                            ArrayList<Episode> episodes = new ArrayList<>();
                            for (DocumentSnapshot snapshot: queryDocumentSnapshots) {
                                episodes.add(snapshot.toObject(Episode.class));
                            }

                            EpisodeAdapter adapter = new EpisodeAdapter(episodes, getContext(), getFragmentManager());
                            rcEpList.setAdapter(adapter);
                            rcEpList.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));

                        }
                    });

        }
        return myView;
    }
}
