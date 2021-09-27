package com.example.ginkgo;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ginkgo.Dialog.AlarmsDialog;
import com.example.ginkgo.Share.Share;
import com.example.ginkgo.databaseService.AlarmService;
import com.example.ginkgo.entity.Alarm;
import com.example.ginkgo.menu.MainMenu;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AlarmsPage extends AppCompatActivity {

    RecyclerView listView;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarms_page);

        
        TextView menu;
        menu = findViewById(R.id.menu);
        MainMenu mainMenu = new MainMenu();
        AlarmService alarmService = new AlarmService(AlarmsPage.this);
        listView = findViewById(R.id.listViwe);
        FloatingActionButton ADD = findViewById(R.id.fab);
        ADD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlarmsDialog dd = new AlarmsDialog();

                dd.show(getSupportFragmentManager(), "");
            }
        });

        Share share = new Share();
        if (share.isNetworkConnected(getBaseContext())) {

            FirebaseDatabase.getInstance().getReference().getRoot().child("Service").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if (share.isNetworkConnected(getBaseContext())) {

                        alarmService.displayAlarmByListView(listView);

                    } else {
                        share.showDialog(AlarmsPage.this, "please check on Internit", null);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } else {
            Intent intent = new Intent(AlarmsPage.this, HomePage.class);
            Toast.makeText(AlarmsPage.this, "please check on Internit", Toast.LENGTH_SHORT).show();
            startActivity(intent);

        }
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainMenu.showPopup(AlarmsPage.this, view);
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent =new Intent(AlarmsPage.this,HomePage.class) ;
        startActivity(intent);
    }
}