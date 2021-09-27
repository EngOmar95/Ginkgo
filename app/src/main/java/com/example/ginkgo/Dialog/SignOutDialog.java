package com.example.ginkgo.Dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import androidx.appcompat.app.AlertDialog;

import com.example.ginkgo.R;
import com.example.ginkgo.SettingPage;
import com.example.ginkgo.SignIn;

public class SignOutDialog {
    public void showDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getString(R.string.confMessage));
        builder.setPositiveButton(context.getString(R.string.yes),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                       Intent intent = new Intent(context, SignIn.class);
                        context.startActivity(intent);
                    }
                });

        builder.setNegativeButton(context.getString(R.string.No),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

        builder.show();



}    }
