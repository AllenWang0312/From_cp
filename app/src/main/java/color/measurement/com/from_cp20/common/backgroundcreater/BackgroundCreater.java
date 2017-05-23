package color.measurement.com.from_cp20.common.backgroundcreater;

import android.graphics.drawable.Drawable;

/**
 * Created by wpc on 2017/3/1.
 */

public interface BackgroundCreater {
    Drawable getBackground(int position, float present);
}
