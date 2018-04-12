package cis350.upenn.edu.sapling;

import java.util.Collection;
import java.util.HashMap;

// @author: amenarde
// this class wraps a collection of metrics and goals for a day
// and exposes it like a dictionary for easy access it is a
// wrapper and does not know which day it represents

public class DayData {
    private HashMap<String, Metric> metrics;
    private HashMap<String, Goal> goals;

    public DayData() {
        metrics = new HashMap<String, Metric>();
        goals = new HashMap<String, Goal>();
    }

    public void putMetric(Metric metric) {
        if (metric == null) {
            throw new IllegalArgumentException("Metrics must be non-null");
        }

        metrics.put(metric.getName(), metric);
    }

    public Metric getMetric(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Name must be non-null");
        }
        return metrics.get(name);
    }

    public void putGoal(Goal goal) {
        if (goal == null) {
            throw new IllegalArgumentException("Goals must be non-null");
        }
        goals.put(goal.getName(), goal);
    }

    public Goal getGoal(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Name must be non-null");
        }
        return goals.get(name);
    }

    public boolean hasGoal(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Name must be non-null");
        }
        return goals.containsKey(name);
    }

    public boolean hasMetric(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Name must be non-null");
        }
        return metrics.containsKey(name);
    }

    public Collection<Metric> getAllMetrics() {
        return metrics.values();
    }

    public Collection<Goal> getAllGoals() {
        return goals.values();
    }
}
