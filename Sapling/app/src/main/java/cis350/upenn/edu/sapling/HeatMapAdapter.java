package cis350.upenn.edu.sapling;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

// @author: amenarde

public class HeatMapAdapter extends BaseAdapter{

    private Context context;
    private boolean[][] data;
    private int x, y;

    public HeatMapAdapter(Context context, boolean[][] data) {
        this.context = context;
        this.data = data;
        this.x = data.length; this.y = data[0].length;
    }

    @Override
    public int getCount() {
        return x * y;
    }

    @Override
    public Object getItem(int i) {
        return data[i/y][i%y];
    }

    @Override
    public long getItemId(int i) {
        return 0; // Unused
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        TextView viewItem = new TextView(context);
        Object toDisplay = getItem(i);

        if ((Boolean)toDisplay) {
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(80, ViewGroup.LayoutParams.WRAP_CONTENT);

            viewItem.setLayoutParams(params);
            viewItem.setBackgroundColor(Color.GREEN);
            //viewItem.setPadding(5,5,5,5);
            viewItem.setText("x");
        }
        else {
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(80, ViewGroup.LayoutParams.WRAP_CONTENT);
            viewItem.setLayoutParams(params);
            viewItem.setBackgroundColor(Color.DKGRAY);
            //viewItem.setPadding(5,5,5,5);
        }

        return viewItem;

    }
}
