package com.example.ginkgo.Adapaters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ginkgo.R;
import com.example.ginkgo.UpadeReminder;
import com.example.ginkgo.entity.Reminder;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ListReminderAdapter extends RecyclerView.Adapter<ListReminderAdapter.ViewHolder> {


    ArrayList<Reminder> ArrayReminder;
    Context context;

    public ListReminderAdapter(ArrayList<Reminder> ArrayReminder, Context context) {
        this.ArrayReminder = ArrayReminder;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, time;
        CheckBox isCheck;
        ImageButton deleteReminder;
        LinearLayout reminderCard;
        CardView reminderCardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            time = itemView.findViewById(R.id.time);
            isCheck = itemView.findViewById(R.id.CheckBox1);
            deleteReminder = itemView.findViewById(R.id.deleteReminder);
            reminderCard = itemView.findViewById(R.id.reminderCard);
            reminderCardView = itemView.findViewById(R.id.reminderCardView);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_reminder, null, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Reminder reminder = ArrayReminder.get(position);
        holder.title.setText(reminder.getReminderName());
        holder.time.setText(reminder.getTime());
        holder.isCheck.setChecked(reminder.isOn());


        Calendar calendar = Calendar.getInstance();
        Date date = new Date(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        Date date1 = new Date(reminder.getDay().getYear(), reminder.getDay().getMonth(), reminder.getDay().getDate());
        if (date.equals(date1)) {
            holder.reminderCardView.setCardBackgroundColor(Color.WHITE);

        }
        holder.isCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean value = false;
                if (holder.isCheck.isChecked()) {
                    value = true;
                } else {
                    value = false;
                }

                DatabaseReference updateReminder = FirebaseDatabase.getInstance().getReference("Service").child("Reminders").child(reminder.getReminderId());
                reminder.setOn(value);
                Reminder editReminder = new Reminder(reminder.getUserId(), reminder.getReminderId(), reminder.getReminderName(),
                        reminder.getTime(), reminder.getDay(), reminder.getLocation(), reminder.getCommint(), reminder.getAttachment()
                        , reminder.isOn());
                updateReminder.setValue(editReminder);
            }
        });

        holder.deleteReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatabaseReference deleteReminder = FirebaseDatabase.getInstance().getReference("Service").child("Reminders").child(reminder.getReminderId());
                deleteReminder.removeValue();
            }
        });

        holder.reminderCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UpadeReminder.class);
                intent.putExtra("reminderId", reminder.getReminderId());
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return ArrayReminder.size();
    }


}
