package color.measurement.com.from_cp20.common.backgroundcreater;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.Nullable;

import color.measurement.com.from_cp20.common.backgroundcreater.BackgroundCreater;
import color.measurement.com.from_cp20.util.utils.ColorUtils;


/**
 * Created by wpc on 2017/3/1.
 */

public class RandomBgCreater implements BackgroundCreater {

    public int defaultcolors[] = {0xf44336, 0xe91e63, 0x9c27b0, 0x673ab7, 0x3f51b5, 0x2196f3, 0x00bcd4, 0x009688, 0x4caf50, 0xffeb3b, 0xffc107, 0xff9800};
    Integer strokeColor;
    Integer strokeWidth;
    int radius;
    boolean random;
    boolean gradually;

    public RandomBgCreater(@Nullable int colors[], boolean random, boolean gradually) {
        if (colors != null) {
            this.defaultcolors = colors;
        }
        this.random = random;
        this.gradually = gradually;
    }


    @Override
    public Drawable getBackground(int position,float present) {
        GradientDrawable gd;
        int c = 0;
        if (random) {
            c = defaultcolors[(int) (Math.random() * 12)];
        } else {
            c = defaultcolors[position];
        }
        if (gradually) {
            int cs[] = {ColorUtils.getLightColor(c, present), ColorUtils.getDarkColor(c, present)};
            gd = new GradientDrawable(GradientDrawable.Orientation.BL_TR, cs);
        } else {
            gd = new GradientDrawable();
            gd.setColor(c);
        }
//        int strokeColor = Color.parseColor("#2E3135");//边框颜色
        if (radius != 0) {
            gd.setCornerRadius(radius);
        } else {
            gd.setCornerRadius(15);
        }
        if (strokeColor != null) {
            gd.setStroke(strokeWidth, strokeColor);
        }
        return gd;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public void setStroke(int strokeColor, int strokeWidth) {
        this.strokeColor = strokeColor;
        this.strokeWidth = strokeWidth;
    }
}
