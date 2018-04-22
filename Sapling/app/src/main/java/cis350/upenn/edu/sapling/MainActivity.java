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
import android.widget.ImageView;
import android.widget.TextView;
import android.view.ViewGroup;

import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.zip.Inflater;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Date;

import com.jjoe64.graphview.series.Series;
import com.jjoe64.graphview.series.BaseSeries;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.BarGraphSeries;

import android.graphics.Color;


public class MainActivity extends AppCompatActivity {

    private Boolean firstTime = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //reset shared preferences, assuming that main activity is always running (not killed by app close)
        SharedPreferences mPreferences = this.getPreferences(Context.MODE_PRIVATE);
        mPreferences.edit().clear().commit();

        super.onCreate(savedInstanceState);

        //checks if mainactivity has been called from another activity
//        boolean calledInApp = getIntent().getBooleanExtra("calledInApp", false);
//if (!calledInApp) {
    //if the first time opening the app, jump to onboarding process
        Boolean calledInApp = getIntent().getBooleanExtra("calledInApp", false);
        if (!calledInApp) {
            if (isFirstTime()) {
                DataManager dm = DataManager.getInstance();
                dm.purgeFiles(this.getApplicationContext());
                firstTime = false;
                (new NotificationBuilder()).setNotificationCalendar();
                Intent i = new Intent(this, OnboardingActivity.class);
                i.putExtra("requestCode", 1);
                startActivityForResult(i, 1);
            }
        }
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();

        DataManager dm = DataManager.getInstance();
        Iterator<DayData> pastWeek = dm.getLastWeek(new Date(), this.getApplicationContext());

        setPlantImg(qualityOfLife(dm));

        ///TIFFANY'S GRAPH CODE
        DataPoint[] points = new DataPoint[7];
        int dayInWeek = 7;

        while (pastWeek.hasNext()) {
            double totalNum = 0;
            DayData dayData = pastWeek.next();
            dayInWeek -= 1;

            Iterator<Metric> metrics = dayData.getAllMetrics().iterator();
            int numMetrics = 0;
            while(metrics.hasNext()) {
                Metric m = metrics.next();
                Log.v("Main Activity:", "Metric " + m.getName() + " is active? " + dm.getActiveMetrics(getApplicationContext()).containsKey(m.getName().toLowerCase()));
                if (m.getRating() != -1 && dm.getActiveMetrics(getApplicationContext()).containsKey(m.getName().toLowerCase())) {
                    numMetrics++;
                    if (m.getPositive()){
                        totalNum += m.getRating();
                    } else {
                        totalNum += (8 - m.getRating());
                    }
                    Log.v("Main Activity:", "Day " + dayInWeek + " found with metric " + numMetrics + ", " + m.getName() + " with value " + m.getRating());
                }
            }
            Log.v("Main Activity", "total num calculated is " + totalNum);
            double val = totalNum/numMetrics;

            //assign to proper day
            points[dayInWeek] = new DataPoint(dayInWeek + 1, val);
        }

        GraphView graph = (GraphView) findViewById(R.id.graph);
        graph.getViewport().setMinX(1);
        graph.getViewport().setMaxX(7);
        graph.getViewport().setMinY(0.0);
        graph.getViewport().setMaxY(7.0);
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setXAxisBoundsManual(true);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(points);
        series.setColor(Color.WHITE);
        series.setDrawDataPoints(true);
        series.setDataPointsRadius(10);
        series.setThickness(8);
        graph.removeAllSeries();
        graph.addSeries(series);
        graph.setTitle("Quality of Life");


        // updates the elements as per the current day's existing metrics, "--" if not present
        DayData todayData = dm.getDay(new Date(), getApplicationContext());
        int i = 0;
        for (Metric m : todayData.getAllMetrics()) {
            if (dm.getActiveMetrics(getApplicationContext()).containsKey(m.getName().toLowerCase())) {
                System.out.println("found metric with name " +m.getName() + " and rating " + m.getRating());
                if (i == 0) {
                    ((TextView) findViewById(R.id.metric1_title)).setText(m.getName().toLowerCase());
                    if (m.hasScale()) {
                        ((TextView) findViewById(R.id.metric1_stat)).setText(String.valueOf(m.getRating()) + ".0");
                    } else {
                        ((TextView) findViewById(R.id.metric1_stat)).setText("--");
                    }
                } else if (i == 1) {
                    ((TextView) findViewById(R.id.metric2_title)).setText(m.getName().toLowerCase());
                    if (m.hasScale()) {
                        ((TextView) findViewById(R.id.metric2_stat)).setText(String.valueOf(m.getRating()) + ".0");
                    } else {
                        ((TextView) findViewById(R.id.metric2_stat)).setText("--");
                    }
                } else if (i == 2) {
                    ((TextView) findViewById(R.id.metric3_title)).setText(m.getName().toLowerCase());
                    if (m.hasScale()) {
                        ((TextView) findViewById(R.id.metric3_stat)).setText(String.valueOf(m.getRating()) + ".0");
                    } else {
                        ((TextView) findViewById(R.id.metric3_stat)).setText("--");
                    }
                } else if (i == 3) {
                    ((TextView) findViewById(R.id.metric4_title)).setText(m.getName().toLowerCase());
                    if (m.hasScale()) {
                        ((TextView) findViewById(R.id.metric4_stat)).setText(String.valueOf(m.getRating()) + ".0");
                    } else {
                        ((TextView) findViewById(R.id.metric4_stat)).setText("--");
                    }
                }
                i++;
            }
        }

        if (i == 0) {
            ((TextView) findViewById(R.id.metric1_title)).setText("");
            ((TextView) findViewById(R.id.metric1_stat)).setText("");
            i++;
        }
        if (i == 1) {
            ((TextView) findViewById(R.id.metric2_title)).setText("");
            ((TextView) findViewById(R.id.metric2_stat)).setText("");
            i++;
        }
        if (i == 2) {
            ((TextView) findViewById(R.id.metric3_title)).setText("");
            ((TextView) findViewById(R.id.metric3_stat)).setText("");
            i++;
        }
        if (i == 3) {
            ((TextView) findViewById(R.id.metric4_title)).setText("");
            ((TextView) findViewById(R.id.metric4_stat)).setText("");

        }

    }

    public void startDisplay(View view){
        Intent i = new Intent(this, DisplayActivity.class);
        startActivityForResult(i, 1);
    }

    public void startLogging(View view){
        Intent i = new Intent(this, LoggingActivity.class);
        startActivityForResult(i, 2);
    }

    public void startSettings(View view){
        Intent i = new Intent(this, SettingsActivity.class);
        startActivityForResult(i, 4);
    }
    public void startShowcase(View view){
        Intent i = new Intent(this, ShowcaseActivity.class);
        startActivityForResult(i, 5);
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

    private double qualityOfLife (DataManager dm) {

        Iterator<DayData> pastWeek = dm.getLastWeek(new Date(), this.getApplicationContext());
        //Algorithm for plant health
        double totalRating = 0;
        int numMetrics = 0;
        while (pastWeek.hasNext()) {
            DayData dayData = pastWeek.next();
            Iterator<Metric> metrics = dayData.getAllMetrics().iterator();
            while(metrics.hasNext()) {
                Metric m = metrics.next();
                if (dm.getActiveMetrics(getApplicationContext())
                        .containsKey(m.getName().toLowerCase())) {
                    if (m.getPositive() && m.hasScale() && m.getRating()!= -1) {
                        totalRating += m.getRating();
                        numMetrics += 1;
                    } else if (m.hasScale() && m.getRating()!= -1)  {
                        totalRating += (8 - m.getRating());
                        numMetrics += 1;
                    }
                }
            }
        }
        System.out.println("numMetrics =" + numMetrics);
        System.out.println("total rating =" + totalRating);
        System.out.println("average rating =" + (totalRating/numMetrics));
        return  totalRating / numMetrics;
    }

   private void setPlantImg (Double qualityOfLife) {
       double q = (7.0/6.0);
       int qofScaled = 0;

       if (qualityOfLife< (q)) {
           qofScaled = 1;
       } else if (qualityOfLife < (q * 2.0)) {
           qofScaled = 2;
       } else if (qualityOfLife < (q * 3.0)) {
           qofScaled = 3;
       } else if (qualityOfLife < (q * 4.0)) {
           qofScaled = 4;
       } else if (qualityOfLife < (q * 5.0)) {
           qofScaled = 5;
       } else {
           qofScaled = 6;
       }

       int qof = qofScaled;
       ImageView plant_view = (ImageView) findViewById(R.id.homepage_plant);
       switch(qof) {
           case 1 : plant_view.setImageResource(R.drawable.ic_sapling_1);
               break;
           case 2 : plant_view.setImageResource(R.drawable.ic_sapling_2);
               break;
           case 3 : plant_view.setImageResource(R.drawable.ic_sapling_3);
               break;
           case 4 : plant_view.setImageResource(R.drawable.ic_sapling_4);
               break;
           case 5 : plant_view.setImageResource(R.drawable.ic_sapling_5);
               break;
           case 6 : plant_view.setImageResource(R.drawable.ic_sapling_6);
               break;
           default :plant_view.setImageResource(R.drawable.ic_sapling_1);
               break;
       }
   }

}
