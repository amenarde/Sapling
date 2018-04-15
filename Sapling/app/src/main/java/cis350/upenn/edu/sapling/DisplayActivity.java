package cis350.upenn.edu.sapling;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

public class DisplayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        fillHeatMap(new Date());

    }


    private void fillHeatMap(Date endDate) {
        GridView gameField = (GridView) findViewById(R.id.heatmap);
        gameField.setNumColumns(8);

        Set<String> goals = DataManager.getInstance().getActiveGoals(this);
        String[] names = goals.toArray(new String[goals.size()]);

        boolean[][] data = new boolean[names.length][7];
        Iterator<DayData> week = DataManager.getInstance().getLastWeek(endDate, this);
        int y = 6;

        while (week.hasNext()) {
            DayData dd = week.next();

            for (int x = 0; x < names.length; x++) {
                if (dd.hasGoal(names[x])) {
                    data[x][y] = dd.getGoal(names[x]).getCompleted();
                }
                else {
                    data[x][y] = false;
                }
            }

            y--;
            if (y == -1) {
                break;
            }
        }


        HeatMapAdapter adapter = new HeatMapAdapter(this, data, names);
        gameField.setAdapter(adapter);
    }
}
