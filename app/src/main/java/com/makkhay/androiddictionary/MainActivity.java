package com.makkhay.androiddictionary;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

public class MainActivity extends AppCompatActivity {

    ProgressBar prg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // set / request full screen window.
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         prg=(ProgressBar) findViewById(R.id.progressBar3);



        final ImageView iv=(ImageView) findViewById(R.id.imageView);
        final Animation an = AnimationUtils.loadAnimation(getBaseContext(),R.anim.rotate);
        final Animation an1 = AnimationUtils.loadAnimation(getBaseContext(),R.anim.abc_fade_out);

        iv.startAnimation(an);
        an.setAnimationListener(new Animation.AnimationListener() {


            @Override
            public void onAnimationStart(Animation animation) {
                prg.setProgress(35);


            }

            @Override
            public void onAnimationEnd(Animation animation) {

                iv.startAnimation(an1);
                finish();
                prg.setProgress(90);
                Intent i = new Intent(MainActivity.this, ItemListActivity.class);
                startActivity(i);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });




        // set the Activity page orientation to portrait
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Google Analytic tracking code.
        Tracker t = ((AnalyticsApplication) getApplication()).getTracker(AnalyticsApplication.TrackerName.APP_TRACKER);
        t.setScreenName(this.getClass().getSimpleName());
        t.send(new HitBuilders.ScreenViewBuilder().build());


    }
}
