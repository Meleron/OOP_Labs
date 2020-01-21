package com.example.feedapp;

import android.annotation.SuppressLint;
import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadData extends AsyncTask<Void, Void, Object> {
    private String urlString;
    @SuppressLint("StaticFieldLeak")
    private NetworkActivity context;

    DownloadData(NetworkActivity context, String urlString){
        this.context = context;
        this.urlString = urlString;
    }

    @Override
    protected Object doInBackground(Void... ignored) {
        try{
            return download(urlString);
        }
        catch (IOException e){
            return e;
        }
    }

    @Override
    protected void onPostExecute(Object data) {
        super.onPostExecute(data);
        if(data instanceof IOException)
            context.onError((IOException) data);
        else {
            new Parser(context, (InputStream) data).execute();
        }
    }

    static InputStream download(String urlString)
            throws IOException
    {
        HttpURLConnection connection = connect(urlString);
        int responseCode = connection.getResponseCode();
        if(responseCode == HttpURLConnection.HTTP_OK){
            return new BufferedInputStream(connection.getInputStream());// поток с информацией  с ссылки
        }
        throw new ConnectException(connection.getResponseMessage());
    }

    private static HttpURLConnection connect(String urlString)
            throws IOException
    {
        URL url = new URL(urlString);

        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        con.setRequestMethod("GET");
        con.setConnectTimeout(15000);
        con.setReadTimeout(15000);
        con.setDoInput(true);

        return con;
    }
}
