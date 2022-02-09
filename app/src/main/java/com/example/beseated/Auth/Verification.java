package com.example.beseated.Auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.beseated.R;
import com.example.beseated.databinding.ActivityVerificationBinding;

import org.json.JSONException;
import org.json.JSONObject;

public class Verification extends AppCompatActivity {

    ActivityVerificationBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVerificationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();


        binding.verifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject postData = new JSONObject();


                try {
                    if(binding.otpET.getText().toString().matches("")){
                        binding.otpET.setError("Enter Otp");
                        return;
                    }
                    int OTP = Integer.parseInt(binding.otpET.getText().toString());
                    postData.put("otp", OTP);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


                Log.d("POST",postData.toString());
                RequestQueue requestQueue = Volley.newRequestQueue(Verification.this);
                JsonObjectRequest jsonAbjectRequest = new JsonObjectRequest(Request.Method.POST,
                        "https://inspiration-2021-backend.herokuapp.com/api/verify/", postData, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            Toast.makeText(Verification.this,response.getString("message"),Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Verification.this, SignIn.class);
                            startActivity(intent);
                            finish();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }





                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("VolleyDebug","Error retrieving data");

                    }
                });

                requestQueue.add(jsonAbjectRequest);
            }
        });


        binding.resendVerificationOtpText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject postData = new JSONObject();

                try {
                    Intent intent =getIntent();
                    postData.put("email", intent.getStringExtra("email"));


                } catch (JSONException e) {
                    e.printStackTrace();
                }


                Log.d("POST",postData.toString());
                RequestQueue requestQueue = Volley.newRequestQueue(Verification.this);
                JsonObjectRequest jsonAbjectRequest = new JsonObjectRequest(Request.Method.POST,
                        "https://delta-inspiration.herokuapp.com/api/resend/verify/", postData, new Response.Listener<JSONObject>() {
                    @Override

                    public void onResponse(JSONObject response) {

                        try {
                            Toast.makeText(Verification.this,response.getString("message"),Toast.LENGTH_SHORT).show();


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }





                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("VolleyDebug","Error retrieving data");

                    }
                });

                requestQueue.add(jsonAbjectRequest);
            }
        });
    }
}