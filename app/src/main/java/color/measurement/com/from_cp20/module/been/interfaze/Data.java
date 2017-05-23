package color.measurement.com.from_cp20.module.been.interfaze;

import android.content.ContentValues;
import android.content.Context;

import java.util.HashMap;

/**
 * Created by wpc on 2017/4/17.
 */

public interface Data {
    //save to sqlite
    ContentValues getContentValue();
    //save to mysql
    HashMap<String, Object> toHashMapForMySql(Context context);
}
