package com.example.beseated;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.beseated.databinding.ActivityPastBookingsBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class PastBookings extends AppCompatActivity {

    ActivityPastBookingsBinding binding;
    RecyclerViewAdapterList recyclerViewAdapter1;
    List<Restaurant> restaurants;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPastBookingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        restaurants = new ArrayList<>();
        SharedPreferences sharedPreferences = getSharedPreferences("AuthToken",MODE_PRIVATE);

        binding.progressBar.setVisibility(View.VISIBLE);

        binding.recyclerViewList.setLayoutManager(new LinearLayoutManager(this));

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonAbjectRequest = new JsonObjectRequest(Request.Method.GET,
                "https://inspiration-2021-backend.herokuapp.com/api/past-bookings/", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    binding.progressBar.setVisibility(View.GONE);
                    JSONArray arr = response.getJSONArray("data");
                    Log.d("time","inside");

                    for(int i = 0 ; i<arr.length();i++){
                        JSONObject obj = arr.getJSONObject(i);
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date date = format.parse(obj.getString("timing"));
                        long timestamp = date.getTime();
                        String weekday = String.format(Locale.ENGLISH, "%tA", timestamp * 1000L);

                        SimpleDateFormat df = new SimpleDateFormat("hh:mm");
                        String time = df.format(new Date(timestamp * 1000L));

                        Log.d("time",weekday+" "+time);

                        restaurants.add(new Restaurant(weekday,time, obj.getInt("cart_items") ));

                    }
                } catch (JSONException | ParseException e) {
                    Log.d("VolleyDebug","Error Printing data");
                    e.printStackTrace();
                }

                recyclerViewAdapter1 = new RecyclerViewAdapterList(PastBookings.this,restaurants,sharedPreferences.getString("token",""),R.layout.recycler_item_view_list);
                binding.recyclerViewList.setAdapter(recyclerViewAdapter1);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("VolleyDebug","Error retrieving data");
                Log.d("VolleyDebug",error.toString());
//                Log.d("VolleyDebug",error.networkResponse.toString());


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


}