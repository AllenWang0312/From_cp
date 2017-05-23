package color.measurement.com.from_cp20.manager;

import java.util.ArrayList;
import java.util.HashMap;

import color.measurement.com.from_cp20.module.been.data.LightColorData;
import color.measurement.com.from_cp20.util.blankj.StringUtils;

/**
 * Created by wpc on 2017/5/5.
 */

public class DataNamedHelper {


    public static boolean  isNumEndedString(String str) {
        if (StringUtils.isNumeric(String.valueOf(str.charAt(str.length() - 1)))) {
            return true;
        }
        return false;
    }

    public static HashMap<String, NumEndedString> getNames(ArrayList<LightColorData> datas) {
        HashMap<String, NumEndedString> result = new HashMap<>();
        for (LightColorData data : datas) {
            String stand_name = data.getStand_name();
            NumEndedString name = NumEndedString.create(stand_name);
            if (result.keySet().contains(name.content)) {
                if (name.num > result.get(stand_name).num) {
                    result.put(stand_name, name);
                }
            } else {
                result.put(stand_name, name);
            }
        }
        return result;
    }
}
