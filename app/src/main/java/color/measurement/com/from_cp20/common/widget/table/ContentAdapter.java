package color.measurement.com.from_cp20.common.widget.table;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.HashMap;

import color.measurement.com.from_cp20.common.util.UiUtils;
import color.measurement.com.from_cp20.module.been.interfaze.CompareableData;

/**
 * Created by wpc on 2017/5/4.
 */

public class ContentAdapter extends RecyclerView.Adapter<ContentAdapter.Content> {
    Context mContext;

    ArrayList<String> titles;
    ArrayList<CompareableData> values;

    public ContentAdapter(Context context, ArrayList<String> titles, ArrayList<CompareableData> values) {
        mContext = context;
        this.titles = titles;
        this.values = values;
    }

    @Override
    public Content onCreateViewHolder(ViewGroup parent, int viewType) {
        LinearLayout ll = new LinearLayout(mContext);
        ll.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, UiUtils.Dp2Px(mContext,40)));
        return new Content(ll);
    }

    @Override
    public void onBindViewHolder(Content holder, int position) {
        HashMap<String, Object> map = values.get(position).toHashMap(mContext);
        for (String str : titles) {
            UiUtils.addTextViewToLinearLayout(mContext, holder.ll, String.valueOf(map.get(str)), null);
        }
    }

    @Override
    public int getItemCount() {
        return values.size();
    }

    class Content extends RecyclerView.ViewHolder {
        LinearLayout ll;

        public Content(View itemView) {
            super(itemView);
            ll = (LinearLayout) itemView;
        }
    }
}
