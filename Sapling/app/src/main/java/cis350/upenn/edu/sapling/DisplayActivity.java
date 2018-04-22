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

import java.text.SimpleDateFormat;
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
import java.util.HashSet;
import java.util.HashMap;

public class DisplayActivity extends AppCompatActivity {

    HashSet<String> displayedMetrics;
    Collection<Metric> allActiveMetrics;
    HashMap<String, Integer> legend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        DataManager dm = DataManager.getInstance();
        allActiveMetrics = dm.getDay(new Date(), getApplicationContext()).getAllMetrics();

    }

    @Override
    protected void onStart() {
        super.onStart();

        displayedMetrics = new HashSet<String>();
        for (Metric m : allActiveMetrics) {
            displayedMetrics.add(m.getName().toLowerCase());
        }
        legend = new HashMap<String, Integer>();

        fillHeatMap(new Date());
        fillGraph();
        fillCheckList();

    }


    private void fillCheckList() {
        ListView checklist = (ListView) findViewById(R.id.checklist);

        String[] names = new String[allActiveMetrics.size()];
        int i = 0;
        for (Metric m : allActiveMetrics) {
            names[i] = m.getName();
            i++;
        }

        Boolean[] checked = new Boolean[names.length];
        for (i = 0; i < checked.length; i++) {
            checked[i] = true;
        }

        int[] colors = new int[names.length];
        for (i = 0; i < checked.length; i++) {
            colors[i] = legend.get(names[i]);
        }

        ChecklistAdapter cla = new ChecklistAdapter(this.getApplicationContext(),
                                                    names, checked, this, colors);
        checklist.setAdapter(cla);
    }

    private void fillHeatMap(Date endDate) {


        GridView heatMap = (GridView) findViewById(R.id.heatmap);
        ListView namesList = (ListView) findViewById(R.id.goal_names);

        heatMap.setNumColumns(7);

        Set<String> goals = DataManager.getInstance().getActiveGoals(this);
        String[] names = goals.toArray(new String[goals.size()]);

        boolean[][] data = new boolean[names.length][7];
        Iterator<DayData> week = DataManager.getInstance().getLastWeek(endDate, this);
        int y = 0;

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

            y++;
            if (y == 7) {
                break;
            }
        }


        GoalNamesAdapter goalAdapter = new GoalNamesAdapter(this, names);
        HeatMapAdapter heatMapAdapter = new HeatMapAdapter(this, data, endDate);

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
            return names.length + 1;
        }

        @Override
        public Object getItem(int i) {
            return names[i-1];
        }

        @Override
        public long getItemId(int i) {
            return 0; //unused
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            if (i == 0) {
                TextView viewItem = new TextView(context);
                return viewItem;
            }

            TextView viewItem = new TextView(context);
            String name = (String)getItem(i);

            viewItem.setTextColor(context.getResources().getColor(R.color.off_white));
            viewItem.setText(name);

            return viewItem;
        }
    }

    public void hideMetric(String s) {
        if (displayedMetrics.contains(s)) {
            displayedMetrics.remove(s);
        }
        fillGraph();
    }

    public void showMetric(String s) {
        if (!displayedMetrics.contains(s)) {
            displayedMetrics.add(s);
        }
        fillGraph();
    }

    private void fillGraph() {
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

        for (Metric m : allActiveMetrics) {
            if (displayedMetrics.contains(m.getName())) {

                if (metricCount == 0) {
                    legend.put(m.getName(), Color.rgb(255, 255, 255));
                } else if (metricCount == 1) {
                    legend.put(m.getName(), Color.rgb(230, 255, 247));
                } else if (metricCount == 2) {
                    legend.put(m.getName(), Color.rgb(179, 255, 231));
                } else if (metricCount == 3) {
                    legend.put(m.getName(), Color.rgb(128, 255, 215));
                }


                pastWeek = dm.getLastWeek(new Date(), this.getApplicationContext());
                int dayInWeek = 0;
                while (pastWeek.hasNext()) {

                    DayData dayData = pastWeek.next();
                    dayInWeek += 1;
                    if (dayData.getMetric(m.getName().toLowerCase()) != null && dayData.getMetric(m.getName().toLowerCase()).getRating() != -1) {
                        //assign to proper day
                        if (metricCount == 0) {
                            pointsM1[dayInWeek - 1] = new DataPoint(dayInWeek - 1, dayData.getMetric(m.getName().toLowerCase()).getRating());
                        } else if (metricCount == 1) {
                            pointsM2[dayInWeek - 1] = new DataPoint(dayInWeek - 1, dayData.getMetric(m.getName().toLowerCase()).getRating());
                        } else if (metricCount == 2) {
                            pointsM3[dayInWeek - 1] = new DataPoint(dayInWeek - 1, dayData.getMetric(m.getName().toLowerCase()).getRating());
                        } else if (metricCount == 3) {
                            pointsM4[dayInWeek - 1] = new DataPoint(dayInWeek - 1, dayData.getMetric(m.getName().toLowerCase()).getRating());
                        }
                    } else {
                        //assign to proper day
                        if (metricCount == 0) {
                            pointsM1[dayInWeek - 1] = new DataPoint(dayInWeek - 1, 0);
                        } else if (metricCount == 1) {
                            pointsM2[dayInWeek - 1] = new DataPoint(dayInWeek - 1, 0);
                        } else if (metricCount == 2) {
                            pointsM3[dayInWeek - 1] = new DataPoint(dayInWeek - 1, 0);
                        } else if (metricCount == 3) {
                            pointsM4[dayInWeek - 1] = new DataPoint(dayInWeek - 1, 0);
                        }
                    }
                }
                metricCount++;
            }
        }

        GraphView graph = (GraphView) findViewById(R.id.graph);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(6);
        graph.getViewport().setMinY(0.0);
        graph.getViewport().setMaxY(7.0);
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setXAxisBoundsManual(true);

        graph.removeAllSeries();

        if (pointsM1[0] != null) {
            LineGraphSeries<DataPoint> series1 = new LineGraphSeries<>(pointsM1);
            series1.setColor(Color.WHITE);
            series1.setDrawDataPoints(true);
            series1.setDataPointsRadius(10);
            series1.setThickness(8);
            graph.addSeries(series1);
        }
        if (pointsM2[0] != null) {
            LineGraphSeries<DataPoint> series2 = new LineGraphSeries<>(pointsM2);
            series2.setColor(Color.rgb(230, 255, 247));
            series2.setDrawDataPoints(true);
            series2.setDataPointsRadius(10);
            series2.setThickness(8);
            graph.addSeries(series2);
        }
        if (pointsM3[0] != null) {
            LineGraphSeries<DataPoint> series3 = new LineGraphSeries<>(pointsM3);
            series3.setColor(Color.rgb(179, 255, 231));
            series3.setDrawDataPoints(true);
            series3.setDataPointsRadius(10);
            series3.setThickness(8);
            graph.addSeries(series3);
        }
        if (pointsM4[0] != null) {
            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(pointsM4);
            series4.setColor(Color.rgb(128, 255, 215));
            series4.setDrawDataPoints(true);
            series4.setDataPointsRadius(10);
            series4.setThickness(8);
            graph.addSeries(series4);
        }

        graph.setTitle("Last Week's Data");
    }
}
