package cis350.upenn.edu.sapling;

//@author: amenarde

import java.io.IOException;
import java.util.HashSet;

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
