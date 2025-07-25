package com.elbourn.android.catgame;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.webkit.WebViewAssetLoader;
import androidx.webkit.WebViewClientCompat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private String TAG = "MainActivity";

    WebView myWebView;
    String url = "https://appassets.androidplatform.net/assets/website_catgame/index.html";

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Context context = getApplicationContext();
        runOnUiThread(new Runnable() {
            public void run() {
                String msg = "Loading Cat Game .. please wait.";
                Log.i(TAG, "msg: " + msg);
                Toast.makeText(context, msg, Toast.LENGTH_LONG).show();

            }
        });
        myWebView = (WebView) findViewById(R.id.webview);
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
//        webSettings.setAppCacheEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        final WebViewAssetLoader assetLoader = new WebViewAssetLoader.Builder()
                .addPathHandler("/assets/", new WebViewAssetLoader.AssetsPathHandler(this))
                .build();
        myWebView.setWebViewClient(new WebViewClientCompat() {
            @Override
            @RequiresApi(21)
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                return assetLoader.shouldInterceptRequest(request.getUrl());
            }
            @Override
            @SuppressWarnings("deprecation") // for API < 21
            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
                return assetLoader.shouldInterceptRequest(Uri.parse(url));
            }
        });
        myWebView.loadUrl(url);

        // Handle back pressed
        OnBackPressedCallback onBackPressedCallback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // the 3 lines below replace the functionality of super.onBackPressed();
                setEnabled(false);
                getOnBackPressedDispatcher().onBackPressed();
                setEnabled(true);
                // any additional code
                Log.i(TAG, "handleOnBackPressed");

                finishAffinity();
            }
        };
        getOnBackPressedDispatcher().addCallback(this, onBackPressedCallback);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "start onResume");
        myWebView.loadUrl(url);
        Log.i(TAG, "end onResume");
    }

//    @Override
//    public void onBackPressed(){
//        super.onBackPressed();
//        finishAffinity();
//    }
}
