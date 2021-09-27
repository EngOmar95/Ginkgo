package com.example.ginkgo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.ginkgo.Share.Share;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPassword extends AppCompatActivity {
    TextView back;
    Button sendEmail;
    TextInputEditText email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        sendEmail=findViewById(R.id.sendEmail)  ;
         email=findViewById(R.id.email)  ;
         back=findViewById(R.id.back)           ;

        Share dialog = new Share();

        sendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

                if(dialog.isNetworkConnected(getApplicationContext())) {
                    firebaseAuth.sendPasswordResetEmail(email.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                dialog.showDialog(ForgetPassword.this, "please cheack your Email", null);
                                //    Toast.makeText(ForgetPassword.this, "please cheack your Email", Toast.LENGTH_SHORT).show();
                            } else {
                                dialog.showDialog(ForgetPassword.this, "Invalid Email", null);
                                //  Toast.makeText(ForgetPassword.this, "Invalid Email", Toast.LENGTH_SHORT).show();
                            }
                        }


                    });
                }else {
                    dialog.showDialog(ForgetPassword.this, "please check on internt ", null)  ;
                }
            }});
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


    }
}