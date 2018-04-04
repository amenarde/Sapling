package cis350.upenn.edu.sapling;
import android.app.Activity;
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
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


public class LoggingActivity extends AppCompatActivity {
    DataManager dataManager;
    DayData dayData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logging);

        // COMMUNICATES W/DATA LAYER
        dataManager = DataManager.getInstance();
        Date today = new Date();
        dayData = dataManager.getDay(today, this.getApplicationContext());
        voidPopulateCheckBoxes(dayData);
        populateSeekBars(dayData);
    }

private void populateSeekBars(final DayData day) {
    Iterator<Metric> itMetrics = day.getAllMetrics().iterator();

    while(itMetrics.hasNext()) {
        Metric m = itMetrics.next();
        String label = m.getName().toUpperCase();
        LinearLayout metric_labels = findViewById(R.id.metrics_view_list);
        TextView textV = new TextView(this);
        textV.setText(label);
        textV.setTextSize(20);

        MetricScale seekBar = new MetricScale(this, label, m.getPositive());
        seekBar.setMax(6);
        ShapeDrawable thumb = new ShapeDrawable(new OvalShape());
        thumb.setIntrinsicHeight(30);
        thumb.setIntrinsicWidth(30);
        seekBar.setMinimumWidth(1000);
        seekBar.setThumb(thumb);
        if (m.hasScale()) {
            seekBar.setProgress(m.getRating() - 1);
        } else {
            seekBar.setProgress(3);
        }
        seekBar.setVisibility(View.VISIBLE);

        metric_labels.addView(textV);
        metric_labels.addView(seekBar);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // nothing
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // nothing
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
                MetricScale sk = (MetricScale) seekBar;
                Metric newM = new Metric(sk.getName(), new Scale(progress + 1), sk.getPositive());
                day.putMetric(newM);
                dataManager.putDay(new Date(),dayData,getApplicationContext());
            }
        });
    }
}
    private void voidPopulateCheckBoxes(final DayData dayData) {
        Iterator<Goal> itGoals = dayData.getAllGoals().iterator();

        while (itGoals.hasNext()) {
            Goal g = itGoals.next();
            LinearLayout goal_labels = findViewById(R.id.goals_view_list);
            String input = g.getName().trim().toLowerCase();
            String formattedText = input.substring(0, 1).toUpperCase() + input.substring(1);

            CheckBox checkBox = new CheckBox(this);
            checkBox.setText(formattedText);
            checkBox.setTextSize(20);
            checkBox.setChecked(g.getCompleted());
            goal_labels.addView(checkBox);

            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Is the view now checked?
                    boolean checked = ((CheckBox) view).isChecked();
                    CheckBox cb = (CheckBox) view;
                    String label = cb.getText().toString().toLowerCase();
                    Goal newG;

                    if (checked) {
                        CharSequence txt = cb.getText();
                        newG = new Goal(txt.toString(), true);
                    } else {
                        CharSequence txt = cb.getText();
                        newG = new Goal(txt.toString(), false);
                    }
                    dayData.putGoal(newG);
                    dataManager.putDay(new Date(),dayData,getApplicationContext());
                }
            });
        }

        dataManager.addModelMetric("Productivity", true, getApplicationContext());
        dataManager.addModelMetric("Fatness", false, getApplicationContext());
        dataManager.addModelMetric("Sadness", false, getApplicationContext());
        dataManager.addModelGoal("Go to the gym", getApplicationContext());
        dataManager.addModelGoal("Eat fruits", getApplicationContext());
        dataManager.addModelGoal("Go to class", getApplicationContext());

        dataManager.deprecateModelGoal("Go to class", getApplicationContext());
        dataManager.deprecateModelMetric("Fatness", false, getApplicationContext());

        Map<String, Metric> metrics = dataManager.getActiveMetrics(this.getApplicationContext());
        Set<String> goals = dataManager.getActiveGoals(this.getApplicationContext());
        Map<String, Metric> inactiveMetrics = dataManager.getinativeMetrics(this.getApplicationContext());
        Set<String> inactiveGoals = dataManager.getinactiveGoals(this.getApplicationContext());

        System.out.println("=--------------SHIT----------");
        System.out.println("Active metrics " + metrics.size());
        System.out.println("Active goals " + goals.size());
        System.out.println("inactive metrics " + inactiveMetrics.size());
        System.out.println("inactive goals " + inactiveGoals.size());


        for (String s : metrics.keySet()) {
            Metric metric = metrics.get(s);
            System.out.println("Active Metric: " + metric.getName() + " " + metric.getPositive());
        }
        System.out.println();
        for (String s : goals) {
            System.out.println("Active Goal: " + s);
        }
        System.out.println();
        for (String s : inactiveMetrics.keySet()) {
            Metric metric = inactiveMetrics.get(s);
            System.out.println("Inactive Metric: " + metric.getName() + " " + metric.getPositive());
        }
        System.out.println();
        for (String s : inactiveGoals) {
            System.out.println("Inactive Goal: " + s);
        }

    }
}