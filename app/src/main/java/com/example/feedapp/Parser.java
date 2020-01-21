package com.example.feedapp;

import android.annotation.SuppressLint;
import android.os.AsyncTask;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Pattern;

public class Parser extends AsyncTask<Void, Void, Object> {

    @SuppressLint("StaticFieldLeak")
    private NetworkActivity context;
    private InputStream stream;

    Parser(NetworkActivity context, InputStream stream) {
        this.context = context;
        this.stream = stream;
    }

    @Override
    protected Object doInBackground(Void... ignored) {
        try{
            return parse();
        }
        catch (Exception e){
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
            new SaveData(context, (Feed)data).execute();
        }
    }

    private Feed parse()
            throws IOException, XmlPullParserException {

        XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();

        parser.setInput(stream, null);

        String text = null;
        boolean inItem = false;
        Feed feed = new Feed();
        Article article = new Article();

        int event = parser.getEventType();

        while (event != XmlPullParser.END_DOCUMENT)
        {
            String tagName = parser.getName();

            switch (event)
            {
                case XmlPullParser.START_TAG:
                    if (tagName.equalsIgnoreCase("item"))
                    {
                        article = new Article();
                        inItem = true;
                    }
                    else if (tagName.equalsIgnoreCase("enclosure"))
                    {
                        String type = parser.getAttributeValue(null, "type");
                        if(type.equals("image/jpeg"))
                        {
                            article.setImageURL(parser.getAttributeValue(null, "url"));
                        }
                    }
                    break;

                case XmlPullParser.TEXT:
                    text = parser.getText();
                    break;

                case XmlPullParser.END_TAG:

                    if (inItem)
                    {
                        if (tagName.equalsIgnoreCase("title"))
                        {
                            article.setTitle(text);
                        }
                        else if (tagName.equalsIgnoreCase("description"))
                        {
                            article.setDescription(text);
                        }
                        else if (tagName.equalsIgnoreCase("pubDate"))
                        {
                            article.setDate(text);
                        }
                        else if (tagName.equalsIgnoreCase("link"))
                        {
                            article.setLink(text);
                        }
                    }
                    else {
                        if (tagName.equalsIgnoreCase("title"))
                        {
                            feed.setTitle(text);
                        }
                        else if (tagName.equalsIgnoreCase("description"))
                        {
                            feed.setDescription(text);
                        }
                        else if (tagName.equalsIgnoreCase("url"))
                        {
                            feed.setImageURL(text);
                        }
                    }

                    if (tagName.equalsIgnoreCase("item")) {
                        String preview = article.getDescription()
                                .replaceAll("&#x3C;", "<")
                                .replaceAll("&#x3E;", ">")
                                .replaceAll("<br>", "\n")
                                .replaceAll("<p>", "\n");
                        preview = Pattern.compile("<[^<]*>")
                                .matcher(preview)
                                .replaceAll("");

                        article.setPreview(preview);
                        feed.getArticles().add(article);
                        inItem = false;
                    }

                    break;
            }

            event = parser.next();
        }
        return feed;
    }
}