package color.measurement.com.from_cp20.module.been.data;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;

import com.orhanobut.logger.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import color.measurement.com.from_cp20.R;
import color.measurement.com.from_cp20.common.util.StringFormat;
import color.measurement.com.from_cp20.manager.sp.SPConsts;
import color.measurement.com.from_cp20.module.been.interfaze.CompareableData;
import color.measurement.com.from_cp20.module.measure.lightcolor.bean.TestResult_290;
import color.measurement.com.from_cp20.util.utils.TimeUtils;

/**
 * 分光测色 数据 基础类
 * litepal 数据类型
 * Created by wpc on 2017/4/1.
 */

public class LightColorData extends CompareableData {

    ArrayList<Double> SCI;
    //    private String data_string;
    TestResult_290 resultData;//SCI及变种数据

    Integer light_set, angle_set, test_mod;


    public LightColorData(ArrayList<Double> SCI, Integer test_mod, Integer light_set, Integer angle_set) {
        this.SCI = SCI;
        this.test_mod = test_mod;
        this.angle_set = angle_set;
        this.light_set = light_set;
        moment = System.currentTimeMillis();
        String t = TimeUtils.getTimeWithFormat(moment, TimeUtils.MONTH_DAY_NO_ZERO);
        String[] strs = t.split(" ");
        date = strs[0];
        time = strs[1];
    }

    public LightColorData(Cursor c) {
       super(c);
        this.moment = c.getLong(c.getColumnIndex("moment"));
        ArrayList<Double> sci = new ArrayList<>();
        for (int i = 1; i <= 31; i++) {
            sci.add(c.getDouble(c.getColumnIndex("SCI" + i)));
        }
        this.SCI = sci;
        this.result = c.getString(c.getColumnIndex("result"));
        this.light_set = c.getInt(c.getColumnIndex("light"));
        this.angle_set = c.getInt(c.getColumnIndex("angle"));
        this.test_mod = c.getInt(c.getColumnIndex("test_mod"));
    }

    public LightColorData(ResultSet set) {
        try {
            stand_name = set.getString("stand_name");
            name = set.getString("name");
            tips = set.getString("tips");
            isStandard = set.getInt("isStandard") == 1;
            date = set.getString("date");
            time = set.getString("time");
            moment = set.getLong("moment");
            result = set.getString("result");
            SCI = new ArrayList<>();
            for (int i = 1; i <= 31; i++) {
                SCI.add(set.getDouble("SCI" + i));
            }
            light_set = set.getInt("light");
            angle_set = set.getInt("angle");
            test_mod = set.getInt("test_mod");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public ArrayList<Double> getSCI() {
        return SCI;
    }

    public void setSCI(ArrayList<Double> SCI) {
        this.SCI = SCI;
    }

    //数据实时设置
    public TestResult_290 getResultData(SharedPreferences sp) {
        return new TestResult_290(0, SCI, sp.getInt(SPConsts.LIGHT_SET, 4), sp.getInt(SPConsts.ANGLE_SET, 0));
    }

    //数据测量时设置
    public TestResult_290 getResultData() {
        return new TestResult_290(0, SCI, light_set, angle_set);
    }

    public void setResultData(TestResult_290 resultData) {
        this.resultData = resultData;
    }

    private String getString(ArrayList<Double> sci) {
        StringBuffer sb = new StringBuffer();
        for (Double d : sci) {
            sb.append(d + "_");
        }
        String result = sb.toString();
        Logger.i(result);
        return result;
    }

    @Override
    public void setServiceId(int id) {

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
        for (int i = 1; i < 32; i++) {
            cv.put("SCI" + i, SCI.get(i - 1));
        }
        cv.put("test_mod", test_mod);
        cv.put("light", light_set);
        cv.put("angle", angle_set);
        return cv;
    }


    @Override
    public HashMap<String, Object> toHashMapForMySql(Context context) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("stand_name", stand_name);
        map.put("name", name);
        map.put("tips", tips);
        map.put("isStandard", isStandard);
        map.put("date", date);
        map.put("time", time);
        map.put("moment", moment);
        map.put("result", result);
        for (int i = 1; i <= 31; i++) {
            map.put("SCI" + i, StringFormat.FourDecimal(SCI.get(i - 1)));
        }
        map.put("test_mod", test_mod);
        map.put("light", light_set);
        map.put("angle", angle_set);
//        map.putAll(getResultData().toHashMap());
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
        for (int i = 0; i < 31; i++) {
            map.put((400 + i * 10) + "nm", SCI.get(i));
        }
        map.put("测量模式", context.getResources().getStringArray(R.array.test_mod)[test_mod]);
        map.put("光照", context.getResources().getStringArray(R.array.light_src)[light_set]);
        map.put("角度", context.getResources().getStringArray(R.array.angle_set)[angle_set]);
        map.putAll(getResultData().toHashMap());
        return map;
    }

}
