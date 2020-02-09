package com.button.musicsearch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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

import java.lang.reflect.Array;
import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity
{
    RequestQueue requestQueue;

    EditText searchText;

    Context context;

    private final String apiString = "https://api.deezer.com/search?q=";

    ArrayList<String> songNames = new ArrayList<String>();
    ArrayList<String> artistNames = new ArrayList<String>();
    ArrayList<String> albumNames = new ArrayList<String>();
    ArrayList<String> albumImages = new ArrayList<String>();

    MyAdapter adapter;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        this.getSupportActionBar().hide();
        context = this;


        searchText = findViewById(R.id.searchText);
        listView = findViewById(R.id.listView);

        adapter = new MyAdapter(this, songNames, artistNames, albumNames, albumImages);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(context, SongView.class);
                intent.putExtra("songName", songNames.get(position));
                intent.putExtra("artistName", artistNames.get(position));
                intent.putExtra("albumName", albumNames.get(position));
                intent.putExtra("albumImage", albumImages.get(position));
                startActivity(intent);
            }
        });

    }

    public void OnSavedSongsClick(View view)
    {
        Intent intent = new Intent(this, SavedSongsActivity.class);
        startActivity(intent);
        finish();
    }

    public void OnHomeClick(View view)
    {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    public void OnSearchClick(View view)
    {
        String res = searchText.getText().toString();
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
                                        songNames.clear();
                                        artistNames.clear();
                                        albumNames.clear();
                                        albumImages.clear();

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
                                            artistNames.add(artistName);
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


}
