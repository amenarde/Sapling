package cis350.upenn.edu.sapling;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

// @author: amenarde

public class HeatMapAdapter extends BaseAdapter{

    private Context context;
    private boolean[][] data;
    private int x, y;
    private Date endDate;

    public HeatMapAdapter(Context context, boolean[][] data, Date endDate) {
        this.context = context;
        this.data = data;
        this.x = data.length; this.y = data[0].length;
        this.endDate = endDate;
    }

    @Override
    public int getCount() {
        return (x + 1) * y;
    }

    @Override
    public Object getItem(int i) {
        return data[(i-7)/y][(i-7)%y];
    }

    @Override
    public long getItemId(int i) {
        return 0; // Unused
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if (i < y) {
            SimpleDateFormat sdf = new SimpleDateFormat("EEEEE");
            Date currentDate = new Date(endDate.getTime() - (86_400_000 * (6 - i))); //millis in a day
            TextView viewItem = new TextView(context);
            viewItem.setTextColor(context.getResources().getColor(R.color.off_white));
            viewItem.setText(sdf.format(currentDate));
            return viewItem;
        }

        TextView viewItem = new TextView(context);
        Object toDisplay = getItem(i);

        if ((Boolean)toDisplay) {
            AbsListView.LayoutParams params = new AbsListView.LayoutParams(60, 60);

            viewItem.setLayoutParams(params);
            viewItem.setBackgroundColor(context.getResources().getColor(R.color.off_white));
            viewItem.setPadding(5,5,5,5);
        }
        else {
            AbsListView.LayoutParams params = new AbsListView.LayoutParams(60, 60);
            viewItem.setLayoutParams(params);
            viewItem.setBackgroundColor(context.getResources().getColor(R.color.colorPrimaryDark));
            viewItem.setPadding(5,5,5,5);
        }

        return viewItem;

    }
}
