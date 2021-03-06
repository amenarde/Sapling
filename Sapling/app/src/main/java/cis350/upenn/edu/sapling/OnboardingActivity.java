package cis350.upenn.edu.sapling;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;
import android.view.animation.Animation;
import android.view.animation.AlphaAnimation;
import android.support.constraint.ConstraintLayout;
import android.content.SharedPreferences;
import android.content.Context;
import android.util.Log;

import org.honorato.multistatetogglebutton.MultiStateToggleButton;
import org.honorato.multistatetogglebutton.ToggleButton;

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
    boolean runGuide = true;
    ConstraintLayout nameLayout;
    ConstraintLayout metricsLayout;
    ConstraintLayout habitsLayout;

    HashSet<String> newMetrics = new HashSet<String>();
    HashSet<String> newGoals = new HashSet<String>();
    MultiStateToggleButton[] toggleArray = new  MultiStateToggleButton[4];
    HashSet<MultiStateToggleButton> toggleButtons = new HashSet<MultiStateToggleButton>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);
        int requestCode = getIntent().getIntExtra("requestCode",1);
        if (requestCode == 2) {runGuide = false;}
        //locate views necessary
        nameLayout = findViewById(R.id.name_layout);
        //set the default metrics
        metricsLayout = findViewById(R.id.metrics_layout);

        loadToggleButtons();
        setToggleDefaults();

        Boolean calledInApp = getIntent().getBooleanExtra("calledInApp", false);
        runGuide = !calledInApp;

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

    private void loadToggleButtons() {
        MultiStateToggleButton button1 = (MultiStateToggleButton) this.findViewById(R.id.mstb_1);
        MultiStateToggleButton button2 = (MultiStateToggleButton) this.findViewById(R.id.mstb_2);
        MultiStateToggleButton button3 = (MultiStateToggleButton) this.findViewById(R.id.mstb_3);
        MultiStateToggleButton button4 = (MultiStateToggleButton) this.findViewById(R.id.mstb_4);

        button1.setOnValueChangedListener(new ToggleButton.OnValueChangedListener() {
            @Override
            public void onValueChanged(int position) {
                System.out.println("Button 1 : "  + position);
            }
        });
        button2.setOnValueChangedListener(new ToggleButton.OnValueChangedListener() {
            @Override
            public void onValueChanged(int position) {
                System.out.println("Button 2 : "  + position);
            }
        });
        button3.setOnValueChangedListener(new ToggleButton.OnValueChangedListener() {
            @Override
            public void onValueChanged(int position) {
                System.out.println("Button 3 : "  + position);
            }
        });
        button4.setOnValueChangedListener(new ToggleButton.OnValueChangedListener() {
            @Override
            public void onValueChanged(int position) {
                System.out.println("Button 4 : "  + position);
            }
        });

        toggleArray[0] = button1;
        toggleArray[1] = button2;
        toggleArray[2] = button3;
        toggleArray[3] = button4;
    }

    private void setToggleDefaults() {
        for (int i = 0; i < 4; i++) {
            MultiStateToggleButton button = toggleArray[i];
            button.enableMultipleChoice(false);
            if (i != 1) {
                button.setValue(0);
            } else {
                button.setValue(1);
            }
        }
    }

    public void onboardingNext(View view) {
        DataManager dm = DataManager.getInstance();

        if (currState == 0) {
            nameLayout.startAnimation(out);
            //save name for settings
            String name = ((EditText) findViewById(R.id.name_input)).getText().toString();
            Log.v("Name entered is ", name);
            try {
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
                boolean positive = (toggleArray[0].getValue() == 0);
                dm.addModelMetric(metric1, positive, getApplicationContext());
                newMetrics.add(metric1);

            } else if (metric1.length() > 0) {
                newMetrics.add(metric1);
            }

            if (metric2.length() > 0 && !activeMetrics.containsKey(metric2)) {
                boolean positive = (toggleArray[1].getValue() == 0);
                dm.addModelMetric(metric2, positive, getApplicationContext());
                newMetrics.add(metric2);

            } else if (metric2.length() > 0) {
                newMetrics.add(metric2);
            }

            if (metric3.length() > 0 && !activeMetrics.containsKey(metric3)) {
                boolean positive = (toggleArray[2].getValue() == 0);
                dm.addModelMetric(metric3, positive, getApplicationContext());
                newMetrics.add(metric3);
            } else if (metric3.length() > 0) {
                newMetrics.add(metric3);
            }

            if (metric4.length() > 0 && !activeMetrics.containsKey(metric4)) {
                boolean positive = (toggleArray[3].getValue() == 0);
                dm.addModelMetric(metric4, positive, getApplicationContext());
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
                dm.addModelGoal(habit1, getApplicationContext());
                newGoals.add(habit1);
            } else if (habit1.length() > 0) {
                newGoals.add(habit1);
            }
            if (habit2.length() > 0 && !activeGoals.contains(habit2)) {
                dm.addModelGoal(habit2, getApplicationContext());
                newGoals.add(habit2);
            } else if (habit2.length() > 0) {
                newGoals.add(habit2);
            }
            if (habit3.length() > 0 && !activeGoals.contains(habit3)) {
                dm.addModelGoal(habit3, getApplicationContext());
                newGoals.add(habit3);
            } else if (habit3.length() > 0) {
                newGoals.add(habit3);
            }
            if (habit4.length() > 0 && !activeGoals.contains(habit4)) {
                dm.addModelGoal(habit4, getApplicationContext());
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

            if (runGuide) {
                Intent showCaseIntent = new Intent(this, ShowcaseActivity.class);
                startActivityForResult(showCaseIntent, 1);
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                //save habits for settings
                finish();
            } else {
                startMainActivity(view);
            }
        }

    }
    private void startMainActivity(View view){
        Intent i = new Intent(this, MainActivity.class);
        i.putExtra("calledInApp", true);
        startActivityForResult(i, 2);
    }
}
