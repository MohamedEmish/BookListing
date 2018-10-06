package com.example.amosh.booklisting;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.DrawFilter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;


public class BookAdapter extends ArrayAdapter<Book> {

    public BookAdapter(Context context, int resource) {
        super(context, resource);
    }


    @Override
    public View getView(int position, View view, ViewGroup parent) {

        final Book book = getItem(position);
        View listItemView = view;

        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(
                    R.layout.item_list, parent, false);
        }

        TextView title = (TextView) view.findViewById(R.id.title);
        title.setText(book.getTitle());

        TextView authorTxt = (TextView) view.findViewById(R.id.authorA);
        authorTxt.setText(book.getAuthor());

        ImageView thumb = (ImageView) view.findViewById(R.id.thumb);

        // To get Picasso ..
        // put in ur build.gradle(Module:app)
        // implementation 'com.squareup.picasso:picasso:2.5.2'
        // Only


        Uri bookUri = Uri.parse(book.getImageUrl());
        Picasso.with(getContext()).load(bookUri).into(thumb);

        return view;
    }

}