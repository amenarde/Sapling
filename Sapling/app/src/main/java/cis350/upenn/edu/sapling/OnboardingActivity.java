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
import java.util.Map;
import java.util.Set;

/**
 * Created by Tiffany_Yue on 2/23/18.
 */

public class OnboardingActivity extends AppCompatActivity {

    int currState = 0;
    final Animation in = new AlphaAnimation(0.0f, 1.0f);
    final Animation out = new AlphaAnimation(1.0f, 0.0f);
    ConstraintLayout nameLayout;
    ConstraintLayout metricsLayout;
    ConstraintLayout habitsLayout;


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
        int num = 1;
        String metric1="";
        String metric2="";
        String metric3="";
        String metric4="";
        for (String s : activeMetrics.keySet()) {
            Metric m = activeMetrics.get(s);
            System.out.println("active m: " + m.getName() + " " + m.getPositive());
            if (num == 1) {
                metric1 = m.getName();
            } else if (num == 2) {
                metric2 = m.getName();
            } else if (num == 3) {
                metric3 = m.getName();
            } else {
                metric4 = m.getName();
            }
            num++;
        }

        ((EditText) findViewById(R.id.metrics_input1)).setText(metric1);
        ((EditText) findViewById(R.id.metrics_input2)).setText(metric2);
        ((EditText) findViewById(R.id.metrics_input3)).setText(metric3);
        ((EditText) findViewById(R.id.metrics_input4)).setText(metric4);

        Set<String> activeGoals = dm.getActiveGoals(getApplicationContext());
        System.out.println("active goal size : " + activeGoals.size());
        int count = 1;
        String goal1 = "";
        String goal2 = "";
        String goal3 = "";
        String goal4 = "";
        for (String s : activeGoals) {
            System.out.println("active g: " + s);
            if (count == 1) {
                goal1 = s;
            } else if (count == 2) {
                goal2 = s;
            } else if (count == 3) {
                goal3 = s;
            } else {
                goal4 = s;
            }
            count++;
        }
        ((EditText) findViewById(R.id.habits_input1)).setText(goal1);
        ((EditText) findViewById(R.id.habits_input2)).setText(goal2);
        ((EditText) findViewById(R.id.habits_input3)).setText(goal3);
        ((EditText) findViewById(R.id.habits_input4)).setText(goal4);


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

            DataManager dm = DataManager.getInstance();

            Map<String, Metric> activeMetrics = dm.getActiveMetrics(getApplicationContext());
            int i = 1;
            for (String m : activeMetrics.keySet()) {
                if (i == 1 && !m.equals(metric1) && metric1.length() != 0) {
                    dm.deprecateModelMetric(m, true, getApplicationContext());
                    dm.addModelMetric(metric1, true, getApplicationContext());
                }
                if (i == 2 && !m.equals(metric2) && metric1.length() != 0) {
                    dm.deprecateModelMetric(m, true, getApplicationContext());
                    dm.addModelMetric(metric2, true, getApplicationContext());
                }
                if (i == 3 && !m.equals(metric3) && metric1.length() != 0) {
                    dm.deprecateModelMetric(m, true, getApplicationContext());
                    dm.addModelMetric(metric3, true, getApplicationContext());
                }
                if (i == 4 && !m.equals(metric4) && metric1.length() != 0) {
                    dm.deprecateModelMetric(m, true, getApplicationContext());
                    dm.addModelMetric(metric4, true, getApplicationContext());;
                }
                i++;
            }



            /*
            // test code for DataManager
            dm.addModelMetric("Productivity", true, getApplicationContext());
            dm.addModelMetric("Laziness", false, getApplicationContext());
            dm.addModelGoal("Eat an Apple", getApplicationContext());
            dm.addModelGoal("Go to the gym", getApplicationContext());
            Map<String, Metric> activeMetrics = dm.getActiveMetrics(getApplicationContext());
            System.out.println("active metric size : " + activeMetrics.size());
            for (String s : activeMetrics.keySet()) {
                Metric m = activeMetrics.get(s);
                System.out.println("active m: " + m.getName() + " " + m.getPositive());
            }
            System.out.println("active goals size: " + dm.getActiveGoals(getApplicationContext()).size());
            */

        } else {


            //save habits for settings
            String habit1 = ((EditText) findViewById(R.id.habits_input1)).getText().toString();
            String habit2 = ((EditText) findViewById(R.id.habits_input2)).getText().toString();
            String habit3 = ((EditText) findViewById(R.id.habits_input3)).getText().toString();
            String habit4 = ((EditText) findViewById(R.id.habits_input4)).getText().toString();

            Log.v("Habits entered are ", habit1 + " " + habit2 + " " + habit3 + " " + habit4);

            DataManager dm = DataManager.getInstance();

            Set<String> activeGoals = dm.getActiveGoals(getApplicationContext());
            int i = 1;
            for (String m : activeGoals) {
                if (i == 1 && !m.equals(habit1) && habit1.length() != 0) {
                    dm.deprecateModelGoal(m, getApplicationContext());
                    dm.addModelGoal(habit1, getApplicationContext());
                }
                if (i == 2 && !m.equals(habit2) && habit1.length() != 0) {
                    dm.deprecateModelGoal(m, getApplicationContext());
                    dm.addModelGoal(habit2, getApplicationContext());
                }
                if (i == 3 && !m.equals(habit3) && habit1.length() != 0) {
                    dm.deprecateModelGoal(m, getApplicationContext());
                    dm.addModelGoal(habit3, getApplicationContext());
                }
                if (i == 4 && !m.equals(habit4) && habit4.length() != 0) {
                    dm.deprecateModelGoal(m, getApplicationContext());
                    dm.addModelGoal(habit4, getApplicationContext());
                }
                i++;
            }
            

            //return to main activity
            Intent i = new Intent();
            setResult(RESULT_OK, i);
            //save habits for settings
            finish();
        }

    }


}
