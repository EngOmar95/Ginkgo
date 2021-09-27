package com.example.ginkgo;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.ginkgo.Dialog.AlarmsDialog;
import com.example.ginkgo.Dialog.ChangeLaunguageDialog;
import com.example.ginkgo.Dialog.SignOutDialog;

public class SettingPage extends AppCompatActivity {
    Button profile, languages, signOut;
    Intent intent;
    TextView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_page);
        profile = findViewById(R.id.profile);
        languages = findViewById(R.id.languages);
        signOut = findViewById(R.id.signOut);
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(getBaseContext(), ProfilePage.class);
                startActivity(intent);
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(getBaseContext(), ProfilePage.class);
                startActivity(intent);
            }
        });


        languages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangeLaunguageDialog changeLaunguageDialog = new ChangeLaunguageDialog(SettingPage.this);

                changeLaunguageDialog.show(getSupportFragmentManager(), "");
            }
        });

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignOutDialog signOutDialog = new SignOutDialog();
                signOutDialog.showDialog(SettingPage.this);
            }

        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent =new Intent(SettingPage.this,HomePage.class) ;
        startActivity(intent);
    }


}