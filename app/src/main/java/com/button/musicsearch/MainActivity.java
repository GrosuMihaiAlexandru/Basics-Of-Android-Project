package com.button.musicsearch;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity
{
    private final String apiString = "https://api.deezer.com/search?q=";

    RequestQueue requestQueue;

    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        button = findViewById(R.id.button);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void OnClick(View view)
    {
        final String apiSearch = apiString + "hafanana";
    
        requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, apiSearch, null,
                        new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                Log.d("AAA", response.toString());

                                /*
                                try
                                {
                                    JSONObject lunchMenu = response.getJSONObject("LunchMenu");
                                    JSONArray setMenus = lunchMenu.getJSONArray("SetMenus");

                                    if (setMenus.length() == 0)
                                    {
                                        titles.add("No meal");
                                    }
                                    else
                                    {
                                        for (int i = 0; i < setMenus.length(); i++)
                                        {

                                            JSONObject menu = setMenus.getJSONObject(i);
                                            JSONArray mealArray = menu.getJSONArray("Meals");

                                            for (int j = 0; j < mealArray.length(); j++)
                                            {
                                                JSONObject meal = mealArray.getJSONObject(j);

                                                // Adding the info to the arrayLists
                                                String name = meal.getString("Name");
                                                titles.add(name);
                                                Log.d("AAA", "Name: " + name);
                                                JSONArray diets = meal.getJSONArray("Diets");
                                                String description = "";
                                                for (int k = 0; k < diets.length(); k++)
                                                {
                                                    description += diets.getString(k);
                                                }
                                                Log.d("AAA", "Description: " + description);
                                                descriptions.add(description);
                                            }
                                        }
                                        adapter.notifyDataSetChanged();
                                    }
                                }
                                catch(Exception e)
                                {
                                    e.printStackTrace();
                                }
                                */

                            }
                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("AAA", "onErrorResponse: ERROR");
                    }});

        requestQueue.add(jsonObjectRequest);
    }
}
