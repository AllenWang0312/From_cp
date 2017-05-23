package color.measurement.com.from_cp20.common.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import java.util.ArrayList;

/**
 * Created by wpc on 2017/5/2.
 */

public class MultipleChoiceRecAdapter extends RecyclerView.Adapter<MultipleChoiceRecAdapter.ViewHolder> {

    Context mContext;

    boolean[] check_state;
    ArrayList<String> datas;

    public MultipleChoiceRecAdapter(Context context, boolean[] check_state, ArrayList<String> datas) {
        mContext = context;
        this.check_state = check_state;
        this.datas = datas;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CheckBox cb = new CheckBox(mContext);
        cb.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return new ViewHolder(cb);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.cb.setChecked(check_state[position]);
        holder.cb.setText(datas.get(position));
        holder.cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_state[position] = holder.cb.isChecked();
                Log.i("onitemcheckchange",position+"");
            }
        });
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox cb;

        public ViewHolder(View itemView) {
            super(itemView);
            cb = (CheckBox) itemView;
        }
    }

    public boolean[] getCheck_state() {
        for (int i = 0; i <check_state.length ; i++) {
            Log.i("check_state"+i, String.valueOf(check_state[i]));
        }

        return check_state;
    }

    public void setCheck_state(boolean[] check_state) {
        this.check_state = check_state;
    }

    public ArrayList<String> getDatas() {
        return datas;
    }

    public void setDatas(ArrayList<String> datas) {
        this.datas = datas;
    }
}
