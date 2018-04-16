package cis350.upenn.edu.sapling;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import java.util.Collection;

public class DisplayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        fillHeatMap(new Date());

    }


    private void fillHeatMap(Date endDate) {
        GridView heatMap = (GridView) findViewById(R.id.heatmap);
        ListView namesList = (ListView) findViewById(R.id.goal_names);

        heatMap.setNumColumns(8);

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


        GoalNamesAdapter goalAdapter = new GoalNamesAdapter(this, names);
        HeatMapAdapter heatMapAdapter = new HeatMapAdapter(this, data);

        namesList.setAdapter(goalAdapter);
        heatMap.setAdapter(heatMapAdapter);
    }

    class GoalNamesAdapter extends BaseAdapter {

        String[] names;
        Context context;

        public GoalNamesAdapter(Context context, String[] names) {
            this.names = names;
            this.context = context;
        }

        @Override
        public int getCount() {
            return names.length;
        }

        @Override
        public Object getItem(int i) {
            return names[i];
        }

        @Override
        public long getItemId(int i) {
            return 0; //unused
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            TextView viewItem = new TextView(context);
            String name = (String)getItem(i);

            viewItem.setBackgroundColor(Color.WHITE);
            viewItem.setText(name);

            return viewItem;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        DataManager dm = DataManager.getInstance();

        ///TIFFANY'S GRAPH CODE
        Iterator<DayData> pastWeek;
        DataPoint[] pointsM1 = new DataPoint[7];
        DataPoint[] pointsM2 = new DataPoint[7];
        DataPoint[] pointsM3 = new DataPoint[7];
        DataPoint[] pointsM4 = new DataPoint[7];


        //get the set of all metrics
        //if active, iterate through the week's data and add to series

        Collection<Metric> metrics = dm.getDay(new Date(), getApplicationContext()).getAllMetrics();
        System.out.println("num metrics is " + metrics.size());
        int metricCount = 0;
        for (Metric m : metrics) {
            pastWeek = dm.getLastWeek(new Date(), this.getApplicationContext());
            int dayInWeek = 7;
            while (pastWeek.hasNext()) {

                DayData dayData = pastWeek.next();
                dayInWeek -= 1;
                if (dayData.getMetric(m.getName().toLowerCase()).getRating() != -1) {
                    //assign to proper day
                    if (metricCount == 0) {
                        pointsM1[dayInWeek] = new DataPoint(dayInWeek + 1, dayData.getMetric(m.getName().toLowerCase()).getRating());
                    } else if (metricCount == 1) {
                        pointsM2[dayInWeek] = new DataPoint(dayInWeek + 1, dayData.getMetric(m.getName().toLowerCase()).getRating());
                    } else if (metricCount == 2) {
                        pointsM3[dayInWeek] = new DataPoint(dayInWeek + 1, dayData.getMetric(m.getName().toLowerCase()).getRating());
                    } else if (metricCount == 3) {
                        pointsM4[dayInWeek] = new DataPoint(dayInWeek + 1, dayData.getMetric(m.getName().toLowerCase()).getRating());
                    }
                } else {
                    //assign to proper day
                    if (metricCount == 0) {
                        pointsM1[dayInWeek] = new DataPoint(dayInWeek + 1, 0);
                    } else if (metricCount == 1) {
                        pointsM2[dayInWeek] = new DataPoint(dayInWeek + 1, 0);
                    } else if (metricCount == 2) {
                        pointsM3[dayInWeek] = new DataPoint(dayInWeek + 1, 0);
                    } else if (metricCount == 3) {
                        pointsM4[dayInWeek] = new DataPoint(dayInWeek + 1, 0);
                    }
                }
            }

            metricCount++;
        }


        GraphView graph = (GraphView) findViewById(R.id.graph);
        graph.getViewport().setMinX(1);
        graph.getViewport().setMaxX(7);
        graph.getViewport().setMinY(0.0);
        graph.getViewport().setMaxY(7.0);
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setXAxisBoundsManual(true);

        LineGraphSeries<DataPoint> series1 = new LineGraphSeries<>(pointsM1);
        LineGraphSeries<DataPoint> series2 = new LineGraphSeries<>(pointsM2);
        LineGraphSeries<DataPoint> series3 = new LineGraphSeries<>(pointsM3);
        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(pointsM4);

        series1.setColor(Color.WHITE);
        series1.setDrawDataPoints(true);
        series1.setDataPointsRadius(10);
        series1.setThickness(8);
        series2.setColor(Color.rgb(0, 204, 136));
        series2.setDrawDataPoints(true);
        series2.setDataPointsRadius(10);
        series2.setThickness(8);
        series3.setColor(Color.rgb(128, 255, 212));
        series3.setDrawDataPoints(true);
        series3.setDataPointsRadius(10);
        series3.setThickness(8);
        series4.setColor(Color.rgb(0, 77, 51));
        series4.setDrawDataPoints(true);
        series4.setDataPointsRadius(10);
        series4.setThickness(8);


        graph.removeAllSeries();
        graph.addSeries(series1);
        graph.addSeries(series2);
        graph.addSeries(series3);
        graph.addSeries(series4);

    }
}
