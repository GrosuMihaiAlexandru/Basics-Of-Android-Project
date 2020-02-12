package com.button.musicsearch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class HomeActivity extends AppCompatActivity
{
    private final String apiString = "https://api.deezer.com/search?q=";

    RequestQueue requestQueue;

    private EditText searchText;

    SharedPreferences sharedPref;

    // Data about all saved songs
    public static Set<String> savedSongNames = new HashSet<String>();
    public static Set<String> savedArtistNames = new HashSet<String>();
    public static Set<String> savedAlbumNames = new HashSet<String>();
    public static Set<String> savedAlbumImages = new HashSet<String>();
    public static Set<String> savedSongsPreview = new HashSet<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.getSupportActionBar().hide();
        setContentView(R.layout.activity_home);
        searchText = findViewById(R.id.searchText);

        sharedPref = getApplicationContext().getSharedPreferences("Pulla", Context.MODE_PRIVATE);

        //Log.d("AAA", sharedPref.getStringSet("songNames", new HashSet<String>()).toString());

        savedSongNames = new HashSet<String>(sharedPref.getStringSet("songNames", new HashSet<String>()));
        savedArtistNames = new HashSet<String>(sharedPref.getStringSet("songArtists", new HashSet<String>()));
        savedAlbumNames = new HashSet<String>(sharedPref.getStringSet("albumNames", new HashSet<String>()));
        savedAlbumImages = new HashSet<String>(sharedPref.getStringSet("albumImages", new HashSet<String>()));
        savedSongsPreview = new HashSet<String>(sharedPref.getStringSet("songsPreview", new HashSet<String>()));

        Log.d("AAA", savedSongNames.toString());
    }

    public void OnClick(View view)
    {
        String res = searchText.getText().toString();
        Log.d("AAA", "Searched: " + "\"" + res + "\"");
        final String apiSearch = apiString + res;

        requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, apiSearch, null,
                        new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                // Log.d("AAA", response.toString());

                                try
                                {
                                    JSONArray array = response.getJSONArray("data");

                                    if (array.length() == 0)
                                    {
                                        Log.d("AAA", "No results");
                                    }
                                    else
                                    {
                                        for(int i = 0; i < array.length(); i++)
                                        {
                                            JSONObject songInfo = array.getJSONObject(i);

                                            int id  = songInfo.getInt("id");
                                            String title = songInfo.getString("title");
                                            String link = songInfo.getString("link");
                                            String preview = songInfo.getString("preview");
                                            int duration = songInfo.getInt("duration");

                                            JSONObject artist = songInfo.getJSONObject("artist");

                                            int artistID = artist.getInt("id");
                                            String artistName = artist.getString("name");
                                            String artistLink = artist.getString("link");

                                            Log.d("AAA", id + " ::: " + title + " ::: " + link + " ::: " + preview + " ::: " + duration + " ::: " + artistID + " ::: " + artistName + " ::: " + artistLink);

                                        }
                                    }
                                }
                                catch(Exception e)
                                {
                                    e.printStackTrace();
                                }



                            }
                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("AAA", "onErrorResponse: ERROR");
                    }});

        requestQueue.add(jsonObjectRequest);
    }

    public void OnSearchClick(View view)
    {
        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
        finish();
    }

    public void OnSavedSongsClick(View view)
    {
        Intent intent = new Intent(this, SavedSongsActivity.class);
        startActivity(intent);
        finish();
    }
}
