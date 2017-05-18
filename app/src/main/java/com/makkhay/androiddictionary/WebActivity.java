package com.makkhay.androiddictionary;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.makkhay.androiddictionary.helper.Helper;

public class WebActivity extends AppCompatActivity {

    private WebView wikipediaWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        setTitle(Helper.WIKI_CONTENT);

        Tracker t = ((AnalyticsApplication) getApplication()).getTracker(AnalyticsApplication.TrackerName.APP_TRACKER);
        t.setScreenName(this.getClass().getSimpleName());
        t.send(new HitBuilders.ScreenViewBuilder().build());

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String searchWord = bundle.getString("SEARCH_WORD");

        wikipediaWebView = (WebView)findViewById(R.id.wikipedia);
        wikipediaWebView.setBackgroundColor(0);
        if(Build.VERSION.SDK_INT >= 11){
            wikipediaWebView.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);
        }
        wikipediaWebView.getSettings().setBuiltInZoomControls(true);
        wikipediaWebView.getSettings().setJavaScriptEnabled(true);

        wikipediaWebView.setWebViewClient(new WebViewClient() {
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(WebActivity.this, description, Toast.LENGTH_SHORT).show();
            }
        });
        String searchQuery = "https://en.wikipedia.org/wiki/" + searchWord;
        wikipediaWebView.loadUrl(searchQuery);
    }
}
