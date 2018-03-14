package cis350.upenn.edu.sapling;

import com.google.gson.Gson;
import java.util.Date;
import java.util.HashMap;

// @author: amenarde

public class Day {
    private HashMap<String, Scale> metrics;
    private HashMap<String, Boolean> goals;
    private final Date date;

    public Day(Date date) {
        if (date == null) { throw new IllegalArgumentException("null argument"); }

        this.date = date;

        metrics = new HashMap<String, Scale>();
        goals = new HashMap<String, Boolean>();
    }

    public void putMetric(String name, Scale value) {
        if (name == null || value == null) { throw new IllegalArgumentException("null argument"); }

        metrics.put(name, value);
    }

    public Scale getMetric(String name) {
        if (name == null) { throw new IllegalArgumentException("null argument"); }

        return metrics.get(name);
    }

    public void putGoal(String name, Boolean achieved) {
        if (name == null || achieved == null) { throw new IllegalArgumentException("null argument"); }

        goals.put(name, achieved);
    }

    public Boolean getGoal(String name) {
        if (name == null) { throw new IllegalArgumentException("null argument"); }

        return goals.get(name);
    }

    public Date getDate() {
        return date;
    }

    public String toJSON() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public Day fromJSON(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, Day.class);
    }
}
