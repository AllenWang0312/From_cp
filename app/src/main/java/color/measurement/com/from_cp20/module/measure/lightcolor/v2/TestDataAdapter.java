package color.measurement.com.from_cp20.module.measure.lightcolor.v2;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.HashMap;

import color.measurement.com.from_cp20.R;
import color.measurement.com.from_cp20.common.interfaze.OnItemClickListener;
import color.measurement.com.from_cp20.common.interfaze.OnItemLongClickListener;
import color.measurement.com.from_cp20.common.util.StringFormat;
import color.measurement.com.from_cp20.common.util.UiUtils;
import color.measurement.com.from_cp20.module.been.interfaze.CompareableData;

/**
 * Created by wpc on 2017/4/27.
 */

public class TestDataAdapter<T extends CompareableData> extends RecyclerView.Adapter<TestDataAdapter.ViewHolder> {

    SharedPreferences sp;
    Context mContext;
    public int selected_color = 0xff4caf50;

//    public int hege_color;
//    public int buhege_color;

    ArrayList<String> keys;
    ArrayList<T> tests;
    Integer selectIndex;

    public void setMaxLength(Integer maxLength) {
        this.maxLength = maxLength;
    }

    Integer maxLength;

    public TestDataAdapter(Context context, SharedPreferences sp) {
        mContext = context;
        this.sp = sp;
    }
    public void refeshDatas( ArrayList<String> keys, ArrayList<T> tests,int selectIndex){
        this.tests = tests;
        this.keys = keys;
        this.selectIndex=selectIndex;
        notifyDataSetChanged();
    }

    public OnItemClickListener getItemClickListener() {
        return mItemClickListener;
    }

    OnItemClickListener mItemClickListener;
    OnItemLongClickListener mOnItemLongClickListener;

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    public void setItemLongClickListener(OnItemLongClickListener itemLongClickListener) {
        mOnItemLongClickListener = itemLongClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LinearLayout ll = new LinearLayout(mContext);
        ll.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,  UiUtils.Dp2Px(mContext,40)));
        ll.setGravity(Gravity.CENTER_VERTICAL);
        return new ViewHolder(ll);
    }

    @Override
    public void onBindViewHolder(final TestDataAdapter.ViewHolder holder, final int position) {
        T data = tests.get(position);
        HashMap<String, Object> item = data.toHashMap(mContext);
        holder.ll.removeAllViews();

//        TextView t = new TextView(mContext);
//        t.setGravity(Gravity.CENTER);
//        t.setText(String.valueOf(position + 1));
//        if (data.isHasSaved()) {
//            t.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.ic_check_white_36dp));
//        }
//        t.setLayoutParams(new LinearLayout.LayoutParams(100, ViewGroup.LayoutParams.MATCH_PARENT));
//        holder.ll.addView(t);
        UiUtils.addLeftDrawTextViewToLinearLayout(mContext, holder.ll, String.valueOf(tests.size()-position), data.isHasSaved() ? R.drawable.ic_check_circle_black_24dp : null);
        if (selectIndex != null && position == selectIndex) {
            holder.ll.setBackgroundColor(selected_color);
        } else {
            holder.ll.setBackgroundColor(Color.TRANSPARENT);
        }
//        else {
//            if (data.getResult().equals("合格")) {
//                holder.ll.setBackgroundColor(hege_color);
//            } else if (data.getResult().equals("不合格")) {
//                holder.ll.setBackgroundColor(buhege_color);
//            }
//        }
        for (String str : keys) {
            Object d = item.get(str);
            UiUtils.addTextViewToLinearLayout(mContext, holder.ll, d == null ? "---" : StringFormat.Object(d), null);
        }
        holder.ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemClickListener.onItemClick(holder.ll, position);
            }
        });
//        holder.ll.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                mOnItemLongClickListener.onItemLongClick(position);
//                return false;
//            }
//        });
    }

    public Integer getSelectIndex() {
        return selectIndex;
    }

    @Override
    public int getItemCount() {
        if(maxLength!=null){
            return tests.size()<maxLength ? tests.size() : maxLength;
        }else {
            return tests.size();
        }

    }

    public void setSelectIndex(Integer selectIndex) {
        this.selectIndex = selectIndex;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout ll;

        public ViewHolder(View itemView) {
            super(itemView);
            ll = (LinearLayout) itemView;
        }
    }
}

