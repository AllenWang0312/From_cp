package color.measurement.com.from_cp20.manager.Ble_4;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import color.measurement.com.from_cp20.manager.Ble_4.bean.BLE_Order;
import color.measurement.com.from_cp20.manager.Ble_4.bean.BleDefinedUUIDs;
import color.measurement.com.from_cp20.manager.Ble_4.bean.DataPackager;
import color.measurement.com.from_cp20.manager.Ble_4.bean.LightColorDataPackager;
import color.measurement.com.from_cp20.module.App;
import color.measurement.com.from_cp20.util.java.clsPublic;
import color.measurement.com.from_cp20.util.utils.L;

import static color.measurement.com.from_cp20.manager.Ble_4.Ble_Consts.INIT_SUCCESS;

/**
 * Created by wpc on 2016/12/6.
 */

public class BlueToothManagerForBLE {


    private static final int REQUEST_ENABLE_BT = 1;

//    public static final String INSTRUMENT_BUTTON_PREASED = "com.color.measurement.instrument_button_preased";

    private static Activity mContext;
    private static BlueToothManagerForBLE instance;

    public static boolean isConnected = false;

    public static BluetoothAdapter mBluetoothAdapter;
    public static V4 v4manager;
    public static V2 v2manager;

    public static BlueToothManagerForBLE getInstance(Activity context) {
        mContext = context;
        if (instance == null) {
            instance = new BlueToothManagerForBLE(context);
        }
        return instance;
    }

    private BlueToothManagerForBLE(Activity context) {
        mContext = context;
        if (!context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(context, "设备不支持 no BluetoothManager found", Toast.LENGTH_SHORT).show();
        } else {
            BluetoothManager mBluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
            mBluetoothAdapter = mBluetoothManager.getAdapter();
            if (mBluetoothAdapter == null) {
                Toast.makeText(context, "设备不支持 no BluetoothAdapter  found", Toast.LENGTH_SHORT).show();
            }
        }
        v4manager = new V4();
        v2manager = new V2();
    }

    public void checkEnable() {
        if (!mBluetoothAdapter.isEnabled()) {
            mBluetoothAdapter.enable();
        }
    }


    public class V4 {
        //v4
        private BluetoothGatt mBluetoothGatt;
        private BluetoothGattService mBTService = null;
        private BluetoothGattCharacteristic mBTValueCharacteristic = null;


        private BluetoothAdapter.LeScanCallback mLeScanCallback;
        public boolean isScanning = false;

        public void scanDevices(boolean scan) {
            if (scan) {
                if (mBluetoothAdapter != null) {
                    if (mLeScanCallback != null) {
                        isScanning = true;
                        mBluetoothAdapter.startLeScan(mLeScanCallback);
                    }
                }
            } else {
                if (mLeScanCallback != null) {
                    isScanning = false;
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                }
            }
        }

        public void connectToDevice(BluetoothDevice device) {
            mBluetoothGatt = device.connectGatt(mContext, true, mGattCallback);
        }

        final private UUID fgce_service = BleDefinedUUIDs.Service.FGCS_SERVICE;
        final private UUID fgce_characteristic = BleDefinedUUIDs.Characteristic.FGCS_CHARACTERISTIC;

        public DataPackager getDataPackager() {
            return mDataPackager;
        }

        DataPackager mDataPackager;
        private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
            @Override
            public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
                if (newState == BluetoothProfile.STATE_CONNECTED) {//连接成功
                    mBluetoothGatt.discoverServices();//连接成功后就去找出该设备中的服务
                    int type = App.logged_user.getConnectIns().getType();
                    switch (type) {
                        case 0:
                            mDataPackager = new LightColorDataPackager(mContext);
                            break;
                        case 1:
                            break;
                    }
                } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {//连接失败
                    Logger.i("连接失败");
                }
            }

            @Override//当设备是否找到服务时，会回调该函数
            public void onServicesDiscovered(BluetoothGatt gatt, int status) {
                if (status == BluetoothGatt.GATT_SUCCESS) {//找到服务了
                    L.i(gatt.toString());
                    //在这里可以对服务进行解析，寻找到你需要的服务
                    mBTService = mBluetoothGatt.getService(fgce_service);
                    if (mBTService == null) {
                    } else {
                        L.i("Service successfully retrieved");
                        getHrCharacteristic();
                    }
                } else {
                }
            }


            @Override//当向设备Descriptor中写数据时，会回调该函数
            public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
                super.onDescriptorWrite(gatt, descriptor, status);
            }

            @Override//设备发出通知时会调用到该接口
            public void onCharacteristicChanged(BluetoothGatt gatt,
                                                BluetoothGattCharacteristic characteristic) {
                L.i(clsPublic.bytesToHexString(characteristic.getValue()));
                byte[] item = characteristic.getValue();
                mDataPackager.receiverData(item);
                if (item.length == 5 && item[1] == 0x10) {
                    writeToCharacteristic(BLE_Order.LC.TEST_DATA);
                }
            }

            //读取到的数据存在characteristic当中，可以通过characteristic.getValue();函数取出。然后再进行解析操作。
            //int charaProp = characteristic.getProperties();if ((charaProp | BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0)表示可发出通知。  判断该Characteristic属性
            @Override//当读取设备时会回调该函数
            public void onCharacteristicRead(BluetoothGatt gatt,
                                             BluetoothGattCharacteristic characteristic,
                                             int status) {
                L.i(clsPublic.bytesToHexString(characteristic.getValue()));
            }

            @Override
            public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
                L.i(clsPublic.bytesToHexString(characteristic.getValue()));
            }

            @Override
            public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
            }
        };

        private void getHrCharacteristic() {
            mBTValueCharacteristic = mBTService.getCharacteristic(fgce_characteristic);
            if (mBTValueCharacteristic == null) {
            } else {
                enableNotificationForHr();
            }
        }

        private void enableNotificationForHr() {
            boolean success = mBluetoothGatt.setCharacteristicNotification(mBTValueCharacteristic, true);
            if (!success) {
                return;
            } else {
                Logger.i("Enabling notification success! connect success");
                mContext.sendBroadcast(new Intent(INIT_SUCCESS));
                isConnected = true;
                //                writeToCharacteristic(BLE_Order.getPowerData());
            }

//        BluetoothGattDescriptor descriptor = mBTValueCharacteristic.getDescriptor(BleDefinedUUIDs.Descriptor.CHAR_CLIENT_CONFIG);
//        if (descriptor != null) {
//            descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
//            mBluetoothGatt.writeDescriptor(descriptor);
//            L.i("enableNotificationForHr", "Notification enabled");
//        } else {
//            L.i("enableNotificationForHr", "Could not get descriptor for characteristic! Notification are not enabled.");
//        }
        }

        public boolean writeToCharacteristic(byte[] bytes) {
            if (mBTValueCharacteristic == null) {
                return false;
            }
            final int charaProp = mBTValueCharacteristic.getProperties();
            if ((charaProp | BluetoothGattCharacteristic.PROPERTY_READ) > 0) {
                // If there is an active notification on a characteristic, clear
                // it first so it doesn't update the data field on the user interface.
                //读取数据，数据将在回调函数中
                //mBluetoothLeService.readCharacteristic(characteristic);
//                byte[] value = new byte[20];
//                value[0] = (byte) 0x00;
//                if (edittext_input_value.getText().toString().equals("")) {
//                    Toast.makeText(getApplicationContext(), "请输入！", Toast.LENGTH_SHORT).show();
//                    return;
//                } else {
//                mBTValueCharacteristic.setValue(value[0], BluetoothGattCharacteristic.FORMAT_UINT8, 0);
                mBTValueCharacteristic.setValue(bytes);
                if (mBluetoothGatt.writeCharacteristic(mBTValueCharacteristic)) {
                    Logger.i("写入成功");
                    return true;
                }
//                    Toast.makeText(getApplicationContext(), "写入成功！", Toast.LENGTH_SHORT).show();
//                }
            }
//            if ((charaProp | BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0) {
//                mNotifyCharacteristic = characteristic;
//                mBluetoothLeService.setCharacteristicNotification(characteristic, true);
//            }
//            edittext_input_value.setText("");

            return false;
        }

        public boolean isConnected = false;

        public void disconnect() {
//        mBluetoothAdapter = null;
//        mBluetoothDevices = null;
            isConnected = false;
            if (mBluetoothGatt != null) {
                mBluetoothGatt.close();
                mBluetoothGatt.disconnect();
            }
            mBTService = null;
            mBTValueCharacteristic = null;
//        mServiceConnection = null;
            //        mContext = null;

//        mBluetoothLeService = null;
//        mContext.finish();
//        mSelectBLEDriveDialog = null;
//        mBluetoothLeService=null;
        }

//    public ServiceConnection getServiceConnection() {
//        return mServiceConnection;
//    }
//
//    public void setServiceConnection(ServiceConnection serviceConnection) {
//        mServiceConnection = serviceConnection;
//    }

        public void registScanCallback(BluetoothAdapter.LeScanCallback leScanCallback) {
            mLeScanCallback = leScanCallback;
        }

//    public ArrayList<ArrayList<BluetoothGattCharacteristic>> getGattCharacteristics() {
//        return mGattCharacteristics;
//    }
//
//    public void setGattCharacteristics(ArrayList<ArrayList<BluetoothGattCharacteristic>> gattCharacteristics) {
//        mGattCharacteristics = gattCharacteristics;
//    }

//    public static int getPower() {
//        return power;
//    }
//
//    public static void setPower(int power) {
//        BlueToothManagerForBLE.power = power;
//    }
//
//    public static byte[] getInstrumentId() {
//        return instrumentId;
//    }
//
//    public static void setInstrumentId(byte[] instrumentId) {
//        BlueToothManagerForBLE.instrumentId = instrumentId;
//    }


        //    public boolean wirteCharacteristic(BluetoothGattCharacteristic characteristic) {
//
//        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
//            Log.w(TAG, "BluetoothAdapter not initialized");
//        } else {
//            return;
//        }
//        return false;
//    }

    }

    public class V2 {
        public static final String uuid = "00001101-0000-1000-8000-00805F9B34FB";
        public boolean isScanning = false;

        public void scanDevices(boolean scan) {
            if (scan) {
                if (mBluetoothAdapter != null) {
                    isScanning = true;
                    mBluetoothAdapter.startDiscovery();
                }
            } else {
                isScanning = false;
                mBluetoothAdapter.cancelDiscovery();
            }
        }

        //2.0
        BluetoothSocket mBluetoothSocket;
        InputStream in = null;
        OutputStream out = null;

        //连接成功与否 可预见
        public boolean connectTo_2_Device(String address) {
            BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
            try {
//                Method m = device.getClass().getMethod(
//                        "createRfcommSocket", new Class[]{int.class});
//                mBluetoothSocket = (BluetoothSocket) m.invoke(device, 1);//这里端口为1

//                mBluetoothSocket = device.createInsecureRfcommSocketToServiceRecord( UUID.fromString(uuid));
                if(device.getUuids()==null){
                    mBluetoothSocket = device.createInsecureRfcommSocketToServiceRecord( UUID.fromString(uuid));
                }else {
                    mBluetoothSocket = device.createInsecureRfcommSocketToServiceRecord(device.getUuids()[0].getUuid());
                }

//                device.getUuids()[0].getUuid();
            } catch (IOException e) {
                e.printStackTrace();
            }
//            catch (NoSuchMethodException e) {
//                e.printStackTrace();
//            } catch (IllegalAccessException e) {
//                e.printStackTrace();
//            } catch (InvocationTargetException e) {
//                e.printStackTrace();
//            }
            try {
                mBluetoothSocket.connect();
                return true;
            } catch (IOException e) {
//                mBluetoothSocket.close();
                e.printStackTrace();
            }
            return false;
//            try {
//                mBluetoothSocket = device.createInsecureRfcommSocketToServiceRecord(uuid);
//                mBluetoothSocket.connect();
//                return true;
//            } catch (IOException e1) {
//                e1.printStackTrace();
//            }
//            return false;
        }

        public byte[] whiteToSocket(byte[] data, int length) {
            int index = 0;
            byte[] re = new byte[length];
            try {

                Log.i("mBluetoothSocket",mBluetoothSocket.toString());
                in = mBluetoothSocket.getInputStream();
                out = mBluetoothSocket.getOutputStream();
                out.write(data);
                do {
                    byte[] result = new byte[64];
                    int i = in.read(result);
                    System.arraycopy(result, 0, re, index, i);
                    index += i;
                } while (index != length);
                return re;
            } catch (IOException e) {

                e.printStackTrace();
            } finally {
                try {
                    out.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        public void disconnect() {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                in = null;
            }
            if (out != null) {
//                    out.flush();
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                out = null;
            }
            if (mBluetoothSocket != null)
                try {
                    mBluetoothSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            mBluetoothSocket = null;
            Log.i("socket", "null");
            Log.i("ble v2manage", "disconnect");
        }

    }

    public void openBlueToothIfNeed() {
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            mContext.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
    }

    public boolean checkPermission(Activity context) {
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.BLUETOOTH_ADMIN)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(context,
                    Manifest.permission.BLUETOOTH_ADMIN)) {
                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(context,
                        new String[]{Manifest.permission.READ_CONTACTS},
                        1);
                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {

            //
        }
        ActivityCompat.requestPermissions(context,
                new String[]{Manifest.permission.BLUETOOTH_ADMIN},
                1);
        return true;
    }
}
