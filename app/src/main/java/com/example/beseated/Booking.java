package com.example.beseated;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.beseated.databinding.ActivityBookingBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Booking extends AppCompatActivity {

    ActivityBookingBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBookingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SharedPreferences sharedPreferences = getSharedPreferences("AuthToken",MODE_PRIVATE);

        Intent intent = getIntent();


        binding.remainingText.setText(""+intent.getIntExtra("availableSeats",0)+" seats are remaining !!");


        binding.DateAndTimeET.setInputType(InputType.TYPE_NULL);

        binding.DateAndTimeET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateAndTimeDialog(binding.DateAndTimeET);
            }
        });

        binding.bookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject postData = new JSONObject();

                try {
                    Log.d("id2",intent.getStringExtra("id"));
                    postData.put("restaurant_id", intent.getStringExtra("id"));
                    if(binding.DateAndTimeET.getText().toString().matches("")){
                        binding.DateAndTimeET.setError("Field Required");
                        return;
                    }
                    String time = binding.DateAndTimeET.getText().toString()+":00" ;
                    Log.d("time",time);
                    if(Integer.parseInt(binding.NumberOfSeatsET.getText().toString()) > intent.getIntExtra("availableSeats",0)){
                        binding.NumberOfSeatsET.setError("Cannot exceed "+intent.getIntExtra("availableSeats",0));
                        return;
                    }
                    postData.put("seats",Integer.parseInt(binding.NumberOfSeatsET.getText().toString()));
                    postData.put("timing", time);


                } catch (JSONException e) {
                    e.printStackTrace();
                }


                Log.d("POST",postData.toString());
                RequestQueue requestQueue = Volley.newRequestQueue(Booking.this);
                JsonObjectRequest jsonAbjectRequest = new JsonObjectRequest(Request.Method.POST,
                        "https://inspiration-2021-backend.herokuapp.com/api/booking/", postData, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Toast.makeText(Booking.this, "Seats Booked", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Booking.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                        Log.d("POST","Successful");
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        String responseBody = null;
                        try {
                            responseBody = new String(error.networkResponse.data, "utf-8");
                            JSONObject data = new JSONObject(responseBody);
                            JSONObject errors = data.getJSONObject("error");
                            JSONArray jsonMessage = errors.getJSONArray("timing");
                            String message = jsonMessage.getString(0);

                            Log.d("message ",message);

                        } catch (UnsupportedEncodingException | JSONException e) {
                            e.printStackTrace();
                        }



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
        });





    }
    private void showDateAndTimeDialog(EditText dateAndTime) {
        Calendar calendar = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR,year);
                calendar.set(Calendar.MONTH,month);
                calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);


                TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
                        calendar.set(Calendar.MINUTE,minute);
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                        dateAndTime.setText(simpleDateFormat.format(calendar.getTime()));
                    }
                };
                new TimePickerDialog(Booking.this,timeSetListener,calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),false).show();

            }
        };
        new DatePickerDialog(Booking.this,dateSetListener,calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();

    }
}