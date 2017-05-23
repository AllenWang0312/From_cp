package color.measurement.com.from_cp20.common.util;

import android.support.v7.view.menu.MenuPopupHelper;
import android.support.v7.widget.PopupMenu;

import java.lang.reflect.Field;

/**
 * Created by wpc on 2017/3/30.
 */

public class ReflectUtils {

    public static void ForceShowIcon(PopupMenu p){
        try {
            Field field = p.getClass().getDeclaredField("mPopup");
            field.setAccessible(true);
            MenuPopupHelper mHelper = (MenuPopupHelper) field.get(p);
            mHelper.setForceShowIcon(true);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }

    }
}
