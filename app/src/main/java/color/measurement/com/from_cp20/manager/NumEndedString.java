package color.measurement.com.from_cp20.manager;

import color.measurement.com.from_cp20.common.util.StringFormat;
import color.measurement.com.from_cp20.util.blankj.StringUtils;

/**
 * Created by wpc on 2017/5/5.
 */

public class NumEndedString {
    public static final int NUM_DEFAULT_LENGTH = 3;
    String content;
    Integer num;
    int num_length;

    public NumEndedString(String content, Integer num, int num_length) {
        this.content = content;
        this.num = num;
        this.num_length = num_length;
    }

    public void numIncreases() {
        num += 1;
    }

    public static NumEndedString create(String str) {
        int num_length = 0;
        Integer num = 0;
        String content = "";
        if(StringUtils.isEmpty(str)){
            return null;
        }else {
            if (DataNamedHelper.isNumEndedString(str)) {
                for (int i = str.length() - 1; i >= 0; i--) {
                    if( StringUtils.isNumeric(String.valueOf(str.charAt(i)))){
                        num_length++;
                    }else {
                        break;
                    }
                }
                content = str.substring(0, str.length() - num_length);
                num = Integer.valueOf(str.substring(str.length() - num_length, str.length()));
                return new NumEndedString(content, num, num_length);
            } else {
                return new NumEndedString(str, 0, NUM_DEFAULT_LENGTH);
            }
        }

    }

    @Override
    public String toString() {
        return content + StringFormat.patchZeroInt(num, num_length);
    }
}