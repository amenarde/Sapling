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
import android.widget.TextView;
import android.view.ViewGroup;

import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.zip.Inflater;
import java.util.Collection;
import java.util.Iterator;


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

    @Override
    protected void onStart() {
        super.onStart();

        DataManager dm = DataManager.getInstance();
        DayData today = dm.getDay(new Date(), this.getApplicationContext());
        Iterator<DayData> pastWeek = dm.getLastWeek(new Date(), this.getApplicationContext());
        int qualityOfLife = 0;
        int daysInWeek = 0;
       while (pastWeek.hasNext()) {
            DayData dayData = pastWeek.next();
            daysInWeek += 1;

          Iterator<Metric> metrics = dayData.getAllMetrics().iterator();
          while(metrics.hasNext()) {
              Metric m = metrics.next();
              if (m.getPositive()){
              qualityOfLife += m.getRating();
          } else {
                  qualityOfLife += (7 - m.getRating());
              }
          }
        }
        qualityOfLife = qualityOfLife / daysInWeek;



        // updates the elements as per the current day's existing metrics, "--" if not present

        /*

        DataManager dm = DataManager.getInstance();
        DayData todayData = dm.getMetricsForDay(Calendar.getInstance().getTime());

        Collection<Metric> todayMetrics = todayData.getAllMetrics();
        Iterator<Metric> iter = todayMetrics.iterator();

        int metricCount = 0;

        while (iter.hasNext()) {
            Metric m = iter.next();

            if (metricCount == 0) {
                ((TextView) findViewById(R.id.metric1_title)).setText(m.getName());
                ((TextView) findViewById(R.id.metric1_stat)).setText(m.getRating());
            } else if (metricCount == 1) {
                ((TextView) findViewById(R.id.metric2_title)).setText(m.getName());
                ((TextView) findViewById(R.id.metric2_stat)).setText(m.getRating());
            } else if (metricCount == 2) {
                ((TextView) findViewById(R.id.metric3_title)).setText(m.getName());
                ((TextView) findViewById(R.id.metric3_stat)).setText(m.getRating());
            } else {
                ((TextView) findViewById(R.id.metric4_title)).setText(m.getName());
                ((TextView) findViewById(R.id.metric4_stat)).setText(m.getRating());
            }

            metricCount++;
        }
        */


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
