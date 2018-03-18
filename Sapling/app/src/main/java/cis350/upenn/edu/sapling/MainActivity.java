package cis350.upenn.edu.sapling;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.LayoutInflater.Factory;
import android.view.View;
import android.view.ViewGroup;

import java.util.zip.Inflater;

public class MainActivity extends AppCompatActivity {

    private Boolean firstTime = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //reset shared preferences, assuming that mainactivity is always running (not killed by app close)
        SharedPreferences mPreferences = this.getPreferences(Context.MODE_PRIVATE);
        mPreferences.edit().clear().commit();

        super.onCreate(savedInstanceState);

        //if the first time opening the app, jump to onboarding process
        if (isFirstTime()) {
            Intent i = new Intent(this, OnboardingActivity.class);
            startActivityForResult(i, 5);
        }

        setContentView(R.layout.activity_main);

    }

    public void startDisplay(View view){
        Intent i = new Intent(this, DisplayActivity.class);
        startActivityForResult(i, 1);
    }

    public void startLogging(View view){
        Intent i = new Intent(this, LoggingActivity.class);
        startActivityForResult(i, 2);
    }

    public void startGoalTracking(View view){
        Intent i = new Intent(this, GoalTrackingActivity.class);
        startActivityForResult(i, 3);
    }

    public void startSettings(View view){
        Intent i = new Intent(this, SettingsActivity.class);
        startActivityForResult(i, 4);
    }

    private boolean isFirstTime() {
        if (firstTime == null) {
            SharedPreferences mPreferences = this.getPreferences(Context.MODE_PRIVATE);
            firstTime = mPreferences.getBoolean("firstTime", true);
            Log.v("First Time after getting boolean?", "it is " + firstTime);
            if (firstTime) {
                SharedPreferences.Editor editor = mPreferences.edit();
                editor.putBoolean("firstTime", false);
                editor.commit();
            }
        }
        return firstTime;
    }

}
