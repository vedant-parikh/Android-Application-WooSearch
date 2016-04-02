package com.vedantparikh.woosearch;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import org.json.JSONArray;

import com.bumptech.glide.Glide;
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
import java.util.StringTokenizer;


public class RecipeFragment extends Fragment {
    private static JSONObject jsonResponseStore;
    protected Button btnLogout;
    protected TextView tvWelcome;
    private EditText searchBox;
    private Button searchButton;
    private View rootview;
    private TextView text;
    private ImageView imagevision2;
    private RatingBar rating;
    private TextView ingredientSet;
    private TextView ratetext;
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

        rootview = inflater.inflate(R.layout.fragment_recipe, container, false);
        searchBox = (EditText) rootview.findViewById(R.id.text_search_box);
        searchButton = (Button) rootview.findViewById(R.id.search);
        rating = (RatingBar) rootview.findViewById(R.id.pop_ratingbar);
        text = (TextView) rootview.findViewById(R.id.text);
        imagevision2 = (ImageView) rootview.findViewById(R.id.image2);
        tvWelcome = (TextView) rootview.findViewById(R.id.tvWelcome);
        btnLogout = (Button) rootview.findViewById(R.id.btnLogout);
        ingredientSet = (TextView) rootview.findViewById(R.id.ingre);
        ratetext = (TextView)rootview.findViewById(R.id.ratetext);

        if(count!=1){
            text.setVisibility(View.INVISIBLE);
            ingredientSet.setVisibility(View.INVISIBLE);
            imagevision2.setVisibility(View.INVISIBLE);
            ratetext.setVisibility(View.INVISIBLE);
            rating.setVisibility(View.INVISIBLE);
        }

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                count=1;
                if (searchBox.getText().toString() != null && !searchBox.getText().toString().isEmpty()) {
                    String search = searchBox.getText().toString();
                    StringTokenizer str = new StringTokenizer(search);
                    String ans = str.nextToken();
                    while (str.hasMoreTokens())
                        ans = "+" + str.nextToken();
                    new RequestTask().execute("http://api.yummly.com/v1/api/recipes?_app_id=3670df6d&_app_key=d89229cf5f9c1ff9a0efab74ec94e5a0&q=" + ans);
                    text.setVisibility(View.VISIBLE);
                    ingredientSet.setVisibility(View.VISIBLE);
                    imagevision2.setVisibility(View.VISIBLE);
                    ratetext.setVisibility(View.VISIBLE);
                    rating.setVisibility(View.VISIBLE);
                }
            }
        });
        /* Code Referenced From www.parse.com */
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser == null) {
            loadLoginView();
        }

        if (currentUser != null)
            tvWelcome.setText("Welcome, " + currentUser.getUsername() + "!");
        /* Code Referenced Over */
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser.logOut();
                loadLoginView();
                jsonResponseStore = null;
                count=0;
            }
        });


        if (jsonResponseStore != null) {
            try {
                JSONArray matches = jsonResponseStore.getJSONArray("matches");
                JSONObject first = matches.getJSONObject(0);
                text.setText(Name + first.getString("recipeName"));
                JSONObject images = first.getJSONObject("imageUrlsBySize");
                String rate = first.getString("rating");
                float d = Float.parseFloat(rate);
                rating.setRating(2 * d);
                Glide.with(RecipeFragment.this).load(images.getString("90")).into(imagevision2);
                JSONArray ingredient = first.getJSONArray("ingredients");
                String giveComma = "";
                for (int i = 0; i < ingredient.length(); i++) {
                    giveComma = giveComma + ingredient.get(i).toString() + ", ";
                }
                ingredientSet.setText("Ingredients : " + giveComma.toString());
            } catch (JSONException je) {

            }
        }

        return rootview;
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
            /* Code Referenced Over */
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            if (response != null) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    JSONArray matches = jsonResponse.getJSONArray("matches");
                    JSONObject first = matches.getJSONObject(0);
                    text.setText(Name + first.getString("recipeName"));
                    JSONObject images = first.getJSONObject("imageUrlsBySize");
                    String rate = first.getString("rating");
                    float d = Float.parseFloat(rate);
                    rating.setRating(2 * d);
                    Glide.with(RecipeFragment.this).load(images.getString("90")).into(imagevision2);
                    JSONArray ingredient = first.getJSONArray("ingredients");
                    jsonResponseStore = jsonResponse;
                    String giveComma = "";
                    for (int i = 0; i < ingredient.length(); i++) {
                        giveComma = giveComma + ingredient.get(i).toString() + ", ";
                    }
                    ingredientSet.setText("Ingredients : " + giveComma.toString());
                } catch (Exception e) {
                }
            }
        }
    }
}
