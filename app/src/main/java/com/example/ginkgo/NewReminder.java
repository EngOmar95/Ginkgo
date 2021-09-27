package com.example.ginkgo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.example.ginkgo.Dialog.AlarmsDialog;
import com.example.ginkgo.Share.Share;
import com.example.ginkgo.databaseService.ReminderService;
import com.example.ginkgo.entity.Reminder;
import com.example.ginkgo.menu.MainMenu;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

public class NewReminder extends AppCompatActivity {
    TextInputLayout showCitys;
    AlarmsDialog alarmsDialog;
    TextView menu, add, title;
    MainMenu mainMenu;
    Intent intent;
    ImageView image;
    CalendarView date;
    Calendar dateOfDay;
    Share share;
    TextInputEditText reminderName, location, commint;
    Button addReminder;
    TimePicker time;
    final AwesomeValidation awesomeValidation=new AwesomeValidation(ValidationStyle.BASIC);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_reminder);
        //  AutoCompleteTextView  autoCompleteTextView =   findViewById(R.id.autoCompleteTextView)  ;
        //  showCitys  =findViewById(R.id.showCitys)  ;
        dateOfDay = Calendar.getInstance();

        share = new Share();
        alarmsDialog = new AlarmsDialog();
        reminderName = findViewById(R.id.reminderName);
        date = findViewById(R.id.date);
        time = findViewById(R.id.time);
        location = findViewById(R.id.location);
        commint = findViewById(R.id.Commint);
        addReminder = findViewById(R.id.addReminder);
        intent = getIntent();

        menu = findViewById(R.id.menu);
        add = findViewById(R.id.add);
        title = findViewById(R.id.title);
        mainMenu = new MainMenu();
        image = findViewById(R.id.image);

        awesomeValidation.addValidation(this, R.id.reminderName, RegexTemplate.NOT_EMPTY,R.string.Invalid_Required);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(intent, 100);
            }
        });

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainMenu.showPopup(NewReminder.this, view);
            }
        });

        date.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                dateOfDay = Calendar.getInstance();
                int year = i;
                int month = i1;
                int day = i2;

                dateOfDay.set(Calendar.MONTH, month);
                dateOfDay.set(Calendar.YEAR, year);
                dateOfDay.set(Calendar.DAY_OF_MONTH, day);

            }
        });


        addReminder.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                if (share.isNetworkConnected(getBaseContext())) {
                    if(awesomeValidation.validate()) {
                        ReminderService reminderService = new ReminderService(NewReminder.this);
                   //     Toast.makeText(getApplicationContext(), String.valueOf(dateOfDay.get(Calendar.YEAR)), Toast.LENGTH_SHORT).show();
                        reminderService.addReminder(setReminderInfo());
                        intent = new Intent(getBaseContext(), ReminderPage.class);
                        startActivity(intent);
                    }

                } else {
                    share.showDialog(NewReminder.this, "check please on internet", null);
                }
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            try {

                Bitmap Image = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                image.setVisibility(View.VISIBLE);
                image.setImageBitmap(Image);
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private Reminder setReminderInfo() {

        Reminder reminder = new Reminder();
        Date date = new Date(dateOfDay.get(Calendar.YEAR), dateOfDay.get(Calendar.MONTH), dateOfDay.get(Calendar.DAY_OF_MONTH));
        reminder.setReminderName(reminderName.getText().toString());
        reminder.setDay(date);
        reminder.setTime(share.editTime(time.getHour(), time.getMinute()));
        reminder.setOn(false);
        reminder.setCommint(commint.getText().toString());
        if (image.getVisibility() != View.GONE) {
            reminder.setAttachment(share.Image_ConvertTo_String(image));
        }
        return reminder;
    }


}