package cis350.upenn.edu.sapling;

// @author: amenarde, juezhou
// a metric is a conceptual daily check-in of something that can be measured, such as happiness,
// eating healthy, tiredness. While some of those things can be coded as booleans (was/wasn't
// happy today), they generally make more sense along a further continuum.
// metrics come in two types:
// positive -- more is better (happiness)
// negative -- less is better (sadness)

public class Metric {
    private final String name;
    private Scale scale;
    private final boolean positive;

    public Metric(String name, Scale scale, boolean positive) {
        if (name == null) {
            throw new IllegalArgumentException("Name must be non-null");
        }
        this.name = name;
        this.scale = scale;
        this.positive = positive;
    }

    public Metric(String name, boolean positive) {
        if (name == null) {
            throw new IllegalArgumentException("Name must be non-null");
        }
        this.name = name;
        this.positive = positive;
        this.scale = null;
    }

    public String getName() {
        return name;
    }

    public boolean hasScale() {
        return scale != null;
    }

    public void putScale(Scale scale) {
        if (scale == null) {
            throw new IllegalArgumentException("Scale can not be null");
        }

        this.scale = scale;
    }

    public int getRating() {
        if (scale == null) {
            return -1;
        }
        return scale.getValue();
    }
    public boolean getPositive() { return positive;}
}

