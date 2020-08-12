package com.testapp.clientappsub;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.testapp.clientappsub.models.RequestModel;

import es.dmoral.toasty.Toasty;


/**
 * A simple {@link Fragment} subclass.
 */
public class RequestFragment extends Fragment {

    public RequestFragment() {
        // Required empty public constructor
    }

    private InterstitialAd interstitialAd;
    private RewardedVideoAd rewardedVideoAd;
    FirebaseFirestore db;
    CollectionReference ref;
    EditText etName,etImage;
    TextView tvName,tvImage;
    Button save,cancel;
    View myView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myView = inflater.inflate(R.layout.fragment_request, container, false);

        GoogleAdsMob adsMob = new GoogleAdsMob();
        adsMob.loadBanner(myView, getContext(), getActivity());
        interstitialAd = adsMob.loadInterAds(getContext());
        rewardedVideoAd = adsMob.loadRewarded(getContext());

        etName = myView.findViewById(R.id.et_movie_name);
        etImage = myView.findViewById(R.id.et_image_link);
        save = myView.findViewById(R.id.save);
        cancel = myView.findViewById(R.id.cancel);
        db = FirebaseFirestore.getInstance();
        ref = db.collection(getString(R.string.request_ref));

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etName.getText().toString().trim().equals("")) {
                    Toasty.error(getContext(), "Please fill data", Toasty.LENGTH_LONG).show();
                }
                else {
                    if (interstitialAd.isLoaded()) {
                        interstitialAd.show();
                    }
                    if (rewardedVideoAd.isLoaded()) {
                        rewardedVideoAd.show();
                    }
                    RequestModel model = new RequestModel();
                    model.setTitle(etName.getText().toString().trim());
                    model.setImageLink(etImage.getText().toString().trim());
                    ref.add(model);
                    etName.setText("");
                    etImage.setText("");
                    Toasty.success(getContext(), "Save OK", Toasty.LENGTH_LONG).show();
                }

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etName.setText("");
                etImage.setText("");
            }
        });

        return myView;
    }
}
