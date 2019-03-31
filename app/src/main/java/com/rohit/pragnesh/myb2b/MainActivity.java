package com.rohit.pragnesh.myb2b;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.rohit.pragnesh.myb2b.activities.LoginActivity;
import com.rohit.pragnesh.myb2b.activities.PostActivity;
import com.rohit.pragnesh.myb2b.retrofit.APIService;
import com.rohit.pragnesh.myb2b.retrofit.ApiUtils;


public class MainActivity extends AppCompatActivity {

    public static PrefConfig prefConfig;
    public static APIService mAPIService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefConfig = new PrefConfig(this);

        mAPIService = ApiUtils.getAPIService();

        //splash screen
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent;
                if (prefConfig.readLoginStatus())
                    intent = new Intent(MainActivity.this, PostActivity.class);
                else
                    intent = new Intent(MainActivity.this, LoginActivity.class);

                startActivity(intent);
                finish();
            }
        }, 1000);
    }
}