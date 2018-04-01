package cis350.upenn.edu.sapling;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
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
            (new NotificationBuilder()).setNotificationCalendar();
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
            Log.v("First Time bool true?", "it is " + firstTime);
            if (firstTime) {
                SharedPreferences.Editor editor = mPreferences.edit();
                editor.putBoolean("firstTime", false);
                editor.commit();
            }
        }
        return firstTime;
    }


    public class NotificationBuilder {
        public void setNotificationCalendar() {

            // https://stackoverflow.com/questions/23440251/how-to-repeat-notification-daily-on-specific-time-in-android-through-background
            //


            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, 17);
            calendar.set(Calendar.MINUTE, 22);
            Intent intent = new Intent(MainActivity.this, Notification.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intent, 0);
            AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            //am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
            Log.v("Notifications", "Time until notification sent:" + (calendar.getTimeInMillis() - System.currentTimeMillis()));
            am.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    SystemClock.elapsedRealtime() + (calendar.getTimeInMillis() - System.currentTimeMillis()),
                    pendingIntent);


//            long when = System.currentTimeMillis();
//            NotificationManager notificationManager = (NotificationManager) MainActivity.this
//                    .getSystemService(Context.NOTIFICATION_SERVICE);
//
//            Intent notificationIntent = new Intent(MainActivity.this, LoggingActivity.class);
//            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//
//            pendingIntent = PendingIntent.getActivity(MainActivity.this, 0,
//                    notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//            NotificationCompat.Builder mNotifyBuilder = new NotificationCompat.Builder(MainActivity.this, "default")
//                    .setSmallIcon(R.drawable.ic_sapling_1)
//                    .setContentTitle("Alarm Fired")
//                    .setContentText("Events to be Performed")
//                    .setAutoCancel(true).setWhen(when)
//                    .setContentIntent(pendingIntent)
//                    .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});
//            notificationManager.notify(1, mNotifyBuilder.build());
        }
    }
}
