package com.example.feedapp;


import android.os.Bundle;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener;

import java.util.Objects;

public class WebActivity extends NetworkActivity {

    private String link;
    private SwipeRefreshLayout refreshLayout;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        link = Objects.requireNonNull(getIntent().getExtras()).getString("Link");

        webView = findViewById(R.id.main_web_view);
        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        refreshLayout = findViewById(R.id.web_refresh_view);

        webView.setWebViewClient(new WebViewClient(){
            int loadedResourceCounter = 0;
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                refreshLayout.setRefreshing(false);
            }

            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
                loadedResourceCounter++;
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                if(loadedResourceCounter <= 1) {
                    Toast.makeText(WebActivity.this, "No connection", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                webView.loadUrl(link);
            }
        });

        refreshLayout.post(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(true);
                webView.loadUrl(link);
            }
        });
    }
}
