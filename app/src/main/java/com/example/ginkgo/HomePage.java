package com.example.ginkgo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ginkgo.Dialog.SignOutDialog;
import com.example.ginkgo.Share.Share;
import com.example.ginkgo.Service.Service;
import com.example.ginkgo.databaseService.ReminderService;
import com.example.ginkgo.entity.Alarm;
import com.example.ginkgo.entity.Reminder;
import com.example.ginkgo.menu.MainMenu;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class HomePage extends AppCompatActivity {
    TextView numToday, nameDay, menu, todayMode, percentageTaskOfDay;
    ImageView alarma, reminders, aboutUs, setting, imageMode;
    LinearLayout displayCalendar;
    Intent intent;
    Share share;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        ReminderService reminderService = new ReminderService(HomePage.this);
        alarma = findViewById(R.id.alarma);
        reminders = findViewById(R.id.reminders);
        aboutUs = findViewById(R.id.aboutUs);
        setting = findViewById(R.id.setting);
        imageMode = findViewById(R.id.imageMode);
        share = new Share();

        displayCalendar = findViewById(R.id.calendar);
        numToday = findViewById(R.id.numToday);
        nameDay = findViewById(R.id.nameDay);
        todayMode = findViewById(R.id.todayMode);
        percentageTaskOfDay = findViewById(R.id.percentageTaskOfDay);
        menu = findViewById(R.id.menu);
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        MainMenu mainMenu = new MainMenu();
        calendar.setTime(date);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        SharedPreferences getValues = PreferenceManager.getDefaultSharedPreferences(HomePage.this);
        String nameDayOfWeek = "";
        String numberDayOfMonth = "";


        String lang = getValues.getString("language", "en");
        if (lang.equals("ar")) {
            Locale locale = new Locale("ar");
            nameDayOfWeek = new SimpleDateFormat("E", locale).format(date);
            numberDayOfMonth = new SimpleDateFormat("dd", locale).format(date);
        } else {

            nameDayOfWeek = new SimpleDateFormat("E").format(date);
            numberDayOfMonth = new SimpleDateFormat("dd").format(date);

        }
        String gender=  getValues.getString("gender", "male");

        if(gender.equals("male"))  {
            imageMode.setImageResource(R.drawable.male);
        } else {
            imageMode.setImageResource(R.drawable.female);
        }

        numToday.setText(numberDayOfMonth);
        nameDay.setText(nameDayOfWeek);


        displayCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(HomePage.this, CalendarPage.class);
                startActivity(intent);
            }
        });
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainMenu.showPopup(HomePage.this, view);
            }
        });

        alarma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(getBaseContext(), AlarmsPage.class);
                startActivity(intent);
            }
        });

        reminders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(getBaseContext(), ReminderPage.class);
                startActivity(intent);
            }
        });

        aboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(getBaseContext(), About_Us.class);
                startActivity(intent);
            }
        });

        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(getBaseContext(), SettingPage.class);
                startActivity(intent);
            }
        });


        ArrayList<Alarm> alarmList = new ArrayList<>();
        ArrayList<Reminder> reminderList = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference().child("Service").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    refreshService(reminderList, alarmList);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        reminderService.getModeToday(todayMode, percentageTaskOfDay, imageMode);


    }

    public void refreshService(ArrayList<Reminder> reminderList, ArrayList<Alarm> alarmList) {
        Intent runService = new Intent(getApplicationContext(), Service.class);
        stopService(runService);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        FirebaseDatabase.getInstance().getReference("Service").child("Reminders").orderByChild("userId").equalTo(sp.getString("userId", "0")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    reminderList.clear();
                    for (DataSnapshot snapshots : snapshot.getChildren()) {
                        Reminder reminder = snapshots.getValue(Reminder.class);
                        reminderList.add(reminder);

                    }
                    stopService(runService);
                    startService(reminderList, alarmList);
                }


                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getBaseContext());


                FirebaseDatabase.getInstance().getReference("Service").child("Alarms").orderByChild("userId").equalTo(sp.getString("userId", "0")).addValueEventListener(new ValueEventListener() {


                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.exists()) {
                            alarmList.clear();

                            for (DataSnapshot snapshots : snapshot.getChildren()) {
                                Alarm alarm = snapshots.getValue(Alarm.class);
                                alarmList.add(alarm);


                            }

                            stopService(runService);
                            startService(reminderList, alarmList);
                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    public void startService(ArrayList<Reminder> reminderList, ArrayList<Alarm> alarmList) {
        Intent runService = new Intent(HomePage.this, Service.class);
        Bundle args = new Bundle();
        args.putSerializable("reminders", (Serializable) reminderList);
        args.putSerializable("alarms", (Serializable) alarmList);
        runService.putExtra("BUNDLE", args);
        startService(runService);
    }

    @Override
    public void onBackPressed() {

        SignOutDialog signOutDialog = new SignOutDialog();
        signOutDialog.showDialog(HomePage.this);

    }


}