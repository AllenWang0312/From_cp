package color.measurement.com.from_cp20.common.util;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
 * Created by wpc on 2017/3/23.
 */


public class ParcelUtil {

    //parcelabel 转byte[]  用于对象保存
    public static byte[] marshall(Parcelable parceable) {
        Parcel parcel = Parcel.obtain();
        parcel.setDataPosition(0);
        parceable.writeToParcel(parcel, 0);
        byte[] bytes = parcel.marshall();
        Log.d("ParcelableTest", "bytes = " + String.valueOf(bytes) + "parcel" + parcel.toString());
        parcel.recycle();
        return bytes;
    }

    // 取出数据的parcel
    public static Parcel unmarshall(byte[] bytes) {
        Parcel parcel = Parcel.obtain();
        parcel.unmarshall(bytes, 0, bytes.length);
        parcel.setDataPosition(0);
        return parcel;
    }

}
