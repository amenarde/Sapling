package cis350.upenn.edu.sapling;

import java.io.IOException;
import java.util.HashSet;

// @author: juezhou

// Singleton class used for in memory storage of metrics/goals
class DataModel {
    private static DataModel instance;
    static HashSet<String> activeMetrics;
    static HashSet<String> activeGoals;
    static HashSet<String> inactiveMetrics;
    static HashSet<String> inactiveGoals;
    static ModelIO modelIO;    // an IO writer that keeps track of the same data in a local txt file
    static String modelFilePath = "./path.txt";

    static DataModel getInstance() {
        if (instance == null) {
            instance = new DataModel();
        }
        return instance;
    }

    private DataModel() {
        activeMetrics = new HashSet<String>();
        //addDefaultMetrics();
        inactiveMetrics = new HashSet<String>();
        activeGoals = new HashSet<String>();
        inactiveGoals = new HashSet<String>();
        modelIO = new ModelIO(modelFilePath, this);
    }

    // add default metrics to track upon initial setup
    public void addDefaultMetrics() {
        try {
            activeMetrics.add("Happiness");
            activeMetrics.add("Stress");
            activeMetrics.add("Productivity");
            modelIO.updateFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // add a goal to currently track
    public void addGoal(String goal) throws IOException {
        activeGoals.add(goal);
        modelIO.updateFile();
    }
    // deprecate a goal
    public void deprecateGoal(String goal) throws IOException {
        if (activeGoals.contains(goal)) {
            activeGoals.remove(goal);
            inactiveGoals.add(goal);
            modelIO.updateFile();
        }
    }

    // add a metric to currently track
    public void addMetric(String metric) throws IOException {
        activeGoals.add(metric);
        modelIO.updateFile();
    }

    // deprecate a metric
    public void deprecateMetric(String metric) throws IOException {
        if (activeGoals.contains(metric)) {
            activeGoals.remove(metric);
            inactiveGoals.add(metric);
            modelIO.updateFile();
        }
    }

    // getters for the metrics/goals sets
    public HashSet<String> getActiveGoals() {
        return activeGoals;
    }

    public HashSet<String> getinactiveGoals() {
        return inactiveGoals;
    }

    public HashSet<String> getActiveMetrics() {
        return activeMetrics;
    }

    public HashSet<String> getinativeMetrics() {
        return activeMetrics;
    }


}
