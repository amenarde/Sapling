package cis350.upenn.edu.sapling;

//@author: amenarde

import android.content.Context;

import java.util.ArrayList;
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
        dbWriter = new DBWriter("data/");
    }
    
    public static DataManager getInstance() {
        if (dataManager == null) {
            dataManager = new DataManager();
        }
        
        return dataManager;
    }

    public DayData getDay(Date date, Context context) {
        DayData day = dbWriter.get(date, context);


        if (day == null) {
            DayData data = new DayData();
            data.putMetric(new Metric("Metric1", new Scale(5), true));
            data.putGoal(new Goal("Goal1", Boolean.TRUE));
            return data;

//            DayData toFill = new DayData();
//            Set<String> metrics = getActiveMetrics(context);
//            Set<String> goals = getActiveGoals(context);
//            for (String name : metrics) {
//                toFill.putMetric(new Metric(name, null));
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
    
    // getters for the metrics/goals sets from the Data Model
    private Set<String> getActiveGoals(Context c) {
        return dataModel.getActiveGoals();
    }
    
    private Set<String> getinactiveGoals(Context c) {
        return dataModel.getinactiveGoals();
    }
    
    private Map<String, Metric> getActiveMetrics(Context c) {
        return dataModel.getActiveMetrics();
    }
    
    private Map<String, Metric> getinativeMetrics(Context c) {
        return dataModel.getinativeMetrics();
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
