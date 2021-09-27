package com.example.ginkgo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Patterns;
import android.view.View;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.example.ginkgo.Share.Share;
import com.example.ginkgo.databaseService.UserService;
import com.example.ginkgo.entity.User;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class ProfilePage extends AppCompatActivity {
    TextView back, edit, save;
    TextInputEditText nickName, email, Dob;
    RadioButton male, female;
    RadioGroup gender;
    String[] cheacked = {""};
    SharedPreferences sp;
    final AwesomeValidation awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);
        back = findViewById(R.id.back);
        edit = findViewById(R.id.edit);
        save = findViewById(R.id.save);
        nickName = findViewById(R.id.nickName);
        email = findViewById(R.id.email);
        male = findViewById(R.id.male);
        female = findViewById(R.id.female);
        Dob = findViewById(R.id.Dob);
        gender = findViewById(R.id.gender);
        UserService userService = new UserService(ProfilePage.this);
        sp = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        userService.ViewUserById(nickName, email, Dob, male, female);

        awesomeValidation.addValidation(this, R.id.nickName, RegexTemplate.NOT_EMPTY,R.string.Invalid_Required);
        awesomeValidation.addValidation(this, R.id.Dob, RegexTemplate.NOT_EMPTY,R.string.Invalid_Required);
        awesomeValidation.addValidation(this, R.id.email, Patterns.EMAIL_ADDRESS ,R.string.Invalid_Emil);


        Dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(ProfilePage.this, android.R.style.Theme_Holo_Light_Dialog, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        String BrithDate = i + "/" + i1 + "/" + i2;
                        Dob.setText(BrithDate);
                    }
                }, 2000, 01, 01);

                // datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.GRAY));
                datePickerDialog.show();
            }
        });


        gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {

                    case R.id.male:
                        cheacked[0] = "male";
                        return;
                    case R.id.female:
                        cheacked[0] = "female";
                        return;
                }


            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nickName.setEnabled(true);
                email.setEnabled(true);
                male.setEnabled(true);
                female.setEnabled(true);
                Dob.setEnabled(true);
                edit.setVisibility(View.GONE);
                save.setVisibility(View.VISIBLE);

            }
        });
        Share share = new Share();
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (share.isNetworkConnected(getBaseContext())) {
                    if(awesomeValidation.validate()) {
                        nickName.setEnabled(false);
                        email.setEnabled(false);
                        male.setEnabled(false);
                        female.setEnabled(false);
                        Dob.setEnabled(false);
                        edit.setVisibility(View.VISIBLE);
                        save.setVisibility(View.GONE);

                        User user = new User();
                        user.setNickName(nickName.getText().toString());
                        user.setGender(cheacked[0]);
                        user.setBrithOfDate(Dob.getText().toString());
                        user.setEmail(email.getText().toString());
                        userService.updateUser(user);

                        share.showDialog(ProfilePage.this, "Done", null);
                    }
                } else {
                    share.showDialog(ProfilePage.this, "please check on Internit", null);
                }
            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }


}