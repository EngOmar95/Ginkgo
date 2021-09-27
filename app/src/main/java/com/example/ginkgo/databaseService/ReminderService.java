package com.example.ginkgo.databaseService;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ginkgo.Adapaters.ListCalendarAdapater;
import com.example.ginkgo.Adapaters.ListReminderAdapter;
import com.example.ginkgo.R;
import com.example.ginkgo.ReminderPage;
import com.example.ginkgo.Share.Share;
import com.example.ginkgo.entity.Reminder;
import com.example.ginkgo.model.CalenderView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.naishadhparmar.zcustomcalendar.CustomCalendar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class ReminderService {

    Share share = new Share();
    String SERVICE = "Service";
    String REMINDERS = "Reminders";
    Context context;
    SharedPreferences sp;
    String USER_ID = "";
    String GENDER;

    public ReminderService(Context context) {
        this.context = context;
        sp = PreferenceManager.getDefaultSharedPreferences(context);
        USER_ID = sp.getString("userId", "0");
        GENDER = sp.getString("gender", "0");
    }

    public void addReminder(Reminder reminder) {


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(SERVICE).child(REMINDERS);
        String ReminderId = String.valueOf(databaseReference.push().getKey());
        reminder.setReminderId(ReminderId);
        reminder.setUserId(USER_ID);

        databaseReference.child(ReminderId).setValue(reminder);


    }

    public void displayRemindersByListView(RecyclerView listViewOfReminders) {
        ArrayList arrayOfReminders = new ArrayList<>();

        Query fetchAlarmByUserId = FirebaseDatabase.getInstance().getReference(SERVICE).child(REMINDERS).orderByChild("userId").equalTo(USER_ID);
        fetchAlarmByUserId.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (share.isNetworkConnected(context)) {
                    arrayOfReminders.clear();
                    if (snapshot.exists()) {
                        for (DataSnapshot snapshots : snapshot.getChildren()) {
                            Reminder reminder = snapshots.getValue(Reminder.class);
                            arrayOfReminders.add(reminder);

                        }
                    }
                    ListReminderAdapter adp = new ListReminderAdapter(arrayOfReminders, context);
                    listViewOfReminders.setLayoutManager(new GridLayoutManager(context,1));
                    listViewOfReminders.setAdapter(adp);
                   // listViewOfReminders.setSelection(arrayOfReminders.size());
                } else {
                    share.showDialog(context, "please check on Internit", null);


                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    public void ViewReminders(TextInputEditText reminderName, TextInputEditText location, TextInputEditText commint, TextInputEditText reminderDay,
                              TextInputEditText reminderTime, ImageView image, Date date, String reminderId) {

        FirebaseDatabase.getInstance().getReference(SERVICE).child(REMINDERS).child(reminderId).
                addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            Reminder reminder = snapshot.getValue(Reminder.class);
                            date.setYear(reminder.getDay().getYear());
                            date.setMonth(reminder.getDay().getMonth());
                            date.setDate(reminder.getDay().getDate());
                            reminderName.setText(reminder.getReminderName().toString());
                            reminderDay.setText(reminder.getDay().getMonth() +1 + "/" + reminder.getDay().getDate() + "/" + reminder.getDay().getYear());
                            reminderTime.setText(reminder.getTime());
                            location.setText(reminder.getLocation());
                            commint.setText(reminder.getCommint());
                            if (reminder.getAttachment() != null) {
                                image.setVisibility(View.VISIBLE);
                                image.setImageBitmap(share.Image_ConvertTo_Bitmap(reminder.getAttachment()));
                            }

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }


    public void updateReminder(Reminder reminder, String reminderId) {
        if (share.isNetworkConnected(context)) {

            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(SERVICE).child(REMINDERS);

            reminder.setReminderId(reminderId);
            reminder.setUserId(USER_ID);

            databaseReference.child(reminderId).setValue(reminder);
            Intent intent = new Intent(context, ReminderPage.class);
            context.startActivity(intent);

        } else {
            share.showDialog(context, "check please on internet", null);
        }
    }

    public void getModeToday(TextView todayMode, TextView percentageTaskOfDay, ImageView imageMode) {


        FirebaseDatabase.getInstance().getReference(SERVICE).child(REMINDERS).orderByChild("userId").equalTo(USER_ID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Calendar calendar = Calendar.getInstance();
                Date date = new Date(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                if (snapshot.exists()) {
                    double done = 0;
                    double allTask = 0;
                    for (DataSnapshot snapshots : snapshot.getChildren()) {

                        Reminder reminder = snapshots.getValue(Reminder.class);
                        Date date1 = new Date(reminder.getDay().getYear(), reminder.getDay().getMonth(), reminder.getDay().getDate());
                        if (date.equals(date1)) {
                            if (reminder.isOn() == true) {
                                done++;

                            }
                            allTask++;
                        }
                    }
                    int sum = (int) ((done / allTask) * 100);

                    if (sum < 100) {
                        todayMode.setText("You Can Do Better!");
                        if (GENDER.equals("male")) {
                            imageMode.setImageResource(R.drawable.male_sad);
                        } else {
                            imageMode.setImageResource(R.drawable.female_sad);
                        }

                    } else {
                        todayMode.setText("You Are Doing Great! ");
                        todayMode.setTextColor(Color.WHITE);
                        if (GENDER.equals("male")) {
                            imageMode.setImageResource(R.drawable.male_happy);
                        } else {
                            imageMode.setImageResource(R.drawable.female_happy);
                        }


                    }

                    percentageTaskOfDay.setText("You Had Done " + sum + "% Of Daily Tasks");

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getDaysAndDisplay(ArrayList<Integer> days, CustomCalendar customCalendar) {
        days.clear();

        Share share = new Share();

        if (share.isNetworkConnected(context)) {
            FirebaseDatabase.getInstance().getReference(SERVICE).child(REMINDERS).orderByChild("userId").equalTo(USER_ID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (share.isNetworkConnected(context)) {
                        HashMap<Integer, Object> mapDateToDesc1 = new HashMap<>();
                        Calendar calendar = Calendar.getInstance();
                        if (snapshot.exists()) {
                            for (DataSnapshot snapshots : snapshot.getChildren()) {
                                Reminder reminder = snapshots.getValue(Reminder.class);
                                String currentMonth = String.valueOf(calendar.getTime().getMonth());
                                String month = String.valueOf(reminder.getDay().getMonth());
                                if (currentMonth.equals(month)) {
                                    days.add(reminder.getDay().getDate());
                                    mapDateToDesc1.put(reminder.getDay().getDate(), "SHAP_FILED_DAYS");

                                }


                            }
                            customCalendar.setDate(calendar, mapDateToDesc1);

                        }

                    } else {
                        share.showDialog(context, "please check on Internit", null);


                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
    }


    public void getAllReminderForDay(Date date, ListView listOfRemindersForOneDay) {

        FirebaseDatabase.getInstance().getReference(SERVICE).child(REMINDERS).orderByChild("userId").equalTo(USER_ID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<CalenderView> arrayOfCalender = new ArrayList<>();
                if (snapshot.exists()) {

                    for (DataSnapshot snapshots : snapshot.getChildren()) {

                        Reminder reminder = snapshots.getValue(Reminder.class);
                        Date date1 = new Date(reminder.getDay().getYear(), reminder.getDay().getMonth(), reminder.getDay().getDate());
                        if (date.equals(date1)) {
                            CalenderView calenderView = new CalenderView();
                            calenderView.setTitle(reminder.getReminderName());
                            calenderView.setCommint(reminder.getCommint());
                            calenderView.setDone(reminder.isOn());
                            arrayOfCalender.add(calenderView);
                        }
                    }
                }
                ListCalendarAdapater adp = new ListCalendarAdapater(arrayOfCalender, context);
                listOfRemindersForOneDay.setAdapter(adp);
                listOfRemindersForOneDay.setSelection(arrayOfCalender.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
