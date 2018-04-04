package cis350.upenn.edu.sapling;

// @author: amenarde

public class Metric {
    private final String name;
    private final Scale scale;
    private final boolean positive;

    public Metric(String name, Scale scale, boolean positive) {
        if (name == null) {
            throw new IllegalArgumentException("null argument");
        }
        this.name = name;
        this.scale = scale;
        this.positive = positive;
    }

    public String getName() {
        return name;
    }
    public int getRating() {
        return scale.getValue();
    }
    public boolean getPositive() { return positive;}
}

