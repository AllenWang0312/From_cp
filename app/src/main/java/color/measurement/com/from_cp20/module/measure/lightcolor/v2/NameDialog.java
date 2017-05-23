package color.measurement.com.from_cp20.module.measure.lightcolor.v2;//package color.measurement.com.from_cp20.module.measure.lightcolor.v2.bean;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import color.measurement.com.from_cp20.R;
import color.measurement.com.from_cp20.manager.NumEndedString;

/**
 * Created by wpc on 2017/4/17.
 */
@SuppressLint("ValidFragment")
public class NameDialog extends DialogFragment {


    Context mContext;
    View.OnClickListener mOnClickListener, mstartTest;

    public EditText et_name;
    public EditText et_tips;
    Button bt_start;
    Button bt_qx;
    Button bt_qd;
    NumEndedString name;
    String tips;

    public NameDialog(@Nullable NumEndedString name, @Nullable String tips) {
        this.name = name;
        if(name!=null){
            this.name.numIncreases();
        }
        this.tips = tips;
    }

    public void setListener(View.OnClickListener onClickListener, View.OnClickListener startClickListener) {
        mOnClickListener = onClickListener;
        mstartTest = startClickListener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mContext = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("提示");
        View v = LayoutInflater.from(mContext).inflate(R.layout.named_layout, null);

        et_name = (EditText) v.findViewById(R.id.et_content_named);
        et_tips = (EditText) v.findViewById(R.id.et_tips_named);

        et_name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

            }
        });

        if (name != null) et_name.setText(name.toString());
        if (tips != null) et_tips.setText(tips);

        bt_qd = (Button) v.findViewById(R.id.bt_queding);
        bt_qx = (Button) v.findViewById(R.id.bt_quxiao);
        bt_start = (Button) v.findViewById(R.id.bt_named_start_test);

        bt_qd.setOnClickListener(mOnClickListener);
        bt_qx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        bt_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnClickListener.onClick(v);
                mstartTest.onClick(v);
            }
        });
        builder.setView(v);
//        builder.setNegativeButton("取消", null);
        setCancelable(false);
        return builder.create();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
