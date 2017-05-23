package color.measurement.com.from_cp20.module.database.lightcolor;//package color.measurement.com.from_cp20.module.database.lightcolor.stand;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import color.measurement.com.from_cp20.manager.sp.SPConsts;
import color.measurement.com.from_cp20.R;
import color.measurement.com.from_cp20.common.interfaze.DismissCallback;
import color.measurement.com.from_cp20.manager.res.ResHelper;

/**
 * Created by wpc on 2017/4/28.
 */
@SuppressLint("ValidFragment")
public class TitleSetDialog extends DialogFragment {
    Context mContext;
    ArrayList<String> selected, unselect, all;

    @BindView(R.id.lv_select_date_type) ListView mLvSelectDateType;
    @BindView(R.id.lv_unselected_date_type) ListView mLvUnselectedDateType;
    Unbinder unbinder;

    private DismissCallback mDismissCallback;

    public TitleSetDialog(DismissCallback dismissCallback) {
        mDismissCallback = dismissCallback;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mContext = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        View v = LayoutInflater.from(mContext).inflate(R.layout.dialog_db_title_set, null);
        unbinder = ButterKnife.bind(this, v);
        builder.setView(v);
        selected = ResHelper.getCheckedTitlesFromSp(mContext, SPConsts.PREFERENCE_LIGHT_COLOR_DB_STAND, R.array.db_base_data);
        all = ResHelper.getallTitlesFromRes(mContext, R.array.db_base_data);
        unselect = new ArrayList<>();
        for (String str : all) {
            if (selected.contains(str)) {
                continue;
            }
            unselect.add(str);
        }
        refesh();
        mLvSelectDateType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                unselect.add(selected.get(position));
                selected.remove(position);
                refesh();
            }
        });
        mLvUnselectedDateType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selected.add(unselect.get(position));
                unselect.remove(position);
                refesh();
            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferences sp = mContext.getSharedPreferences(SPConsts.PREFERENCE_LIGHT_COLOR_DB_STAND, Context.MODE_PRIVATE);
                SharedPreferences.Editor et = sp.edit();
                for (String str : all) {
                    if (selected.contains(str)) {
                        et.putBoolean(str, true);
                    } else {
                        et.putBoolean(str, false);
                    }
                }
                et.commit();
            }
        });
        return builder.create();
    }

    private void refesh() {
        mLvSelectDateType.setAdapter(new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, selected));
        mLvUnselectedDateType.setAdapter(new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, unselect));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
