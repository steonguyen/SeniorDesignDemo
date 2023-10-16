package com.example.d11_demo_noar;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;


public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private ArrayList<locationObject> listItems;
    private Context context;

    private ClickListener listener;

    public MyAdapter(Context context, ArrayList<locationObject> listItems, ClickListener detail_search) {
        this.listItems = listItems;
        this.context = context;
        this.listener = detail_search;
    }

    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.list, parent, false);

        return new ViewHolder(v, listener);
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
        holder.place_id_string = listItem.getPlace_id();

    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView place_id;
        private String place_id_string;
        private TextView rating;
        private TextView latitude;
        private TextView longitude;
        private TextView diff_angle;
        private TextView isOpen;
        private Button detail_search;

        public ViewHolder(@NonNull View itemView, ClickListener listener) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            place_id = itemView.findViewById(R.id.place_id);
            rating = itemView.findViewById(R.id.rating);
            latitude = itemView.findViewById(R.id.latitude);
            longitude = itemView.findViewById(R.id.longitude);
            diff_angle = itemView.findViewById(R.id.diff_angle);
            isOpen = itemView.findViewById(R.id.isOpen);

            detail_search = itemView.findViewById(R.id.detail_search);
            detail_search.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    System.out.println("BRUHHH" + place_id_string);
                    openDetailActivity(place_id_string);
                }
            });

        }

        public void openDetailActivity(String place_id_string){
            Intent detail_intent = new Intent(context, DetailsActivity.class);
            detail_intent.putExtra("place_id_string", place_id_string);
            context.startActivity(detail_intent);
        }

    }
}
