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

        Iterator<Metric> itMetrics = dayData.getAllMetrics().iterator();

        while(itMetrics.hasNext()) {
            Metric m = itMetrics.next();
            String label = m.getName().toUpperCase();
            LinearLayout metric_labels = findViewById(R.id.metrics_view_list);
            TextView textV = new TextView(this);
            textV.setText(label);
            textV.setTextSize(20);

            MetricScale seekBar = new MetricScale(this, label);
            seekBar.setMax(6);
            ShapeDrawable thumb = new ShapeDrawable(new OvalShape());
            thumb.setIntrinsicHeight(30);
            thumb.setIntrinsicWidth(30);
            seekBar.setMinimumWidth(1000);
            seekBar.setThumb(thumb);
            seekBar.setProgress(1);
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
                 Metric newM = new Metric(sk.getName(), new Scale(progress + 1));
                 dayData.putMetric(newM);

                    //test checkbox listeners
                    System.out.println("Seekbar moved");
                    System.out.println(dayData.getMetric(newM.getName()).getName());
                    System.out.println("New value:" + dayData.getMetric(newM.getName()).getRating());
                }
            });

        }


        Iterator<Goal> itGoals = dayData.getAllGoals().iterator();

        while(itGoals.hasNext()) {
            Goal g = itGoals.next();
            LinearLayout goal_labels = findViewById(R.id.goals_view_list);
            String input = g.getName().trim().toLowerCase();
            String formattedText = input.substring(0, 1).toUpperCase() + input.substring(1);

            CheckBox checkBox = new CheckBox(this);
            checkBox.setText(formattedText);
            checkBox.setTextSize(20);
            goal_labels.addView(checkBox);

            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Is the view now checked?
                    boolean checked = ((CheckBox) view).isChecked();
                    CheckBox cb = (CheckBox) view;
                    String label =  cb.getText().toString().toLowerCase();
                    Goal newG;

                    if (checked) {
                        CharSequence txt = cb.getText();
                        newG = new Goal(txt.toString(), true);
                    } else {
                        CharSequence txt = cb.getText();
                        newG = new Goal(txt.toString(), false);
                    }
                    dayData.putGoal(newG);

                    //test checkbox listeners
                    System.out.println("Box clicked");
                    System.out.println(dayData.getGoal(newG.getName()).getName());
                    System.out.println("New value:" + dayData.getGoal(newG.getName()).getCompleted());
                }
            });
        }
    }
}