package color.measurement.com.from_cp20.common.imageloader;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Created by wpc on 2017/3/14.
 */

public class GlideImageLoader implements ImageLoader {
    Context mContext;

    @Override
    public void init(Context context) {
        mContext = context;
    }

    @Override
    public void displayImage(String imageUrl, ImageView imageView, int defaultImage) {
        Glide.with(mContext).load(imageUrl).centerCrop().placeholder(defaultImage).into(imageView);
    }

    @Override
    public void displayImage(int resId, ImageView imageView, int defaultImage) {
        Glide.with(mContext).load(resId).centerCrop().placeholder(defaultImage).into(imageView);
    }
}
