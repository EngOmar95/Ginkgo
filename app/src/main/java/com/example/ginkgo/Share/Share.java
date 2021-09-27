package com.example.ginkgo.Share;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.os.Build;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.NotificationCompat;


import com.example.ginkgo.StartPage;
import com.example.ginkgo.R;


import java.io.ByteArrayOutputStream;
import java.util.Locale;


public class Share {

    public AlertDialog.Builder showDialog(Context context, String title, Intent toPage) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setNegativeButton(context.getString(R.string.ok),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Intent intent = new Intent(context, SignIn.class);

                        if (toPage != null) {
                            context.startActivity(toPage);
                        } else {

                        }

                    }
                });


        builder.show();

        return builder;


    }

    public boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    public String Image_ConvertTo_String(ImageView image) {

        BitmapDrawable bitmapDrawable = (BitmapDrawable) image.getDrawable();
        Bitmap bitmapImage = bitmapDrawable.getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmapImage.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        String encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);

        return encodedImage;
    }


    public Bitmap Image_ConvertTo_Bitmap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }

    }

    public String editTime(int Hour, int minutes) {

        String appointmentTime = "";
        String min = String.valueOf(minutes);
        if (minutes < 10) {
            min = "0" + minutes;
        }
        if (Hour > 12) {

            Hour -= 12;


            appointmentTime = String.valueOf(Hour).concat(":").concat(min.concat(" PM"));
        } else {

            appointmentTime = String.valueOf(Hour).concat(":").concat(min.concat(" AM"));
        }


        return appointmentTime;
    }

    public Notification notification(Context context, String content, String title, Class<?> toPage) {
        Intent snoozeIntent = new Intent(context, toPage);
        PendingIntent snoozePendingIntent = PendingIntent.getActivity(context, 0, snoozeIntent, 0);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "my_channel_id_01";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            @SuppressLint("WrongConstant") NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notifications", NotificationManager.IMPORTANCE_MAX);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.BLUE);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }


        NotificationCompat.Builder Builder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                .setContentTitle(title)
                .setSmallIcon(R.drawable.logo)

                .setContentText(content)
                .setAutoCancel(true)
                .addAction(R.drawable.logo, title, snoozePendingIntent)
                .setNumber(1);


        return Builder.build();

    }

    public void changeLang(String toLanguage, Context context, int flag) {

        Locale locale = new Locale(toLanguage);
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        Configuration configuration = context.getResources().getConfiguration();
        configuration.locale = locale;
        context.getResources().updateConfiguration(configuration, displayMetrics);
        if (flag == 2) {
            Intent refresh = new Intent(context, StartPage.class);
            context.startActivity(refresh);
        }


    }


}
