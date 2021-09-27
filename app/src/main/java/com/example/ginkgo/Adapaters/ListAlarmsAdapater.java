package com.example.ginkgo.Adapaters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ginkgo.R;
import com.example.ginkgo.entity.Alarm;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ListAlarmsAdapater extends RecyclerView.Adapter<ListAlarmsAdapater.ViewHolder> {

    ArrayList<Alarm> ArrayAlarms;
    Context context;

    public ListAlarmsAdapater(ArrayList<Alarm> ArrayAlarms, Context context) {
        this.ArrayAlarms = ArrayAlarms;
        this.context = context;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, time;
        Switch isOn;
        ImageButton deleteAlarm;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            time = itemView.findViewById(R.id.time);
            isOn = itemView.findViewById(R.id.switch1);
            deleteAlarm = itemView.findViewById(R.id.deleteAlarm);
        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_alarms, null, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Alarm alarm = ArrayAlarms.get(position);
        holder.title.setText(alarm.getAlarmName());
        holder.time.setText(alarm.getTime());
        holder.isOn.setChecked(alarm.isOn());


        holder.isOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean value = false;
                if (holder.isOn.isChecked()) {
                    value = true;
                } else {
                    value = false;
                }

                DatabaseReference updateAlarm = FirebaseDatabase.getInstance().getReference("Service").child("Alarms").child(alarm.getAlarmId());
                alarm.setOn(value);
                Alarm editAlarm = new Alarm(alarm.getUserId(), alarm.getAlarmId(), alarm.getAlarmName(), alarm.getTime(), value);
                updateAlarm.setValue(editAlarm);
            }
        });
        holder.deleteAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatabaseReference deleteAlarm = FirebaseDatabase.getInstance().getReference("Service").child("Alarms").child(alarm.getAlarmId());
                deleteAlarm.removeValue();
            }
        });

    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return ArrayAlarms.size();
    }


}
