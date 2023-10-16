package com.example.d11_demo_noar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private ArrayList<locationObject> listItems;
    private Context context;

    public MyAdapter(Context context, ArrayList<locationObject> listItems) {
        this.listItems = listItems;
        this.context = context;
    }

    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.list, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.ViewHolder holder, int position) {
        locationObject listItem = listItems.get(position);
        holder.name.setText(listItem.getName());
        holder.rating.setText(listItem.getRating());
        holder.place_id.setText(listItem.getPlace_id());
        holder.latitude.setText(listItem.getLatitude());
        holder.longitude.setText(listItem.getLongitude());
        holder.rating.setText(listItem.getRating());
        holder.diff_angle.setText(listItem.getDiff_angle());
        holder.isOpen.setText(listItem.getIsOpen());
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        TextView place_id;
        TextView rating;
        TextView latitude;
        TextView longitude;
        TextView diff_angle;
        TextView isOpen;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            place_id = itemView.findViewById(R.id.place_id);
            rating = itemView.findViewById(R.id.rating);
            latitude = itemView.findViewById(R.id.latitude);
            longitude = itemView.findViewById(R.id.longitude);
            diff_angle = itemView.findViewById(R.id.diff_angle);
            isOpen = itemView.findViewById(R.id.isOpen);

        }
    }
}
