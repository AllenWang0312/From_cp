package color.measurement.com.from_cp20.module;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.webkit.WebView;

import cn.jpush.android.api.JPushInterface;
import cn.sharesdk.framework.ShareSDK;
import color.measurement.com.from_cp20.R;
import color.measurement.com.from_cp20.common.imageloader.ImageLoaderProxy;
import color.measurement.com.from_cp20.module.been.User;
import color.measurement.com.from_cp20.third.AppKeyContants;
import color.measurement.com.from_cp20.util.blankj.Utils;

/**
 * Created by wpc on 2017/2/17.
 */

public class App extends Application {


//    public class BmobConsts {
//        public static final String Bmob_app_id = "e2723e5437c6b3278635e4d5fd63210d";
//        public static final String Bmob_SMS_model_name = "短信验证";
//    }

    //天行数据 api key
    //homepage 测试数据

    public static User logged_user = new User();
    public static String tel_id;
    public static final String tianxing_apiKey = "9291ea05284c4520b600cbb6db6b12b1";
    public static String[] angles, lights;

    @Override
    public void onCreate() {
        super.onCreate();
        initPlugins();
        initSDK();
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        tel_id = tm.getDeviceId();
        angles = getResources().getStringArray(R.array.angle_set);
        lights = getResources().getStringArray(R.array.light_src);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            if (0 != (getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE)) {
//                WebView.setWebContentsDebuggingEnabled(true);
//            }
            WebView.setWebContentsDebuggingEnabled(true);
        }
    }

    //开源工具
    private void initPlugins() {
//        FreelineCore.init(this);
//        LitePal.initialize(this);
//        SqlScoutServer.create(this, getPackageName());

//        Stetho.initializeWithDefaults(this);
//        new OkHttpClient.Builder()
//                .addNetworkInterceptor(new StethoInterceptor())
//                .build();

        ImageLoaderProxy.getInstance().init(this);
        Utils.init(this);
    }

    //第三方sdk
    private void initSDK() {
        //验证微信分享  包名+签名
        //Review.MD5Review(this, "color.measurement.com.from_cp20", "14705573c385d9d8922a36d73b46c301");
        //推送
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        ShareSDK.initSDK(this, AppKeyContants.SHARE_APPKEY);
    }


}
