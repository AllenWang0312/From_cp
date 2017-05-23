package color.measurement.com.from_cp20.common.imageloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.disc.impl.LimitedAgeDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.utils.StorageUtils;

import color.measurement.com.from_cp20.R;

/**
 * Created by wpc on 2017/3/14.
 */

public  class UniversalImageLoader implements ImageLoader {

    private final long discCacheLimitTime = 3600 * 24 * 15l;
    private com.nostra13.universalimageloader.core.ImageLoader imageLoader = com.nostra13.universalimageloader.core.ImageLoader.getInstance();

    @Override
    public void init(Context context) {
        if (!imageLoader.isInited()) {
            ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context).
                    threadPriority(Thread.NORM_PRIORITY)
                    .denyCacheImageMultipleSizesInMemory()
                    .memoryCache(new WeakMemoryCache())
                    .memoryCacheSize(2 * 1024 * 1024)
                    .memoryCacheSizePercentage(13)
                    .discCacheFileNameGenerator(new Md5FileNameGenerator())
                    .discCache(new LimitedAgeDiscCache(StorageUtils.getCacheDirectory(context), discCacheLimitTime))
                    .tasksProcessingOrder(QueueProcessingType.LIFO).build();
            com.nostra13.universalimageloader.core.ImageLoader.getInstance().init(config);

        }
    }

    @Override
    public void displayImage(String imageUrl, ImageView imageView, int defaultImage) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.ic_launcher)
                .showImageForEmptyUri(R.mipmap.ic_launcher).showImageOnFail(R.mipmap.ic_launcher)
                .cacheInMemory(true).cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new SimpleBitmapDisplayer()).build();
        imageLoader.displayImage(imageUrl, imageView, options);
    }

    @Override
    public void displayImage(int resId, ImageView imageView, int defaultImage) {

    }
}
