package color.measurement.com.from_cp20.util.java;

import java.util.ArrayList;

/**
 * Created by wpc on 2016/11/8.
 */

public class ArrayUtil {

    public static int getNearestIndex(ArrayList<Float> datas, float com_num) {
        int index = 0;
        float distance = Integer.MAX_VALUE;
        for (int i = 0; i < datas.size(); i++) {
            float dis = Math.abs(datas.get(i) - com_num);
            if (distance > dis) {
                distance = dis;
                index = i;
            }
        }
        return index;
    }

    public ArrayList<String> Add(ArrayList<String> main, ArrayList<String> add) {
        main.addAll(add);
        return main;
    }

    public static void addStringsToStringArray(ArrayList<String> strings, String[] str) {
        for (int i = 0; i < str.length; i++) {
            strings.add(str[i]);
        }
    }

    public static void addStringsToStringArrayIfNotExit(ArrayList<String> strings, String[] str) {
        for (int i = 0; i < str.length; i++) {
            if (!strings.contains(str[i])) {
                strings.add(str[i]);
            }
        }
    }

    public static byte[] copyBytes(byte[] from, int start_index, int length) {
        byte[] bytes = new byte[length];
        System.arraycopy(from, start_index, bytes, 0, length);
        return bytes;
    }

    public static String[] add(String[] str1, String[] str2) {
        String[] result = new String[str1.length + str2.length];
        for (int i = 0; i < result.length; i++) {
            if (i < str1.length) {
                result[i] = str1[i];
            } else {
                result[i] = str2[i - str1.length];
            }

        }

        return result;
    }

    public static ArrayList<Double> getListAverage(ArrayList<ArrayList<Double>> stand_temp) {
        int size = stand_temp.size();
        ArrayList<Double> result = new ArrayList<>();
        for (int i = 0; i < 31; i++) {
            Double total = 0.0;
            for (int j = 0; j < size; j++) {
                total += stand_temp.get(j).get(i);
            }
            result.add(total / size);
        }
        return result;
    }
}
