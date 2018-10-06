package com.example.amosh.booklisting;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class QueryUtils {

    private QueryUtils() {
    }

    static String authorsListInString = "No Author";


    public static String ListOfAuthors(JSONArray authorsList) throws JSONException {


        if (authorsList.length() == 0) {
            return null;
        }

        for (int i = 0; i < authorsList.length(); i++) {
            if (i == 0) {
                authorsListInString = authorsList.getString(0);
            } else {
                authorsListInString += ", " + authorsList.getString(i);
            }
        }

        return authorsListInString;
    }


    public static List<Book> extractBooks(String json) {

        List<Book> books = new ArrayList<>();

        try {
            JSONObject jsonResponse = new JSONObject(json);

            if (jsonResponse.getInt("totalItems") == 0) {
                return books;
            }
            JSONArray jsonArray = jsonResponse.getJSONArray("items");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject bookObject = jsonArray.getJSONObject(i);


                JSONObject bookInfo = bookObject.getJSONObject("volumeInfo");
                String title = bookInfo.getString("title");
                String url = bookInfo.getString("infoLink");

                JSONObject bookImageUrl = bookInfo.getJSONObject("imageLinks");
                String imageUrl = bookImageUrl.getString("smallThumbnail");


                if (bookInfo.has("authors")) {
                    JSONArray authorsArray = bookInfo.optJSONArray("authors");
                    String authors = ListOfAuthors(authorsArray);
                    Book book = new Book(authors, title, url, imageUrl);
                    books.add(book);
                } else {
                    String authors = authorsListInString;
                    Book book = new Book(authors, title, url, imageUrl);
                    books.add(book);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return books;
    }
}