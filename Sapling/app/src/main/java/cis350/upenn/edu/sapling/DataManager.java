package cis350.upenn.edu.sapling;

//@author: amenarde

import android.content.Context;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DataManager {
    private static DataManager dataManager;
    
    private DataModel dataModel;
    private DBWriter dbWriter;
    
    private DataManager() {
        dataModel = DataModel.getInstance();
        dbWriter = new DBWriter();
    }
    
    public static DataManager getInstance() {
        if (dataManager == null) {
            dataManager = new DataManager();
        }
        
        return dataManager;
    }

    public DayData getDay(Date date, Context context) {

        DayData dd = new DayData();
        dd.putMetric(new Metric("Antonio's Metric", new Scale(3), true));
        dd.putGoal(new Goal("Antonio please go to gym", false));

        dbWriter.put(new Date(), dd, context);

        DayData day = dbWriter.get(date, context);

        if (day == null) {
            DayData dx = new DayData();
            dd.putMetric(new Metric("bad", new Scale(3), true));
            dd.putGoal(new Goal("more bad", false));

//            DayData toFill = new DayData();
//            Map<String, Metric> metrics = getActiveMetrics(context);
//            Set<String> goals = getActiveGoals(context);
//            for (Metric m : metrics.values()) {
//                toFill.putMetric(m);
//            }
//            for (String name : goals) {
//                toFill.putGoal(new Goal(name, null));
//            }
//            return toFill;
        }

        return day;
    }

    public void putDay(Date date, DayData dayData, Context context) {
        if (dayData == null) {
            throw new IllegalArgumentException("null argument");
        }

        dbWriter.put(date, dayData, context);
    }

    // Hands back in order: today, yesterday, ...
    public Iterator<DayData> getLastWeek(Date date, Context context) {
        ArrayList<DayData> list = new ArrayList<DayData>(7);
        for (int i = 6; i >= 0; i--) {
            Date newDate = new Date(date.getTime() - (86_400_000 * i)); //millis in a day
            list.add(getDay(newDate, context));
        }
        return list.iterator();
    }
    
    /* <-------------------------------- DataModel Methods --------------------------> */

    // adds a few default metrics to the metric map and txt file
    public void addDefaultMetrics(Context c) throws IOException {
        dataModel.addDefaultMetrics(c);
    }

    // adds a few default goals to the goal set and txt file
    public void addDefaultGoals(Context c) throws IOException {
        dataModel.addDefaultGoals(c);
    }

    // getters for the metrics/goals sets from the Data Model
    public Set<String> getActiveGoals(Context c) {
        return dataModel.getActiveGoals();
    }

    public Set<String> getinactiveGoals(Context c) {
        return dataModel.getinactiveGoals();
    }

    public Map<String, Metric> getActiveMetrics(Context c) {
        return dataModel.getActiveMetrics();
    }

    public Map<String, Metric> getinativeMetrics(Context c) {
        return dataModel.getinactiveMetrics();
    }

    // setters for the metrics/goals sets from the Data Model
    public void addModelMetric(String s, boolean positive, Context c) {
        try {
            dataModel.addMetric(s, positive, c);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deprecateModelMetric(String s, boolean positive, Context c) {
        try {
            dataModel.deprecateMetric(s, positive, c);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addModelGoal(String s, Context c) {
        try {
            dataModel.addGoal(s, c);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deprecateModelGoal(String s, Context c) {
        try {
            dataModel.deprecateGoal(s, c);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
