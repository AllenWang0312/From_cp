package color.measurement.com.from_cp20.common.widget.table;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import java.util.ArrayList;

/**
 * Created by wpc on 2017/5/4.
 */

class NameAdapter extends RecyclerView.Adapter<NameAdapter.CheckBoxViewHolder> {
    Context mContext;
    ArrayList<String> names;
    ArrayList<Integer> checked_indexs;
    NameAdapter(Context context, ArrayList<String> names) {
        mContext = context;
        this.names = names;
        checked_indexs=new ArrayList<>();
    }

    @Override
    public CheckBoxViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CheckBox cb = new CheckBox(mContext);
        cb.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return new CheckBoxViewHolder(cb);
    }

    @Override
    public void onBindViewHolder(CheckBoxViewHolder holder, final int position) {
        holder.cb.setText(names.get(position));
        holder.cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    checked_indexs.add(position);
                } else {
                    checked_indexs.remove((Integer) position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return names.size();
    }

    class CheckBoxViewHolder extends RecyclerView.ViewHolder {
        CheckBox cb;

        public CheckBoxViewHolder(View itemView) {
            super(itemView);
            cb = (CheckBox) itemView;
        }
    }
}

