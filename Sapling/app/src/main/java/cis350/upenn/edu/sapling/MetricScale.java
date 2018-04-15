package cis350.upenn.edu.sapling;

import android.content.Context;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 * Created by vanessakmakuvaro on 3/18/18.
 */

public class MetricScale extends android.support.v7.widget.AppCompatSeekBar {
    String name;
    boolean positive;
    TextView tracker;
    public MetricScale(Context context, String name, boolean positive) {
        super(context);
        this.name = name;
        this.positive = positive;
        this.tracker = new TextView(context);
    }

    public String getName() {
        return name;
    }
    public boolean getPositive() { return positive;}
}
