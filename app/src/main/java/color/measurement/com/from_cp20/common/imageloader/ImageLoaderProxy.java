package color.measurement.com.from_cp20.common.imageloader;

import android.content.Context;
import android.widget.ImageView;

/**
 * Created by wpc on 2017/3/14.
 */

public class ImageLoaderProxy implements ImageLoader {
    // ImageLoaderProxy.getInstance().displayImage(）；

    private ImageLoader imageLoader;//代理对象
    private static ImageLoaderProxy imageLoaderProxy;

    public static ImageLoaderProxy getInstance() {
        if (imageLoaderProxy == null) {
            imageLoaderProxy = new ImageLoaderProxy();
        }
        return imageLoaderProxy;
    }

    private ImageLoaderProxy() {
//        imageLoader = new UniversalImageLoader();
        imageLoader = new GlideImageLoader();
    }

    @Override
    public void init(Context context) {
        imageLoader.init(context);
    }

    @Override
    public void displayImage(String imageUrl, ImageView imageView, int defaultImage) {
        imageLoader.displayImage(imageUrl, imageView, defaultImage);
    }

    @Override
    public void displayImage(int resId, ImageView imageView, int defaultImage) {
        imageLoader.displayImage(resId, imageView, defaultImage);
    }
}