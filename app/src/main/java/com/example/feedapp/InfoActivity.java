package com.example.feedapp;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.squareup.picasso.Picasso;

import java.util.Objects;

public class InfoActivity extends NetworkActivity  {

    private String title = "";
    private String description = "";
    private String imageURL = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        title = Objects.requireNonNull(getIntent().getExtras()).getString("title");

        description = Objects.requireNonNull(getIntent().getExtras()).getString("description");

        imageURL = Objects.requireNonNull(getIntent().getExtras()).getString("imageURL");

        ImageView urlFeed = findViewById(R.id.imagefeed);

        Picasso.get()
                .load(imageURL)
                .into(urlFeed);

        TextView utextFeed = findViewById(R.id.titlefeed);
        utextFeed.setText(title);

        TextView utexdtFeed = findViewById(R.id.descripturl);
        utexdtFeed.setText(description);
    }
}
