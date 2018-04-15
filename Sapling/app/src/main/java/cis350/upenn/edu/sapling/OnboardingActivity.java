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

    HashSet<String> metricStrings = new HashSet<String>();
    HashSet<String> goalStrings = new HashSet<String>();


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
            metricStrings.add(s);
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
            goalStrings.add(s);
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
        } else if (currState == 1) {
            metricsLayout.startAnimation(out);

            //save metrics for settings
            String metric1 = ((EditText) findViewById(R.id.metrics_input1)).getText().toString();
            String metric2 = ((EditText) findViewById(R.id.metrics_input2)).getText().toString();
            String metric3 = ((EditText) findViewById(R.id.metrics_input3)).getText().toString();
            String metric4 = ((EditText) findViewById(R.id.metrics_input4)).getText().toString();
            Log.v("Metrics entered are ", metric1 + " " + metric2 + " " + metric3 + " " + metric4);

            Map<String, Metric> activeMetrics = dm.getActiveMetrics(getApplicationContext());

            if (metric1.length() > 0 && !activeMetrics.containsKey(metric1)) {
                dm.addModelMetric(metric1, true, getApplicationContext());
            } else {
                dm.deprecateModelMetric(metric1, true, getApplicationContext());
            }
            if (metric2.length() > 0 && !activeMetrics.containsKey(metric2)) {
                dm.addModelMetric(metric2, true, getApplicationContext());
            } else {
                dm.deprecateModelMetric(metric2, true, getApplicationContext());
            }
            if (metric3.length() > 0 && !activeMetrics.containsKey(metric3)) {
                dm.addModelMetric(metric3, true, getApplicationContext());
            } else {
                dm.deprecateModelMetric(metric3, true, getApplicationContext());
            }
            if (metric4.length() > 0 && !activeMetrics.containsKey(metric4)) {
                dm.addModelMetric(metric4, true, getApplicationContext());
            } else {
                dm.deprecateModelMetric(metric4, true, getApplicationContext());
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
                dm.addModelGoal(habit1, getApplicationContext());
            } else {
                dm.deprecateModelGoal(habit1, getApplicationContext());
            }
            if (habit2.length() > 0 && !activeGoals.contains(habit2)) {
                dm.addModelGoal(habit2, getApplicationContext());
            } else {
                dm.deprecateModelGoal(habit2, getApplicationContext());
            }
            if (habit3.length() > 0 && !activeGoals.contains(habit3)) {
                dm.addModelGoal(habit3, getApplicationContext());
            } else {
                dm.deprecateModelGoal(habit3, getApplicationContext());
            }
            if (habit4.length() > 0 && !activeGoals.contains(habit4)) {
                dm.addModelGoal(habit4, getApplicationContext());
            } else {
                dm.deprecateModelGoal(habit4, getApplicationContext());
            }


            //return to main activity
//            Intent intent = new Intent();
//            setResult(RESULT_OK, intent);
            Intent showCaseIntent = new Intent(this, ShowcaseActivity.class);
            startActivityForResult(showCaseIntent, 5);
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            //save habits for settings
            finish();
        }

    }


}
