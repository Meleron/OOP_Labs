package com.example.feedapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class SaveData extends AsyncTask<Void, Void, Void> {
    @SuppressLint("StaticFieldLeak")
    private Context context;
    private Feed feed;
    private static final String filename = "file";

    SaveData(Context context, Feed feed){
        this.context = context;
        this.feed = feed;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try{
            save();
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    private void save()
            throws IOException {
        File file = new File(context.getFilesDir(), filename);
        FileOutputStream fos = new FileOutputStream(file);
        ObjectOutputStream os = new ObjectOutputStream(fos);
        os.writeObject(feed);
        os.close();
        fos.close();
    }
}
