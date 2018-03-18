package cis350.upenn.edu.sapling;

//@author: amenarde

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class DataManager {
    private static DataManager dataManager;
    
    private DataModel dataModel;
    private DBWriter dbWriter;
    
    private DataManager() {
        dataModel = DataModel.getinstance();
        dbWriter = new DBWriter("./data/");
    }
    
    public static DataManager getInstance() {
        if (dataManager == null) {
            dataManager = new DataManager();
        }
        
        return dataManager;
    }
    
    public DayData getDay(Date date) {
        DayData day = dbWriter.get(date);
        if (day == null) {
            DayData toFill = new DayData();
            Set<String> metrics = getActiveMetrics();
            Set<String> goals = getActiveGoals();
            for (String name : metrics) {
                toFill.putMetric(new Metric(name, null));
            }
            for (String name : goals) {
                toFill.putGoal(new Goal(name, null));
            }
            return toFill;
        }

        return day;
    }

    public void putDay(Date date, DayData dayData) {
        if (dayData == null) {
            throw new IllegalArgumentException("null argument");
        }

        dbWriter.put(date, dayData);
    }

    // Hands back in order: today, yesterday, ...
    public Iterator<DayData> getLastWeek(Date date) {
        ArrayList<DayData> list = new ArrayList<DayData>(7);
        for (int i = 6; i >= 0; i--) {
            Date newDate = new Date(date.getTime() - (86_400_000 * i)); //millis in a day
            list.add(getDay(newDate));
        }
        return list.iterator();
    }
    
    /* <-------------------------------- DataModel Methods --------------------------> */
    
    // getters for the metrics/goals sets from the Data Model
    private Set<String> getActiveGoals() {
        return dataModel.getActiveGoals();
    }
    
    private Set<String> getinactiveGoals() {
        return dataModel.getinativeMetrics();
    }
    
    private Set<String> getActiveMetrics() {
        return dataModel.getActiveMetrics();
    }
    
    private Set<String> getinativeMetrics() {
        return dataModel.getinativeMetrics();
    }
    
    // setters for the metrics/goals sets from the Data Model
    public void addModelMetric(String s) {
        try {
            dataModel.addMetric(s);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void deprecateModelMetric(String s) {
        try {
            dataModel.deprecateMetric(s);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void addModelGoal(String s) {
        try {
            dataModel.addGoal(s);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void deprecateModelGoal(String s) {
        try {
            dataModel.deprecateGoal(s);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
