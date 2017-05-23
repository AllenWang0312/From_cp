package color.measurement.com.from_cp20.manager.ins;

import android.content.Context;

import color.measurement.com.from_cp20.R;
import color.measurement.com.from_cp20.util.blankj.StringUtils;

/**
 * Created by wpc on 2017/3/24.
 */

public class Instrument {

    public static final int[] ins_type = new int[]{
            R.array.light_color_instrument_model,
            R.array.chromatism_instrument_model,
            R.array.lustre_instrument_model,
            R.array.raman_instrument_model};

    // ble_version
    // R.array.ble_version_4, R.array.ble_version_2;

    public static final String[] ins_types = {"分光测色仪", "色差仪", "光泽度计", "拉曼", "未知设备"};

    public static final int TYPE_LIGHTCOLOR = 0;
    public static final int TYPE_CHEOMATISM = 1;
    public static final int TYPE_LUSTRE = 2;
    public static final int TYPE_RAMAN = 3;

    public static int getTypeWithInstrumentName(String name, Context context) {
        if (!StringUtils.isEmpty(name)) {
            for (int i = 0; i < ins_type.length; i++) {
                String[] typs = context.getResources().getStringArray(ins_type[i]);
                for (String str : typs) {
                    if (name.contains(str)) {
                        return i;
                    }
                }
            }
        }
        return 4;
    }
    public static int getBleVersionWithInstrumentName(String name, Context context) {
        if (!StringUtils.isEmpty(name)) {
            String[] v4 = context.getResources().getStringArray(R.array.ble_version_4);
            for (String str : v4) {
                if (name.contains(str)) {
                    return 4;
                }
            }
            String[] v2 = context.getResources().getStringArray(R.array.ble_version_2);
            for (String str : v2) {
                if (name.contains(str)) {
                    return 2;
                }
            }
        }
        return 0;
    }

    public static String getTypeStringWithInstrumentName(String name, Context context) {
        return getTypeStringWithType(getTypeWithInstrumentName(name, context));
    }

    public static String getTypeStringWithType(int i) {
        return ins_types[i];
    }

    public static int tableType(String table_name) {
//        boolean b="null光泽度计20170512104734".contains(ins_types[2]);
        for (int i = 0; i < ins_types.length; i++) {
            if (table_name.contains(ins_types[i])) {
                return i;
            }
        }
        return 4;
    }

}
