package com.myapp.clientappsub;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.myapp.clientappsub.models.Series;

public class MainActivity extends AppCompatActivity {

    public static String mediumFrag;
    public static Series series;
    public static String currentFrag = "";
    public static String prevFrag = "";
    DrawerLayout drawerLayout;
    Fragment activeFrag;
    NavigationView navMenu;
    public static Toolbar toolbar;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer_layout);
        navMenu = findViewById(R.id.nav_menu);
        toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawerLayout, toolbar,
                R.string.open_drawer,
                R.string.close_drawer);
        toggle.syncState();

        View headerView = navMenu.getHeaderView(0);
        TextView tvAppVersion, tvAppName;
        tvAppVersion = headerView.findViewById(R.id.appVersion);
        tvAppName = headerView.findViewById(R.id.appName);

        tvAppName.setText(getString(R.string.app_name));
        tvAppVersion.setText("Version " + com.myapp.clientappsub.BuildConfig.VERSION_NAME);

        setFragment(new HomeFragment());
        setTitle("Home");
        currentFrag = getString(R.string.home_frag);
        prevFrag = getString(R.string.home_frag);

        navMenu.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case R.id.home_menu:
                        setFragment(new HomeFragment());
                        setTitle(getString(R.string.home_frag));
                        currentFrag = getString(R.string.home_frag);
                        prevFrag = getString(R.string.home_frag);
                        break;
                    case R.id.series_menu:
                        setFragment(new SeriesFragment());
                        setTitle(getString(R.string.series_frag));
                        prevFrag = getString(R.string.home_frag);
                        currentFrag = getString(R.string.series_frag);
                        break;
                    case R.id.movie_menu:
                        setFragment(new MovieFragment());
                        setTitle(getString(R.string.movie_frag));
                        prevFrag = getString(R.string.home_frag);
                        currentFrag = getString(R.string.movie_frag);
                        break;
                    case R.id.request_menu:
                        setFragment(new RequestFragment());
                        setTitle(getString(R.string.request_frag));
                        prevFrag = getString(R.string.home_frag);
                        currentFrag = getString(R.string.request_frag);
                        break;
                    case R.id.search_menu:
                        setFragment(new SearchFragment());
                        setTitle(getString(R.string.search_frag));
                        prevFrag = getString(R.string.home_frag);
                        currentFrag = getString(R.string.search_frag);
                        break;
                    case R.id.about_menu:
                        setFragment(new AboutFragment());
                        setTitle(getString(R.string.about_frag));
                        prevFrag = getString(R.string.home_frag);
                        currentFrag = getString(R.string.about_frag);
                        break;
                    case R.id.share_menu:
                        Intent shareIntent = new Intent();
                        shareIntent.setAction(Intent.ACTION_SEND);
                        shareIntent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=" + com.myapp.clientappsub.BuildConfig.APPLICATION_ID);
                        shareIntent.setType("text/plain");
                        startActivity(shareIntent);
                        break;
                    case R.id.sign_in:
                        setFragment(new SignInFragment());
                        setTitle("SignIn");
                        prevFrag = "Home";
                        currentFrag = "SignIn";
                }

                drawerLayout.closeDrawer(Gravity.LEFT);

                return true;
            }
        });

    }


    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
            drawerLayout.closeDrawer(Gravity.LEFT);
        }
        else {

            if (prevFrag.equals(getString(R.string.home_frag)) &&
            currentFrag.equals(getString(R.string.home_frag))) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Exit");
                builder.setMessage("Are You Sure To Exit");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finishAffinity();
                        finish();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();
            }
            else if (currentFrag.equals(getString(R.string.movie_frag))
            || currentFrag.equals(getString(R.string.search_frag))
            || currentFrag.equals(getString(R.string.series_frag))
            || currentFrag.equals(getString(R.string.about_frag))
            || currentFrag.equals(getString(R.string.request_frag))
            ){
            setFragment(new HomeFragment());
            setTitle(getString(R.string.home_frag));
            currentFrag = getString(R.string.home_frag);
            prevFrag = getString(R.string.home_frag);
            }
            else if (currentFrag.equals(getString(R.string.series_det_frag)) &&
            prevFrag.equals(getString(R.string.home_frag))) {
                setFragment(new HomeFragment());
                setTitle(getString(R.string.home_frag));
                currentFrag = getString(R.string.home_frag);
                prevFrag = getString(R.string.series_det_frag);
            }
            else if (currentFrag.equals(getString(R.string.video_det_frag)) &&
            prevFrag.equals(getString(R.string.series_det_frag))) {

                SeriesDetFragment detFragment = new SeriesDetFragment();
                detFragment.myModel = MainActivity.series;
                series = null;
                setFragment(detFragment);
                setTitle(getString(R.string.series_frag));
                currentFrag = getString(R.string.series_det_frag);
                prevFrag = MainActivity.mediumFrag;
            }
            else if (currentFrag.equals(getString(R.string.video_det_frag)) &&
            prevFrag.equals(getString(R.string.home_frag))) {
                setFragment(new HomeFragment());
                setTitle(getString(R.string.home_frag));
                currentFrag = getString(R.string.home_frag);
                prevFrag = getString(R.string.home_frag);
            }
            else if (currentFrag.equals(getString(R.string.video_det_frag)) &&
            prevFrag.equals(getString(R.string.movie_frag))) {
                setFragment(new MovieFragment());
                setTitle(getString(R.string.movie_frag));
                currentFrag = getString(R.string.movie_frag);
                prevFrag = getString(R.string.video_det_frag);
            }
            else if (currentFrag.equals(getString(R.string.video_det_frag)) &&
            prevFrag.equals(getString(R.string.search_frag))) {
                setFragment(new SearchFragment());
                setTitle(getString(R.string.search_frag));
                currentFrag = getString(R.string.search_frag);
                prevFrag = getString(R.string.video_det_frag);
            }
            else if (currentFrag.equals(getString(R.string.series_det_frag)) &&
                    prevFrag.equals(getString(R.string.series_frag))) {
                setFragment(new SeriesFragment());
                setTitle(getString(R.string.series_frag));
                currentFrag = getString(R.string.series_frag);
                prevFrag = getString(R.string.series_det_frag);
            }
            else if (currentFrag.equals(getString(R.string.series_det_frag)) &&
                    prevFrag.equals(getString(R.string.search_frag))) {
                setFragment(new SearchFragment());
                setTitle(getString(R.string.search_frag));
                currentFrag = getString(R.string.search_frag);
                prevFrag = getString(R.string.series_det_frag);
            }


        }

        if (VideoDetFragment.player != null) {
            VideoDetFragment.player.stop();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (VideoDetFragment.player != null) {
            VideoDetFragment.player.setPlayWhenReady(false);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (VideoDetFragment.player != null) {
            VideoDetFragment.player.setPlayWhenReady(false);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (VideoDetFragment.player != null) {
            VideoDetFragment.player.setPlayWhenReady(true);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (VideoDetFragment.player != null) {
            VideoDetFragment.player.stop();
        }
    }

    public void setFragment(Fragment f) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.content_fragment, f);
        ft.commit();
    }
}
