package cis350.upenn.edu.sapling;

// @author: amenarde

public class Goal {
    private final String name;
    private final Boolean completed;

    public Goal(String name, Boolean completed) {
        if (name == null || completed == null) {
            throw new IllegalArgumentException("null argument");
        }

        this.name = name;
        this.completed = completed;
    }

    public String getName() {
        return name;
    }

    public boolean getCompleted() {
        return completed;
    }
}
