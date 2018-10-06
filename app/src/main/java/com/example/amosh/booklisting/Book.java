package com.example.amosh.booklisting;

import android.os.Parcel;
import android.os.Parcelable;


public class Book implements Parcelable {

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };
    private String author;
    private String title;
    private String url;
    private String imageUrl;

    Book(String author, String title, String url, String imageUrl) {
        this.author = author;
        this.title = title;
        this.url = url;
        this.imageUrl = imageUrl;
    }

    private Book(Parcel in) {
        author = in.readString();
        title = in.readString();
        url = in.readString();
        imageUrl = in.readString();
    }

    String getAuthor() {
        return author;
    }

    String getTitle() {
        return title;
    }

    String getUrl() {
        return url;
    }

    String getImageUrl() {
        return imageUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(author);
        parcel.writeString(title);
        parcel.writeString(url);
        parcel.writeString(imageUrl);
    }
}