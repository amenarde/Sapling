package cis350.upenn.edu.sapling;

import com.google.gson.Gson;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

// @author: amenarde

public class DayData {
    private HashMap<String, Metric> metrics;
    private HashMap<String, Goal> goals;

    public DayData() {
        metrics = new HashMap<String, Metric>();
        goals = new HashMap<String, Goal>();
    }

    public void putMetric(Metric metric) {
        if (metric == null) { throw new IllegalArgumentException("null argument"); }
        metrics.put(metric.getName(), metric);
    }

    public Metric getMetric(String name) {
        if (name == null) { throw new IllegalArgumentException("null argument"); }
        return metrics.get(name);
    }

    public void putGoal(Goal goal) {
        if (goal == null) { throw new IllegalArgumentException("null argument"); }
        goals.put(goal.getName(), goal);
    }

    public Goal getGoal(String name) {
        if (name == null) { throw new IllegalArgumentException("null argument"); }
        return goals.get(name);
    }

    public Collection<Metric> getAllMetrics() {
        return metrics.values();
    }

    public Collection<Goal> getAllGoals() {
        return goals.values();
    }
}
