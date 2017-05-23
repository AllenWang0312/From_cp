package color.measurement.com.from_cp20.module.measure.lightcolor.v2;//package color.measurement.com.from_cp20.module.measure.lightcolor.v2.bean;//package color.measurement.com.from_cp20.module.measure.lightcolor.v2;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TableLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import color.measurement.com.from_cp20.R;
import color.measurement.com.from_cp20.common.adapter.MultipleChoiceRecAdapter;
import color.measurement.com.from_cp20.manager.res.ResHelper;
import color.measurement.com.from_cp20.manager.sp.SPConsts;

/**
 * Created by wpc on 2017/3/31.
 */

@SuppressLint("ValidFragment")
public class SettingDialog extends DialogFragment {
    @BindView(R.id.tl_stand_setting) TableLayout mTlStandSetting;
    @BindView(R.id.sp_setting_dialog_light_color) Spinner mSp;
    @BindView(R.id.sp_light_set) Spinner mSpLightSet;
    @BindView(R.id.sp_angle_set) Spinner mSpAngleSet;
    @BindView(R.id.sp_test_mod) Spinner mSpTestMod;
    @BindView(R.id.sp_test_times) Spinner mSpTestTimes;
    @BindView(R.id.rg_stand_show_mode) Spinner mRgStandShowMode;
    @BindView(R.id.tl_simple_setting) TableLayout mTlSimpleSetting;
    //    @BindView(R.id.lv_select_date_type) ListView mLvSelectedDateType;
//    @BindView(R.id.lv_unselected_date_type) ListView mLvUnselectedDateType;
    //    @BindView(R.id.bt_date_choice) Button mBtDateChoice;
    @BindView(R.id.rv_select_date_type) RecyclerView mRecyclerView;
    @BindView(R.id.cb_stand_auto_named) CheckBox mCbStandAutoNamed;
    @BindView(R.id.rg_simple_show_mode) Spinner mRgSimpleShowMode;
    @BindView(R.id.cb_simple_auto_named) CheckBox mCbSimpleAutoNamed;

    public SettingDialog() {

    }


    Context mContext;
    int index;
    Unbinder unbinder;
    SharedPreferences sp;
    //    String[] type;
    DialogInterface.OnClickListener mPosOnClickListener;

    ArrayList<String> selected, unselect, all;
    boolean[] checkState;

    public SettingDialog(int index, DialogInterface.OnClickListener listener) {
        this.index = index;
        mPosOnClickListener = listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        sp = mContext.getSharedPreferences(SPConsts.PREFERENCE_LIGHT_COLOR, Context.MODE_PRIVATE);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("设置");
        View v = LayoutInflater.from(mContext).inflate(R.layout.dialog_setting_lightcolor, null);
        unbinder = ButterKnife.bind(this, v);
        selected = ResHelper.getCheckedTitlesFromSp(mContext, SPConsts.PREFERENCE_LIGHT_COLOR, R.array.db_base_data);
        all = ResHelper.getallTitlesFromRes(mContext, R.array.db_base_data);
        checkState = getCheckState(selected, all);

        unselect = new ArrayList<>();
        initViews();
        refesh();
        final MultipleChoiceRecAdapter adapter = new MultipleChoiceRecAdapter(mContext, checkState, all);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.setAdapter(adapter);
//        mLvSelectedDateType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                unselect.add(selected.get(position));
//                selected.remove(position);
//                refesh();
//            }
//        });
//        mLvUnselectedDateType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                selected.add(unselect.get(position));
//                unselect.remove(position);
//                refesh();
//            }
//        });
        builder.setView(v);
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Editor edit = sp.edit();
                if (index == 0) {
                    edit.putInt(SPConsts.STAND_DATA_DISPLAY_TYPE, mSp.getSelectedItemPosition());
                    edit.putInt(SPConsts.ANGLE_SET, mSpAngleSet.getSelectedItemPosition());
                    edit.putInt(SPConsts.LIGHT_SET, mSpLightSet.getSelectedItemPosition());
                    edit.putInt(SPConsts.TEST_MOD, mSpTestMod.getSelectedItemPosition());
                    edit.putInt(SPConsts.AVERAGE_TIMES, mSpTestTimes.getSelectedItemPosition());
                    edit.putInt(SPConsts.STAND_DATA_MOD, mRgStandShowMode.getSelectedItemPosition());
                    edit.putBoolean(SPConsts.STAND_AUTO_NAME, mCbStandAutoNamed.isChecked());
                } else {
                    for (String str : all) {
                        if (selected.contains(str)) {
                            edit.putBoolean(str, true);
                        } else {
                            edit.putBoolean(str, false);
                        }
                    }
                    edit.putInt(SPConsts.SIMPLE_DATA_MOD, mRgSimpleShowMode.getSelectedItemPosition());
                    edit.putBoolean(SPConsts.SIMPLE_AUTO_NAME, mCbSimpleAutoNamed.isChecked());
                }

//                int count = mFbl.getChildCount();
//                for (int i = 0; i < count; i++) {
//                    edit.putBoolean(type[i], ((CheckBox) mFbl.getChildAt(i)).isChecked());
//                }
                edit.commit();
                ResHelper.saveState( adapter.getCheck_state(), adapter.getDatas(), sp);
                mPosOnClickListener.onClick(dialog, which);
            }
        });
        setCancelable(false);
        builder.setNegativeButton("取消", null);
        return builder.create();
    }

    private boolean[] getCheckState(ArrayList<String> selected, ArrayList<String> all) {
        boolean[] checkstate = new boolean[all.size()];
        for (int i = 0; i < all.size(); i++) {
            if (selected.contains(all.get(i))) {
                checkstate[i] = true;
            } else {
                checkstate[i] = false;
            }
        }
        return checkstate;
    }

    private void initViews() {
        if (index == 0) {
            mTlSimpleSetting.setVisibility(View.GONE);
            mTlStandSetting.setVisibility(View.VISIBLE);
        } else {
            mTlSimpleSetting.setVisibility(View.VISIBLE);
            mTlStandSetting.setVisibility(View.GONE);
        }
        for (String str : all) {
            if (selected.contains(str)) {
                continue;
            }
            unselect.add(str);
        }
        mSp.setSelection(sp.getInt(SPConsts.STAND_DATA_DISPLAY_TYPE, 0));
        mSpAngleSet.setSelection(sp.getInt(SPConsts.ANGLE_SET, 0));
        mSpLightSet.setSelection(sp.getInt(SPConsts.LIGHT_SET, 0));
        mSpTestMod.setEnabled(false);
        mSpTestMod.setSelection(sp.getInt(SPConsts.TEST_MOD, 0));
        mSpTestTimes.setSelection(sp.getInt(SPConsts.AVERAGE_TIMES, 0));

        mRgStandShowMode.setSelection(sp.getInt(SPConsts.STAND_DATA_MOD, 1));
        mRgSimpleShowMode.setSelection(sp.getInt(SPConsts.SIMPLE_DATA_MOD, 0));

        mCbStandAutoNamed.setChecked(sp.getBoolean(SPConsts.STAND_AUTO_NAME, true));
        mCbSimpleAutoNamed.setChecked(sp.getBoolean(SPConsts.SIMPLE_AUTO_NAME, true));
    }

    private void refesh() {
//        mLvSelectedDateType.setAdapter(new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, selected));
//        mLvUnselectedDateType.setAdapter(new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, unselect));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
