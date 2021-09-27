package com.example.ginkgo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainPage extends AppCompatActivity {
    Button signIn  , signUp  ;
    Intent intent;
    TextView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        signIn=findViewById(R.id.signIn)         ;
        signUp=findViewById(R.id.signUp)         ;
        back=findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent =new Intent(getBaseContext(), SignIn.class)      ;
                startActivity(intent);
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent =new Intent(getBaseContext(), SignUp.class)      ;
                startActivity(intent);
            }
        });
    }
    @Override
    public void onBackPressed() {

        Intent intent =new Intent(MainPage.this, StartPage.class) ;
        startActivity(intent);
    }
}