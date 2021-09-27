package com.example.ginkgo.databaseService;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ginkgo.Adapaters.ListAlarmsAdapater;
import com.example.ginkgo.Dialog.ProgressBarDialog;
import com.example.ginkgo.Share.Share;
import com.example.ginkgo.entity.Alarm;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AlarmService {

    Share share = new Share();
    String SERVICE = "Service";
    String ALARMS = "Alarms";
    Context context;
    SharedPreferences sp;
    String USER_ID = "";
    ProgressBarDialog progressBarDialog = new ProgressBarDialog();

    public AlarmService(Context context) {
        this.context = context;
        sp = PreferenceManager.getDefaultSharedPreferences(context);
        USER_ID = sp.getString("userId", "0");
    }

    public void addAlarm(Alarm alarm) {
        progressBarDialog.show(((FragmentActivity) context).getSupportFragmentManager(), "");
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(SERVICE).child(ALARMS);
        String AlarmId = String.valueOf(databaseReference.push().getKey());
        String userId = sp.getString("userId", "0");
        alarm.setUserId(userId);
        alarm.setAlarmId(AlarmId);
        alarm.setOn(false);
        databaseReference.child(AlarmId).setValue(alarm);
        progressBarDialog.dismiss();
    }

    public void displayAlarmByListView(RecyclerView listViewOfAlarms) {
        ArrayList<Alarm> arrayOfAlarms =new ArrayList<>();
        Query fetchAlarmByUserId = FirebaseDatabase.getInstance().getReference(SERVICE).child(ALARMS).orderByChild("userId").equalTo(USER_ID);

        fetchAlarmByUserId.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    for (DataSnapshot snapshots : snapshot.getChildren()) {
                        Alarm alarm = snapshots.getValue(Alarm.class);
                        arrayOfAlarms.add(alarm);

                    } }
                ListAlarmsAdapater adp = new ListAlarmsAdapater(arrayOfAlarms, context);
                listViewOfAlarms.setLayoutManager(new GridLayoutManager(context,1));
                listViewOfAlarms.setAdapter(adp);





            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }}

