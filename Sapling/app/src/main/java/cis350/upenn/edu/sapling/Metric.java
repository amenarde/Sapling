package cis350.upenn.edu.sapling;

// @author: amenarde, juezhou

public class Metric {
    private final String name;
    private Scale scale;
    private final boolean positive;

    public Metric(String name, Scale scale, boolean positive) {
        if (name == null) {
            throw new IllegalArgumentException("null argument");
        }
        this.name = name;
        this.scale = scale;
        this.positive = positive;
    }

    public Metric(String name, boolean positive) {
        if (name == null) {
            throw new IllegalArgumentException("null argument");
        }
        this.name = name;
        this.positive = positive;
        this.scale = null;
    }

    public String getName() {
        return name;
    }
    public boolean hasScale() {return scale != null;}
    public void putScale(Scale scale) { this.scale = scale; }
    public int getRating() {
        return scale.getValue();
    }
    public boolean getPositive() { return positive;}
}

