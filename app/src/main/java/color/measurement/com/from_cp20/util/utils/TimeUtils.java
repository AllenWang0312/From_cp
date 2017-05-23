package color.measurement.com.from_cp20.util.utils;

import android.text.format.Time;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static java.util.Calendar.getInstance;

/**
 * Created by wpc on 2016/10/11.
 */

public class TimeUtils {

    static Time t = new Time();
    static Calendar c = getInstance();

    public static final String NO_BLANK="yyyyMMddHHmmss";
    public static final String CHINESE_COLON="yyyy年MM月dd日 HH:mm:ss";
    public static final String BLANK_COLON="yyyy-MM-dd HH:mm:ss";
    public static final String MONTH_DAY_NO_ZERO="yyyy-M-d HH-mm-ss";

    public static final String HOUR_MIN="HH:mm";
    public static final String HOUR_MIN_SEC="HH:mm:ss";

    public static String getTimeWithFormat(long moment,String str) {
        Date date = new Date(moment);
        SimpleDateFormat sdf = new SimpleDateFormat(str);
        return sdf.format(date);
    }

    public static String getTimeWithFormat(String str) {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat(str);
        return sdf.format(date);
    }

    public static void LogTime() {
        t.setToNow();
        Log.i("Time", t.year + "-" + t.month + "-" + t.monthDay);
        Log.i("Calendar", c.get(Calendar.YEAR) + "-" + c.get(Calendar.MONTH) + "-" + c.get(Calendar.DAY_OF_MONTH));
        Log.i("Date", getTimeWithFormat(NO_BLANK));
    }
}
