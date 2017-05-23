package color.measurement.com.from_cp20.util.utils;

import android.content.Context;
import android.widget.Toast;

import es.dmoral.toasty.Toasty;

/**
 * Created by wpc on 2017/4/1.
 */

public class T {

    public static final boolean DeBug = true;

    public static final int duration = Toast.LENGTH_SHORT;

    public static void showDeBug(Context context, CharSequence str) {
        if (DeBug) {
            Toast.makeText(context, str, duration).show();
        }
    }

    public static void isDevelopping(Context cont) {
//        Toast.makeText(cont, "模块正在开发中", duration).show();
        Toasty.warning(cont, "模块正在开发中").show();
    }

    public static void show(Context context, CharSequence str) {
        Toast.makeText(context, str, duration).show();
    }

    public static void showWarning(Context context, CharSequence str) {
        Toasty.warning(context, str).show();
    }

    public static void showError(Context context, CharSequence str) {
        Toasty.error(context, str).show();
    }

    public static void showSuccess(Context context, CharSequence str) {
        Toasty.success(context, str).show();
    }
}
