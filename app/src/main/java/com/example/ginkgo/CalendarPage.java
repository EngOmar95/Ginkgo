package com.example.ginkgo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ginkgo.Adapaters.ListCalendarAdapater;
import com.example.ginkgo.Share.Share;
import com.example.ginkgo.databaseService.ReminderService;
import com.example.ginkgo.entity.Reminder;
import com.example.ginkgo.menu.MainMenu;
import com.example.ginkgo.model.CalenderView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.naishadhparmar.zcustomcalendar.CustomCalendar;
import org.naishadhparmar.zcustomcalendar.OnDateSelectedListener;
import org.naishadhparmar.zcustomcalendar.OnNavigationButtonClickedListener;
import org.naishadhparmar.zcustomcalendar.Property;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class CalendarPage extends AppCompatActivity implements OnNavigationButtonClickedListener {
    CustomCalendar customCalendar;
    TextView menu;
    ArrayList<Integer> allDays;
    ListView listViwe;
    HashMap<Object, Property> mapShapsToProp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_page);
        MainMenu mainMenu = new MainMenu();
        menu = findViewById(R.id.menu);
        Share share = new Share();
        allDays = new ArrayList<>();
        ReminderService reminderService = new ReminderService(CalendarPage.this);
        listViwe = findViewById(R.id.listView);
        customCalendar = findViewById(R.id.custom_calendar);
        mapShapsToProp = new HashMap<>();


        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainMenu.showPopup(CalendarPage.this, view);
            }
        });

        setProperty();
        if (share.isNetworkConnected(getBaseContext())) {
            reminderService.getDaysAndDisplay(allDays, customCalendar);
        } else {
            Intent intent = new Intent(CalendarPage.this, HomePage.class);
            Toast.makeText(CalendarPage.this, "please check on Internit", Toast.LENGTH_SHORT).show();
            startActivity(intent);
        }


        customCalendar.setOnDateSelectedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(View view, Calendar selectedDate, Object desc) {

                for (int modelCalender : allDays) {
                    if (selectedDate.get(Calendar.DAY_OF_MONTH) == modelCalender) {
                        Date date = new Date(selectedDate.get(Calendar.YEAR), selectedDate.get(Calendar.MONTH), selectedDate.get(Calendar.DAY_OF_MONTH));
                        reminderService.getAllReminderForDay(date, listViwe);
                        changeColorChecked(date.getDate());
                        return;
                    }

                }
            }
        });


        customCalendar.setOnNavigationButtonClickedListener(CustomCalendar.PREVIOUS, this);
        customCalendar.setOnNavigationButtonClickedListener(CustomCalendar.NEXT, this);


    }

    @Override
    public Map<Integer, Object>[] onNavigationButtonClicked(int whichButton, Calendar newMonth) {

        Map<Integer, Object>[] mapDate = new Map[4];

        Calendar calendar = Calendar.getInstance();

        if (calendar.get(Calendar.MONTH) == newMonth.get(Calendar.MONTH)) {
            mapDate[0] = new HashMap<>();
            for (int day : allDays) {
                mapDate[0].put(day, "SHAP_FILED_DAYS");


            }
        }
        ArrayList<CalenderView> arrayOfCalender = new ArrayList<>();
        ListCalendarAdapater adp = new ListCalendarAdapater(arrayOfCalender, CalendarPage.this);
        listViwe.setAdapter(adp);
        listViwe.setSelection(arrayOfCalender.size());
        return mapDate;
    }

    private void setProperty(){

        Property shapFiledDays = new Property();
        shapFiledDays.layoutResource = R.layout.default_view;
        shapFiledDays.dateTextViewResource = R.id.info_text;
        mapShapsToProp.put("SHAP_FILED_DAYS", shapFiledDays);

        Property clickOnDay = new Property();
        clickOnDay.layoutResource = R.layout.checked;
        clickOnDay.dateTextViewResource = R.id.info_text;
        mapShapsToProp.put("SHAP_CLICK_ON_DAY", clickOnDay);

        customCalendar.setMapDescToProp(mapShapsToProp);
    }


    private void changeColorChecked(int day) {
        HashMap<Integer, Object> mapDateToDesc1 = new HashMap<>();
        for (int modelCalender : allDays) {

            if (modelCalender == day) {

                mapDateToDesc1.put(modelCalender, "SHAP_CLICK_ON_DAY");

            } else {
                mapDateToDesc1.put(modelCalender, "SHAP_FILED_DAYS");
            }


        }
        Calendar calendar = Calendar.getInstance();
        customCalendar.setDate(calendar, mapDateToDesc1);


    }


}