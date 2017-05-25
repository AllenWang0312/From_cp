package color.measurement.com.from_cp20.module.main;//package color.measurement.com.from_cp20.module.measure.lightcolor.v2.bean;//package color.measurement.com.from_cp20.module.measure.lightcolor.v2;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import color.measurement.com.from_cp20.R;
import color.measurement.com.from_cp20.module.been.Ins;
import color.measurement.com.from_cp20.manager.ins.Instrument;

/**
 * Created by wpc on 2017/3/31.
 */

@SuppressLint("ValidFragment")
public class InsInfoDialog extends DialogFragment {

    Ins mIns;
    Context mContext;
    @BindView(R.id.tv_type_info) TextView mTvTypeInfo;
    @BindView(R.id.tv_name_info) EditText mTvNameInfo;
    @BindView(R.id.tv_serial_info) TextView mTvSerialInfo;
    @BindView(R.id.tv_device_name_info) TextView mTvDeviceNameInfo;
    @BindView(R.id.tv_device_address_info) TextView mTvDeviceAddressInfo;
    Unbinder unbinder;

    public InsInfoDialog(Ins ins) {
        mIns = ins;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mContext = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("仪器信息");
        View v = LayoutInflater.from(mContext).inflate(R.layout.dialog_ins_info, null);
        unbinder = ButterKnife.bind(this, v);
        mTvTypeInfo.setText(Instrument.getTypeStringWithInstrumentName(mIns.getBleName(),mContext));
        mTvNameInfo.setText(mIns.getBleName());
//        mTvSerialInfo.setText();
        mTvDeviceNameInfo.setText(mIns.getBleName());
        mTvDeviceAddressInfo.setText(mIns.getAddress());

        builder.setView(v);
        builder.setPositiveButton("确定", null);
        return builder.create();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
