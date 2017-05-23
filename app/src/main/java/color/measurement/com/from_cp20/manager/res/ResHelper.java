package color.measurement.com.from_cp20.manager.res;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import color.measurement.com.from_cp20.R;
import color.measurement.com.from_cp20.module.been.data.LightColorData;
import color.measurement.com.from_cp20.module.been.data.LustreData;
import color.measurement.com.from_cp20.module.measure.lightcolor.bean.ResultAndDValues;
import color.measurement.com.from_cp20.util.java.ArrayUtil;
import edu.tjrac.swant.color_measurement.DValue;


/**
 * Created by wpc on 2017/2/16.
 */

public class ResHelper {
    public static HashMap<String, Float> getSettings(ArrayList<String> titles,SharedPreferences sp) {
        HashMap<String, Float> sets = new HashMap<>();
        for (int i = 0; i < titles.size(); i++) {
            sets.put(titles.get(i), sp.getFloat(titles.get(i) + "rc", 2.0f));
        }
        return sets;
    }

    public static String[] getSCGStitles(Context context) {
        return context.getResources().getStringArray(R.array.sechagongshi);
    }

    public static ArrayList<String> getChecked_titles(Context context, SharedPreferences sp, int type_res_id, int[] title_type) {
        ArrayList<String> titles = new ArrayList<>();
        boolean[] check_state = getCheckedStateFromSP(context, sp, type_res_id);
        for (int i = 0; i < check_state.length; i++) {
            if (check_state[i]) {
                ArrayUtil.addStringsToStringArrayIfNotExit(titles, context.getResources().getStringArray(title_type[i]));
            }
        }
        return titles;
    }

    public static String[] getStandTitles(Context context, String sp_name, int array_id[], String type_name) {
        SharedPreferences sp = context.getSharedPreferences(sp_name, Context.MODE_PRIVATE);
        return context.getResources().getStringArray(array_id[sp.getInt(type_name, 0)]);
    }
    public static Boolean getResultWithCheckItems(ResultAndDValues resultAndDValues) {
        Collection<Boolean> r = resultAndDValues.getResult().values();
        Boolean bol;
        if (r == null | r.size() == 0) {
            bol = null;
        } else {
            bol = true;
            for (boolean b : r) {
                if (!b) {
                    bol = false;
                }
            }
        }
        return bol;
    }
    public static ResultAndDValues getWC(SharedPreferences sp, ArrayList<String> titles, String[] dv_title, HashMap<String, Float> sets, LightColorData stand, LightColorData test) {
        ResultAndDValues res = new ResultAndDValues();
        HashMap<String, Double> stand_map = stand.getResultData(sp).toHashMap(), test_map = test.getResultData(sp).toHashMap();
        HashMap<String, Double> wc = new HashMap<>();
        for (String str : titles) {
            Double t = test.getResultData(sp).toHashMap().get(str);
            if (t != null) {
                Double d = (t - stand.getResultData(sp).toHashMap().get(str));
                wc.put(str, d);
            }
        }
        List<Double> dv = DValue.getDValue(stand_map.get("L*"), stand_map.get("a*"), stand_map.get("b*"),
                test_map.get("L*"), test_map.get("a*"), test_map.get("b*"),
                stand_map.get("c*"), stand_map.get("h"), test_map.get("c*"), test_map.get("h"),
                stand_map.get("u*"), stand_map.get("v*"), test_map.get("u*"), test_map.get("v*"),
                stand_map.get("L(Hunter)"), stand_map.get("a(Hunter)"), stand_map.get("b(Hunter)"),
                test_map.get("L(Hunter)"), test_map.get("a(Hunter)"), test_map.get("b(Hunter)"), stand.getSCI(), test.getSCI()
        );
        for (int i = 0; i < dv_title.length; i++) {
            wc.put(dv_title[i], dv.get(i));
        }
        res.setData(wc);
        HashMap<String, Boolean> result = new HashMap<>();
        for (String str : titles) {
            if (sets.get(str) < Math.abs(wc.get(str))) {
                result.put(str, false);
            } else {
                result.put(str, true);
            }
        }
        res.setResult(result);
        return res;
    }
    public static ResultAndDValues getWC(ArrayList<String> titles, HashMap<String, Float> sets, LustreData stand, LustreData test) {
        ResultAndDValues rv=new ResultAndDValues();
        HashMap<String, Double> stand_map = stand.toHashMap(), test_map = test.toHashMap();
        HashMap<String, Double> wc = new HashMap<>();
        for (String str : titles) {
            Double t =test_map.get(str);
            if (t != null) {
                Double d = (t - stand_map.get(str));
                wc.put(str, d);
            }
        }
        rv.setData(wc);
        HashMap<String, Boolean> result = new HashMap<>();
        for (String str : titles) {
            if (sets.get(str) < Math.abs(wc.get(str))) {
                result.put(str, false);
            } else {
                result.put(str, true);
            }
        }
        rv.setResult(result);
        return rv;
    }
    public static boolean[] getCheckedStateFromSP(Context context, SharedPreferences sp, int type_res_id) {
        String[] type = context.getResources().getStringArray(type_res_id);
        boolean[] checkstate = new boolean[type.length];
        for (int i = 0; i < type.length; i++) {
            checkstate[i] = sp.getBoolean(type[i], false);
        }
        return checkstate;
    }

    public static ArrayList<String> getCheckedTitlesFromSp(Context context, String sp_name, int type_res_id) {
        SharedPreferences sp = context.getSharedPreferences(sp_name, Context.MODE_PRIVATE);
        String[] type = context.getResources().getStringArray(type_res_id);
        ArrayList<String> titles = new ArrayList<>();
        for (int i = 0; i < type.length; i++) {
            if (sp.getBoolean(type[i], false)) {
                titles.add(type[i]);
            }
        }
        return titles;
    }

    public static ArrayList<String> getallTitlesFromRes(Context context, int type_res_id) {
        String[] type = context.getResources().getStringArray(type_res_id);
        ArrayList<String> titles = new ArrayList<>();
        for (int i = 0; i < type.length; i++) {
            titles.add(type[i]);

        }
        return titles;
    }

    public static void saveState(boolean[] check_state, ArrayList<String> datas, SharedPreferences sp) {
        SharedPreferences.Editor edit = sp.edit();
        for (int i = 0; i < datas.size(); i++) {
            if (check_state[i]) {
                edit.putBoolean(datas.get(i), true);
            } else {
                edit.putBoolean(datas.get(i), false);
            }
        }
        edit.commit();
    }


}
