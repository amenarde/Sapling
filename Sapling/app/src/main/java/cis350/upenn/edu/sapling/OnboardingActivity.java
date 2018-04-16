package cis350.upenn.edu.sapling;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.EditText;
import android.view.animation.Animation;
import android.view.animation.AlphaAnimation;
import android.support.constraint.ConstraintLayout;
import android.content.SharedPreferences;
import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Collection;
import java.util.stream.Collector;

/**
 * Created by Tiffany_Yue on 2/23/18.
 */

public class OnboardingActivity extends AppCompatActivity {

    // the different steps in the onboarding process are "states" - these are incremented and fade in/
    // out layouts as necessary.
    int currState = 0;
    final Animation in = new AlphaAnimation(0.0f, 1.0f);
    final Animation out = new AlphaAnimation(1.0f, 0.0f);
    ConstraintLayout nameLayout;
    ConstraintLayout metricsLayout;
    ConstraintLayout habitsLayout;

    HashSet<String> newMetrics = new HashSet<String>();
    HashSet<String> newGoals = new HashSet<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        //locate views necessary
        nameLayout = findViewById(R.id.name_layout);
        //set the default metrics
        metricsLayout = findViewById(R.id.metrics_layout);

        DataManager dm = DataManager.getInstance();
        try {
            dm.addDefaultGoals(getApplicationContext());
            dm.addDefaultMetrics(getApplicationContext());
        } catch (IOException e) {

        }

        Map<String, Metric> activeMetrics = dm.getActiveMetrics(getApplicationContext());
        System.out.println("active metric size : " + activeMetrics.size());

        int metricI = 1;
        for (String s : activeMetrics.keySet()) {
            if (metricI == 1) {
                ((EditText) findViewById(R.id.metrics_input1)).setText(s);
            } else if (metricI == 2) {
                ((EditText) findViewById(R.id.metrics_input2)).setText(s);
            } else if (metricI == 3) {
                ((EditText) findViewById(R.id.metrics_input3)).setText(s);
            } else if (metricI == 4) {
                ((EditText) findViewById(R.id.metrics_input4)).setText(s);
            }
            metricI++;
        }

        Set<String> activeGoals = dm.getActiveGoals(getApplicationContext());
        System.out.println("active goal size : " + activeGoals.size());


        int goalI = 1;
        for (String s : activeGoals) {
            if (goalI == 1) {
                ((EditText) findViewById(R.id.habits_input1)).setText(s);
            } else if (goalI == 2) {
                ((EditText) findViewById(R.id.habits_input2)).setText(s);
            } else if (goalI == 3) {
                ((EditText) findViewById(R.id.habits_input3)).setText(s);
            } else if (goalI == 4) {
                ((EditText) findViewById(R.id.habits_input4)).setText(s);
            }
            goalI++;
        }

        metricsLayout.setVisibility(View.INVISIBLE);
        habitsLayout = findViewById(R.id.habits_layout);
        habitsLayout.setVisibility(View.INVISIBLE);


        in.setDuration(350);

        out.setDuration(350);
        out.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (currState == 0) {
                    nameLayout.setAlpha(0.0f);
                    metricsLayout.setVisibility(View.VISIBLE);
                    metricsLayout.startAnimation(in);
                    currState++;
                } else if (currState == 1) {
                    metricsLayout.setAlpha(0.0f);
                    habitsLayout.setVisibility(View.VISIBLE);
                    habitsLayout.startAnimation(in);
                    currState++;
                } else if (currState == 2) {
                    habitsLayout.setAlpha(0.0f);
                    currState++;
                }

            }
        });

    }

    public void onboardingNext(View view) {
        DataManager dm = DataManager.getInstance();

        if (currState == 0) {
            nameLayout.startAnimation(out);
            //save name for settings
            String name = ((EditText) findViewById(R.id.name_input)).getText().toString();
            Log.v("Name entered is ", name);
            try {
                System.out.println("ADDDING A FUCKING NAME");
                dm.addUsername(name, this.getApplicationContext());
            } catch (Exception e) { e.printStackTrace(); }
        } else if (currState == 1) {
            metricsLayout.startAnimation(out);

            //save metrics for settings
            String metric1 = ((EditText) findViewById(R.id.metrics_input1)).getText().toString().toLowerCase();
            String metric2 = ((EditText) findViewById(R.id.metrics_input2)).getText().toString().toLowerCase();
            String metric3 = ((EditText) findViewById(R.id.metrics_input3)).getText().toString().toLowerCase();
            String metric4 = ((EditText) findViewById(R.id.metrics_input4)).getText().toString().toLowerCase();
            Log.v("Metrics entered are ", metric1 + " " + metric2 + " " + metric3 + " " + metric4);

            Map<String, Metric> activeMetrics = dm.getActiveMetrics(getApplicationContext());

            for (String s : activeMetrics.keySet()) {
                System.out.println("active metric is " + s);
            }

            //add all the new metrics
            //deprecate old ones that aren't contained in the new set


            System.out.println("does activemetrics containKey " + metric1  + ": " + activeMetrics.containsKey(metric1));
            System.out.println("does activemetrics containKey " + metric2  + ": " + activeMetrics.containsKey(metric2));
            System.out.println("does activemetrics containKey " + metric3  + ": " + activeMetrics.containsKey(metric3));
            System.out.println("does activemetrics containKey " + metric4  + ": " + activeMetrics.containsKey(metric4));

            if (metric1.length() > 0 && !activeMetrics.containsKey(metric1)) {
                dm.addModelMetric(metric1, true, getApplicationContext());
                newMetrics.add(metric1);
            } else if (metric1.length() > 0) {
                newMetrics.add(metric1);
            }

            if (metric2.length() > 0 && !activeMetrics.containsKey(metric2)) {
                dm.addModelMetric(metric2, true, getApplicationContext());
                newMetrics.add(metric2);
            } else if (metric2.length() > 0) {
                newMetrics.add(metric2);
            }
            if (metric3.length() > 0 && !activeMetrics.containsKey(metric3)) {
                dm.addModelMetric(metric3, true, getApplicationContext());
                newMetrics.add(metric3);
            } else if (metric3.length() > 0) {
                newMetrics.add(metric3);
            }
            if (metric4.length() > 0 && !activeMetrics.containsKey(metric4)) {
                dm.addModelMetric(metric4, true, getApplicationContext());
                newMetrics.add(metric4);
            } else if (metric4.length() > 0) {
                newMetrics.add(metric4);
            }

            activeMetrics = dm.getActiveMetrics(getApplicationContext());

            HashSet<String> activeMetricsSet = new HashSet<String>();

            for (Metric m : activeMetrics.values()) {
                activeMetricsSet.add(m.getName().toLowerCase());
            }
            for (String m : activeMetricsSet) {
                if (!newMetrics.contains(m.toLowerCase())) {
                    dm.deprecateModelMetric(m.toLowerCase(), true, getApplicationContext());
                }
            }

        } else {


            //save habits for settings
            String habit1 = ((EditText) findViewById(R.id.habits_input1)).getText().toString();
            String habit2 = ((EditText) findViewById(R.id.habits_input2)).getText().toString();
            String habit3 = ((EditText) findViewById(R.id.habits_input3)).getText().toString();
            String habit4 = ((EditText) findViewById(R.id.habits_input4)).getText().toString();

            Log.v("Habits entered are ", habit1 + " " + habit2 + " " + habit3 + " " + habit4);

            Set<String> activeGoals = dm.getActiveGoals(getApplicationContext());

            if (habit1.length() > 0 && !activeGoals.contains(habit1)) {
                dm.addModelMetric(habit1, true, getApplicationContext());
                newGoals.add(habit1);
            } else if (habit1.length() > 0) {
                newGoals.add(habit1);
            }
            if (habit2.length() > 0 && !activeGoals.contains(habit2)) {
                dm.addModelMetric(habit2, true, getApplicationContext());
                newGoals.add(habit2);
            } else if (habit2.length() > 0) {
                newGoals.add(habit2);
            }
            if (habit3.length() > 0 && !activeGoals.contains(habit3)) {
                dm.addModelMetric(habit3, true, getApplicationContext());
                newGoals.add(habit3);
            } else if (habit3.length() > 0) {
                newGoals.add(habit3);
            }
            if (habit4.length() > 0 && !activeGoals.contains(habit4)) {
                dm.addModelMetric(habit4, true, getApplicationContext());
                newGoals.add(habit4);
            } else if (habit4.length() > 0) {
                newGoals.add(habit4);
            }
            activeGoals = dm.getActiveGoals(getApplicationContext());

            HashSet<String> activeGoalsSet = new HashSet<String>();

            for (String g : activeGoals) {
                activeGoalsSet.add(g);
            }
            for (String s : activeGoalsSet) {
                if (!newGoals.contains(s)) {
                    dm.deprecateModelGoal(s, getApplicationContext());
                }
            }

            //return to main activity
            Intent showCaseIntent = new Intent(this, ShowcaseActivity.class);
            startActivityForResult(showCaseIntent, 5);
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            //save habits for settings
            finish();
        }

    }


}
