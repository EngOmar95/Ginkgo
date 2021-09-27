package com.example.ginkgo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;

import com.example.ginkgo.Share.Share;

public class StartPage extends AppCompatActivity {
    Button letsStart;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_page);
        letsStart = findViewById(R.id.letsStart);

        letsStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(getBaseContext(), MainPage.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(StartPage.this, StartPage.class);
        startActivity(intent);
    }


    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences getValues = PreferenceManager.getDefaultSharedPreferences(StartPage.this);
        Share share = new Share();
        String lang = getValues.getString("language", "en");
        share.changeLang(lang, StartPage.this , 1);

    }
}