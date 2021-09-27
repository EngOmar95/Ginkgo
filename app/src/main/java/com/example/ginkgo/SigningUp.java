package com.example.ginkgo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.example.ginkgo.Share.Share;
import com.example.ginkgo.databaseService.UserService;
import com.example.ginkgo.entity.User;
import com.google.android.material.textfield.TextInputEditText;

public class SigningUp extends AppCompatActivity {
    DatePicker Dob;
    Button done;
    TextView back;
    RadioGroup gender;
    TextInputEditText nickName;
    String email;
    String password;
    String[] cheacked = {""};
    final AwesomeValidation awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signing_up);
        Share share = new Share();
        done = findViewById(R.id.done);
        Dob = findViewById(R.id.Dob);
        back = findViewById(R.id.back);
        nickName = findViewById(R.id.nickName);
        gender = findViewById(R.id.gender);
        UserService userService = new UserService(SigningUp.this);
        Intent intentgetValue = getIntent();
        email = intentgetValue.getStringExtra("email");
        password = intentgetValue.getStringExtra("password");
        cheacked[0] = "male";

        awesomeValidation.addValidation(this, R.id.nickName, RegexTemplate.NOT_EMPTY, R.string.Invalid_Required);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {

                    case R.id.male:
                        cheacked[0] = "male";
                        return;
                    case R.id.fmale:
                        cheacked[0] = "female";
                        return;
                }


            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (share.isNetworkConnected(getBaseContext())) {
                    if (awesomeValidation.validate()) {
                        userService.createAuthUser(SigningUp.this, email, password, setUserInfo());
                    }
                } else {
                    share.showDialog(SigningUp.this, "please check on internt ", null);
                }
            }
        });
    }

    private User setUserInfo() {
        User user = new User();
        String BrithDate = Dob.getMonth() + "/" + Dob.getDayOfMonth() + "/" + Dob.getYear();
        user.setEmail(email.toLowerCase());
        user.setPassword(password);
        user.setBrithOfDate(BrithDate);
        user.setGender(cheacked[0]);
        user.setNickName(nickName.getText().toString());
        return user;

    }
}