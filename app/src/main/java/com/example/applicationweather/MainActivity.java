package com.example.applicationweather;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;

public class MainActivity extends AppCompatActivity {

    TextView mdate,mcity,mtemp,mdescription;
    ImageView imgicon;
    String maVille = "Toronto";

    private RequestQueue rqueue;


    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        rqueue = Volley.newRequestQueue(this);

        mdate = findViewById(R.id.mdate);
        mcity = findViewById(R.id.mcity);
        mtemp = findViewById(R.id.mtemp);
        mdescription = findViewById(R.id.mdescription);
        mdate.setText(LocalDate.now().toString());
        afficher();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void afficher()
    {
        String url = "https://api.openweathermap.org/data/2.5/weather?q=Toronto&appid=f214627d726036042b132bf68b40acf9";
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response)
            {
                try {
                    JSONObject main_object=response.getJSONObject("main");
                    JSONArray array= response.getJSONArray("weather");
                    Object name = response.get("name");
                    JSONObject object = array.getJSONObject(0);
                    Log.d("Tag","resultat = " +array.toString()); // log resultat du logcat

                    mcity.setText(name.toString());
                    mtemp.setText(String.valueOf(Math.ceil(Double.parseDouble(main_object.get("temp").toString()) - 273.15))); // pour mettre en degreés et arrondir
                    mdescription.setText(object.get("main").toString()); // mettre le text du logcat "description"
                    String icon = object.getString("icon");
                    String city = object.getString("name");
                    String description = object.getString("description");

                    //gestion de l'image
                    String imageUri="http://openweathermap.org/img/w/" + icon + ".png";
                    imgicon = findViewById(R.id.imgicon);
                    Uri myUri = Uri.parse(imageUri);
                    Picasso.with(MainActivity.this).load(myUri).resize(250,250).into(imgicon);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        rqueue.add(jsonObjectRequest);
    }
}