package com.example.amosh.booklisting;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class BookAdapter  extends ArrayAdapter<Book> {
    private static final String LOG_TAG = BookAdapter.class.getSimpleName();

    public BookAdapter(Activity context, ArrayList<Book> placeArrayList) {
        super(context, 0, placeArrayList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.item_list, parent, false);
        }
        Book currentBook = getItem(position);

        TextView bookNameTextView = (TextView) listItemView.findViewById(R.id.bookName);
        bookNameTextView.setText(currentBook.getmBookName());

        TextView authorNameTextView = (TextView) listItemView.findViewById(R.id.authorName);
        authorNameTextView.setText(currentBook.getmAuthor());


        View textContainer = listItemView.findViewById(R.id.text_container);
        return listItemView;

    }
}
