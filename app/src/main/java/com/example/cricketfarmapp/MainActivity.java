package com.example.cricketfarmapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.Console;

public class MainActivity extends AppCompatActivity {

    Toolbar topToolbar;
    BottomNavigationView bottomNavigationView;
    FirebaseDatabase database;

    //To prevent recreating fragments I create them here and switch between them in the code
    Fragment homeFragment = new HomeFragment();
    Fragment listFragment = new ListFragment();
    Fragment settingsFragment  = new SettingsFragment();
    Fragment helpFragment = new HelpFragment();
    Fragment creditFragment = new CreditsFragment();
    FragmentManager fmanager = getSupportFragmentManager();
    Fragment activeFragment = homeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Set up the toolbar
        topToolbar = findViewById(R.id.topToolbar);
        setSupportActionBar(topToolbar);

        //Set up the bottom navigation view
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                //Switch depending on which button is clicked
                switch (menuItem.getItemId()) {
                    case R.id.bottomBarHome:
                        //Change the toolbar text
                        topToolbar.setTitle("Home");
                        //Hide the current fragment and show the Home fragment
                        fmanager.beginTransaction().hide(activeFragment).show(homeFragment).commit();
                        activeFragment = homeFragment;
                        return true;
                    case R.id.bottomBarList:
                        topToolbar.setTitle("List of readings");
                        fmanager.beginTransaction().hide(activeFragment).show(listFragment).commit();
                        activeFragment = listFragment;
                        return true;
                    case R.id.bottomBarSettings:
                        topToolbar.setTitle("Settings");
                        fmanager.beginTransaction().hide(activeFragment).show(settingsFragment).commit();
                        activeFragment = settingsFragment;
                        return true;
                    case R.id.bottomBarHelp:
                        topToolbar.setTitle("Help");
                        fmanager.beginTransaction().hide(activeFragment).show(helpFragment).commit();
                        activeFragment = helpFragment;
                        return true;
                }

                return false;
            }
        });

        //Check if the user has seen the tutorial already, if not : launch it
        if(!PreferenceManager.getDefaultSharedPreferences(this).getBoolean("boardingDone",false)){
            startActivity(new Intent(this,BoardingActivity.class));
        }


        //Set up the fragments and display the active one
        fmanager.beginTransaction().add(R.id.frameLayout,listFragment).hide(listFragment).commit();
        fmanager.beginTransaction().add(R.id.frameLayout,settingsFragment).hide(settingsFragment).commit();
        fmanager.beginTransaction().add(R.id.frameLayout,helpFragment).hide(helpFragment).commit();
        fmanager.beginTransaction().add(R.id.frameLayout,homeFragment).hide(homeFragment).commit();
        fmanager.beginTransaction().add(R.id.frameLayout,creditFragment).hide(creditFragment).commit();
        fmanager.beginTransaction().show(activeFragment).commit();

        database = FirebaseDatabase.getInstance();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toptoolbarmenu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void goToCredits(View view){
        fmanager.beginTransaction().hide(activeFragment).show(creditFragment).commit();
        activeFragment=creditFragment;
    }

    public void  openDoor (View view){
        if(PreferenceManager.getDefaultSharedPreferences(this).getString("systemKey","") != "") {
            DatabaseReference ref = database.getReference("order").push();
            ref.child("type").setValue("openDoor");
            ref.child("systemKey").setValue(PreferenceManager.getDefaultSharedPreferences(this).getString("systemKey",""));
        }
        else{
            Toast.makeText(this, "You have to synchronize the app with your system !", Toast.LENGTH_SHORT).show();
        }
    }

    public void activateNormalMode (View view){
        if(PreferenceManager.getDefaultSharedPreferences(this).getString("systemKey","") != "") {
            DatabaseReference ref = database.getReference("order").push();
            ref.child("type").setValue("activateNormalMode");
            ref.child("systemKey").setValue(PreferenceManager.getDefaultSharedPreferences(this).getString("systemKey",""));
        }
        else{
            Toast.makeText(this, "You have to synchronize the app with your system !", Toast.LENGTH_SHORT).show();
        }
    }

    public void activatePickUpMode (View view) {
        if(PreferenceManager.getDefaultSharedPreferences(this).getString("systemKey","") != "") {
            DatabaseReference ref = database.getReference("order").push();
            ref.child("type").setValue("activatePickUpMode");
            ref.child("systemKey").setValue(PreferenceManager.getDefaultSharedPreferences(this).getString("systemKey",""));
        }
        else{
            Toast.makeText(this, "You have to synchronize the app with your system !", Toast.LENGTH_SHORT).show();
        }
    }

    public void synchronizeWithSystem (View view) {
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String systemKey = preferences.getString("systemKey","");
        if(systemKey == "") {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Enter the unique key of your system");
            final EditText userInput = new EditText(this);
            builder.setView(userInput);

            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String newSystemKey = userInput.getText().toString();
                    FirebaseMessaging.getInstance().subscribeToTopic(newSystemKey);
                    preferences.edit().putString("systemKey",newSystemKey).apply();
                    reloadListAndHomeFragment();
                }
            });
            builder.setNegativeButton("Back", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.show();
        }
        else {
            Toast.makeText(this, "You already entered a system key, reset the parameters if you want to change it.", Toast.LENGTH_SHORT).show();
        }
    }

    public void resetParameters (View view) {
        String systemKey = PreferenceManager.getDefaultSharedPreferences(this).getString("systemKey","");
        if(systemKey != "") {
            FirebaseMessaging.getInstance().unsubscribeFromTopic("prototype");
            PreferenceManager.getDefaultSharedPreferences(this).edit().putString("systemKey","").apply();
            reloadListAndHomeFragment();
        }
    }

    //When the systemKey is modified, the lastReading as well as the list of readings have to change so we reload those fragments
    public void reloadListAndHomeFragment(){
        fmanager.beginTransaction().remove(homeFragment).remove(listFragment).commit();
        homeFragment = new HomeFragment();
        listFragment = new ListFragment();
        fmanager.beginTransaction().add(R.id.frameLayout,listFragment).hide(listFragment).commit();
        fmanager.beginTransaction().add(R.id.frameLayout,homeFragment).hide(homeFragment).commit();
    }

    public void goToTutorial (View view){
        Intent tutorialIntent = new Intent(this,BoardingActivity.class);
        startActivity(tutorialIntent);
    }
}
