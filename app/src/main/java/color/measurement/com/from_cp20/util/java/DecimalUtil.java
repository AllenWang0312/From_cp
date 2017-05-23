package color.measurement.com.from_cp20.util.java;

import java.text.DecimalFormat;

/**
 * Created by wpc on 2017/2/13.
 */

public class DecimalUtil {
    public static String getFormatDouble(double d, int a, int b){
        StringBuffer sb=new StringBuffer();
        for (int i = 0; i <a ; i++) {
            sb.append("#");
        }
        sb.append("0.");
        for (int i = 0; i <b ; i++) {
            sb.append("0");
        }
        DecimalFormat df = new DecimalFormat(sb.toString());
        return df.format(d);
    }
}
