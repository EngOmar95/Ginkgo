package com.example.ginkgo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ginkgo.Share.Share;
import com.example.ginkgo.databaseService.ReminderService;
import com.example.ginkgo.menu.MainMenu;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ReminderPage extends AppCompatActivity {
    FloatingActionButton newReminder;
    Intent intent;
    TextView menu;
    MainMenu mainMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_page);

        newReminder = findViewById(R.id.newReminder);
        menu = findViewById(R.id.menu);
        mainMenu = new MainMenu();
//        final ListView listView = findViewById(R.id.listViwe);
        final RecyclerView recyclerView = findViewById(R.id.listViwe);


        Share share = new Share();
        ReminderService reminderService = new ReminderService(ReminderPage.this);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        if (share.isNetworkConnected(getBaseContext())) {
            FirebaseDatabase.getInstance().getReference().getRoot().child("Service").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if (share.isNetworkConnected(getBaseContext())) {

                        reminderService.displayRemindersByListView(recyclerView);

                    } else {
                        share.showDialog(ReminderPage.this, "please check on Internit", null);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });



        } else {
            Intent intent = new Intent(ReminderPage.this, HomePage.class);
            Toast.makeText(ReminderPage.this, "please check on Internit", Toast.LENGTH_SHORT).show();
            startActivity(intent);
        }
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainMenu.showPopup(ReminderPage.this, view);
            }
        });

        newReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(getBaseContext(), NewReminder.class);
                startActivity(intent);

            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent =new Intent(ReminderPage.this,HomePage.class) ;
        startActivity(intent);
    }
}