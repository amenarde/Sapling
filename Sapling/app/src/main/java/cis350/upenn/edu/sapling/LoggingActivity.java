package cis350.upenn.edu.sapling;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.provider.ContactsContract;
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

    DataManager dm = DataManager.getInstance();
    while(itMetrics.hasNext()) {
        Metric m = itMetrics.next();

        if (dm.getActiveMetrics(getApplicationContext()).containsKey(m.getName().toLowerCase())) {
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
            seekBar.setMinimumWidth(1250);
            seekBar.setThumb(thumb);
            if (m.hasScale()) {
                System.out.println("SCALE IS : " + m.getRating());
                seekBar.setProgress(m.getRating() - 1);
            } else {
                System.out.println("SCALE DOES NOT EXIST");
                seekBar.setProgress(0);
            }
            seekBar.setVisibility(View.VISIBLE);

            metric_labels.addView(textV);
            metric_labels.addView(seekBar);
            metric_labels.addView(seekBar.tracker);

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
                    System.out.println("PROGRESS CHANGES to " + (progress + 1));
                    MetricScale sk = (MetricScale) seekBar;
                    TextView textView = sk.tracker;

                    //move tracker
                    int val = (progress * (seekBar.getWidth() - 2 * seekBar.getThumbOffset())) / seekBar.getMax();
                    textView.setText("" + (progress + 1));
                    textView.setX(seekBar.getX() + val + seekBar.getThumbOffset() / 2);

                    //update DayData
                    Metric newM = new Metric(sk.getName().toLowerCase(), new Scale(progress + 1), sk.getPositive());
                    dayData.putMetric(newM);
                    dataManager.putDay(new Date(),dayData,getApplicationContext());


                }
            });
        }


    }
}
    private void voidPopulateCheckBoxes(final DayData dayData) {
        Iterator<Goal> itGoals = dayData.getAllGoals().iterator();
        DataManager dm = DataManager.getInstance();

        while (itGoals.hasNext()) {
            Goal g = itGoals.next();
            System.out.println("Goal :" + g.getName() + "completed on open? " + g.getCompleted());

            LinearLayout goal_labels = findViewById(R.id.goals_view_list);
            String input = g.getName();
            String formattedText = input.substring(0, 1).toUpperCase() + input.substring(1);

            if (dm.getActiveGoals(getApplicationContext()).contains(g.getName().toLowerCase())) {
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

        }
    }

    public void submit(View view) {
        //saves inputs and returns to mainActivity
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }
}