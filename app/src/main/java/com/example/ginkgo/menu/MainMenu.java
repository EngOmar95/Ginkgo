package com.example.ginkgo.menu;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import com.example.ginkgo.About_Us;
import com.example.ginkgo.AlarmsPage;
import com.example.ginkgo.CalendarPage;
import com.example.ginkgo.Dialog.ChangeLaunguageDialog;
import com.example.ginkgo.Dialog.SignOutDialog;
import com.example.ginkgo.HomePage;
import com.example.ginkgo.ProfilePage;
import com.example.ginkgo.R;
import com.example.ginkgo.ReminderPage;
import com.example.ginkgo.Service.Service;

public class MainMenu extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

   Context context;
   Intent intent ;
    PopupMenu popup;
    ChangeLaunguageDialog changeLaunguageDialog;
    public void showPopup(Context context, View v) {
         changeLaunguageDialog = new ChangeLaunguageDialog(context);
       this.context=context ;


         popup = new PopupMenu(context, v);
        MenuInflater inflater = popup.getMenuInflater();
        popup.setOnMenuItemClickListener(this);
        inflater.inflate(R.menu.main_menu, popup.getMenu());

        popup.show();
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    public boolean onMenuItemClick(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.alarma:

                 intent =new Intent(context, AlarmsPage.class)      ;
                context.startActivity(intent);
                return true;
            case R.id.calendar:
                intent =new Intent(context, CalendarPage.class)      ;
                context.startActivity(intent);
                return true;
            case R.id.home:
                intent =new Intent(context, HomePage.class)      ;
                context.startActivity(intent);
                return true;
            case R.id.aboutUsMenu:

                intent =new Intent(context, About_Us.class)      ;
                context.startActivity(intent);
                return true;
            case R.id.profile:
                intent =new Intent(context, ProfilePage.class)      ;
                context.startActivity(intent);
                return true;
            case R.id.changeLanguage:
                 FragmentActivity myContext = (FragmentActivity) context;
                changeLaunguageDialog.show(myContext.getSupportFragmentManager(), "");
                return true;
            case R.id.SignOt:
                SignOutDialog signOutDialog =new SignOutDialog();
                Intent runService = new Intent(context, Service.class);
                context.stopService(runService);
                signOutDialog.showDialog(context);
                return true;
            case R.id.reminders:
                intent =new Intent(context, ReminderPage.class)      ;
                context.startActivity(intent);
                return true;
           



            default:
                return false;
        }



    }

    public void  makeAllMeueEnable(){
        Menu n = null;
         getMenuInflater().inflate(R.menu.main_menu,n)  ;
        MenuItem  alarma  =n.getItem(1)  ;
//        MenuItem  calendar  =findViewById(R.id.calendar)  ;
//        MenuItem  home  =findViewById(R.id.home)  ;
//        MenuItem  aboutUsMenu  =findViewById(R.id.aboutUsMenu)  ;
//        MenuItem  profile  =findViewById(R.id.profile)  ;
//        MenuItem  reminders  =findViewById(R.id.reminders)  ;
        alarma.setEnabled(false);
//        calendar.setEnabled(true);
//        home.setEnabled(true) ;
//        aboutUsMenu.setEnabled(true) ;
//        profile.setEnabled(true);
//        reminders.setEnabled(true);





    }

    public void  makeMeueDisenable( int id){
        MenuItem  menu  =findViewById(id)  ;

        menu.setEnabled(false);


    }

}
