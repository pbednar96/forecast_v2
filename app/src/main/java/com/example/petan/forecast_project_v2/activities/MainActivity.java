package com.example.petan.forecast_project_v2.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import com.example.petan.forecast_project_v2.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import com.example.petan.forecast_project_v2.model.Forecast_day;
import com.example.petan.forecast_project_v2.adapters.RecycleViewAdapter;

public class MainActivity extends Activity {

    private final String JSON_URL = "http://api.apixu.com/v1/forecast.json?key=e97d1234f16444b7b3893855182310&q=Ostrava&days=7";

    private JsonObjectRequest request;
    private RequestQueue requestQueue;
    private List<Forecast_day> lst_forecast = new ArrayList<> ();
    private RecyclerView recyclerView;

    private RecycleViewAdapter recyclerAdapter;
    private RecyclerView.LayoutManager layoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_main);

       recyclerView = findViewById (R.id.recycleView);
       jsonrequest ();
    }

    private void jsonrequest() {

        request = new JsonObjectRequest (Request.Method.GET, JSON_URL, null, new Response.Listener<JSONObject> () {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONObject forecast = response.getJSONObject ("forecast");

                    JSONArray task = forecast.getJSONArray ("forecastday");
                    for (int i = 0; i < task.length (); i++) {

                        JSONObject temperature = task.getJSONObject (i);
                        String mDate = temperature.getString ("date");

                        JSONObject day = temperature.getJSONObject ("day");
                        String mTemperature = day.getString ("avgtemp_c");

                        JSONObject icon = day.getJSONObject ("condition");
                        String mIconUrl = "http:" + icon.getString ("icon");
                        String sss = String.valueOf (task.length());

                        Forecast_day fd = new Forecast_day (mDate,mTemperature,mIconUrl);
                        lst_forecast.add (fd);

                    }
                } catch (JSONException e) {
                    e.printStackTrace ();
                }

                setuprecyclerview(lst_forecast);
            }
        }, new Response.ErrorListener () {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        requestQueue = Volley.newRequestQueue (MainActivity.this);
        requestQueue.add (request);

    }

    private void setuprecyclerview(List<Forecast_day> lst_forecast) {
        recyclerView.setHasFixedSize (true);
        layoutManager = new LinearLayoutManager (this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager (layoutManager);
        recyclerAdapter = new RecycleViewAdapter (this, lst_forecast);
        recyclerView.setAdapter (recyclerAdapter);

    }
}
