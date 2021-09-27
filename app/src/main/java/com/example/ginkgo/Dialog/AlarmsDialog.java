package com.example.ginkgo.Dialog;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.ginkgo.R;
import com.example.ginkgo.Share.Share;
import com.example.ginkgo.databaseService.AlarmService;
import com.example.ginkgo.entity.Alarm;


public class AlarmsDialog extends AppCompatDialogFragment {


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater i = getActivity().getLayoutInflater();
        View v = i.inflate(R.layout.new_alarm, null, false);
        builder.setView(v);
        final EditText alarmName = v.findViewById(R.id.alarmName);
        final TimePicker time = v.findViewById(R.id.time);
        Button create = v.findViewById(R.id.create);

        create.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                Share share = new Share();
                AlarmService alarmService = new AlarmService(getActivity());
                Alarm alarm = new Alarm();
                alarm.setAlarmName(alarmName.getText().toString());
                alarm.setTime(share.editTime(time.getHour(), time.getMinute()));
                alarm.setOn(false);

                alarmService.addAlarm(alarm);


                dismiss();

            }
        });


        return builder.create();


    }


}



