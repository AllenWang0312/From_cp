package color.measurement.com.from_cp20.module.measure.lightcolor.v2;//package color.measurement.com.from_cp20.module.measure.lightcolor.v2.bean;//package color.measurement.com.from_cp20.module.measure.lightcolor.v2;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TableLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import color.measurement.com.from_cp20.R;
import color.measurement.com.from_cp20.common.adapter.MultipleChoiceRecAdapter;
import color.measurement.com.from_cp20.manager.res.ResHelper;
import color.measurement.com.from_cp20.module.been.LCSetting;

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
    //    SharedPreferences sp;
    LCSetting mSetting;
    //    String[] type;
    DialogInterface.OnClickListener mPosOnClickListener;

    List<String>
            selected,
    //            unselect, 
    all;

    boolean[] checkState;

    public SettingDialog(int index, LCSetting settings) {
        this.index = index;
        mSetting = settings;
    }

    public void setPositive(DialogInterface.OnClickListener mPosOnClickListener) {
        this.mPosOnClickListener = mPosOnClickListener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("设置");
        View v = LayoutInflater.from(mContext).inflate(R.layout.dialog_setting_lightcolor, null);
        unbinder = ButterKnife.bind(this, v);
        all = ResHelper.getallTitlesFromRes(mContext, R.array.db_base_data);
        checkState = getCheckState(selected, all);
        initViews();
        refesh();
        final MultipleChoiceRecAdapter adapter = new MultipleChoiceRecAdapter(mContext, checkState, all);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.setAdapter(adapter);
        builder.setView(v);
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (index == 0) {
                    mSetting.getStand().setStand_data_display_type(mSp.getSelectedItemPosition());
                    mSetting.getStand().setAngle(mSpAngleSet.getSelectedItemPosition());
                    mSetting.getStand().setLight(mSpLightSet.getSelectedItemPosition());
                    mSetting.getStand().setTest_mod(mSpTestMod.getSelectedItemPosition());
                    mSetting.getStand().setAverage_times(mSpTestTimes.getSelectedItemPosition());
                    mSetting.getStand().setStand_view_mod(mRgStandShowMode.getSelectedItemPosition());
                    mSetting.getStand().setStand_auto_name(mCbStandAutoNamed.isChecked());
                } else {
                    List<String> checed = new ArrayList<String>();
                    boolean[] checkstate = adapter.getCheck_state();
                    for (int i = 0; i < checkstate.length; i++) {
                        if (checkstate[i]) {
                            checed.add(all.get(i));
                        }
                    }
                    mSetting.getTest().setTest_data_display_type(checed);
                    mSetting.getTest().setTest_view_mod(mRgSimpleShowMode.getSelectedItemPosition());
                    mSetting.getTest().setTest_auto_name(mCbSimpleAutoNamed.isChecked());
                }
                mPosOnClickListener.onClick(dialog, which);
            }
        });
        setCancelable(false);
        builder.setNegativeButton("取消", null);
        return builder.create();
    }

    private boolean[] getCheckState(List<String> selected, List<String> all) {
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
        selected = mSetting.getTest().getTest_data_display_type();
        if (index == 0) {
            mTlSimpleSetting.setVisibility(View.GONE);
            mTlStandSetting.setVisibility(View.VISIBLE);
            mSp.setSelection(mSetting.getStand().getStand_data_display_type());
            mSpAngleSet.setSelection(mSetting.getStand().getAngle());
            mSpLightSet.setSelection(mSetting.getStand().getLight());
            mSpTestMod.setEnabled(false);
            mSpTestMod.setSelection(mSetting.getStand().getTest_mod());
            mSpTestTimes.setSelection(mSetting.getStand().getAverage_times());
            mRgStandShowMode.setSelection(mSetting.getStand().getStand_view_mod());
            mCbStandAutoNamed.setChecked(mSetting.getStand().isStand_auto_name());
        } else {

            mTlSimpleSetting.setVisibility(View.VISIBLE);
            mTlStandSetting.setVisibility(View.GONE);

            mRgSimpleShowMode.setSelection(mSetting.getTest().getTest_view_mod());
            mCbSimpleAutoNamed.setChecked(mSetting.getTest().isTest_auto_name());
        }
//        for (String str : all) {
//            if (selected.contains(str)) {
//                continue;
//            }
//            unselect.add(str);
//        }
    }

    private void refesh() {
    }

    public LCSetting getSetting() {
        return mSetting;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
