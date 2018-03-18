package cis350.upenn.edu.sapling;

// @author: amenarde

public class Metric {
    private final String name;
    private final Scale scale;

    public Metric(String name, Scale scale) {
        if (name == null) {
            throw new IllegalArgumentException("null argument");
        }

        this.name = name;
        this.scale = scale;
    }

    public String getName() {
        return name;
    }
    public int getRating() {
        return scale.getValue();
    }
}

