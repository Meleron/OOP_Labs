package com.example.feedapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterArticle extends ArrayAdapter<Article> {
    private LayoutInflater inflater;
    private int layout;
    private ArrayList<Article> articles;
    private Picasso picasso;
    private int width;

    AdapterArticle(Context context, int resource, ArrayList<Article> articles, int width)
    {
        super(context, resource, articles);
        this.articles = articles;
        this.layout = resource;
        this.width = width;
        this.inflater = LayoutInflater.from(context);
        picasso = Picasso.get();
    }

    @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup parent)
    {
        View view = convertView == null?
                inflater.inflate(this.layout, parent, false)
                :convertView;
        if(convertView == null){
            view.setTag(new ViewHolder(view));
        }

        final ViewHolder holder = (ViewHolder)view.getTag();

        final Article article = articles.get(position);

        holder.title.setText(article.getTitle());
        holder.date.setText(article.getDate());
        holder.preview.setText(article.getPreview());

        if(!article.getImageURL().isEmpty())
        {
            try
            {
                picasso.load(article.getImageURL())
                        .placeholder(R.drawable.ic_placeholder)
                        .resize(width, 0)
                        .into(holder.image);

                holder.image.setVisibility(View.VISIBLE);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            holder.image.setVisibility(View.GONE);
        }

        return view;
    }

    private class ViewHolder
    {
        ViewHolder(View view)
        {
            this.view = view;
            title = view.findViewById(R.id.title);
            preview = view.findViewById(R.id.preview);
            date = view.findViewById(R.id.date);
            image = view.findViewById(R.id.image);
        }

        View view;
        TextView title;
        TextView preview;
        TextView date;
        ImageView image;
    }
}