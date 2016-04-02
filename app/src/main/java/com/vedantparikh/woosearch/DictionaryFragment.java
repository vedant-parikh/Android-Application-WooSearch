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
import android.widget.TextView;

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

import org.json.JSONArray;

public class DictionaryFragment extends Fragment {
    protected Button btnLogout;
    protected TextView tvWelcome;
    private EditText searchBox;
    private Button searchButton;
    private View rootview;
    private TextView text;
    private TextView meaning;
    private TextView textmeaning;
    private static JSONArray jsonResponseStore1;
    private static JSONObject jsonResponseStore2;
    private String WordName ="Name: ";
    private String WordMeaning ="Meaning: ";
    private String WordExample = "Example: ";
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

        rootview = inflater.inflate(R.layout.fragment_dictionary, container, false);
        searchBox = (EditText) rootview.findViewById(R.id.text_search_box);
        searchButton = (Button) rootview.findViewById(R.id.search);
        text = (TextView) rootview.findViewById(R.id.text);
        meaning = (TextView) rootview.findViewById(R.id.meaning);
        tvWelcome = (TextView) rootview.findViewById(R.id.tvWelcome);
        btnLogout = (Button) rootview.findViewById(R.id.btnLogout);
        textmeaning = (TextView) rootview.findViewById(R.id.textmeaning);


        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (searchBox.getText().toString() != null && !searchBox.getText().toString().isEmpty()) {
                    String search = searchBox.getText().toString();
                    StringTokenizer str = new StringTokenizer(search);
                    String ans = str.nextToken();
                    while (str.hasMoreTokens())
                        ans = "%20" + str.nextToken();
                    new RequestTask().execute("http://api.wordnik.com:80/v4/word.json/" + ans + "/definitions?limit=200&includeRelated=true&useCanonical=false&includeTags=false&api_key=a2a73e7b926c924fad7001ca3111acd55af2ffabf50eb4ae5");
                    new RequestTask().execute("http://api.wordnik.com:80/v4/word.json/" + ans + "/topExample?useCanonical=false&api_key=a2a73e7b926c924fad7001ca3111acd55af2ffabf50eb4ae5");
                }
            }
        });

        /* Code Referenced From www.parse.com */
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser == null) {
            loadLoginView();
        }

        if (currentUser != null)
            tvWelcome.setText( Welcome + currentUser.getUsername() + "!");
        /* Code Referenced Over */

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser.logOut();
                loadLoginView();
                jsonResponseStore1 =null;
                jsonResponseStore2 = null;
            }
        });
        if (jsonResponseStore1 != null && jsonResponseStore2 != null) {
            try {

                if (jsonResponseStore1.toString().charAt(0) == '[') {
                    text.setText(WordName + jsonResponseStore1.getJSONObject(0).getString("word"));
                    meaning.setText(WordMeaning + jsonResponseStore1.getJSONObject(0).getString("text"));
                    textmeaning.setText(WordExample + jsonResponseStore2.get("text"));
                }
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
                    if (response.charAt(0) == '[') {
                        JSONArray jsonResponse = new JSONArray(response);
                        text.setText(WordName + jsonResponse.getJSONObject(0).getString("word"));
                        meaning.setText(WordMeaning + jsonResponse.getJSONObject(0).getString("text"));
                        jsonResponseStore1 = jsonResponse;
                    } else {
                        JSONObject jResponse = new JSONObject(response);
                        textmeaning.setText(WordExample + jResponse.get("text"));
                        jsonResponseStore2 = jResponse;
                    }
                } catch (Exception e) {
                }
            }
        }
    }

}
