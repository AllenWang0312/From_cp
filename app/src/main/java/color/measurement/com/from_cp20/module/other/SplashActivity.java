package color.measurement.com.from_cp20.module.other;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import color.measurement.com.from_cp20.manager.sp.SPConsts;

/**
 * Created by wpc on 2017/4/19.
 */

public class SplashActivity extends AppCompatActivity {

    //    Handler mHandler = new Handler();
    SharedPreferences sp;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        setContentView(R.layout.activity_splash);
        sp = getSharedPreferences(SPConsts.PREFERENCE_APP_CONFIG, MODE_PRIVATE);
        boolean b = sp.getBoolean(SPConsts.FIRST_LANCHERED, true);
        if (b) {
            initSettings();

        }

//            SharedPreferences.Editor et = sp.edit();
//            et.putBoolean(SPConsts.FIRST_LANCHERED, false);
//            et.commit();
//            finish();
//            startActivity(new Intent(SplashActivity.this, GuideActivity.class));
//        } else {
        finish();
        startActivity(new Intent(SplashActivity.this, AdvertiseActivity.class));
//        }

//        mHandler.postDelayed(new Runnable() {
//            public void run() {
//
//            }
//        }, 1000);
    }

    SharedPreferences.Editor editor;

    void initSettings() {
        editor = sp.edit();

        editor.commit();
        sp = getSharedPreferences(SPConsts.PREFERENCE_LIGHT_COLOR, MODE_PRIVATE);
        editor = sp.edit();
        editor.putBoolean("名称", true);
        editor.putBoolean("日期", true);
        editor.putBoolean("时间", true);
        editor.putBoolean("测量模式", true);
        editor.putBoolean("光照", true);
        editor.putBoolean("角度", true);
        editor.putBoolean("L*a*b", true);
        editor.putBoolean(SPConsts.STAND_AUTO_NAME,true);
        editor.putBoolean(SPConsts.SIMPLE_AUTO_NAME,true);
        editor.putInt(SPConsts.STAND_DATA_MOD,1);
        editor.putInt(SPConsts.SIMPLE_DATA_MOD,1);
        editor.commit();

        sp = getSharedPreferences(SPConsts.PREFERENCE_LIGHT_COLOR_DB_STAND, MODE_PRIVATE);
        editor = sp.edit();
        editor.putBoolean("名称", true);
        editor.putBoolean("日期", true);
        editor.putBoolean("时间", true);
        editor.putBoolean("测量模式", true);
        editor.putBoolean("光照", true);
        editor.putBoolean("角度", true);
        editor.putBoolean("L*a*b", true);
        editor.commit();
        sp = getSharedPreferences(SPConsts.PREFERENCE_LIGHT_COLOR_DB_TEST, MODE_PRIVATE);
        editor = sp.edit();
        editor.putBoolean("名称", true);
        editor.putBoolean("日期", true);
        editor.putBoolean("时间", true);
        editor.putBoolean("测量模式", true);
        editor.putBoolean("光照", true);
        editor.putBoolean("角度", true);
        editor.putBoolean("L*a*b", true);
        editor.commit();
        sp = getSharedPreferences(SPConsts.PREFERENCE_LUSTRE, MODE_PRIVATE);
        editor = sp.edit();
        editor.putBoolean("20°", true);
        editor.putBoolean("60°", true);
        editor.putBoolean("80°", true);
        editor.putBoolean("名称", true);
        editor.putBoolean("日期", true);
        editor.putBoolean("时间", true);
        editor.putBoolean(SPConsts.STAND_AUTO_NAME,true);
        editor.putBoolean(SPConsts.SIMPLE_AUTO_NAME,true);
        editor.commit();
    }

}
