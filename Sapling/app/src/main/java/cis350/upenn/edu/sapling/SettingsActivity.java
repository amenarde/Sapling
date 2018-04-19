package cis350.upenn.edu.sapling;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.Date;

public class SettingsActivity extends AppCompatActivity {
private DataManager dm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        this.dm = DataManager.getInstance();
        setWelcome();
    }

    @Override
    protected void onStart() {
        super.onStart();
        setContentView(R.layout.activity_settings);
        this.dm = DataManager.getInstance();
        setWelcome();
    }


    public void resetAll(View view){
        dm.purgeFiles(getApplicationContext());
        startOnboarding(view);
    }
    public void resetToday(View view){
        Date today = new Date();
        dm.removeDayData(today,getApplicationContext());
        startLogging(view);
    }
    private void setWelcome() {
        String message = "Hi " + dm.getUsername() + ",";
        TextView display = findViewById(R.id.message_display);
        display.setText(message);
    }
    private void startOnboarding(View view){
        Intent i = new Intent(this, OnboardingActivity.class);
        i.putExtra("requestCode", 2);
        i.putExtra("calledInApp", true);
        startActivityForResult(i, 2);
    }
    private void startLogging(View view){
        Intent i = new Intent(this, LoggingActivity.class);
        startActivityForResult(i, 1);
    }
}
