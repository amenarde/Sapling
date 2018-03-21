package cis350.upenn.edu.sapling;

import android.content.Context;

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

    static String modelFilePath = "./modelIOTest.txt";

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
        modelIO = new ModelIO(this);
    }


    // add a goal to currently track
    public void addGoal(String goal, Context c) throws IOException {
        activeGoals.add(goal);
        modelIO.updateFile(c);
    }
    // deprecate a goal
    public void deprecateGoal(String goal, Context c) throws IOException {
        if (activeGoals.contains(goal)) {
            activeGoals.remove(goal);
            inactiveGoals.add(goal);
            modelIO.updateFile(c);
        }
    }

    // add a metric to currently track
    public void addMetric(String metric, Context c) throws IOException {
        activeMetrics.add(metric);
        modelIO.updateFile(c);
    }

    // deprecate a metric
    public void deprecateMetric(String metric, Context c) throws IOException {
        if (activeMetrics.contains(metric)) {
            activeMetrics.remove(metric);
            inactiveMetrics.add(metric);
            modelIO.updateFile(c);
        }
    }

    // getters for the metrics/goals sets
    public HashSet<String> getActiveGoals(Context c) {
        return activeGoals;
    }

    public HashSet<String> getinactiveGoals(Context c) {
        return inactiveGoals;
    }

    public HashSet<String> getActiveMetrics(Context c) {
        return activeMetrics;
    }

    public HashSet<String> getinativeMetrics(Context c) {
        return activeMetrics;
    }


}
