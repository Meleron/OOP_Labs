package com.example.feedapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.squareup.picasso.Picasso;

import java.util.concurrent.ExecutionException;

public class MainActivity extends NetworkActivity
{
    ListView articlesView;
    TextView feedName;
    AutoCompleteTextView rssEdit;
    SwipeRefreshLayout refreshLayout;
    Feed feed;


    LinearLayout root;
    int lastHeight = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        articlesView = findViewById(R.id.list_view);


        articlesView = findViewById(R.id.list_view);

        articlesView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Article article = (Article) articlesView.getAdapter().getItem(position);
                //if(isOnline() || article.) {
                    Intent intent = new Intent(getApplicationContext(), WebActivity.class);
                    Article article = (Article) articlesView.getAdapter().getItem(position);
                    intent.putExtra("Link", article.getLink());
                    startActivity(intent);
                //} else
                //    Toast.makeText(MainActivity.this, "No internet connection", Toast.LENGTH_LONG).show();
            }
        });

        refreshLayout = findViewById(R.id.swipe_refresh);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                loadFeed();
            }
        });
        refreshLayout.setRefreshing(true);

        feedName = findViewById(R.id.rss_name);
        rssEdit = findViewById(R.id.rss_edit);
        rssEdit.setText(rssurl.get());

        feedName.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                v.setVisibility(View.INVISIBLE);
                rssEdit.requestFocus();
            }
        });



        rssEdit.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

                if(imm == null)
                {
                    return;
                }

                if(hasFocus)
                {
                    rssEdit.selectAll();
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                }
                else
                {
                    imm.hideSoftInputFromWindow(articlesView.getWindowToken(), 0);
                }
            }
        });

        root = findViewById(R.id.root);
        root.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener()
                {
                    @Override
                    public void onGlobalLayout()
                    {
                        int newHeight = root.getHeight();

                        if(newHeight == lastHeight)
                        {
                            return;
                        }
                        if(newHeight > lastHeight)
                        {
                            showTitle();
                        }

                        lastHeight = newHeight;
                    }
                }
        );
        loadFeed();

        Button button = findViewById(R.id.button3);

        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getApplicationContext(), InfoActivity.class);
                intent.putExtra("title", feed.getTitle());
                intent.putExtra("description", feed.getDescription());
                intent.putExtra("imageURL", feed.getImageURL());
                startActivity(intent);
            }
        });
    }

    @Override
    void onGetFeed(Feed feed)
    {
        this.feed = feed;
        feedName.setText(feed.getTitle());
        rssurl.set(rssEdit.getText().toString());

        ImageView urlFeed = findViewById(R.id.urlFeed);
        Picasso.get()
        .load(feed.getImageURL())
        .into(urlFeed);

        int width = articlesView.getWidth();
        articlesView.setAdapter(new AdapterArticle(
                this, R.layout.article_layout, feed.getArticles(), width));
        refreshLayout.setRefreshing(false);
    }

    @Override
    void onError(Exception e)
    {
        refreshLayout.setRefreshing(false);
        super.onError(e);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        rssEdit.clearFocus();
    }

    void showTitle()
    {
        feedName.setVisibility(View.VISIBLE);
        rssEdit.clearFocus();
        if(!rssurl.get().equals(rssEdit.getText().toString()))
        {
            ImageView urlFeed = findViewById(R.id.urlFeed);
            Picasso.get()
                    .load(feed.getImageURL())
                    .into(urlFeed);

            new DownloadData(this, rssEdit.getText().toString()).execute();
            refreshLayout.setRefreshing(true);
        }
    }

    void loadFeed(){
        if(isOnline())
        {
            new DownloadData(this, rssEdit.getText().toString()).execute();
        }
        else{
            if(feed == null)
                offlineToast.show();
            try {
                feed = (Feed) new LoadData(this).execute().get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
