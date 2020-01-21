package com.example.feedapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public abstract class NetworkActivity extends AppCompatActivity {
    private NetworkStateReceiver stateReceiver;
    private boolean isOnline;
    protected Toast onlineToast;
    protected Toast offlineToast;
    protected RSSURL rssurl;
    ListView articlesView;

    @SuppressLint("ShowToast")
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        rssurl = new RSSURL(this);

        onlineToast =  Toast.makeText(this, R.string.online, Toast.LENGTH_LONG);

        offlineToast = Toast.makeText(this, R.string.offline, Toast.LENGTH_LONG);

        stateReceiver = new NetworkStateReceiver(this);

        isOnline = stateReceiver.getState();
    }
    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(stateReceiver, filter);
        onNetworkChange(stateReceiver.getState());
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(stateReceiver);
    }

    void onNetworkChange(boolean isOnline)
    {
        if(this.isOnline == isOnline)
        {
            return;
        }

        this.isOnline = isOnline;

        if(isOnline)
        {
            onlineToast.show();
        }
        else
        {
            offlineToast.show();
        }
    }

    public boolean isOnline()
    {
        return isOnline;
    }

    void onGetFeed(Feed feed){}

    void onError(Exception e)
    {
        Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
    }
}
