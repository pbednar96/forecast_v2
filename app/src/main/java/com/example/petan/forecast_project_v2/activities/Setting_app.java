package com.example.petan.forecast_project_v2.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.petan.forecast_project_v2.R;

public class Setting_app extends Activity implements View.OnClickListener {

    static Button btn_x;
    static ImageView img1;
    static ImageView img2;
    static RelativeLayout myLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.setting_app);

        myLayout = (RelativeLayout) findViewById(R.id.rootRL);
        img1 = (ImageView) findViewById (R.id.image_bg1);
        img2 = (ImageView) findViewById (R.id.image_bg2);

        img1.setOnClickListener (this);
        img2.setOnClickListener (this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId ()) {
            case R.id.image_bg1 : {

                int id = getResources().getIdentifier("bg_praha", "drawable", getPackageName());
                Intent intent =  new Intent();
                intent.putExtra("bg_img", String.valueOf(id));
                setResult(205,intent);
                finish ();
                break;
            }
            case R.id.image_bg2 : {

                int id = getResources().getIdentifier("bg_roma", "drawable", getPackageName());
                Intent intent =  new Intent();
                intent.putExtra("bg_img", String.valueOf(id));
                setResult(205,intent);
                finish ();
                break;
            }
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent (this, MainActivity.class);
        Setting_app.this.finish ();
        startActivity (i);
    }

}
