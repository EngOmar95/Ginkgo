package com.example.ginkgo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.example.ginkgo.Share.Share;
import com.google.android.material.textfield.TextInputEditText;

public class SignUp extends AppCompatActivity {
    Button signUp;
    Intent intent;
    TextView back;
    TextInputEditText email, password, confPassword;
    final AwesomeValidation awesomeValidation=new AwesomeValidation(ValidationStyle.BASIC);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        signUp = findViewById(R.id.signUp);
        back = findViewById(R.id.back);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confPassword = findViewById(R.id.confPassword);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor edit = sp.edit();
        Share share = new Share();


        awesomeValidation.addValidation(this, R.id.password, "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$",R.string.Invalid_Password);
        awesomeValidation.addValidation(this, R.id.confPassword, R.id.password,R.string.Invalid_ConfPassword);
        awesomeValidation.addValidation(this, R.id.email, Patterns.EMAIL_ADDRESS ,R.string.Invalid_Emil);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (share.isNetworkConnected(getBaseContext())) {
                    if(awesomeValidation.validate()){
                    edit.apply();
                    intent = new Intent(SignUp.this, SigningUp.class);
                    intent.putExtra("email", email.getText().toString());
                    intent.putExtra("password", password.getText().toString());

                    startActivity(intent);   }
                } else {
                    share.showDialog(SignUp.this, "please check on internt ", null);
                }


            }
        });

    }
}