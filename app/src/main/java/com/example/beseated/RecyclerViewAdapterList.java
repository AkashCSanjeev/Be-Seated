package com.example.beseated;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class RecyclerViewAdapterList extends RecyclerView.Adapter<RecyclerViewAdapterList.ViewHolder> {

    private Context context;
    private List<Restaurant> mdata;
    String mtoken;
    int layoutId ;


    public RecyclerViewAdapterList() {

    }

    public RecyclerViewAdapterList(Context context, List<Restaurant> data, String token, int layoutId) {
        Log.d("check:", "constructor");
        this.context = context;
        this.mdata = data;
        this.mtoken = token;
        this.layoutId = layoutId;
    }


    @Override
    public RecyclerViewAdapterList.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        Log.d("check:", "recyclerOnCreate");
        return new ViewHolder(view);
    }



    @Override
    public void onBindViewHolder(RecyclerViewAdapterList.ViewHolder holder, int position) {
        Restaurant task = mdata.get(position);



        holder.noOfSeats.setText(""+task.getNoOfSeats());
        holder.time.setText(task.getBookedTime());
        holder.day.setText(task.getDay());





    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }




    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView noOfSeats,time,day;


        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            noOfSeats = itemView.findViewById(R.id.bookedSeats);
            time = itemView.findViewById(R.id.bookedTime);
            day = itemView.findViewById(R.id.day);



        }


        @Override
        public void onClick(View v) {
//            int position = this.getAdapterPosition();
//            Restaurant task = mdata.get(position);
//
//            Intent intent = new Intent(context, Details.class);
//            intent.putExtra("id",task.getId());
//
//            context.startActivity(intent);


        }
    }


}