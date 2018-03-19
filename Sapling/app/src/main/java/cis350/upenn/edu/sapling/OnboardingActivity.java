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
        metricsLayout = findViewById(R.id.metrics_layout);
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

            //DataManager dm = DataManager.getInstance();
            /*
            if (metric1.length() > 0) {
                dm.addMetric(metric1);
            }
            if (metric2.length() > 0) {
                dm.addMetric(metric2);
            }
            if (metric3.length() > 0) {
                dm.addMetric(metric3);
            }
            if (metric4.length() > 0) {
                dm.addMetric(metric4);
            }*/


        } else {


            //save habits for settings
            String habit1 = ((EditText) findViewById(R.id.habits_input1)).getText().toString();
            String habit2 = ((EditText) findViewById(R.id.habits_input2)).getText().toString();
            String habit3 = ((EditText) findViewById(R.id.habits_input3)).getText().toString();
            String habit4 = ((EditText) findViewById(R.id.habits_input4)).getText().toString();

            /*
            if (habit1.length() > 0) {
                dm.addGoal(habit1);
            }
            if (habit2.length() > 0) {
                dm.addGoal(habit2);
            }
            if (habit3.length() > 0) {
                dm.addGoal(habit3);
            }
            if (habit4.length() > 0) {
                dm.addGoal(habit4);
            }*/

            //return to main activity
            Intent i = new Intent();
            setResult(RESULT_OK, i);
            //save habits for settings
            finish();
        }

    }


}
