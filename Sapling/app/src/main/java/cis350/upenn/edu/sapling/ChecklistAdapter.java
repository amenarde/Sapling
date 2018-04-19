package cis350.upenn.edu.sapling;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;

public class ChecklistAdapter extends BaseAdapter{

    Context context;
    String[] metricNames;
    Boolean[] checked;
    DisplayActivity parent;

    public ChecklistAdapter(Context context, String[] metricNames, Boolean[] checked, DisplayActivity parent) {
        if (metricNames.length != checked.length) {
            throw new IllegalArgumentException("Must have the same sized arrays");
        }

        this.context = context;
        this.metricNames = metricNames;
        this.checked = checked;
    }

    @Override
    public int getCount() {
        return metricNames.length;
    }

    @Override
    public Object getItem(int i) {
        return null; //unused
    }

    @Override
    public long getItemId(int i) {
        return 0; //unused
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final CheckBox cb = new CheckBox(context);
        cb.setText(metricNames[i]);
        cb.setChecked(checked[i]);

        cb.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(cb.isChecked()){
                    parent.hideMetric(cb.getText().toString());
                }else{
                    parent.showMetric(cb.getText().toString());
                }
            }
        });

        return cb;
    }
}