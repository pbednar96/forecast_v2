package com.example.petan.forecast_project_v2.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.petan.forecast_project_v2.R;
import com.example.petan.forecast_project_v2.adapters.RecycleViewAdapter;
import com.example.petan.forecast_project_v2.model.Forecast_day;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends Activity implements View.OnClickListener {

    SharedPreferences mySharedPref;
    SharedPreferences.Editor mySharedEditor;

    private String JSON_URL;

    private JsonObjectRequest request;
    private RequestQueue requestQueue;
    private List<Forecast_day> lst_forecast = new ArrayList<> ();
    private RecyclerView recyclerView;

    private RecycleViewAdapter recyclerAdapter;
    private RecyclerView.LayoutManager layoutManager;

    static TextView cityName;
    static TextView temperatureNow;
    static TextView textView_pressure;
    static TextView textView_wind;
    static ImageView img;
    static TextView date_time;
    static ImageView btn_refresh;
    static ImageView btn_add;
    static ImageView btn_setting;
    public String img_url = "1";
    boolean tmp = true;
    boolean tmp_imt = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_main);


        temperatureNow = (TextView) findViewById (R.id.textView_tempretature);
        cityName = (TextView) findViewById (R.id.textView_city);
        textView_pressure = (TextView) findViewById (R.id.textView_pressure);
        textView_wind = (TextView) findViewById (R.id.textView_wind);
        date_time = (TextView) findViewById (R.id.textView_date);
        img = (ImageView) findViewById (R.id.imageView_weather);
        recyclerView = findViewById (R.id.recycleView);
        btn_refresh = (ImageView) findViewById (R.id.btn_refresh);
        btn_add = (ImageView) findViewById (R.id.btn_add);
        btn_setting = (ImageView) findViewById (R.id.btn_setting);
        btn_refresh.setOnClickListener (this);
        btn_add.setOnClickListener (this);

        Date c = Calendar.getInstance ().getTime ();
        SimpleDateFormat df = new SimpleDateFormat ("dd.MMM.yyyy");
        date_time.setText (df.format (c));


        mySharedPref = getSharedPreferences ("myPref", this.MODE_PRIVATE);
        JSON_URL = mySharedPref.getString ("url", "http://api.apixu.com/v1/forecast.json?key=e97d1234f16444b7b3893855182310&q=ostrava&days=7");
        cityName.setText (mySharedPref.getString ("name_city", "Praha"));
        temperatureNow.setText (mySharedPref.getString ("temperatureNow", "0"));
        textView_pressure.setText (mySharedPref.getString ("textView_pressure", "0"));
        textView_wind.setText (mySharedPref.getString ("textView_wind", "0"));
        new Handler ().postDelayed (new Runnable () {
            @Override
            public void run() {
                jsonrequest ();
                new Handler ().postDelayed (new Runnable () {
                    @Override
                    public void run() {
                        new DownLoadImageTask (img).execute (img_url);
                    }
                }, 500);
            }
        }, 500);


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
                        String date_in = temperature.getString ("date");

                        SimpleDateFormat format = new SimpleDateFormat ("yyyy-MM-dd");
                        Date date_date = format.parse (date_in);

                        SimpleDateFormat sdf = new SimpleDateFormat ("EEEE");
                        String mDate = sdf.format (date_date);

                        JSONObject day = temperature.getJSONObject ("day");
                        String mTemperature = day.getString ("avgtemp_c") + "°C";

                        JSONObject icon = day.getJSONObject ("condition");
                        String mIconUrl = "http:" + icon.getString ("icon");


                        Forecast_day fd = new Forecast_day (mDate, mTemperature, mIconUrl);
                        lst_forecast.add (fd);

                    }

                    //---------------------------------------------------------------

                    JSONObject city_data_location = response.getJSONObject ("location");
                    String city = city_data_location.getString ("name");
                    String country = city_data_location.getString ("country");

                    JSONObject city_data_current = response.getJSONObject ("current");
                    String temp_c = city_data_current.getString ("temp_c");
                    //String temp_f = city_data_current.getString ("temp_f");
                    String precip = city_data_current.getString ("precip_mm");
                    String wind = city_data_current.getString ("wind_kph");

                    JSONObject city_data_condition = city_data_current.getJSONObject ("condition");
                    img_url = "http:" + city_data_condition.getString ("icon");


                    mySharedEditor = mySharedPref.edit ();
                    mySharedEditor.putString ("name_city", cityName.getText ().toString ());
                    mySharedEditor.putString ("temperatureNow", temperatureNow.getText ().toString ());
                    mySharedEditor.putString ("textView_pressure", textView_pressure.getText ().toString ());
                    mySharedEditor.putString ("textView_wind", textView_wind.getText ().toString ());
                    mySharedEditor.putString ("city", city);
                    mySharedEditor.apply ();

                    cityName.setText (city + ", " + country);
                    temperatureNow.setText (temp_c + "°C");
                    textView_pressure.setText ("Sražky : " + precip + " mm");
                    textView_wind.setText ("Rychlost větru : " + wind + " km/h");


                } catch (JSONException e) {
                    e.printStackTrace ();
                } catch (ParseException e) {
                    e.printStackTrace ();
                }

                setuprecyclerview (lst_forecast);
                tmp = false;
            }
        }, new Response.ErrorListener () {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText (MainActivity.this, "No internet connect", Toast.LENGTH_SHORT).show ();
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

    @Override
    public void onClick(View view) {
        switch (view.getId ()) {
            case R.id.btn_refresh: {
                if (tmp == true) {
                    new Handler ().postDelayed (new Runnable () {
                        @Override
                        public void run() {
                            jsonrequest ();
                            if (tmp_imt == false) {
                                new Handler ().postDelayed (new Runnable () {
                                    @Override
                                    public void run() {
                                        new DownLoadImageTask (img).execute (img_url);
                                    }
                                }, 1000);
                            }
                        }
                    }, 1000);
                    Toast.makeText (this, "Refresh", Toast.LENGTH_SHORT).show ();
                } else {
                    new Handler ().postDelayed (new Runnable () {
                        @Override
                        public void run() {
                            lst_forecast.clear ();
                            jsonrequest ();
                            if (tmp_imt == false) {
                                new Handler ().postDelayed (new Runnable () {
                                    @Override
                                    public void run() {
                                        new DownLoadImageTask (img).execute (img_url);
                                    }
                                }, 3000);
                            }
                        }
                    }, 3000);
                    Toast.makeText (this, "Refresh", Toast.LENGTH_SHORT).show ();
                }
                break;
            }
            case R.id.btn_add: {
                Intent intent = new Intent (this, Choose_city.class);
                startActivityForResult (intent, 200);
                break;

            }
            case R.id.btn_setting: {
                Intent intent = new Intent (this, Setting_app.class);
                startActivityForResult (intent,205);
                break;
            }
        }
    }

    private class DownLoadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView imageView2;

        public DownLoadImageTask(ImageView imageView) {
            this.imageView2 = imageView;
        }

        protected Bitmap doInBackground(String... urls) {
            String urlOfImage = urls[0];
            Bitmap logo = null;
            try {
                InputStream is = new URL (urlOfImage).openStream ();
                logo = BitmapFactory.decodeStream (is);
                tmp_imt = true;
            } catch (Exception e) {
                e.printStackTrace ();
                tmp_imt = false;
            }
            return logo;
        }

        protected void onPostExecute(Bitmap result) {
            imageView2.setImageBitmap (result);

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult (requestCode, resultCode, data);
        if (requestCode == 200) {

            JSON_URL = "http://api.apixu.com/v1/forecast.json?key=e97d1234f16444b7b3893855182310&q=" + data.getStringExtra ("city") + "&days=7";
            mySharedEditor = mySharedPref.edit ();
            mySharedEditor.putString ("url", JSON_URL);
            mySharedEditor.apply ();
            new Handler ().postDelayed (new Runnable () {
                @Override
                public void run() {
                    lst_forecast.clear ();
                    jsonrequest ();
                    new Handler ().postDelayed (new Runnable () {
                        @Override
                        public void run() {
                            new DownLoadImageTask (img).execute (img_url);
                        }
                    }, 1000);
                }
            }, 1000);
        }

        if (requestCode == 205) {


        }
    }


}
