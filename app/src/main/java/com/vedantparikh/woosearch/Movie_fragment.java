package com.vedantparikh.woosearch;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.StringTokenizer;


public class Movie_fragment extends Fragment {
    protected Button btnLogout;
    protected TextView tvWelcome;
    private EditText searchBox;
    private Button searchButton;
    private View rootview;
    private TextView text;
    private String imageurl;
    private ImageView imagevision;
    private RatingBar rating;
    private RatingBar giveStar;
    private RatingBar appRating;
    private ParseObject userRating;
    private String movieName;
    private Button rateSubmit;
    private TextView ratetext;
    private TextView wooRating;
    private TextView giveRate;
    private static JSONObject jsonResponseStore;
    private static int count;
    private String Name ="Name: ";
    private String Welcome ="Welcome, ";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    private void loadLoginView() {
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootview = inflater.inflate(R.layout.fragment_movie, container, false);
        searchBox = (EditText) rootview.findViewById(R.id.text_search_box);
        searchButton = (Button) rootview.findViewById(R.id.search);
        rating = (RatingBar) rootview.findViewById(R.id.pop_ratingbar);
        appRating = (RatingBar) rootview.findViewById(R.id.appRating);
        rateSubmit = (Button) rootview.findViewById(R.id.rateSubmit);
        giveStar = (RatingBar) rootview.findViewById(R.id.giveStar);
        text = (TextView) rootview.findViewById(R.id.text);
        imagevision = (ImageView) rootview.findViewById(R.id.image1);
        tvWelcome = (TextView) rootview.findViewById(R.id.tvWelcome);
        btnLogout = (Button) rootview.findViewById(R.id.btnLogout);
        ratetext = (TextView)rootview.findViewById(R.id.ratetext);
        wooRating = (TextView)rootview.findViewById(R.id.wooRating);
        giveRate = (TextView)rootview.findViewById(R.id.giveRate);

        if(count!=1) {
            rateSubmit.setVisibility(View.INVISIBLE);
            ratetext.setVisibility(View.INVISIBLE);
            rating.setVisibility(View.INVISIBLE);
            wooRating.setVisibility(View.INVISIBLE);
            giveRate.setVisibility(View.INVISIBLE);
            giveStar.setVisibility(View.INVISIBLE);
            imagevision.setVisibility(View.INVISIBLE);
            text.setVisibility(View.INVISIBLE);
            appRating.setVisibility(View.INVISIBLE);
        }

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                count=1;
                if (searchBox.getText().toString() != null && !searchBox.getText().toString().isEmpty()) {
                    rateSubmit.setVisibility(View.VISIBLE);
                    ratetext.setVisibility(View.VISIBLE);
                    rating.setVisibility(View.VISIBLE);
                    wooRating.setVisibility(View.VISIBLE);
                    giveRate.setVisibility(View.VISIBLE);
                    giveStar.setVisibility(View.VISIBLE);
                    imagevision.setVisibility(View.VISIBLE);
                    text.setVisibility(View.VISIBLE);
                    appRating.setVisibility(View.VISIBLE);
                    giveStar.setRating(0.0F);
                    String search = searchBox.getText().toString();
                    StringTokenizer str = new StringTokenizer(search);
                    String ans = str.nextToken();
                    while (str.hasMoreTokens())
                        ans = "%20" + str.nextToken();
                    new RequestTask().execute("http://www.omdbapi.com/?t=" + ans);
                }
            }
        });
        /* Code Referenced From www.parse.com */
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser == null) {
            loadLoginView();
        }

        if (currentUser != null)
            tvWelcome.setText(Welcome + currentUser.getUsername() + "!");

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser.logOut();
                loadLoginView();
                jsonResponseStore = null;
                count=0;
            }
        });
        /* Code Referenced Over */
        rateSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (searchBox.getText().toString() != null && !searchBox.getText().toString().isEmpty()) {
                    ParseQuery<ParseObject> query = ParseQuery.getQuery("Rating");
                    query.whereEqualTo("user", ParseUser.getCurrentUser()).whereEqualTo("movie", movieName);
                    query.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> msgList, ParseException e) {
                            getActivity().setProgressBarIndeterminateVisibility(false);
                            if (e == null) {
                                if (!msgList.isEmpty()) {
                                    userRating = msgList.get(0);
                                }
                            } else {
                                Log.d(getClass().getSimpleName(), "Error: " + e.getMessage());
                            }
                            if (userRating == null) {
                                userRating = new ParseObject("Rating");
                                userRating.put("user", ParseUser.getCurrentUser());
                                userRating.put("movie", movieName);
                            }
                            userRating.put("rating", giveStar.getRating());
                            userRating.saveInBackground();
                            getUserRating();
                        }
                    });
                }
            }
        });
        if (jsonResponseStore != null) {
            try {
                text.setText(Name + jsonResponseStore.getString("Title"));
                movieName = jsonResponseStore.getString("Title");
                Glide.with(Movie_fragment.this).load(jsonResponseStore.getString("Poster")).into(imagevision);
                String rate = jsonResponseStore.getString("imdbRating");
                float d = Float.parseFloat(rate);
                rating.setRating(d);
                imageurl = jsonResponseStore.getString("Poster");
                setRating();
                getUserRating();
            } catch (JSONException je) {

            }
        }
        return rootview;
    }


    private void getUserRating() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Rating");
        query.whereEqualTo("movie", movieName);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> msgList, ParseException e) {
                getActivity().setProgressBarIndeterminateVisibility(false);
                if (e == null) {
                    int countUsers = 0;
                    float userRatings = 0.0f;
                    for (ParseObject msg : msgList) {
                        countUsers++;
                        userRatings += Float.parseFloat(msg.get("rating").toString());
                    }
                    appRating.setRating(userRatings / countUsers);
                } else {
                }
            }
        });
    }

    private void setRating() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Rating");
        query.whereEqualTo("user", ParseUser.getCurrentUser()).whereEqualTo("movie", movieName);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> msgList, ParseException e) {
                getActivity().setProgressBarIndeterminateVisibility(false);
                if (e == null) {
                    for (ParseObject msg : msgList) {
                        giveStar.setRating(Float.parseFloat(msg.get("rating").toString()));
                    }
                } else {
                }
            }
        });
    }

    private class RequestTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... uri) {
            /* Code Referenced From StackOverFlow.com */
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response;
            String responseString = null;
            try {
                response = httpclient.execute(new HttpGet(uri[0]));
                StatusLine statusLine = response.getStatusLine();
                if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    response.getEntity().writeTo(out);
                    out.close();
                    responseString = out.toString();
                } else {
                    response.getEntity().getContent().close();
                    throw new IOException(statusLine.getReasonPhrase());
                }
            } catch (Exception e) {
            }
            return responseString;
        }
        /* Code Referenced Over */
        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            if (response != null) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    text.setText(Name + jsonResponse.getString("Title"));
                    movieName = jsonResponse.getString("Title");
                    Glide.with(Movie_fragment.this).load(jsonResponse.getString("Poster")).into(imagevision);
                    String rate = jsonResponse.getString("imdbRating");
                    float d = Float.parseFloat(rate);
                    rating.setRating(d);
                    imageurl = jsonResponse.getString("Poster");
                    setRating();
                    jsonResponseStore = jsonResponse;
                    getUserRating();
                } catch (Exception e) {
                }
            }
        }
    }

}
