package com.example.ginkgo.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.ginkgo.R;
import com.example.ginkgo.Share.Share;


public class ChangeLaunguageDialog extends AppCompatDialogFragment {
    Context context;

    public ChangeLaunguageDialog(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View v = layoutInflater.inflate(R.layout.change_language, null, false);
        builder.setView(v);
        final RadioGroup languages = v.findViewById(R.id.languages);
        RadioButton english = v.findViewById(R.id.english);
        RadioButton arabic = v.findViewById(R.id.arabic);
        String[] cheacked = {""};
        Share share = new Share();
        SharedPreferences saved_values = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit = saved_values.edit();
        Button create = v.findViewById(R.id.create);
        String lang = saved_values.getString("language", "0");
        if (lang.equals("ar")) {
            arabic.setChecked(true);
        } else {

            english.setChecked(true);
        }

        languages.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {

                    case R.id.english:
                        edit.putString("language", "en");
                        edit.commit();
                        share.changeLang("en", context, 2);

                        return;
                    case R.id.arabic:
                        edit.putString("language", "ar");
                        edit.commit();
                        share.changeLang("ar", context, 2);
                        return;
                }


            }
        });

        create.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {

                Toast.makeText(getContext(), cheacked[0], Toast.LENGTH_SHORT).show();

                dismiss();

            }
        });


        return builder.create();


    }


}
