package com.example.system3.shop_locator;
import android.Manifest;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.transition.Fade;
import android.support.transition.Transition;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.transition.Explode;
import android.transition.Slide;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.CycleInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class Splash extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 0;
    // public static String macid;
    ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iv = (ImageView) findViewById(R.id.img);
        iv.setVisibility(View.INVISIBLE);

        Animation am = new TranslateAnimation(-getWindowManager().getDefaultDisplay().getHeight(), 0.0f, 0.0f, 0.0f);    //getWindowManager().getDefaultDisplay().getHeight()
        am.setDuration(1000);
        am.setInterpolator(new OvershootInterpolator());
        am.setFillAfter(false);
        iv.setAnimation(am);
        iv.setVisibility(View.VISIBLE);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission_group.STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT < 23) {
                //Do not need to check the permission
            } else {

                if (checkAndRequestPermissions()) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(new Intent(Splash.this, Home.class));
                            overridePendingTransition(R.anim.fadein,R.anim.fade);
                            //finish();
                        }
                    }, 2000);

                    //If you have already permitted the permission
                }
            }
        }
    }
    private boolean checkAndRequestPermissions() {
        int storagePermission = ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE);
        int location = ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (storagePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.READ_EXTERNAL_STORAGE);

        }
        if (location != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this,listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 0);
            return false;
        }

        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 0:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(new Intent(Splash.this, Home.class));
                            overridePendingTransition(R.anim.fadein,R.anim.fade);
                            //finish();
                        }
                    }, 2000);
                } else {
                    //You did not accept the request can not use the functionality.

                }
                break;
        }
    }
}
