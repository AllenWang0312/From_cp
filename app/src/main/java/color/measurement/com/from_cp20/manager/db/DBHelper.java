package color.measurement.com.from_cp20.manager.db;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import color.measurement.com.from_cp20.common.util.ParcelUtil;
import color.measurement.com.from_cp20.manager.ins.Instrument;
import color.measurement.com.from_cp20.module.been.Ins;
import color.measurement.com.from_cp20.module.been.interfaze.CompareableData;
import color.measurement.com.from_cp20.module.been.data.LightColorData;
import color.measurement.com.from_cp20.module.been.data.LustreData;
import color.measurement.com.from_cp20.module.been.wapper.FormattingTable;
import color.measurement.com.from_cp20.module.been.wapper.GroupData;
import color.measurement.com.from_cp20.util.blankj.FileUtils;

/**
 * Created by wpc on 2017/4/13.
 */

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DBConsts.CREATE_USER_TABLE);
        db.execSQL(DBConsts.CREATE_INSTRUMENT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public static ArrayList<Ins> initProductDataFromDataBase(SQLiteDatabase db, Integer id) {
        ArrayList<Ins> ins = new ArrayList<>();
        Cursor c = db.rawQuery("select * from " + DBConsts.INS_TAB_NAME + " where userId = ? ", new String[]{String.valueOf(id)});
        if (c.getCount() > 0) {
            if (c.moveToFirst()) {
                do {
                    Ins i = new Ins(c);
//                    String absPath = c.getString(c.getColumnIndex("cacheAbsPath"));
//                    File f = new File(absPath);
//                    if (f.exists()) {
//                        BluetoothDevice device = BluetoothDevice.CREATOR.createFromParcel(ParcelUtil.unmarshall(FileUtils.readFile2Bytes(f)));
//                        Log.i(device.getAddress(), device.getBondState() == BluetoothDevice.BOND_BONDED ? "bounded" : "none");
//                        i.setDevice(device);
//                    }
                    ins.add(i);
                } while (c.moveToNext());
            }
        }
        return ins;
    }

    public static ArrayList<Ins> initProductDataFromFiles(String datapath, Context context) {
        ArrayList<Ins> mProductDevicess = new ArrayList<>();
//        mProductDevicess.add(new DevicesInfo("name%address%serial"));
        File file = new File(datapath);
        FileUtils.createOrExistsDir(datapath);
        String[] files = file.list();
        if (files != null) {
            for (String str : files) {
                mProductDevicess.add(Ins.initProductFromFile(datapath, str, context));
            }
        }
        return mProductDevicess;
    }

    public static BluetoothDevice getDeviceFromFile(String absPath) {
        File f = new File(absPath);
        return BluetoothDevice.CREATOR.createFromParcel(ParcelUtil.unmarshall(FileUtils.readFile2Bytes(f)));
    }

    public static ArrayList<String> getTableNames(SQLiteDatabase db, int type, String userName) {
        ArrayList<String> tables = new ArrayList<>();
        Cursor c = db.rawQuery("select * from " + DBConsts.INS_TAB_NAME + " where type = ? and userName = ?", new String[]{String.valueOf(type), userName});
        if (c.moveToFirst()) {
            do {
                tables.add(c.getString(c.getColumnIndex("dataTableName")));
            } while (c.moveToNext());
        }
        c.close();
        return tables;
    }

    public static String getTableName(SQLiteDatabase db, String user_name, String ble_address) {
        Cursor c = db.rawQuery("select * from " + DBConsts.INS_TAB_NAME + " where userName = ? and address = ?", new String[]{user_name, ble_address});
        if (c.getCount() > 0) {
            if (c.moveToFirst()) {
                do {
                    return c.getString(c.getColumnIndex("dataTableName"));
                } while (c.moveToNext());
            }
        }
        c.close();
        return null;
    }

    public static ArrayList<String> getIns_Info(SQLiteDatabase db, int type) {
        ArrayList<String> ins_info = new ArrayList<>();
        Cursor c = db.rawQuery("select * from " + DBConsts.INS_TAB_NAME + " where type = ?", new String[]{String.valueOf(type)});
        if (c.moveToFirst()) {
            do {
                ins_info.add(
                        c.getString(c.getColumnIndex("bleName"))
                                + "_"
                                + c.getString(c.getColumnIndex("address"))
                );
            } while (c.moveToNext());

        }
        c.close();
        return ins_info;
    }

    public static ArrayList<LightColorData> getStandData(SQLiteDatabase db, String name) {
        ArrayList<LightColorData> datas = new ArrayList<>();
        Cursor c = db.query(name, null, null, null, null, null, null);
        if (c.moveToFirst()) {
            do {
                boolean isStandard = c.getInt(c.getColumnIndex("isStandard")) == 1;
                if (isStandard) {
                    datas.add(new LightColorData(c));
                }
            } while (c.moveToNext());
        }
        c.close();
        Log.i("getStandData", "name=" + name + datas.size());
        return datas;
    }

    public static ArrayList<GroupData<CompareableData>> getGroups(SQLiteDatabase db, String name) {
        ArrayList<String> stand_names = new ArrayList<>();
        ArrayList<GroupData<CompareableData>> datas = new ArrayList<>();
        int type = Instrument.tableType(name);
        Cursor c = db.query(name, null, null, null, null, null, null);
        if (c.moveToFirst()) {
            do {
                String stand_name = c.getString(c.getColumnIndex("stand_name"));
                boolean isStandard = c.getInt(c.getColumnIndex("isStandard")) == 1;
                if (!stand_names.contains(stand_name)) {
                    datas.add(0, new GroupData());
                    stand_names.add(0, stand_name);
                }
                if (isStandard) {

                    datas.get(stand_names.indexOf(stand_name)).setStand(type == 0 ? new LightColorData(c) : type == 2 ? new LustreData(c) : null);
                } else {
                    datas.get(stand_names.indexOf(stand_name)).addTest(type == 0 ? new LightColorData(c) : type == 2 ? new LustreData(c) : null);
                }

            } while (c.moveToNext());
        }
        c.close();
        Log.i("getStandData", "name=" + name + datas.size());
        return datas;
    }

    public static ArrayList<CompareableData> getStands(ArrayList<GroupData<CompareableData>> groups) {
        ArrayList<CompareableData> stands = new ArrayList<>();
        for (GroupData<CompareableData> group : groups) {
            stands.add(group.getStand());
        }
        return stands;
    }

    public static FormattingTable<LightColorData> getDisplayDataWithTableName(SQLiteDatabase db, String tablename) {
        ArrayList<String> standNames = new ArrayList<>();
        HashMap<String, GroupData<LightColorData>> datas = new HashMap<>();
        Cursor c = db.query(tablename, null, null, null, null, null, null);
        if (c.moveToFirst()) {
            do {
                String stand_name = c.getString(c.getColumnIndex("stand_name"));
                LightColorData data = new LightColorData(c);
                if (!datas.keySet().contains(stand_name)) {
                    datas.put(stand_name, new GroupData<LightColorData>());
                    standNames.add(stand_name);
                    datas.get(stand_name).setStand(data);
                } else {
                    datas.get(stand_name).addTest(data);
                }

            } while (c.moveToNext());
            c.close();

        }
        return new FormattingTable(standNames, datas);
    }

    public String getDeviceFilePath(SQLiteDatabase db, String name, String address) {
        Cursor c = db.rawQuery("select * from " + DBConsts.INS_TAB_NAME + " where userName = ? and address = ? ", new String[]{name, address});
        if (c.getCount() == 1) {
            c.moveToFirst();
            return c.getString(c.getColumnIndex("cacheAbsPath"));
        }
        return null;
    }
}
