package cis350.upenn.edu.sapling;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

// @author: amenarde

public class HeatMapAdapter extends BaseAdapter{

    private boolean thereExistGoals = true;
    private Context context;
    private boolean[][] data;
    private int x, y;
    private String[] names;

    public HeatMapAdapter(Context context, boolean[][] data, String[] names) {
        if (names.length == 0) {
            thereExistGoals = false;
            return;
        }

        this.context = context;
        this.data = data;
        this.names = names;
        this.x = data.length; this.y = data[0].length;
    }

    @Override
    public int getCount() {
        if (!thereExistGoals) {
            return 0;
        }

        else return x * (y + 1);
    }

    @Override
    public Object getItem(int i) {
        if (i%(y+1) == 0) {
            return names[i/(y+1)];
        }
        else {
            return data[i/(y+1)][i%(y+1) - 1];
        }
    }

    @Override
    public long getItemId(int i) {
        return 0; // Unused
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        TextView viewItem = new TextView(context);
        Object toDisplay = getItem(i);

        if (toDisplay instanceof String) {
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(300, ViewGroup.LayoutParams.WRAP_CONTENT);
            viewItem.setLayoutParams(params);
            viewItem.setBackgroundColor(Color.WHITE);
            viewItem.setText((String)toDisplay);
            return viewItem;
        }
        else if (toDisplay instanceof Boolean) {
            if ((Boolean)toDisplay) {
                ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(60, ViewGroup.LayoutParams.WRAP_CONTENT);
                viewItem.setLayoutParams(params);
                viewItem.setBackgroundColor(Color.GREEN);
                viewItem.setText("x");
            }
            else {
                ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(60, ViewGroup.LayoutParams.WRAP_CONTENT);
                viewItem.setLayoutParams(params);
                viewItem.setBackgroundColor(Color.DKGRAY);
            }

            return viewItem;
        }
        else {
            Log.v("Crash", "Object casting issue at heatmap level");
            assert(false);
            return null;
        }
    }
}
