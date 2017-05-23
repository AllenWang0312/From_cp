package color.measurement.com.from_cp20.module.measure.lustre;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;

import com.google.android.flexbox.FlexboxLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import color.measurement.com.from_cp20.R;
import color.measurement.com.from_cp20.common.interfaze.DismissCallback;
import color.measurement.com.from_cp20.manager.sp.SPConsts;

/**
 * Created by wpc on 2017/5/9.
 */
@SuppressLint("ValidFragment")
public class SettingDialog extends DialogFragment {

    String[] titles;
    DismissCallback mCallback;
    SharedPreferences sp;
    Context mContext;
    public SettingDialog(SharedPreferences sp,  DismissCallback callback) {
        this.sp = sp;

        mCallback = callback;
    }

    @BindView(R.id.ll_stand_angles) FlexboxLayout mLlStandAngles;
    @BindView(R.id.sp_test_times_lustre) Spinner mSpTestTimesLustre;

    @BindView(R.id.cb_remb_name_stand_lustre) CheckBox mCbRembNameStandLustre;
    @BindView(R.id.tl_set_stand_lustre) TableLayout mTlSetStandLustre;

    Unbinder unbinder;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mContext = getActivity();
        titles = mContext.getResources().getStringArray(R.array.gzd_angles_test);
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setCancelable(false);

        View v = LayoutInflater.from(mContext).inflate(R.layout.fragment_lustre_setting, null);
        unbinder = ButterKnife.bind(this, v);

        for (String str : titles) {
            CheckBox cb = new CheckBox(mContext);
            cb.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            cb.setText(str);
            cb.setChecked(sp.getBoolean(str, false));
            mLlStandAngles.addView(cb);
        }
        builder.setTitle("设置").setView(v);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferences.Editor et = sp.edit();
                et.putInt(SPConsts.AVERAGE_TIMES, mSpTestTimesLustre.getSelectedItemPosition() + 1);
                for (int i = 0; i < titles.length; i++) {
                    et.putBoolean(titles[i], ((CheckBox) mLlStandAngles.getChildAt(i)).isChecked());
                }
                et.putBoolean(SPConsts.STAND_AUTO_NAME, mCbRembNameStandLustre.isChecked());
                et.commit();
                mCallback.dismiss();
            }
        });
        builder.setNegativeButton("取消", null);
        return builder.create();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
