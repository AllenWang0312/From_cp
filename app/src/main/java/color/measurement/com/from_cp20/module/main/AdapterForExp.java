package color.measurement.com.from_cp20.module.main;

import android.app.Activity;
import android.content.Context;
import android.database.DataSetObserver;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;


/**
 * Created by wpc on 2017/3/23.
 */

public class AdapterForExp implements ExpandableListAdapter {
    Context mContext;
    String[] group;
    String[][] products = new String[4][5];

    AdapterForExp(Activity context, String[] group, int[] ids) {
        mContext = context;
        this.group = group;
        for (int i = 0; i < group.length; i++) {
            products[i] = mContext.getResources().getStringArray(ids[i]);
        }
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getGroupCount() {
        return group.length;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return products[groupPosition].length;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return products[groupPosition];
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return products[groupPosition][childPosition];
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        TextView t = new TextView(mContext);
        t.setTextSize(18);
        t.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,100));
        t.setGravity(Gravity.CENTER_VERTICAL);
        t.setText(group[groupPosition]);
        return t;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        TextView t = new TextView(mContext);
        t.setTextSize(16);
        t.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 80));
        t.setGravity(Gravity.CENTER_VERTICAL);
        t.setText(products[groupPosition][childPosition]);
        return t;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public void onGroupExpanded(int groupPosition) {

    }

    @Override
    public void onGroupCollapsed(int groupPosition) {

    }

    @Override
    public long getCombinedChildId(long groupId, long childId) {
        return 0;
    }

    @Override
    public long getCombinedGroupId(long groupId) {
        return 0;
    }
}
