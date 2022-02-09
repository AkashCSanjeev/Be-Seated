package com.example.beseated.Auth;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.example.beseated.MainActivity;
import com.example.beseated.R;
import com.example.beseated.databinding.ActivitySignInBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class SignIn extends AppCompatActivity {

    ActivitySignInBinding binding;
    ProgressDialog progressDialog ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();


        binding.SignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject postData = new JSONObject();
                progressDialog = new ProgressDialog(SignIn.this);
                progressDialog.setTitle("Sign In");
                progressDialog.setMessage("Logging in ...");

                try {

                    postData.put("email", binding.emailEt.getText().toString());
                    postData.put("password", binding.passwordEt.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                progressDialog.show();

                Log.d("POST",postData.toString());
                RequestQueue requestQueue = Volley.newRequestQueue(SignIn.this);
                JsonObjectRequest jsonAbjectRequest = new JsonObjectRequest(Request.Method.POST,
                        "https://inspiration-2021-backend.herokuapp.com/api/login/", postData, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            Toast.makeText(SignIn.this,response.getString("message"),Toast.LENGTH_SHORT).show();

                            SharedPreferences sharedPreferences = getSharedPreferences("AuthToken",MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("token",response.getString("token"));
                            editor.apply();

                            Intent intent = new Intent(SignIn.this, MainActivity.class);
                            startActivity(intent);
                            progressDialog.dismiss();
                            finish();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }





                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("VolleyDebug","Error retrieving data");
                        progressDialog.dismiss();
                        if(binding.passwordEt.getText().toString().matches("")) {
                            binding.passwordEt.setError("Enter Password");
                        }
                        if(error.networkResponse.statusCode == 400){
                            try {
                                String responseBody = new String(error.networkResponse.data, "utf-8");
                                JSONObject data = new JSONObject(responseBody);
                                JSONObject errors = data.getJSONObject("error");
                                JSONArray jsonMessage = errors.getJSONArray("email");
                                String message = jsonMessage.getString(0);
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                                binding.emailEt.setError("Check Email");

                            } catch (JSONException | UnsupportedEncodingException e) {
                            }
                        }else if(error.networkResponse.statusCode == 406){
                            try {
                                String responseBody = new String(error.networkResponse.data, "utf-8");
                                JSONObject data = new JSONObject(responseBody);
                                String message = data.getString("result");
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                                binding.passwordEt.setError("Incorrect Password");
                            } catch (JSONException | UnsupportedEncodingException e) {
                            }
                        }else{
                            try {
                                String responseBody = new String(error.networkResponse.data, "utf-8");
                                JSONObject data = new JSONObject(responseBody);
                                String message = data.getString("result");
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                                binding.passwordEt.setError("Incorrect Password");
                            } catch (JSONException | UnsupportedEncodingException e) {
                            }
                        }
                    }
                });

                requestQueue.add(jsonAbjectRequest);
            }
        });

        binding.forgotPasswordText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                JSONObject postData = new JSONObject();

                try {

                    postData.put("email", binding.emailEt.getText().toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }


                Log.d("POST",postData.toString());
                RequestQueue requestQueue = Volley.newRequestQueue(SignIn.this);
                JsonObjectRequest jsonAbjectRequest = new JsonObjectRequest(Request.Method.POST,
                        "https://delta-inspiration.herokuapp.com/api/forgot/", postData, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            Toast.makeText(SignIn.this,response.getString("message"),Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SignIn.this, ForgotPassword.class);
                            intent.putExtra("email",binding.emailEt.getText().toString());
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

                        if(error.networkResponse.statusCode == 400){
                            try {
                                binding.emailEt.setError("Email Required");
                                String responseBody = new String(error.networkResponse.data, "utf-8");
                                JSONObject data = new JSONObject(responseBody);
                                JSONObject errors = data.getJSONObject("error");
                                JSONArray jsonMessage = errors.getJSONArray("email");
                                String message = jsonMessage.getString(0);
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

                            } catch (JSONException | UnsupportedEncodingException e) {
                            }
                        }else if(error.networkResponse.statusCode == 500){
                            Toast.makeText(getApplicationContext(), "Create a account first", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(SignIn.this, SignUp.class);
                            startActivity(intent);
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "Check your email", Toast.LENGTH_LONG).show();
                        }
                    }
                });

                requestQueue.add(jsonAbjectRequest);
            }
        });


    }
}