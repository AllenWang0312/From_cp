package color.measurement.com.from_cp20.module.measure.lightcolor;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.HashMap;

import color.measurement.com.from_cp20.manager.sp.SPConsts;
import color.measurement.com.from_cp20.R;
import color.measurement.com.from_cp20.module.been.data.LightColorData;
import color.measurement.com.from_cp20.util.java.DecimalUtil;

/**
 * Created by wpc on 2017/1/11.
 */
@SuppressLint("ValidFragment")
public class DetialDialog extends DialogFragment {

    public static final int[] type_id = {R.array.Lab, R.array.Luv, R.array.LCh, R.array.HunterLab, R.array.XYZ, R.array.Yxy, R.array.RGB,
            R.array.MSE_HVC,
    };

    Context mContext;
    SharedPreferences sp;
    LayoutInflater mInflater;
    String name, tips;

    Spinner mSpinner;
    LinearLayout lift, right;
    EditText et1, et2;
    LightColorData mLightColorData;
    HashMap<String, Double> datas;
    DialogInterface.OnClickListener onPositiveBtnClick, onNavigetionBtnClick;

    public DetialDialog(Context context, LightColorData sim) {
        mContext = context;
        sp=mContext.getSharedPreferences(SPConsts.PREFERENCE_LIGHT_COLOR,Context.MODE_PRIVATE);
        mInflater = LayoutInflater.from(context);
        mLightColorData = sim;
        this.name = sim.getName();
        this.tips = sim.getTips();
        datas = sim.getResultData(sp).toHashMap();

    }

    public void setListener(DialogInterface.OnClickListener onClickListener,
                            DialogInterface.OnClickListener onNagevation) {
        onPositiveBtnClick = onClickListener;
        onNavigetionBtnClick = onNagevation;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("详细信息");
        View v = mInflater.inflate(R.layout.detial_dialog, null);

        lift = (LinearLayout) v.findViewById(R.id.ll_lift_detial);
        right = (LinearLayout) v.findViewById(R.id.ll_right_detial);

        mSpinner = (Spinner) v.findViewById(R.id.sp_detial_dialog);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                lift.removeAllViews();
                right.removeAllViews();
                String[] titles = getResources().getStringArray(type_id[position]);
                for (String str : titles) {
                    TextView tv = new TextView(mContext);
                    tv.setText(str);
                    tv.setGravity(Gravity.CENTER);
                    tv.setMaxLines(1);
                    tv.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    lift.addView(tv);
                    double result = datas.get(str);
                    TextView tv2 = new TextView(mContext);
                    tv2.setText(DecimalUtil.getFormatDouble(result, 5, 4));
                    tv2.setGravity(Gravity.CENTER);
                    tv2.setMaxLines(1);
                    tv2.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    right.addView(tv2);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        et1 = (EditText) v.findViewById(R.id.et1_detial_dialog);
        et1.setText(name);
        et1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                name = et1.getText().toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        et2 = (EditText) v.findViewById(R.id.et2_detial_dialog);
        et2.setText(tips);
        et2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tips = et2.getText().toString();
                Log.i("tips", tips);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        builder.setView(v);
        builder.setNeutralButton("删除", onNavigetionBtnClick);
        builder.setPositiveButton("确认修改", onPositiveBtnClick);
//        builder.setNeutralButton("设为标样", null);
        builder.setNegativeButton("取消", null);
        return builder.create();
    }

    public String getName() {
        return name;
    }

    public String getNameFromET() {
        return et1.getText().toString();
    }

    public String getTipsFromET() {
        return et2.getText().toString();
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    public void setPositiveBt(DialogInterface.OnClickListener onClick) {
        this.onPositiveBtnClick = onClick;
    }
}
