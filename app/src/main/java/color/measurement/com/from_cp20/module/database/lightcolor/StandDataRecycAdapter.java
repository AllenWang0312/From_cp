package color.measurement.com.from_cp20.module.database.lightcolor;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.HashMap;

import color.measurement.com.from_cp20.common.util.StringFormat;
import color.measurement.com.from_cp20.common.util.UiUtils;
import color.measurement.com.from_cp20.module.been.interfaze.CompareableData;

/**
 * Created by wpc on 2017/4/28.
 */

public class StandDataRecycAdapter<T extends CompareableData> extends RecyclerView.Adapter<StandDataRecycAdapter.ViewHolder> {

    Context mContext;
    ArrayList<T> groupDatas;
    ArrayList<String> titles;

    Integer last_checked_item_index=-1, checked_item_index=-1;

    public Integer getChecked_item_index() {
        return checked_item_index;
    }

    public void setChecked_item_index(Integer checked_item_index) {
        last_checked_item_index = this.checked_item_index;
        this.checked_item_index = checked_item_index;
        if(last_checked_item_index!=null){
            notifyItemChanged(last_checked_item_index);
        }
        notifyItemChanged(checked_item_index);
    }

    public StandDataRecycAdapter(Context context, ArrayList<String> titles, ArrayList<T> groupDatas) {
        mContext = context;
        this.titles = titles;
        this.groupDatas = groupDatas;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LinearLayout ll = new LinearLayout(mContext);
        ll.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,  UiUtils.Dp2Px(mContext,40)));
        ll.setGravity(Gravity.CENTER_VERTICAL);
        return new ViewHolder(ll);
    }

    @Override
    public void onBindViewHolder(StandDataRecycAdapter.ViewHolder holder, final int position) {
        Log.i("onBindViewHolder", position + "/" + groupDatas.size());
        CompareableData compareableData=groupDatas.get(position);
        holder.ll.removeAllViews();
        final CheckBox cb = new CheckBox(mContext);
        cb.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        cb.setChecked(checked_item_index == null ? false : checked_item_index == position);
        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    setChecked_item_index(position);
                }
            }
        });
        holder.ll.addView(cb);
        if(compareableData==null){
            UiUtils.addTextViewToLinearLayout(mContext, holder.ll,"数据丢失", null);
        }else {
            HashMap<String, Object> datas = compareableData.toHashMap(mContext);
            for (String str : titles) {
                Object obj = datas.get(str);
                UiUtils.addTextViewToLinearLayout(mContext, holder.ll, Translate(obj), null);
            }
        }
    }

//    public void onBindViewHolder(ViewHolder holder, int position) {
//        HashMap<String, Object> datas = groupDatas.get(position).toHashMap();
//        for (String str : titles) {
//            Object obj = datas.get(str);
//            UiUtils.addTextViewToLinearLayout(mContext, holder.ll, Translate(obj), null);
//        }
//    }


    private String Translate(Object obj) {
        if (obj instanceof String) {
            return (String) obj;
        }
        if (obj instanceof Double) {
            return StringFormat.TwoDecimal((Double) obj);
        }
        if (obj instanceof Integer) {
            return String.valueOf(obj);
        }
        return String.valueOf(obj);
    }

    @Override
    public int getItemCount() {
        return groupDatas.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout ll;

        public ViewHolder(View itemView) {
            super(itemView);
            ll = (LinearLayout) itemView;
        }
    }
}
