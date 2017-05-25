package color.measurement.com.from_cp20.module.been;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.support.annotation.Nullable;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import color.measurement.com.from_cp20.common.util.ParcelUtil;
import color.measurement.com.from_cp20.manager.ins.Instrument;
import color.measurement.com.from_cp20.module.App;
import color.measurement.com.from_cp20.module.been.interfaze.Data;
import color.measurement.com.from_cp20.util.blankj.FileUtils;
import color.measurement.com.from_cp20.util.blankj.StringUtils;
import color.measurement.com.openfilehelper.FileAndPath;

/**
 * 仪器抽象类
 * Created by wpc on 2017/4/5.
 */

public class Ins implements Data {

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    Integer userId;
    Integer type; //0 1 2 3

    String bleName;
    String address;

    //    String serical;
    String cachePath;
    String dataTableName;
    Integer deviceVersion;

    public Ins(Context context, String bleName, String address) {
        this.bleName = bleName;
        this.address = address;
        type = Instrument.getTypeWithInstrumentName(bleName, context);
    }

    public Ins(BluetoothDevice device, Context context, @Nullable String bleName, @Nullable String address) {
        userId = App.logged_user.service_id;
        if (bleName != null) {
            this.bleName = bleName;
        } else {
            this.bleName = device.getName();
        }
        type = Instrument.getTypeWithInstrumentName(this.bleName, context);

        if (address != null) {
            this.address = address;
        } else {
            this.address = device.getAddress();
        }
        deviceVersion = Instrument.getBleVersionWithInstrumentName(this.bleName, context);
        cachePath = FileAndPath.APP_INS_CACHE_PATH + "/" + this.bleName + "_" + this.address + "_" + deviceVersion;
    }

    public static Ins initProductFromFile(String dirPath, String name, Context context) {
        File f = new File(dirPath, name);
        String[] feature = name.split("_");
        BluetoothDevice device = BluetoothDevice.CREATOR.createFromParcel(ParcelUtil.unmarshall(FileUtils.readFile2Bytes(f)));
        Ins ins = new Ins(device, context, feature[0], feature[1]);
        if (StringUtils.isNumeric(feature[2])) {
            ins.setDevice_version(Integer.valueOf(feature[2]));
        }
        ins.setCachePath(dirPath + "/" + name);
        return ins;
    }

    public static Ins initProductFromFile(String absPath, Context context) {
        File f = new File(absPath);
        String name = absPath.substring(absPath.lastIndexOf("/") + 1, absPath.length());
        String[] feature = name.split("_");
        Ins ins = new Ins(context, feature[0], feature[1]);
        if (StringUtils.isNumeric(feature[2])) {
            ins.setDevice_version(Integer.valueOf(feature[2]));
        }
        ins.setCachePath(absPath);
        if (f.exists()) {
            BluetoothDevice device = BluetoothDevice.CREATOR.createFromParcel(ParcelUtil.unmarshall(FileUtils.readFile2Bytes(f)));
//            ins.setDevice(device);

        }
        return ins;
    }

    @Override
    public void setServiceId(int id) {

    }

    @Override
    public ContentValues getContentValue() {
        ContentValues v = new ContentValues();
        v.put("type", type);
        v.put("bleName", bleName);
        v.put("address", address);
        v.put("cacheAbsPath", cachePath);
        v.put("dataTableName", dataTableName);
        v.put("deviceVersion", deviceVersion);
        return v;
    }

    public Ins(Cursor c) {
        userId = c.getInt(c.getColumnIndex("userId"));
        type = c.getInt(c.getColumnIndex("type"));
        bleName = c.getString(c.getColumnIndex("bleName"));
        cachePath = c.getString(c.getColumnIndex("cacheAbsPath"));
        address = c.getString(c.getColumnIndex("address"));
        dataTableName = c.getString(c.getColumnIndex("dataTableName"));
        deviceVersion = c.getInt(c.getColumnIndex("deviceVersion"));
    }

    public Ins(ResultSet c) {
        try {
            userId = c.getInt("userId");
            type = c.getInt("type");
            bleName = c.getString("bleName");
            address = c.getString("address");
            dataTableName = c.getString("dataTableName");
            deviceVersion = c.getInt("deviceVersion");
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public HashMap<String, Object> toHashMapForMySql(Context context) {
        HashMap<String, Object> values = new HashMap<>();
        values.put("userId", userId);
        values.put("type", type);
        values.put("bleName", bleName);
//        values.put("cachePath", cachePath);
        values.put("address", address);
        values.put("dataTableName", dataTableName);
        values.put("deviceVersion", deviceVersion);
        return values;
    }


    public void setCachePath(String path) {
        cachePath = path;
    }

    public String getCachePath() {
        return cachePath;
    }

    public int getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getBleName() {
        return bleName;
    }

    public void setBleName(String bleName) {
        this.bleName = bleName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    public String getDataTableName() {
        return dataTableName;
    }

    public void setDataTableName(String dataTableName) {
        this.dataTableName = dataTableName;
    }

    public BluetoothDevice getDevice(BluetoothAdapter adapter) {
        return adapter.getRemoteDevice(address);
    }

    public boolean hasLocalCache() {
        File file = new File(cachePath);
        return file.exists();
    }

    public int getDevice_version() {
        return deviceVersion;
    }

    public void setDevice_version(int device_version) {
        this.deviceVersion = device_version;
    }


}
