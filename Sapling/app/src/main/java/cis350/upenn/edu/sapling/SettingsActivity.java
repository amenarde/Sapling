package cis350.upenn.edu.sapling;

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
    public void resetAll(View view){
        dm.purgeFiles(getApplicationContext());
    }
    public void resetToday(View view){
        Date today = new Date();
        dm.removeDayData(today,getApplicationContext());
    }
    private void setWelcome() {
        String message = "Hi " + dm.getUsername() + ",";
        TextView display = findViewById(R.id.message_display);
        display.setText(message);
    }
}
