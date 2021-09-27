package com.example.ginkgo.Service;

import android.content.Intent;
import android.icu.util.Calendar;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;

import androidx.annotation.RequiresApi;

import com.example.ginkgo.AlarmsPage;
import com.example.ginkgo.ReminderPage;
import com.example.ginkgo.Share.Share;
import com.example.ginkgo.R;
import com.example.ginkgo.entity.Alarm;
import com.example.ginkgo.entity.Reminder;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Service extends android.app.Service {
    MediaPlayer mediaPlayerAlarm;
    MediaPlayer mediaPlayerReminder;

    public Service() {
    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        // Toast.makeText(this, "onCreate", Toast.LENGTH_SHORT).show();


        super.onCreate();
    }


    @Override
    public void onDestroy() {

        // mediaPlayer.release();
        super.onDestroy();
        mediaPlayerAlarm.stop();
        mediaPlayerReminder.stop();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Share share=new Share();
        Bundle bundle = intent.getBundleExtra("BUNDLE");
        ArrayList<Alarm> alarmList = (ArrayList<Alarm>) bundle.getSerializable("alarms");
        ArrayList<Reminder> reminderList = (ArrayList<Reminder>) bundle.getSerializable("reminders");


        mediaPlayerAlarm = MediaPlayer.create(getBaseContext(), R.raw.alarm);
        mediaPlayerReminder = MediaPlayer.create(getBaseContext(),R.raw.reminder);


        Timer timer = new Timer();

        timer.scheduleAtFixedRate(new TimerTask() {

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void run() {

                Calendar calendar = Calendar.getInstance();
                int Hour = calendar.getTime().getHours();
                int minutes = calendar.getTime().getMinutes();
                for (Alarm alarm : alarmList) {


                    if (share.editTime(Hour, minutes).equals(alarm.getTime()) && alarm.isOn() && !mediaPlayerAlarm.isPlaying()) {
                        mediaPlayerReminder.stop();
                        mediaPlayerAlarm.start();
                        startForeground(1,share.notification(Service.this,alarm.getAlarmName(),"Alarm", AlarmsPage.class));

                    }

                }


                for (Reminder reminder : reminderList) {
                    String today =calendar.get(Calendar.MONTH)+"/"+calendar.get(Calendar.DAY_OF_MONTH)+"/"+calendar.get(Calendar.YEAR) ;
                             String day =reminder.getDay().getMonth()+"/"+reminder.getDay().getDate()+"/"+reminder.getDay().getYear()  ;

                    if (share.editTime(Hour, minutes).equals(reminder.getTime()) && !reminder.isOn() && !mediaPlayerReminder.isPlaying()&&today.equals(day)) {
                        mediaPlayerAlarm.stop();
                        mediaPlayerReminder.start();
                        startForeground(1,share.notification(Service.this,reminder.getReminderName(),"Reminder", ReminderPage.class));


                    }

                }


            }

        }, 0, 1000);

        return START_STICKY;
    }




}



