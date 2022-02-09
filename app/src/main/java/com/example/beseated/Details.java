package com.example.beseated;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
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
import com.bumptech.glide.Glide;
import com.example.beseated.databinding.ActivityDetailsBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Details extends AppCompatActivity {

    ActivityDetailsBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        String id  = intent.getStringExtra("id");

        SharedPreferences sharedPreferences = getSharedPreferences("AuthToken",MODE_PRIVATE);

        binding.progressBar3.setVisibility(View.VISIBLE);


        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonAbjectRequest = new JsonObjectRequest(Request.Method.GET,
                "https://inspiration-2021-backend.herokuapp.com/api/restaurant/"+id+"/", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                binding.progressBar3.setVisibility(View.GONE);
                try {


                    Glide
                            .with(Details.this)
                            .load(response.getString("logo"))
                            .centerCrop()
                            .into(binding.detailsRestImg);

                    binding.restNameText.setText(response.getString("name"));
                    binding.address.setText(response.getString("address") +" "+response.getString("town")+" - "+response.getString("state"));
                    binding.pinCodeText.setText("Pincode : "+response.getString("pincode"));

                    binding.seatAvailableText.setText(""+response.getInt("seats"));
                    binding.description.setText(response.getString("description"));

                    binding.mapIcon.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            try{
                            Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:"+response.getDouble("latitude")+","+ response.getDouble("longitude")+"?z=12") );
                            mapIntent.setPackage("com.google.android.apps.maps");
                            startActivity(mapIntent);
                        } catch (JSONException e) {
                            Log.d("VolleyDebug","Error Printing data");
                            e.printStackTrace();
                        }
                        }
                    });

                    binding.bookingBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            try {
                                Intent intent =  new Intent(Details.this,Booking.class);
                                intent.putExtra("id",response.getString("id"));
                                intent.putExtra("availableSeats",response.getInt("seats"));
                                Log.d("id",""+id);
                                startActivity(intent);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    });




                } catch (JSONException e) {
                    Log.d("VolleyDebug","Error Printing data");
                    e.printStackTrace();
                }




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
}