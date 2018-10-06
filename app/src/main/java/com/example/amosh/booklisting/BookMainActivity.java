package com.example.amosh.booklisting;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;

public class BookMainActivity extends AppCompatActivity {

    static final String SEARCH_RESULTS = "booksSearchResults";
    EditText editText;
    Button button;
    BookAdapter adapter;
    ListView listView;
    TextView NoDataFound;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_main);

        editText = (EditText) findViewById(R.id.editText);
        imageView = (ImageView) findViewById(R.id.thumb);
        button = (Button) findViewById(R.id.button);
        NoDataFound = (TextView) findViewById(R.id.no_data_found);

        adapter = new BookAdapter(this, -1);

        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Book currentBook = adapter.getItem(position);
                Uri bookUri = Uri.parse(currentBook.getUrl());

                // Create a new intent to view the earthquake URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, bookUri);

                // Send the intent to launch a new activity
                startActivity(websiteIntent);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isInternetConnectionAvailable()) {
                    BookAsyncTask task = new BookAsyncTask();
                    task.execute();
                } else {
                    Toast.makeText(BookMainActivity.this, R.string.error_no_internet,
                            Toast.LENGTH_SHORT).show();
                }

            }
        });

        if (savedInstanceState != null) {
            Book[] books = (Book[]) savedInstanceState.getParcelableArray(SEARCH_RESULTS);
            adapter.addAll(books);
        }
    }

    private boolean isInternetConnectionAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null == activeNetwork)
            return false;
        return activeNetwork.isConnectedOrConnecting();
    }

    private void updateUi(List<Book> books) {
        if (books.isEmpty()) {
            NoDataFound.setVisibility(View.VISIBLE);
        } else {
            NoDataFound.setVisibility(View.GONE);
        }
        adapter.clear();
        adapter.addAll(books);
    }

    private String getUserInput() {
        return editText.getText().toString();
    }

    private String getUrlForHttpRequest() {
        final String baseUrl = "https://www.googleapis.com/books/v1/volumes?q=search+";
        String formatUserInput = getUserInput().trim().replaceAll("\\s+", "+");
        String url = baseUrl + formatUserInput;
        return url;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Book[] books = new Book[adapter.getCount()];
        for (int i = 0; i < books.length; i++) {
            books[i] = adapter.getItem(i);
        }
        outState.putParcelableArray(SEARCH_RESULTS, books);
    }

    private class BookAsyncTask extends AsyncTask<URL, Void, List<Book>> {

        LinearLayout linlaHeaderProgress = (LinearLayout) findViewById(R.id.linlaHeaderProgress);

        @Override
        protected void onPreExecute() {
            // SHOW THE SPINNER WHILE LOADING FEEDS
            linlaHeaderProgress.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        }

        @Override
        protected List<Book> doInBackground(URL... urls) {
            URL url = createURL(getUrlForHttpRequest());
            String jsonResponse = "";

            try {
                jsonResponse = makeHttpRequest(url);
            } catch (IOException e) {
                e.printStackTrace();
            }

            List<Book> books = parseJson(jsonResponse);
            return books;
        }

        @Override
        protected void onPostExecute(List<Book> books) {
            if (books == null) {
                return;
            }
            updateUi(books);
            linlaHeaderProgress.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
        }

        private URL createURL(String stringUrl) {
            try {
                return new URL(stringUrl);
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            }
        }

        private String makeHttpRequest(URL url) throws IOException {
            String jsonResponse = "";

            if (url == null) {
                return jsonResponse;
            }

            HttpURLConnection urlConnection = null;
            InputStream inputStream = null;

            try {
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setReadTimeout(10000 /* milliseconds */);
                urlConnection.setConnectTimeout(15000 /* milliseconds */);
                urlConnection.connect();
                if (urlConnection.getResponseCode() == 200) {
                    inputStream = urlConnection.getInputStream();
                    jsonResponse = readFromStream(inputStream);
                } else {
                    Log.e("BookMainActivity", "Error response code: " + urlConnection.getResponseCode());
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            }
            return jsonResponse;
        }

        private String readFromStream(InputStream inputStream) throws IOException {
            StringBuilder output = new StringBuilder();
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
                BufferedReader reader = new BufferedReader(inputStreamReader);
                String line = reader.readLine();
                while (line != null) {
                    output.append(line);
                    line = reader.readLine();
                }
            }
            return output.toString();
        }

        private List<Book> parseJson(String json) {

            if (json == null) {
                return null;
            }

            List<Book> books = QueryUtils.extractBooks(json);
            return books;
        }
    }
}