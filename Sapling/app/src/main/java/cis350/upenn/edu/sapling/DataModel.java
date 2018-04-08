package cis350.upenn.edu.sapling;

import android.content.Context;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

// @author: juezhou

/*
    Singleton class used for in memory storage of metrics/goals.
    The Data Model contains data structures to store active and inactive goals and metrics
    Designers in Activity files can add goals and deprecate goals and metrics, which then triggers
    an update function within an ModelIO object, which handles the read & write
    of on-disk storage in a txt file.
*/
class DataModel {
    private static DataModel instance;
    static Map<String, Metric> activeMetrics;
    static Map<String, Metric> inactiveMetrics;
    static Set<String> activeGoals;
    static HashSet<String> inactiveGoals;
    static ModelIO modelIO;    // an IO writer that keeps track of the same data in a local txt file

    static DataModel getInstance() {
        if (instance == null) {
            instance = new DataModel();
        }
        return instance;
    }

    private DataModel() {
        modelIO = new ModelIO(this, "data/");
        activeMetrics = new HashMap<>();
        inactiveMetrics = new HashMap<>();
        activeGoals = new HashSet<>();
        inactiveGoals = new HashSet<>();
    }

    // adds a few default metrics to track - used in onboarding activity
    public void addDefaultMetrics(Context c) throws IOException {
        addMetric("Happiness", true, c);
        addMetric("Productivity", true, c);
        addMetric("Stress", false, c);
        addMetric("Health", true, c);
    }

    // adds a few default goals to track - used in onboarding activity
    public void addDefaultGoals(Context c) throws IOException {
        addGoal("Eat an Apple a day", c);
        addGoal("Go to the gym", c);
    }

    // add a goal to currently track
    public void addGoal(String goal, Context c) throws IOException {
        if (!activeGoals.contains(goal)) {
            activeGoals.add(goal);
            modelIO.updateFile(c);
        }
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
    public void addMetric(String name, boolean positive, Context c) throws IOException {
        if (!activeMetrics.containsKey(name)) {
            Metric metric = new Metric(name, positive);
            activeMetrics.put(name, metric);
            modelIO.updateFile(c);
        }
    }

    // deprecate a metric
    public void deprecateMetric(String name, boolean positive, Context c) throws IOException {
        if (activeMetrics.containsKey(name)) {
            activeMetrics.remove(name);
            inactiveMetrics.put(name, new Metric(name, positive));
            modelIO.updateFile(c);
        }
    }

    // getters for the metrics/goals sets
    //TODO: eventually this needs to read from path.txt to get the in-disk versions
    public Set<String> getActiveGoals() { return activeGoals; }

    public Set<String> getinactiveGoals() {
        return inactiveGoals;
    }

    public Map<String, Metric> getActiveMetrics() { return activeMetrics; }

    public Map<String, Metric> getinactiveMetrics() {
        return inactiveMetrics;
    }


}
