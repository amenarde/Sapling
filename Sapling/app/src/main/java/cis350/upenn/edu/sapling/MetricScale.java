package cis350.upenn.edu.sapling;

import android.content.Context;
import android.widget.SeekBar;

/**
 * Created by vanessakmakuvaro on 3/18/18.
 */

public class MetricScale extends android.support.v7.widget.AppCompatSeekBar {
    String name;
    public MetricScale(Context context, String name) {
        super(context);
        this.name = name;
    }
}
