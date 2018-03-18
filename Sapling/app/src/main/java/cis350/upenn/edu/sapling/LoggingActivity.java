package cis350.upenn.edu.sapling;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;


import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;

public class LoggingActivity extends AppCompatActivity {
    DataManager dataManager;
    DayData dayData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logging);

        HashSet<String> activeMetrics = new HashSet<String>();
        activeMetrics.add("Health");
        activeMetrics.add("Productivity");
        activeMetrics.add("Stress");


        //Programmatically add metrics to layout
        for (String m : activeMetrics) {

            LinearLayout metric_labels = findViewById(R.id.metrics_view_list);
            TextView textV = new TextView(this);
            textV.setText(m.toUpperCase());
            textV.setTextSize(20);


            SeekBar seekBar = new SeekBar(this);
            seekBar.setMax(7);
            ShapeDrawable thumb = new ShapeDrawable(new OvalShape());
            thumb.setIntrinsicHeight(30);
            thumb.setIntrinsicWidth(30);
            seekBar.setThumb(thumb);
            seekBar.setProgress(1);
            seekBar.setVisibility(View.VISIBLE);

            metric_labels.addView(textV);
            metric_labels.addView(seekBar);
        }

        HashSet<String> activeGoals = new HashSet<String>();
        activeGoals.add("Eat an apple");
        activeGoals.add("Go to gym");

        for (String g: activeGoals ) {
            LinearLayout goal_labels = findViewById(R.id.goals_view_list);
            String input = g.trim().toLowerCase();
            String formattedText = input.substring(0, 1).toUpperCase() + input.substring(1);
            CheckBox checkBox = new CheckBox(this);
            checkBox.setText(formattedText);
            checkBox.setTextSize(20);
            goal_labels.addView(checkBox);
        }

 // COMMUNICATES W/DATA LAYER

//        dataManager = DataManager.getInstance();
//        Date today = new Date();
//        dayData = dataManager.getDay(today);
//
//        Iterator<Metric> itMetrics = dayData.getAllMetrics().iterator();
//
//        while(itMetrics.hasNext()) {
//            Metric m = itMetrics.next();
//            String label = m.getName().toUpperCase();
//            LinearLayout metric_labels = findViewById(R.id.metrics_view_list);
//            TextView textV = new TextView(this);
//            textV.setText(label);
//            textV.setTextSize(20);
//
//            SeekBar seekBar = new MetricScale(this, label);
//            seekBar.setMax(7);
//            ShapeDrawable thumb = new ShapeDrawable(new OvalShape());
//            thumb.setIntrinsicHeight(30);
//            thumb.setIntrinsicWidth(30);
//            seekBar.setThumb(thumb);
//            seekBar.setProgress(1);
//            seekBar.setVisibility(View.VISIBLE);
//
//            metric_labels.addView(textV);
//            metric_labels.addView(seekBar);
//        }
//
//
//        Iterator<Goal> itGoals = dayData.getAllGoals().iterator();
//
//        while(itGoals.hasNext()) {
//            Goal g = itGoals.next();
//            LinearLayout goal_labels = findViewById(R.id.goals_view_list);
//            String input = g.getName().trim().toLowerCase();
//            String formattedText = input.substring(0, 1).toUpperCase() + input.substring(1);
//
//            CheckBox checkBox = new CheckBox(this);
//            checkBox.setText(formattedText);
//            checkBox.setTextSize(20);
//            goal_labels.addView(checkBox);
//        }
//
//    }
//
//    public void onCheckboxClicked(View view) {
//        // Is the view now checked?
//        boolean checked = ((CheckBox) view).isChecked();
//        CheckBox cb = (CheckBox) view;
//        String label =  cb.getText().toString().toLowerCase();
//        Goal g;
//
//        if (checked) {
//            CharSequence txt = cb.getText();
//            g = new Goal(txt.toString(), true);
//        } else {
//            CharSequence txt = cb.getText();
//            g = new Goal(txt.toString(), false);
//        }
//        dayData.putGoal(g);
//
//    }
//    public void onScaleClicked(View view) {
//
  }
}
