package cis350.upenn.edu.sapling;

//@author: amenarde

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import java.io.IOException;
import java.util.HashSet;

public class DataManager {
    private static DataManager dataManager;
    
    private DataModel dataModel;
    private DBWriter dbWriter;
    
    private DataManager() {
        dataModel = DataModel.getInstance();
        dbWriter = new DBWriter("./data/");
    }
    
    public static DataManager getInstance() {
        if (dataManager == null) {
            dataManager = new DataManager();
        }
        
        return dataManager;
    }
    
    public Iterator<Metric> getMetricsForDay(Date date) {
        return dbWriter.get(date).getAllMetrics().iterator();
    }
    
    public Iterator<Goal> getGoalsForDay(Date date) {
        return dbWriter.get(date).getAllGoals().iterator();
    }
    
    public Iterator<Iterator<Metric>> getMetricsForLastWeek(Date date) {
        ArrayList<Iterator<Metric>> list = new ArrayList<Iterator<Metric>>(7);
        for (int i = 6; i >= 0; i--) {
            Date newDate = new Date(date.getTime() - (86_400_000 * i)); //millis in a day
            list.add(dbWriter.get(newDate).getAllMetrics().iterator());
        }
        
        return list.iterator();
    }
    
    public Iterator<Iterator<Goal>> getGoalsForLastWeek(Date date) {
        ArrayList<Iterator<Goal>> list = new ArrayList<Iterator<Goal>>(7);
        for (int i = 6; i >= 0; i--) {
            Date newDate = new Date(date.getTime() - (86_400_000 * i)); //millis in a day
            list.add(dbWriter.get(newDate).getAllGoals().iterator());
        }
        
        return list.iterator();
    }
    
    /* <-------------------------------- DataModel Methods --------------------------> */
    
    // getters for the metrics/goals sets from the Data Model
    public HashSet<String> getActiveGoals() {
        return dataModel.getActiveGoals();
    }
    
    public HashSet<String> getinactiveGoals() {
        return dataModel.getinativeMetrics();
    }
    
    public HashSet<String> getActiveMetrics() {
        return dataModel.getActiveMetrics();
    }
    
    public HashSet<String> getinativeMetrics() {
        return dataModel.getinativeMetrics();
    }
    
    // setters for the metrics/goals sets from the Data Model
    public void addMetric(String s) {
        try {
            dataModel.addMetric(s);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void deprecateMetric(String s) {
        try {
            dataModel.deprecateMetric(s);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void addGoal(String s) {
        try {
            dataModel.addGoal(s);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void deprecateGoal(String s) {
        try {
            dataModel.deprecateGoal(s);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
