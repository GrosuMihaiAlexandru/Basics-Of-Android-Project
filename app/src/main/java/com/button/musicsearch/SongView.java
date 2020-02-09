package com.button.musicsearch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;

public class SongView extends AppCompatActivity
{
    private String songName;
    private String artistName;
    private String albumName;
    private String albumImage;

    TextView songNameText;
    TextView artistNameText;
    TextView albumNameText;
    ImageView albumImageText;

    private final String apiString = "https://api.deezer.com/search?q=";

    RequestQueue requestQueue;

    MyAdapter adapter;
    ListView listView;

    // Data about other songs
    ArrayList<String> songNames = new ArrayList<String>();
    ArrayList<String> artistNames = new ArrayList<String>();
    ArrayList<String> albumNames = new ArrayList<String>();
    ArrayList<String> albumImages = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_view);

        this.getSupportActionBar().hide();

        // Getting the views
        songNameText = findViewById(R.id.songName);
        artistNameText = findViewById(R.id.artistName);
        albumNameText = findViewById(R.id.albumName);
        albumImageText = findViewById(R.id.imageView);

        // Getting the data from the intent
        Intent intent = getIntent();
        songName = intent.getStringExtra("songName");
        artistName = intent.getStringExtra("artistName");
        albumName = intent.getStringExtra("albumName");
        albumImage = intent.getStringExtra("albumImage");

        // Setting the data
        songNameText.setText(songName);
        artistNameText.setText("Artist:" + artistName);
        albumNameText.setText("Album:" + albumName);
        albumNameText.setMovementMethod(new ScrollingMovementMethod());
        // Loading image

        listView = findViewById(R.id.listView);

        // Creating the adapter
        adapter = new MyAdapter(this, songNames, artistNames, albumNames, albumImages);

        listView.setAdapter(adapter);


        // Searching for other songs
        SearchOtherSongs(albumName);
    }

    private void SearchOtherSongs(String res)
    {
        Log.d("AAA", "Searched: " + "\"" + res + "\"");
        final String apiSearch = apiString + res;

        requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, apiSearch, null,
                        new Response.Listener<JSONObject>()
                        {
                            @Override
                            public void onResponse(JSONObject response)
                            {
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
                                        for (int i = 0; i < array.length(); i++)
                                        {
                                            JSONObject songInfo = array.getJSONObject(i);

                                            int id = songInfo.getInt("id");
                                            String title = songInfo.getString("title");
                                            String link = songInfo.getString("link");
                                            String preview = songInfo.getString("preview");
                                            int duration = songInfo.getInt("duration");


                                            JSONObject artist = songInfo.getJSONObject("artist");

                                            int artistID = artist.getInt("id");
                                            String artistName = artist.getString("name");
                                            String artistLink = artist.getString("link");

                                            JSONObject album = songInfo.getJSONObject("album");
                                            String albumName = album.getString("title");
                                            String albumImageURL = album.getString("cover_medium");

                                            Log.d("AAA", id + " ::: " + title + " ::: " + link + " ::: " + preview + " ::: " + duration + " ::: " + artistID + " ::: " + artistName + " ::: " + artistLink + " ::: " + albumImageURL);

                                            songNames.add(title);
                                            artistNames.add("By " + artistName);
                                            albumNames.add(albumName);
                                            albumImages.add(albumImageURL);
                                        }
                                        adapter.notifyDataSetChanged();

                                    }
                                }
                                catch (Exception e)
                                {
                                    e.printStackTrace();
                                }


                            }
                        }, new Response.ErrorListener()
                {

                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Log.d("AAA", "onErrorResponse: ERROR");
                    }
                });

        requestQueue.add(jsonObjectRequest);
    }

    public void onBackClick(View view)
    {
        finish();
    }
}
