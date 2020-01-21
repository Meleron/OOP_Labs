package com.example.feedapp;

import android.annotation.SuppressLint;
import android.os.AsyncTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class LoadData extends AsyncTask<Void, Void, Object> {
    @SuppressLint("StaticFieldLeak")
    private MainActivity context;
    private static final String filename = "file";

    LoadData(MainActivity context){
        this.context = context;
    }

    @Override
    protected Object doInBackground(Void... voids) {
        try{
            return load();
        }catch (Exception e){
            return e;
        }
    }

    @Override
    protected void onPostExecute(Object data) {
        super.onPostExecute(data);
        if (data instanceof Exception)
            context.onError((Exception) data);
        else {
            context.onGetFeed((Feed) data);
        }
    }

    private Feed load()
            throws IOException, ClassNotFoundException{
        File file = new File(context.getFilesDir(), filename);
        FileInputStream fis = new FileInputStream(file);
        ObjectInputStream is = new ObjectInputStream(fis);
        Feed feed;
        feed = (Feed)is.readObject();
        is.close();
        fis.close();
        return feed;
    }

}
