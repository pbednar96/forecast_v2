package com.example.petan.forecast_project_v2.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.petan.forecast_project_v2.R;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;

public class Choose_city extends Activity implements View.OnClickListener {

    static ImageView btn_gps;
    static EditText edit_text;
    private LocationManager locationManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.choose_city);

        btn_gps = (ImageView) findViewById (R.id.btn_gps);
        edit_text = (EditText) findViewById (R.id.editText_location);

        btn_gps.setOnClickListener (this);

    }


    public String hereLocation(double lat, double lon) {

        String currCity = "";

        Geocoder geocoder = new Geocoder (Choose_city.this, Locale.getDefault ());
        List<Address> addressList;
        try {
            addressList = geocoder.getFromLocation (lat, lon, 1);
            if (addressList.size () > 0) {
                currCity = addressList.get (0).getLocality ();
            }
        } catch (Exception e) {
            e.printStackTrace ();
        }
        return currCity;
    }

    public void myClick(View view){
        String city_name = edit_text.getText().toString();
        Intent intent = new Intent ();
        intent.putExtra ("city", city_name);
        Log.d ("INFO123", city_name);
        setResult (200,intent);
        finish ();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId ()) {
            case R.id.btn_gps: {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission (Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions (new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 10);
                    } else {
                        locationManager = (LocationManager) getSystemService (Context.LOCATION_SERVICE);
                        Location location = locationManager.getLastKnownLocation (LocationManager.NETWORK_PROVIDER);
                        double lat = location.getLatitude ();
                        lat = Math.round(lat*100.0)/100.0;
                        double lon = location.getLongitude ();
                        lon = Math.round(lon*100.0)/100.0;
                        String city = hereLocation (lat,lon);
                        edit_text.setText (city);

                    }
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult (requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1000: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    LocationManager locationManager1 = (LocationManager) getSystemService (Context.LOCATION_SERVICE);
                    if (ActivityCompat.checkSelfPermission (this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission (this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    Location location = locationManager1.getLastKnownLocation (locationManager1.NETWORK_PROVIDER);
                    String city = hereLocation (location.getLatitude (),location.getLongitude ());
                    Log.d ("INFO123", city);
                }
                else {
                    Toast.makeText (this, "Permision", Toast.LENGTH_SHORT).show ();
                }
                break;
            }
        }
    }
}
