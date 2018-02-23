package cis350.upenn.edu.sapling;

import java.util.HashSet;

// @author: amenarde

// in memory storage of metrics/goals that are being tracked
class DataModel {
    private static DataModel instance;
    static HashSet<String> activeMetrics;
    static HashSet<String> activeGoals;
    static HashSet<String> inactiveMetrics;
    static HashSet<String> inactiveGoals;


    static DataModel getinstance() {
        if (instance == null) {
            instance = new DataModel();
        }
        return instance;
    }

    private DataModel() {
        activeMetrics = new HashSet<String>();
        inactiveMetrics = new HashSet<String>();
        activeGoals = new HashSet<String>();
        inactiveGoals = new HashSet<String>();
    }

    // add a goal to currently track
    public void addGoal(String goal) {
        activeGoals.add(goal);
    }
    // deprecate a goal
    public void deprecateGoal(String goal) {
        if (activeGoals.contains(goal)) {
            activeGoals.remove(goal);
            inactiveGoals.add(goal);
        }
    }

    // add a metric to currently track
    public void addMetric(String metric) {
        activeGoals.add(metric);
    }
    // deprecate a metric
    public void deprecateMetric(String metric) {
        if (activeGoals.contains(metric)) {
            activeGoals.remove(metric);
            inactiveGoals.add(metric);
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
