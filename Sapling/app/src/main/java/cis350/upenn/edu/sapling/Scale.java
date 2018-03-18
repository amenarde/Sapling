package cis350.upenn.edu.sapling;

// @author: amenarde

public class Scale {
    static final int LOWER_BOUND = 1;
    static final int UPPER_BOUND = 7;

    private int value;

    public Scale(int value) {
        if (value < LOWER_BOUND || value > UPPER_BOUND) {
            throw new IllegalArgumentException(
                    "Scale must be in range: [" + LOWER_BOUND + ", " + UPPER_BOUND + "]");
        }

        this.value = value;
    }

    public int getValue() { return this.value; }
    public int getLowerBound() { return LOWER_BOUND; }
    public int getUpperBound() { return UPPER_BOUND; }
}
