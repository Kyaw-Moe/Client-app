package com.testapp.clientappsub;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.widget.FrameLayout;

import com.google.android.ads.nativetemplates.NativeTemplateStyle;
import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

public class GoogleAdsMob {

    private InterstitialAd interstitialAd;

    public InterstitialAd loadInterAds(Context context) {
        MobileAds.initialize(context, context.getString(R.string.app_id));
        interstitialAd = new InterstitialAd(context);
        interstitialAd.setAdUnitId(context.getString(R.string.interstitial_id));
        interstitialAd.loadAd(new AdRequest.Builder().build());
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                interstitialAd.loadAd(new AdRequest.Builder().build());
            }
        });
        return interstitialAd;
    }

    public void loadBanner(View myView, Context context, Activity activity) {
        MobileAds.initialize(context, context.getString(R.string.app_id));
        FrameLayout adContainer1 = myView.findViewById(R.id.ad_container1);
        AdView adView1 = new AdView(context);
        adView1.setAdUnitId(context.getString(R.string.banner_unit_id));
        adView1.setAdSize(getAdSize(context, activity));
        adView1.loadAd(new AdRequest.Builder().build());
        adContainer1.addView(adView1);
        FrameLayout adContainer2 = myView.findViewById(R.id.ad_container2);
        AdView adView2 = new AdView(context);
        adView2.setAdUnitId(context.getString(R.string.banner_unit_id));
        adView2.setAdSize(getAdSize(context, activity));
        adView2.loadAd(new AdRequest.Builder().build());
        adContainer2.addView(adView2);
    }

    private AdSize getAdSize(Context context, Activity activity) {
        // Step 2 - Determine the screen width (less decorations) to use for the ad width.
        Display display = activity.getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float widthPixels = outMetrics.widthPixels;
        float density = outMetrics.density;

        int adWidth = (int) (widthPixels / density);

        // Step 3 - Get adaptive ad size and return for setting on the ad view.
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(context, adWidth);
    }

    private RewardedVideoAd rewardedVideoAd;

    public RewardedVideoAd loadRewarded(final Context context) {
        MobileAds.initialize(context, context.getString(R.string.app_id));
        rewardedVideoAd = MobileAds.getRewardedVideoAdInstance(context);
        rewardedVideoAd.loadAd(context.getString(R.string.reward_id),
                new AdRequest.Builder().build());
        rewardedVideoAd.setRewardedVideoAdListener(new RewardedVideoAdListener() {
            @Override
            public void onRewardedVideoAdLoaded() {

            }

            @Override
            public void onRewardedVideoAdOpened() {

            }

            @Override
            public void onRewardedVideoStarted() {

            }

            @Override
            public void onRewardedVideoAdClosed() {

                rewardedVideoAd.loadAd(context.getString(R.string.reward_id),
                        new AdRequest.Builder().build());

            }

            @Override
            public void onRewarded(RewardItem rewardItem) {

                rewardedVideoAd.loadAd(context.getString(R.string.reward_id),
                        new AdRequest.Builder().build());
            }

            @Override
            public void onRewardedVideoAdLeftApplication() {

            }

            @Override
            public void onRewardedVideoAdFailedToLoad(int i) {

            }

            @Override
            public void onRewardedVideoCompleted() {

            }
        });
        return rewardedVideoAd;
    }

    public void  loadNativeAds(final View myView, Context context) {
        MobileAds.initialize(context, context.getString(R.string.app_id));
        AdLoader adLoader = new AdLoader.Builder(context, context.getString(R.string.native_id))
                .forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
                    @Override
                    public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                        ColorDrawable background = new ColorDrawable(Color.WHITE);

                        NativeTemplateStyle styles = new
                                NativeTemplateStyle.Builder().withMainBackgroundColor(background).build();

                        TemplateView template = myView.findViewById(R.id.template1);
                        template.setStyles(styles);
                        template.setNativeAd(unifiedNativeAd);

                    }
                })
                .build();
        adLoader.loadAd(new AdRequest.Builder().build());
        AdLoader adLoader2 = new AdLoader.Builder(context, context.getString(R.string.native_id))
                .forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
                    @Override
                    public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                        ColorDrawable background = new ColorDrawable(Color.WHITE);

                        NativeTemplateStyle styles = new
                                NativeTemplateStyle.Builder().withMainBackgroundColor(background).build();

                        TemplateView template = myView.findViewById(R.id.template2);
                        template.setStyles(styles);
                        template.setNativeAd(unifiedNativeAd);

                    }
                })
                .build();
        adLoader2.loadAd(new AdRequest.Builder().build());
    }
}
