package com.example.ginkgo.databaseService;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.RadioButton;

import androidx.annotation.NonNull;

import com.example.ginkgo.Share.Share;
import com.example.ginkgo.HomePage;
import com.example.ginkgo.SignIn;
import com.example.ginkgo.entity.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class UserService {
    Intent intent;
    Share share = new Share();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    String USERS = "Users";
    Context context;
    SharedPreferences sp;
    String USER_ID = "";
    String EMAIL = "";

    public UserService(Context context) {
        this.context = context;
        sp = PreferenceManager.getDefaultSharedPreferences(context);
        EMAIL= sp.getString("Email", "0");
        USER_ID = sp.getString("userId", "0");
    }

    public void createAuthUser(Context context, String email, String password, User user) {


        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    createUser(user);
                    intent = new Intent(context, SignIn.class);
                    share.showDialog(context, "Registration Successful", null);
                    context.startActivity(intent);
                } else {
                    share.showDialog(context, "Registration failed", null);

                }
            }
        });

    }

    private void createUser(User user) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child(USERS);
        String userid = String.valueOf(databaseReference.push().getKey());
        user.setUserId(userid);
        databaseReference.child(userid).setValue(user);

    }

    public void logIn(Context context, String email, String password) {

        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    getUserId(email,password, context);
                } else {
                    share.showDialog(context, "Email or password invalid", null);
                }
            }
        });
    }

    private void getUserId(String email, String password,Context context) {
        SharedPreferences saved_values = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit = saved_values.edit();
        Query databaseReference = FirebaseDatabase.getInstance().getReference(USERS).
                orderByChild("email").equalTo(email);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot snapshots : snapshot.getChildren()) {
                        User user = snapshots.getValue(User.class);
                        edit.putString("userId", user.getUserId().toString());
                        edit.putString("Email", email);
                        edit.putString("password",password);
                        edit.putString("gender",user.getGender());
                        edit.commit();
                        Intent intent = new Intent(context, HomePage.class);
                        context.startActivity(intent);
                    }
                } else {

                    share.showDialog(context, "an error occurred", null);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public void ViewUserById(TextInputEditText nickName, TextInputEditText email, TextInputEditText Dob, RadioButton male, RadioButton female) {


        Query fetchAlarmByUserId = FirebaseDatabase.getInstance().getReference().child("Users").orderByChild("userId").equalTo(USER_ID);

        fetchAlarmByUserId.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    for (DataSnapshot snapshots : snapshot.getChildren()) {
                        User user = snapshots.getValue(User.class);
                        nickName.setText(user.getNickName());
                        email.setText(user.getEmail());
                        Dob.setText(user.getBrithOfDate());

                        if (user.getGender().equals("male")) {
                            male.setChecked(true);
                        } else {
                            female.setChecked(true);
                        }

                    }


                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void updateUser(User user) {
        DatabaseReference updateUser = FirebaseDatabase.getInstance().getReference(USERS);


        user.setUserId(USER_ID);
        updateEmailInAuth(user.getEmail(),EMAIL);
        user.setPassword(null);
        updateUser.child(USER_ID).setValue(user);
    }
    private void updateEmailInAuth(String newEmail , String oldEmail) {

        AuthCredential credential = EmailAuthProvider
                .getCredential(oldEmail, sp.getString("password", "0")); // Current Login Credentials \\

        firebaseAuth.getCurrentUser().reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        user.updateEmail(newEmail)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            SharedPreferences.Editor edit = sp.edit();
                                            edit.putString("Email", newEmail);
                                            edit.commit();
                                        }
                                    }
                                });

                    }
                });

    }
}

