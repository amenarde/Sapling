package cis350.upenn.edu.sapling;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

// @author: amenarde
// this class is the primary controller that the activities interact with;
// it uses a model writer to set and receive data it from the database ;
// it has a data model which it exposes, uses data model to verify data and
// make placeholder/empty days

public class DataManager {

    private static DataManager dataManager;
    private static boolean dummyDataFilled = false;

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

    // for demonstration purposes, primes the app with some old data
    private void fillDummyData(Context context) {
        if (dummyDataFilled) return;

        //TODO: autofill a work week

    }

    // returns the DayData for the given day
    public DayData getDay(Date date, Context context) {

        DayData day = dbWriter.get(date, context);
        if (day == null) {
            return getDefaultDayData(context);
        }

        return day;
    }

    // generates an empty template day from dataModel to be filled
    private DayData getDefaultDayData(Context context) {
        DayData toFill = new DayData();

        Map<String, Metric> metrics = getActiveMetrics(context);
        Set<String> goals = getActiveGoals(context);

        for (Metric m : metrics.values()) {
            toFill.putMetric(m);
        }
        for (String name : goals) {
            toFill.putGoal(new Goal(name, null));
        }

        return toFill;
    }

    // pushes DayData to the database
    public void putDay(Date date, DayData dayData, Context context) {
        if (dayData == null) {
            throw new IllegalArgumentException("null argument");
        }

        dbWriter.put(date, dayData, context);
    }

    // Gets data for the last week (today and last 6 days)
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

    public void addUsername(String name, Context c) throws IOException {
        dataModel.addName(name, c);
    }

    public String getUsername() {
        return dataModel.getUsername();
    }

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

    public Map<String, Metric> getinactiveMetrics(Context c) {
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

    public void purgeFiles(Context c) {
        File dataDir = c.getFilesDir();
        for (File f : dataDir.listFiles()) {
            String name = f.getName();
            if (name.equals("path.txt") || name.endsWith(".JSON")) f.delete();
        }

    }
    
}
