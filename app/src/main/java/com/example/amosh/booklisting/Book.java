package com.example.amosh.booklisting;

import java.util.Date;

public class Book {
    private String mBookName;
    private String mAuthor;
    private String mUrl;

    public Book (String name ,String author ,String url){
        mBookName = name;
        mAuthor = author;
        mUrl = url;

    }

    public String getmUrl() {
        return mUrl;
    }

    public String getmAuthor() {
        return mAuthor;
    }

    public String getmBookName() {
        return mBookName;
    }


}
