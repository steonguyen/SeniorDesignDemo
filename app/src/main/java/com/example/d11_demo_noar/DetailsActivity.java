package com.example.d11_demo_noar;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class DetailsActivity extends AppCompatActivity {

    private String place_id_string;
    private TextView place_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.place_details);

        Bundle extras = getIntent().getExtras();

        if(extras!=null){
            place_id_string = extras.getString("place_id_string");
        }

        place_id = (TextView) findViewById(R.id.poppy);
        place_id.setText(place_id_string);
    }
}