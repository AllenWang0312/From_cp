package color.measurement.com.from_cp20.module.database.lightcolor;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.Calendar;
import java.util.HashMap;

import color.measurement.com.from_cp20.R;
import color.measurement.com.from_cp20.util.blankj.StringUtils;

/**
 * Created by wpc on 2017/4/18.
 */
@SuppressLint("ValidFragment")
public class FindDialog extends DialogFragment {
    Context mContext;
    DialogInterface.OnClickListener mOnClickListener;

    public FindDialog() {

    }

    public void setPostiveListener(DialogInterface.OnClickListener posclick) {
        mOnClickListener = posclick;
    }

    HashMap<String, String> filters;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        filters = new HashMap<>();

        mContext = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("查找");
        final View v = LayoutInflater.from(mContext).inflate(R.layout.instrument_seach_dialog, null);
        final EditText et = (EditText) v.findViewById(R.id.et_seach);
        final RadioGroup rg = (RadioGroup) v.findViewById(R.id.rg_seach);
        et.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                Calendar date = Calendar.getInstance();
                int c_year = date.get(Calendar.YEAR);
                int c_month = date.get(Calendar.MONTH);
                int c_day = date.get(Calendar.DAY_OF_MONTH);

                new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
//                        app.select_mod = 0;
//                        checked_year = year;
//                        checked_mouth = month;
//                        checked_day = day;
                        et.setText(year + "-" + (month + 1) + "-" + day);
                    }
                }, c_year, c_month, c_day).show();
            }
        });

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String str = et.getText().toString().trim();
                if (!StringUtils.isEmpty(str)) {
                    filters.put("date", str);
                }
                String result = ((RadioButton) v.findViewById(rg.getCheckedRadioButtonId())).getText().toString();
                filters.put("result", result);
                Log.i("rg", result);
                mOnClickListener.onClick(dialog, which);
            }
        });

        builder.setView(v);

        return builder.create();
    }

    public HashMap<String, String> getFilters() {
        return filters;
    }
}
