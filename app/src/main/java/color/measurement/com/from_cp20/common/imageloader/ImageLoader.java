package color.measurement.com.from_cp20.common.imageloader;

import android.content.Context;
import android.widget.ImageView;

/**
 * Created by wpc on 2017/3/14.
 */

public interface ImageLoader {
    void init(Context context);
    void displayImage(String imageUrl, ImageView imageView,int defaultImage);
    void displayImage(int resId, ImageView imageView,int defaultImage);
}
