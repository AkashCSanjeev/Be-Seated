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
import com.example.beseated.databinding.ActivityForgotPasswordBinding;

import org.json.JSONException;
import org.json.JSONObject;

public class ForgotPassword extends AppCompatActivity {

    ActivityForgotPasswordBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityForgotPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();

        binding.resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject postData = new JSONObject();

                try {
                    if(binding.otpEt.getText().toString().matches("")){
                        binding.otpEt.setError("Enter Otp");
                        return;
                    }
                    int otp = Integer.parseInt(binding.otpEt.getText().toString());
                    postData.put("otp", otp);
                    postData.put("npw", binding.newPasswordEd.getText().toString());
                    postData.put("cpw", binding.retypeNewPasswordEt.getText().toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }


                Log.d("POST",postData.toString());
                RequestQueue requestQueue = Volley.newRequestQueue(ForgotPassword.this);
                JsonObjectRequest jsonAbjectRequest = new JsonObjectRequest(Request.Method.POST,
                        "https://inspiration-2021-backend.herokuapp.com/api/reset/", postData, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            Toast.makeText(ForgotPassword.this,response.getString("message"),Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ForgotPassword.this, SignIn.class);
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

                        if(binding.newPasswordEd.getText().toString().matches(binding.retypeNewPasswordEt.getText().toString())
                                && (!binding.newPasswordEd.getText().toString().matches(""))){

                        }else {
                            binding.retypeNewPasswordEt.setError("Check Again");
                        }
                    }
                });

                requestQueue.add(jsonAbjectRequest);
            }
        });

        binding.resendOtpText.setOnClickListener(new View.OnClickListener() {
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
                RequestQueue requestQueue = Volley.newRequestQueue(ForgotPassword.this);
                JsonObjectRequest jsonAbjectRequest = new JsonObjectRequest(Request.Method.POST,
                        "https://delta-inspiration.herokuapp.com/api/resend/forgot/", postData, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            Toast.makeText(ForgotPassword.this,response.getString("message"),Toast.LENGTH_SHORT).show();


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