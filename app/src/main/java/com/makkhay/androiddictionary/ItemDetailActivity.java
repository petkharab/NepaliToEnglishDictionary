package com.makkhay.androiddictionary;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.makkhay.androiddictionary.database.DbBackend;
import com.makkhay.androiddictionary.database.QuizObject;
import com.makkhay.androiddictionary.helper.Helper;


import java.util.Locale;

/**
 * An activity representing a single Item detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 *
 * This activity is activated after a search is executed.
 *
 *
 *
 */
public class ItemDetailActivity extends AppCompatActivity {

    private TextToSpeech convertToSpeech;
    private QuizObject dataObject;
    Toolbar toolbar1;

    private InterstitialAd mInterstitialAd;
    private AdRequest adRequestWall;

    private static int adDisplayCounter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);
         toolbar1 = (Toolbar) findViewById(R.id.toolbar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar1);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // Google Analytic tracking code.
        Tracker t = ((AnalyticsApplication) getApplication()).getTracker(AnalyticsApplication.TrackerName.APP_TRACKER);
        t.setScreenName(this.getClass().getSimpleName());
        t.send(new HitBuilders.ScreenViewBuilder().build());

        // use to set the number of time the back button will be pressed before a wall ad will show.
        adDisplayCounter++;

        // Google Interstitial Wall Ad
        mInterstitialAd = new InterstitialAd(ItemDetailActivity.this);
        mInterstitialAd.setAdUnitId(getResources().getString(R.string.wall_ad_id));
        adRequestWall = new AdRequest.Builder().build();
        mInterstitialAd.loadAd(adRequestWall);

        DbBackend dbBackend = new DbBackend(ItemDetailActivity.this);
        int dataId = Integer.parseInt(getIntent().getStringExtra(ItemDetailFragment.ARG_ITEM_ID));
        dataObject = dbBackend.getQuizById(dataId);

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putString(ItemDetailFragment.ARG_ITEM_ID, getIntent().getStringExtra(ItemDetailFragment.ARG_ITEM_ID));
            ItemDetailFragment fragment = new ItemDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction().add(R.id.item_detail_container, fragment).commit();
        }

        Button textToSpeech = (Button)findViewById(R.id.button);
        Button searchTheWeb = (Button)findViewById(R.id.www_button);

        assert textToSpeech != null;
        textToSpeech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String convertTextToSpeech = dataObject.getMeaning();
                convertToSpeech = new TextToSpeech(ItemDetailActivity.this, new TextToSpeech.OnInitListener() {
                    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onInit(int status) {
                        if (status != TextToSpeech.ERROR) {
                            convertToSpeech.setLanguage(Locale.US);
                            if (Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                                convertToSpeech.speak(convertTextToSpeech, TextToSpeech.QUEUE_FLUSH, null, null);
                            } else {
                                convertToSpeech.speak(convertTextToSpeech, TextToSpeech.QUEUE_FLUSH, null);
                            }
                        }
                    }
                });
            }
        });

        assert searchTheWeb != null;
        searchTheWeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!Helper.isNetworkAvailable(getApplicationContext())){
                    Toast.makeText(ItemDetailActivity.this, "There is no internet connection in your device.", Toast.LENGTH_LONG).show();
                    return;
                }
                // get the dictionary word and store it in the intent
                Intent intent = new Intent(ItemDetailActivity.this, WebActivity.class);
                intent.putExtra(Helper.SEARCH_WORD, dataObject.getWord());
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            NavUtils.navigateUpTo(this, new Intent(this, ItemListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        if(convertToSpeech != null){
            convertToSpeech.stop();
            convertToSpeech.shutdown();
        }
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        adDisplayCounter++;
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
            }

            @Override
            public void onAdLoaded() {
                if(adDisplayCounter % 4 == 0){
                    if (mInterstitialAd.isLoaded()) {
                        mInterstitialAd.show();
                    }
                }
            }
        });
        super.onBackPressed();
    }
}
