package com.example.ginkgo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

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

public class UpadeReminder extends AppCompatActivity {
    AlarmsDialog alarmsDialog;
    TextView menu, add;
    MainMenu mainMenu;
    Intent intent;
    ImageView image;
    CalendarView date;
    TextInputLayout controlDay, controlTime;
    TextInputEditText reminderName, location, commint, reminderDay, reminderTime;
    Button save;
    Date dateOfDay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dateOfDay=new Date();
        setContentView(R.layout.activity_upade_reminder);
        controlTime = findViewById(R.id.controlTime);
        controlDay = findViewById(R.id.controlDay);
        alarmsDialog = new AlarmsDialog();
        reminderName = findViewById(R.id.reminderName);
        reminderDay = findViewById(R.id.reminderDay);
        reminderTime = findViewById(R.id.reminderTime);
        location = findViewById(R.id.location);
        commint = findViewById(R.id.Commint);
        save = findViewById(R.id.save);
        intent = getIntent();
        Calendar calendar = Calendar.getInstance();

        Share share = new Share();
        menu = findViewById(R.id.menu);
        add = findViewById(R.id.add);
        mainMenu = new MainMenu();
        image = findViewById(R.id.image);
        String REMINDER_ID = intent.getStringExtra("reminderId");

        ReminderService reminderService = new ReminderService(UpadeReminder.this);
        reminderService.ViewReminders(reminderName, location, commint, reminderDay, reminderTime, image, dateOfDay, REMINDER_ID);
        AwesomeValidation awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        awesomeValidation.addValidation(this, R.id.reminderName, RegexTemplate.NOT_EMPTY, R.string.Invalid_Required);
        awesomeValidation.addValidation(this, R.id.reminderDay, RegexTemplate.NOT_EMPTY, R.string.Invalid_Required);
        awesomeValidation.addValidation(this, R.id.reminderTime, RegexTemplate.NOT_EMPTY, R.string.Invalid_Required);
        reminderDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(UpadeReminder.this, android.R.style.Theme_Holo_Light_Dialog, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        int year = i;
                        int month = i1;
                        int day = i2;
                        dateOfDay = new Date(year, month, day);
                        String selectedDay = i1 + "/" + i2 + "/" + i;
                        reminderDay.setText(selectedDay);

                    }
                }, 2000, 01, 01);

                // datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.GRAY));
                datePickerDialog.show();
            }
        });


        reminderTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(UpadeReminder.this, android.R.style.Theme_Holo_Light_Dialog, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        if (i == 00) {
                            i = 12;
                        } else if (i == 12) {
                            i = 24;
                        }
                        reminderTime.setText(share.editTime(i, i1));
                    }
                }, 24, 0, false);
                timePickerDialog.show();

                // datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.GRAY));

            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Share share = new Share();
                if (share.isNetworkConnected(getBaseContext())) {
                    if (awesomeValidation.validate()) {
                        Reminder reminder = new Reminder();


                        reminder.setReminderName(reminderName.getText().toString());
                        reminder.setDay(dateOfDay);
                        reminder.setTime(reminderTime.getText().toString());
                        reminder.setOn(false);
                        reminder.setLocation(location.getText().toString());
                        reminder.setCommint(commint.getText().toString());
                        if (image.getVisibility() != View.GONE) {
                            reminder.setAttachment(share.Image_ConvertTo_String(image));
                        }
                        reminderService.updateReminder(reminder, REMINDER_ID);
                        finish();

                    }

                } else {
                    share.showDialog(UpadeReminder.this, "check please on internet", null);
                }
            }
        });
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainMenu.showPopup(UpadeReminder.this, view);
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(intent, 100);
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
                image.setImageBitmap(null);
                image.setImageBitmap(Image);
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }


}