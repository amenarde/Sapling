package cis350.upenn.edu.sapling;

import android.content.Intent;
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

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
    }
    public void startDisplay(View view){
        Intent i = new Intent(this, DisplayActivity.class);
        startActivityForResult(i, 1);
    }

    public void startLogging(View view){
        Intent i = new Intent(this, LoggingActivity.class);
        startActivityForResult(i, 1);
    }

    public void startGoalTracking(View view){
        Intent i = new Intent(this, GoalTrackingActivity.class);
        startActivityForResult(i, 1);
    }

    public void startSettings(View view){
        Intent i = new Intent(this, SettingsActivity.class);
        startActivityForResult(i, 1);
    }

}
