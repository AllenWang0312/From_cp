package color.measurement.com.from_cp20.common.backgroundcreater;

import color.measurement.com.from_cp20.R;

/**
 * Created by wpc on 2017/4/18.
 */

public class ColorHelper {

    public static final int[] colors = {
            R.color.accent_red,
            R.color.accent_pink,
            R.color.accent_purple,
            R.color.accent_deep_purple,
            R.color.accent_indago,
            R.color.accent_blue,
            R.color.accent_cyan,
            R.color.accent_teal,
            R.color.accent_green,
            R.color.accent_yellow,
            R.color.accent_amber,
            R.color.accent_orange,
            R.color.accent_brown,
            R.color.accent_grey,
    };

    public  static int getRandomColor() {

        return colors[(int) Math.random() * (colors.length + 1)];
    }
}
