package color.measurement.com.from_cp20.module.database.lightcolor;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;

import color.measurement.com.from_cp20.common.adapter.MultipleChoiceRecAdapter;
import color.measurement.com.from_cp20.common.interfaze.DismissCallback;
import color.measurement.com.from_cp20.manager.res.ResHelper;

/**
 * Created by wpc on 2017/5/2.
 */
@SuppressLint("ValidFragment")
public class DisplayDataTypeCheckDialog extends DialogFragment {

    Context mContext;
    DismissCallback mDismissCallback;
    SharedPreferences sp;
    boolean[] check_state;
    ArrayList<String> titles;
    public DisplayDataTypeCheckDialog(){

    }

    public DisplayDataTypeCheckDialog(Context context,DismissCallback dismissCallback, SharedPreferences sp, int resId) {
        mContext = context;
        mDismissCallback = dismissCallback;
        this.sp = sp;
        check_state = ResHelper.getCheckedStateFromSP(mContext, sp, resId);
        titles = ResHelper.getallTitlesFromRes(mContext, resId);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("选择数据");
        RecyclerView rec = new RecyclerView(mContext);
        rec.setLayoutManager(new LinearLayoutManager(mContext));
        rec.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 400));
        final MultipleChoiceRecAdapter adapter = new MultipleChoiceRecAdapter(mContext, check_state, titles);
//        adapter.set
        rec.setAdapter(adapter);
        builder.setView(rec);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ResHelper.saveState( adapter.getCheck_state(), adapter.getDatas(), sp);
                mDismissCallback.dismiss();
            }
        });
        return builder.create();
    }

}
