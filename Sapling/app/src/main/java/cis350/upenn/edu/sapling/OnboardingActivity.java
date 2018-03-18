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

        in.setDuration(1500);

        out.setDuration(1500);
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
        } else if (currState == 1) {
            metricsLayout.startAnimation(out);
        } else {
            Intent i = new Intent();
            setResult(RESULT_OK, i);
            finish();
        }

    }


}
