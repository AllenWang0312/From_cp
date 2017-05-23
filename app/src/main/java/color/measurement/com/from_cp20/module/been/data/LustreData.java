package color.measurement.com.from_cp20.module.been.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import color.measurement.com.from_cp20.module.been.interfaze.CompareableData;
import color.measurement.com.from_cp20.util.java.clsPublic;
import color.measurement.com.from_cp20.util.utils.TimeUtils;

/**
 * Created by wpc on 2017/4/1.
 */

public class LustreData extends CompareableData {

    byte[] flag_data, native_data;
    ArrayList<Double> lustre;

    public LustreData(byte[] data) {
        flag_data = new byte[5];
        native_data = new byte[12];
        System.arraycopy(data, 0, flag_data, 0, 5);
        System.arraycopy(data, 5, native_data, 0, 12);
        if (flag_data[4] == 1) {
            //仪器繁忙
        }
        lustre = clsPublic.getDouble(native_data, 3);
        moment = System.currentTimeMillis();
        String t = TimeUtils.getTimeWithFormat(moment, TimeUtils.MONTH_DAY_NO_ZERO);
        String[] strs = t.split(" ");
        date = strs[0];
        time = strs[1];
    }


    public LustreData(ResultSet set) {
        try {
            this.stand_name=set.getString("stand_name");
            this.name=set.getString("name");
            this.tips=set.getString("tips");
            this.isStandard=set.getInt("isStandard")==1;
            this.date=set.getString("date");
            this.time=set.getString("time");
            this.moment=set.getLong("moment");
            this.result=set.getString("result");
            lustre=new ArrayList<>();
            lustre.add(set.getDouble("gzd_20"));
            lustre.add(set.getDouble("gzd_60"));
            lustre.add(set.getDouble("gzd_80"));
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public LustreData(Cursor c) {
        super(c);
        lustre = new ArrayList<>();
        lustre.add(c.getDouble(c.getColumnIndex("gzd_20")));
        lustre.add(c.getDouble(c.getColumnIndex("gzd_60")));
        lustre.add(c.getDouble(c.getColumnIndex("gzd_80")));
    }

    @Override
    public ContentValues getContentValue() {
        ContentValues cv = new ContentValues();
        cv.put("stand_name", stand_name);
        cv.put("name", name);
        cv.put("tips", tips);
        cv.put("isStandard", isStandard ? 1 : 0);
        cv.put("date", date);
        cv.put("time", time);
        cv.put("moment", moment);
        cv.put("result", result);

        cv.put("gzd_20", lustre.get(0));
        cv.put("gzd_60", lustre.get(1));
        cv.put("gzd_80", lustre.get(2));
        return cv;
    }

    @Override
    public HashMap<String, Object> toHashMapForMySql(Context context) {
        HashMap<String,Object> map=new HashMap<>();
        map.put("stand_name", stand_name);
        map.put("name", name);
        map.put("tips", tips);
        map.put("isStandard", isStandard ? 1 : 0);
        map.put("date", date);
        map.put("time", time);
        map.put("moment", moment);
        map.put("result", result);

        map.put("gzd_20", lustre.get(0));
        map.put("gzd_60", lustre.get(1));
        map.put("gzd_80", lustre.get(2));
        return map;
    }

    @Override
    public HashMap<String, Object> toHashMap(Context context) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("stand_name", stand_name);
        map.put("名称", name);
        map.put("备注", tips);
        map.put("isStandard", isStandard);
        map.put("日期", date);
        map.put("时间", time);
        map.put("moment", moment);
        map.put("结果", result);

        if (lustre != null && lustre.size() == 3) {
            map.put("20°", lustre.get(0));
            map.put("60°", lustre.get(1));
            map.put("80°", lustre.get(2));
        }
        return map;
    }

    public HashMap<String, Double> toHashMap() {
        HashMap<String, Double> map = new HashMap<>();
        if (lustre != null && lustre.size() == 3) {
            map.put("20°", lustre.get(0));
            map.put("60°", lustre.get(1));
            map.put("80°", lustre.get(2));
        }
        return map;
    }

}
