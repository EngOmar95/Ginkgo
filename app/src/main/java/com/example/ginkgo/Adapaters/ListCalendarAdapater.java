package com.example.ginkgo.Adapaters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

import com.example.ginkgo.R;
import com.example.ginkgo.entity.Alarm;
import com.example.ginkgo.model.CalenderView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ListCalendarAdapater extends BaseAdapter {

        ArrayList<CalenderView> ArrayCalendar;
        Context context;

        public ListCalendarAdapater(ArrayList<CalenderView> ArrayCalendar, Context context) {
            this.ArrayCalendar = ArrayCalendar;
            this.context = context;
        }

        @Override
        public int getCount() {
            return ArrayCalendar.size();
        }

        @Override
        public CalenderView getItem(int i) {
            return ArrayCalendar.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View v = view;


            if (v == null) {

                v = LayoutInflater.from(context).inflate(R.layout.calender_list, null, false);
            }

            final TextView title = v.findViewById(R.id.title);
            final TextView commint = v.findViewById(R.id.commint);
            final CheckBox isDone = v.findViewById(R.id.isDone);


            title.setText(getItem(i).getTitle());
            commint.setText(getItem(i).getCommint());
            isDone.setChecked(getItem(i).isDone());
            isDone.setEnabled(false);
            return v;
        }


    }


