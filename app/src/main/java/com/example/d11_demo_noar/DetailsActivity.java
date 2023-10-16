package com.example.d11_demo_noar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

public class DetailsActivity extends AppCompatActivity {

    private String place_id_string;
    private TextView place_id;

    private Button closep;

    // ADDRESS AND CONTACT

    private TextView name_det;
    private TextView address_det;
    private TextView phone_det;
    private TextView website_det;
    private TextView googleurl_det;
    private TextView price_det;
    private TextView rating_det;
    private TextView review1;
    private TextView review2;

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

        name_det = (TextView) findViewById(R.id.name_det);
        address_det = (TextView) findViewById(R.id.address_det);
        phone_det = (TextView) findViewById(R.id.phone_det);
        website_det = (TextView) findViewById(R.id.website_det);
        googleurl_det = (TextView) findViewById(R.id.googleurl_det);
        price_det = (TextView) findViewById(R.id.price_det);
        rating_det = (TextView) findViewById(R.id.rating_det);

        review1 = (TextView) findViewById(R.id.review1);
        review2 = (TextView) findViewById(R.id.review2);

        searchPlaceDetails(place_id_string);

        closep = (Button) findViewById(R.id.closep);
        closep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DetailsActivity.this.finish();
            }
        });

    }

    private void searchPlaceDetails(String placeid){
        String fields = "name%2Cplace_id%2Cgeometry%2Crating%2Copening_hours%2Creviews";
        String key = "AIzaSyBAyn3y0awsDMe0x7oUZ9i_iiu61lDWCR0";
        String url = "https://maps.googleapis.com/maps/api/place/details/json?place_id=" + placeid + "&fields=" + fields + "&reviews_sort=most_relevant" + "&key=" + key;
        new DetailsActivity.PlaceTask().execute(url);
    }

    private class PlaceTask extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            String data = null;
            try {
                data = downloadUrl(strings[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return data;
        }

        @Override
        protected void onPostExecute(String s) {
            System.out.println("2STREEE: " + s);
            new DetailsActivity.ParserTask().execute(s);
        }
    }

    private String downloadUrl(String string) throws IOException {
        URL url = new URL(string);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.connect();

        InputStream stream = connection.getInputStream();

        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

        StringBuilder builder = new StringBuilder();

        String line = "";

        while((line = reader.readLine()) != null){
            builder.append(line);
        }
        String data = builder.toString();
        reader.close();
        return data;
    }

    private class ParserTask extends AsyncTask<String, Integer, HashMap<String, String>>{

        @Override
        protected HashMap<String, String> doInBackground(String... strings) {
            JsonParser jsonParser2 = new JsonParser();
            HashMap<String,String> detailMap = null;
            JSONObject object = null;
            try {
                object = new JSONObject(strings[0]);
                detailMap = jsonParser2.parseDetailsResult(object);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return detailMap;
        }

        @Override
        protected void onPostExecute(HashMap<String, String> detailMap) {
            name_det.setText(detailMap.get("name"));
            address_det.setText(detailMap.get("formatted_address"));
            phone_det.setText(detailMap.get("formatted_phone_number"));
            website_det.setText(detailMap.get("website"));
            googleurl_det.setText(detailMap.get("googleurl"));
            price_det.setText("Price Level: " + detailMap.get("price_level"));
            rating_det.setText("Average Rating: " + detailMap.get("rating_det"));

            review1.setText(detailMap.get("review1"));
            review2.setText(detailMap.get("review2"));

        }
    }
}