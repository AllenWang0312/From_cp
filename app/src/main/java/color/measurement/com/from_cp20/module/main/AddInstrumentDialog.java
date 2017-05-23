package color.measurement.com.from_cp20.module.main;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import color.measurement.com.from_cp20.R;
import color.measurement.com.from_cp20.manager.Ble_4.BlueToothManagerForBLE;
import color.measurement.com.from_cp20.manager.ins.Instrument;
import color.measurement.com.from_cp20.module.been.Instrument.Ins;

/**
 * Created by wpc on 2017/3/14.
 */
@SuppressLint("ValidFragment")
public class AddInstrumentDialog extends DialogFragment {


    public AddInstrumentDialog() {

    }

    private String[] productsType = {"分光测色", "色差", "光泽度", "拉曼"};
    private int[] productsIds = {R.array.products_fgcs, R.array.products_gzd, R.array.products_sc, R.array.products_lm};
    Activity mContext;

//    @BindView(R.id.swipe_container) SwipeRefreshLayout mSwipeContainer;

    AdapterView.OnItemClickListener
//            mv4ListClick,
            mv2ListClick;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
//                case 0:
//                    mLv4AddDialog.setAdapter(new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, getDevicesNames(V4_devices)));
//                    break;
                case 1:
                    if(mLv2AddDialog!=null){
                        mLv2AddDialog.setAdapter(
//                            new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, getDevicesNames(V2_devices))
                                new ScanDeviceResultAdapter(V2_devices)
                        );
                    }
                    break;
            }

        }
    };
    BlueToothManagerForBLE mManagerForBLE;

    ArrayList<String>
//            v4_address,
            v2_address;
    ArrayList<Ins>
//            V4_devices,
            V2_devices;


    Unbinder unbinder;
    //    @BindView(R.id.lv4_add_dialog) ListView mLv4AddDialog;
    @BindView(R.id.lv2_add_dialog) ListView mLv2AddDialog;

    //    @BindView(R.id.pb_v4_addinstrument) ProgressBar mPbv4Addinstrument;
    @BindView(R.id.pb_v2_addinstrument) ProgressBar mPbv2Addinstrument;
//    @BindView(R.id.exp_add_dialog) ExpandableListView mExpAddDialog;

    //    @BindView(R.id.ll_v4) LinearLayout mLlV4;
    @BindView(R.id.ll_v2) LinearLayout mLlV2;
    @BindView(R.id.ll_add) LinearLayout mLlAdd;

    //    @BindView(R.id.fab_add_left) FloatingActionButton mFabAddLeft;
    @BindView(R.id.fab_addv2_left) FloatingActionButton mFabAddv2Left;
    @BindView(R.id.fab_add_right) FloatingActionButton mFabAddRight;


    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                case BluetoothDevice.ACTION_FOUND:
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    if (!v2_address.contains(device.getAddress())) {
                        v2_address.add(device.getAddress());
                        V2_devices.add(new Ins(device, mContext, null, null));
                        mHandler.sendEmptyMessage(1);
                    }
                    break;
                case BluetoothAdapter.ACTION_DISCOVERY_STARTED:
                    if(mPbv2Addinstrument!=null){
                        mPbv2Addinstrument.setVisibility(View.VISIBLE);
                    }

                    break;
                case BluetoothAdapter.ACTION_DISCOVERY_FINISHED:
                    if(mPbv2Addinstrument!=null){
                        mPbv2Addinstrument.setVisibility(View.GONE);
                    }
                    break;
            }
        }
    };
//    private BluetoothAdapter.LeScanCallback mLeScanCallback =
//            new BluetoothAdapter.LeScanCallback() {
//                @Override
//                public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
//                    final BleUtil.BleAdvertisedData badata = BleUtil.parseAdertisedData(scanRecord);
//                    String name = device.getName();
//                    if (StringUtils.isEmpty(name)) {
//                        name = badata.getName();
//                    }
//                    if (!v4_address.contains(device.getAddress())) {
//                        Ins ins = new Ins(device, mContext, name, null);
//                        V4_devices.add(ins);
//                        v4_address.add(ins.getAddress());
//                        mHandler.sendEmptyMessage(0);
//                    }
//                }
//            };

    AddInstrumentDialog(Activity context, BlueToothManagerForBLE ManagerForBLE,
//                        AdapterView.OnItemClickListener v4lvClickListener,
                        AdapterView.OnItemClickListener v2lvClickListener) {
        mContext = context;
        mManagerForBLE = ManagerForBLE;
//        mManagerForBLE.v4manager.registScanCallback(mLeScanCallback);
//        mv4ListClick = v4lvClickListener;
        mv2ListClick = v2lvClickListener;
        context.registerReceiver(mBroadcastReceiver, getFilter());

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog dialog = new Dialog(getActivity(), R.style.NoPaddingDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // 设置Content前设定
        dialog.setContentView(R.layout.dialog_addinstrument);
        dialog.setCanceledOnTouchOutside(true); // 外部点击取消

        // 设置宽度为屏宽, 靠近屏幕底部。
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.TOP;
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(lp);

        unbinder = ButterKnife.bind(this, dialog);

//        mFabAddLeft.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mLlV4.getVisibility() == View.GONE) {
//                    mLlV4.setVisibility(View.VISIBLE);
//                    mLlV2.setVisibility(View.GONE);
//                    mLlAdd.setVisibility(View.GONE);
//                } else {
//                    if (mManagerForBLE.v4manager.isScanning) {
//                        mPbv4Addinstrument.setVisibility(View.GONE);
//                        mManagerForBLE.v4manager.scanDevices(false);
//                    } else {
//                        if (mManagerForBLE.isEnable()) {
//                            mPbv4Addinstrument.setVisibility(View.VISIBLE);
//                            V4_devices = new ArrayList<>();
//                            v4_address = new ArrayList<>();
//                            mManagerForBLE.v4manager.scanDevices(true);
//                        } else {
//                            mManagerForBLE.openBlueTooth();
//                        }
//                    }
//                }
//            }
//        });
        mFabAddv2Left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLlV2.getVisibility() == View.GONE) {
                    mLlV2.setVisibility(View.VISIBLE);
                    mLlAdd.setVisibility(View.GONE);
                } else {
                    if (!mManagerForBLE.v2manager.isScanning) {
//                        mPbv2Addinstrument.setVisibility(View.GONE);
//                        mManagerForBLE.v2manager.scanDevices(false);
                        mPbv2Addinstrument.setVisibility(View.VISIBLE);
                        mManagerForBLE.v2manager.scanDevices(true);
                        V2_devices = new ArrayList<Ins>();
                        v2_address = new ArrayList<String>();
                    }
                }
            }
        });

        mFabAddRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLlV2.setVisibility(View.GONE);
                mLlAdd.setVisibility(View.VISIBLE);
            }
        });
        mLv2AddDialog.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mv2ListClick.onItemClick(parent, view, position, id);
                dismiss();
            }
        });
//        mLv4AddDialog.setOnItemClickListener(
//                new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                        mv4ListClick.onItemClick(parent, view, position, id);
//                        dismiss();
//                    }
//                }

//                new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if (mManagerForBLE.getBlueScanner().isDiscovering()) {
//                    mManagerForBLE.scanDevices(false);
//                    mManagerForBLE.getBlueScanner().setDiscovering(false);
//                }
////                    this.dismiss();
//                if (mManagerForBLE.connectToDevice(devices.get(position))) {
//                    mContext.startActivity(new Intent(mContext, LightColorActivity.class));
//                }
//            }
//        }
//        );
//        builder.setView(view);
//
//        Dialog dialog=builder.create();
//         设置宽度为屏宽, 靠近屏幕底部。
//        Window window= dialog.getWindow();
//        window.getDecorView().setPadding(0,0,0,0);
//        WindowManager.LayoutParams lp = window.getAttributes();
//        lp.gravity = Gravity.TOP; // 紧贴底部
//        lp.width = WindowManager.LayoutParams.MATCH_PARENT; // 宽度持平
//       window.setAttributes(lp);
        return dialog;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {

        if (mManagerForBLE.v2manager.isScanning) {
            mManagerForBLE.v2manager.scanDevices(false);
            mManagerForBLE.v2manager.disconnect();
        }
        super.onDismiss(dialog);
    }

    public Ins getV2Device(int index) {
        return V2_devices.get(index);
    }


//    Ins getV4Device(int index) {
//        return V4_devices.get(index);
//    }

//    //搜索蓝牙设备相关方法
//    private static ArrayList<String> getDrivesInfo(ArrayList<BluetoothDevice> bluetoothDevices) {
//        return null;
//    }

//    private ArrayList<String> getDevicesNames(ArrayList<Ins> bluetoothDevices) {
//        ArrayList<String> names = new ArrayList<>();
//        for (Ins device : bluetoothDevices) {
//            names.add(device.getBleName() + "_" + device.getAddress());
//        }
//        return names;
//    }

    public IntentFilter getFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        return filter;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    class ScanDeviceResultAdapter extends BaseAdapter {

        Context mContext;
        ArrayList<Ins> result;

        ScanDeviceResultAdapter(ArrayList<Ins> result) {
            this.result = result;
            mContext = getActivity();
        }

        @Override
        public int getCount() {
            return result.size();
        }

        @Override
        public Object getItem(int position) {
            return result.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder vh = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_scan_result, null);
                vh = new ViewHolder(convertView);
                convertView.setTag(vh);
            } else {
                vh = (ViewHolder) convertView.getTag();
            }
            Ins ins = result.get(position);
            vh.name.setText(ins.getBleName());
            vh.type.setText("(" + Instrument.getTypeStringWithType(ins.getType()) + ")");
            vh.address.setText(ins.getAddress());
            return convertView;
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView name, type, address;

            public ViewHolder(View itemView) {
                super(itemView);
                name = (TextView) itemView.findViewById(R.id.tv_ble_name);
                type = (TextView) itemView.findViewById(R.id.tv_ins_type_string);
                address = (TextView) itemView.findViewById(R.id.tv_ble_address);
            }
        }
    }
}
