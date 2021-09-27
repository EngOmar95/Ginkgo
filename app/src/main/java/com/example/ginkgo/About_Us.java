package com.example.ginkgo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Ringtone;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.ginkgo.menu.MainMenu;

public class About_Us extends AppCompatActivity {
    TextView menu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about__us);
                  menu=findViewById(R.id.menu)     ;
        MainMenu mainMenu =new MainMenu();




        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {





                mainMenu.showPopup(About_Us.this,view)     ;
            }
        });
    }
    @Override
    public void onBackPressed() {

        Intent intent =new Intent(About_Us.this,HomePage.class) ;
        startActivity(intent);
    }
}