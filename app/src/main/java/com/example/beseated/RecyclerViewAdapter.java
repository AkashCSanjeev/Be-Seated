package com.example.beseated;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;


import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private Context context;
    private List<Restaurant> mdata;
    String mtoken;
    int layoutId ;


    public RecyclerViewAdapter() {

    }

    public RecyclerViewAdapter(Context context, List<Restaurant> data, String token, int layoutId) {
        Log.d("check:", "constructor");
        this.context = context;
        this.mdata = data;
        this.mtoken = token;
        this.layoutId = layoutId;
    }


    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        Log.d("check:", "recyclerOnCreate");
        return new ViewHolder(view);
    }



    @Override
    public void onBindViewHolder(RecyclerViewAdapter.ViewHolder holder, int position) {
        Restaurant task = mdata.get(position);



            holder.restName.setText(task.getRestName());
            holder.restAddress.setText(task.getTown()+" - "+task.getState());


            Glide
                    .with(context)
                    .load(task.getUrl())
                    .centerCrop()
                    .into(holder.restPic);





    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }




    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView restName,restAddress;
        ImageView restPic;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

                restName = itemView.findViewById(R.id.RestNameText);
                restAddress = itemView.findViewById(R.id.gridAddressText);
                restPic = itemView.findViewById(R.id.restaurantImage);


        }


        @Override
        public void onClick(View v) {
            int position = this.getAdapterPosition();
            Restaurant task = mdata.get(position);

            Intent intent = new Intent(context, Details.class);
            intent.putExtra("id",task.getId());

            context.startActivity(intent);


        }
    }


}

