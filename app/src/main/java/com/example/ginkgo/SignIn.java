package com.example.ginkgo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.example.ginkgo.Share.Share;
import com.example.ginkgo.databaseService.UserService;
import com.google.android.material.textfield.TextInputEditText;

public class SignIn extends AppCompatActivity {
    TextView forgetPassword;
    Button logIn;
    TextInputEditText email, password;
    final AwesomeValidation awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        forgetPassword = findViewById(R.id.forgetPassword);
        forgetPassword.setPaintFlags(forgetPassword.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        logIn = findViewById(R.id.logIn);
        email = findViewById(R.id.email);
        Share share = new Share();
        UserService userService =new UserService(SignIn.this);
        password = findViewById(R.id.password);
        TextView back = findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        awesomeValidation.addValidation(this, R.id.password, RegexTemplate.NOT_EMPTY,R.string.Invalid_Required);
        awesomeValidation.addValidation(this, R.id.email, Patterns.EMAIL_ADDRESS ,R.string.Invalid_Emil);

        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignIn.this, ForgetPassword.class);
                startActivity(intent);
            }
        });

        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (share.isNetworkConnected(getBaseContext())) {
                    if(awesomeValidation.validate()) {
                        userService.logIn(SignIn.this, email.getText().toString(), password.getText().toString());
                    }
                } else {
                    share.showDialog(SignIn.this, "please check on internt ", null);
                }
            }

        });

    }
    @Override
    public void onBackPressed() {

        Intent intent =new Intent(SignIn.this,MainPage.class) ;
        startActivity(intent);
    }
}