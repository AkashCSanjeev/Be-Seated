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
import com.example.beseated.databinding.ActivitySignUpBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class SignUp extends AppCompatActivity {

    ActivitySignUpBinding binding;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        SharedPreferences sharedPreferences = getSharedPreferences("AuthToken",MODE_PRIVATE);

        if(sharedPreferences.getString("token","") != ""){
            Intent intent = new Intent(SignUp.this, MainActivity.class);
            startActivity(intent);
            finish();
            return;

        }

        binding.signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject postData = new JSONObject();

                progressDialog = new ProgressDialog(SignUp.this);
                progressDialog.setTitle("Creating Account");
                progressDialog.setMessage("We are creating your account");


                try {
                    if(binding.nameET.getText().toString().matches("") || binding.emailET.getText().toString().matches("") || binding.passwordET.getText().toString().matches("")){
                        if(binding.nameET.getText().toString().matches("")){
                            binding.nameET.setError("Field Required");

                        }
                        if(binding.emailET.getText().toString().matches("")){
                            binding.emailET.setError("Field Required");
                        }
                        if(binding.passwordET.getText().toString().matches("")){
                            binding.passwordET.setError("Field Required");
                        }

                        return;
                    }

                    postData.put("name", binding.nameET.getText().toString());
                    postData.put("email", binding.emailET.getText().toString());
                    postData.put("password", binding.passwordET.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressDialog.show();

                Log.d("POST",postData.toString());
                RequestQueue requestQueue = Volley.newRequestQueue(SignUp.this);
                JsonObjectRequest jsonAbjectRequest = new JsonObjectRequest(Request.Method.POST,
                        "https://inspiration-2021-backend.herokuapp.com/api/signup/", postData, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            Toast.makeText(SignUp.this,response.getString("message"),Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(SignUp.this, Verification.class);
                            intent.putExtra("email",binding.emailET.getText().toString());
                            progressDialog.dismiss();
                            startActivity(intent);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }





                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        progressDialog.dismiss();

                        Log.d("VolleyDebug","Error retrieving data");
                        if(error.networkResponse.statusCode == 400 ){
                            try {
                                String responseBody = new String(error.networkResponse.data, "utf-8");
                                JSONObject data = new JSONObject(responseBody);
                                JSONObject errors = data.getJSONObject("error");
                                JSONArray jsonMessage = errors.getJSONArray("email");
                                String message = jsonMessage.getString(0);
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                                binding.emailET.setError("Check Email");
                            } catch (JSONException | UnsupportedEncodingException e) {
                            }

                        }else{
                            Intent intent = new Intent(SignUp.this, SignIn.class);
                            Toast.makeText(getApplicationContext(), "Existing account", Toast.LENGTH_LONG).show();

                            startActivity(intent);
                            finish();
                        }

                    }
                });

                requestQueue.add(jsonAbjectRequest);
            }
        });

        binding.hasAccountText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUp.this,SignIn.class);
                startActivity(intent);            }
        });

    }
}