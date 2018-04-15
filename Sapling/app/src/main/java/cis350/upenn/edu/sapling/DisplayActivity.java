package cis350.upenn.edu.sapling;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.Date;
import java.util.Iterator;

public class DisplayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
    }

    @Override
    protected void onStart() {
        super.onStart();

        DataManager dm = DataManager.getInstance();
        Iterator<DayData> pastWeek = dm.getLastWeek(new Date(), this.getApplicationContext());

        DataPoint[] points = new DataPoint[7];
        int dayInWeek = 7;
        while (pastWeek.hasNext()) {
            double totalNum = 0;
            DayData dayData = pastWeek.next();
            dayInWeek -= 1;

            Iterator<Metric> metrics = dayData.getAllMetrics().iterator();
            int numMetrics = 0;
            while(metrics.hasNext()) {
                Metric m = metrics.next();
                numMetrics++;
                if (m.getPositive()){
                    totalNum += m.getRating();
                } else {
                    totalNum += (7 - m.getRating());
                }
                Log.v("Main Activity:", "Day " + dayInWeek + " found with metric " + numMetrics + ", " + m.getName() + " with value " + m.getRating());
            }
            Log.v("Main Activity", "total num calculated is " + totalNum);
            double val = totalNum/numMetrics;

            //assign to proper day
            points[dayInWeek] = new DataPoint(dayInWeek + 1, val);
        }

        GraphView graph = (GraphView) findViewById(R.id.graph);
        graph.getViewport().setMinX(1);
        graph.getViewport().setMaxX(7);
        graph.getViewport().setMinY(0.0);
        graph.getViewport().setMaxY(7.0);
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setXAxisBoundsManual(true);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(points);
        series.setColor(Color.WHITE);
        series.setDrawDataPoints(true);
        series.setDataPointsRadius(10);
        series.setThickness(8);
        graph.removeAllSeries();
        graph.addSeries(series);

    }
}
