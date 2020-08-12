package com.myapp.clientappsub;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.JsonArray;
import com.myapp.clientappsub.models.Episode;
import com.myapp.clientappsub.models.Movie;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;

import es.dmoral.toasty.Toasty;


/**
 * A simple {@link Fragment} subclass.
 */
public class VideoDetFragment extends Fragment {

    public Episode episode;
    public Movie movie;
    ProgressBar progressBar;
    RelativeLayout playerContent;
    public static String link;

    public VideoDetFragment() {
        // Required empty public constructor
    }

    private InterstitialAd interstitialAd;
    private RewardedVideoAd rewardedVideoAd;
    FloatingActionButton btnDownload;
    private boolean isFullScreen = false;
    ImageView fullScreen;
    public static SimpleExoPlayer player;
    SimpleExoPlayerView playerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myView = inflater.inflate(R.layout.fragment_video_det, container, false);

        GoogleAdsMob adsMob = new GoogleAdsMob();
        adsMob.loadBanner(myView, getContext(), getActivity());
        interstitialAd = adsMob.loadInterAds(getContext());
        rewardedVideoAd = adsMob.loadRewarded(getContext());
        adsMob.loadNativeAds(myView, getContext());

        playerView = myView.findViewById(R.id.player_view);
        progressBar = myView.findViewById(R.id.progress_bar);
        fullScreen = myView.findViewById(R.id.full_screen);
        playerContent = myView.findViewById(R.id.player_content);
        btnDownload = myView.findViewById(R.id.btn_download);

        if (movie != null) {
            getActivity().setTitle(movie.getTitle());
        }
        else if (episode != null) {
            getActivity().setTitle(episode.getName());
        }

        String url = "https://media-fire-api.herokuapp.com/?url=";

        try {

            if (movie != null) {
                url = url + URLEncoder.encode(movie.getVideoLink(), "utf-8");
            }
            else if (episode != null) {
                url = url + URLEncoder.encode(episode.getVideoLink(), "utf-8");
            }

        }
        catch (Exception ex) {
            Toasty.error(getContext(), "URL Error", Toasty.LENGTH_LONG).show();
        }

        JsonArrayRequest arrayRequest = new JsonArrayRequest
                (Request.Method.GET,
                        url,
                        null,
                        new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {

                        try {
                            JSONObject data = response.getJSONObject(0);
                            String fileLink = data.getString("file");
                            String [] arr = fileLink.split(":");
                            if (arr[0].equals("http")) {
                                fileLink = arr[0] + "s:" + arr[1];
                            }
                            link = fileLink;

                            player = ExoPlayerFactory.newSimpleInstance(getContext(),
                                    new DefaultTrackSelector());
                            DefaultHttpDataSourceFactory httpDataSourceFactory = new DefaultHttpDataSourceFactory("exoplayer");
                            ExtractorsFactory factory = new DefaultExtractorsFactory();
                            MediaSource source = new ExtractorMediaSource(Uri.parse(fileLink),
                                    httpDataSourceFactory,
                                    factory,
                                    null,
                                    null);
                            player.prepare(source);
                            playerView.setPlayer(player);
                            player.setPlayWhenReady(true);
                            SimpleExoPlayer.EventListener listener = new ExoPlayer.EventListener() {
                                @Override
                                public void onTimelineChanged(Timeline timeline, Object manifest) {

                                }

                                @Override
                                public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

                                }

                                @Override
                                public void onLoadingChanged(boolean isLoading) {

                                }

                                @Override
                                public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

                                    if (playbackState == ExoPlayer.STATE_BUFFERING) {
                                        progressBar.setVisibility(View.VISIBLE);
                                    }
                                    else {
                                        progressBar.setVisibility(View.GONE);
                                    }

                                }

                                @Override
                                public void onRepeatModeChanged(int repeatMode) {

                                }

                                @Override
                                public void onPlayerError(ExoPlaybackException error) {

                                }

                                @Override
                                public void onPositionDiscontinuity() {

                                }

                                @Override
                                public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

                                }
                            };
                            player.addListener(listener);

                        }
                        catch (Exception ex) {
                            Toasty.error(getContext(), "JSON Error", Toasty.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error

                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(arrayRequest);
        RetryPolicy policy = new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 30000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 10;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        };

        arrayRequest.setRetryPolicy(policy);


        fullScreen.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SourceLockedOrientationActivity")
            @Override
            public void onClick(View v) {
                if (isFullScreen) {
                    getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                    getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT);
                    DisplayMetrics displayMetrics = new DisplayMetrics();
                    getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                    int height = (int) (300 * getContext().getResources().getDisplayMetrics().density);

                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) playerView.getLayoutParams();
                    params.width = params.MATCH_PARENT;
                    params.height = height;
                    playerView.setLayoutParams(params);
                    playerContent.setLayoutParams(params);
                    MainActivity.toolbar.setVisibility(View.VISIBLE);
                    isFullScreen = false;

                }
                else {
                    getActivity().getWindow().getDecorView().setSystemUiVisibility(
                            View.SYSTEM_UI_FLAG_FULLSCREEN|
                                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY|
                                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    );
                    getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    DisplayMetrics metrics = new DisplayMetrics();
                    getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
                    Point point = new Point();
                    getActivity().getWindowManager().getDefaultDisplay().getRealSize(point);

                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) playerView.getLayoutParams();
                    params.width = params.MATCH_PARENT;
                    params.height = point.y;
                    playerView.setLayoutParams(params);
                    playerContent.setLayoutParams(params);
                    MainActivity.toolbar.setVisibility(View.GONE);
                    isFullScreen = true;
                }
            }
        });

        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (interstitialAd.isLoaded()) {
                    interstitialAd.show();
                }
                if (rewardedVideoAd.isLoaded()) {
                    rewardedVideoAd.show();
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Confirmation");
                builder.setIcon(R.drawable.ic_action_download);
                builder.setMessage("Are You Sure To Download");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ) {
                            if ((getContext()
                            .checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) ==
                                    PackageManager.PERMISSION_GRANTED) &&
                                    (getContext().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                            PackageManager.PERMISSION_GRANTED)) {
                                downloadFile();

                            }
                            else {
                                String [] permission = new String[] {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

                                if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE) &&
                                        shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                                    Toasty.error(getContext(),"Please Access Permission", Toasty.LENGTH_LONG).show();
                                }

                                requestPermissions(permission, 123);
                            }

                        }
                        else {
                            downloadFile();
                        }

                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();
            }
        });

        return myView;
    }

    public void downloadFile() {
        DownloadManager.Request dlRequest = new DownloadManager.Request(Uri.parse(link));
        dlRequest.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE |
                DownloadManager.Request.NETWORK_WIFI);

        if (movie != null) {
            dlRequest.setTitle(movie.getTitle());
        }
        else if (episode != null) {
            dlRequest.setTitle(episode.getName());
        }
        dlRequest.setDescription("Downloading file...");
        dlRequest.allowScanningByMediaScanner();
        dlRequest.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        String fileName;

        if (movie != null) {
            fileName = movie.getTitle() + System.currentTimeMillis() + ".mp4";
        }
        else {
            fileName = episode.getName() + System.currentTimeMillis() + ".mp4";
        }
        dlRequest.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);
        DownloadManager manager = (DownloadManager) getContext().getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(dlRequest);
        Toasty.success(getContext(), "Download Success", Toasty.LENGTH_LONG).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == 123) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                downloadFile();
            }
            else {
                Toasty.error(getContext(), "Denial! Can't Download", Toasty.LENGTH_LONG).show();
            }
        }
    }
}
