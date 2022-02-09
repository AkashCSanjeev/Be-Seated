package com.example.beseated;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.beseated.Auth.SignUp;
import com.example.beseated.databinding.ActivityMainBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    RecyclerViewAdapter recyclerViewAdapter;
    List<Restaurant> restaurants;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        restaurants = new ArrayList<>();
        SharedPreferences sharedPreferences = getSharedPreferences("AuthToken",MODE_PRIVATE);

        binding.recyclerView.setLayoutManager(new GridLayoutManager(this,2));

        binding.progressBar2.setVisibility(View.VISIBLE);


        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonAbjectRequest = new JsonArrayRequest(Request.Method.GET,
                "https://inspiration-2021-backend.herokuapp.com/api/restaurants/", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                binding.progressBar2.setVisibility(View.GONE);

                try {

                    for(int i = 0 ; i<response.length();i++){
                        JSONObject obj = response.getJSONObject(i);

                        restaurants.add(new Restaurant(obj.getString("town"),obj.getString("state"),obj.getString("name"),obj.getString("logo"),obj.getString("id") ));

                    }
                } catch (JSONException e) {
                    Log.d("VolleyDebug","Error Printing data");
                    e.printStackTrace();
                }

                recyclerViewAdapter = new RecyclerViewAdapter(MainActivity.this,restaurants,sharedPreferences.getString("token",""),R.layout.recycler_item_view_grid);
                binding.recyclerView.setAdapter(recyclerViewAdapter);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("VolleyDebug","Error retrieving data");
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("Authorization", "Bearer "+sharedPreferences.getString("token",""));

                return params;
            }
        };

        requestQueue.add(jsonAbjectRequest);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){

            case R.id.logOut:

                SharedPreferences sharedPreferences = getSharedPreferences("AuthToken",MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();

                Intent intent = new Intent(MainActivity.this, SignUp.class);
                startActivity(intent);
                finish();
                break;


            case R.id.pastBookings:
                Intent i = new Intent(MainActivity.this, PastBookings.class);
                startActivity(i);


        }

        return true;
    }
}