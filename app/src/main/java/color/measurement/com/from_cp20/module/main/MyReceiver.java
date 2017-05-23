package color.measurement.com.from_cp20.module.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import cn.jpush.android.api.JPushInterface;
import color.measurement.com.from_cp20.util.utils.L;

/**
 * Created by cimcenter on 2017/4/11.
 */

public class MyReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(JPushInterface.ACTION_NOTIFICATION_OPENED)){
            L.e("用户点击打开了通知");
            //跳转到推送界面
           /* Intent intent1 = new Intent(context,TuiSongActivity.class);
            intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent1);*/

            //跳转到网页
            /*Intent intent1 = new Intent(context,TuiSongActivity.class);
            intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent1);*/
        }
    }
}