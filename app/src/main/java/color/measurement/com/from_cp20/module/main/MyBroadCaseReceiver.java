package color.measurement.com.from_cp20.module.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import color.measurement.com.from_cp20.manager.db.MySqlConsts;
import color.measurement.com.from_cp20.manager.sp.SPConsts;
import color.measurement.com.from_cp20.module.App;
import color.measurement.com.from_cp20.util.utils.L;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by cimcenter on 2017/5/5.
 */

public class MyBroadCaseReceiver extends BroadcastReceiver {
    private Context mContext;
    @Override
    public void onReceive(final Context context, Intent intent) {
        mContext = context;
        L.e("onclock......................");
        //判断mysql中的字段  标识号+boolean
        SharedPreferences sp = context.getSharedPreferences(SPConsts.PREFERENCE_APP_CONFIG, MODE_PRIVATE);
        final String tm = sp.getString("tm", "");
        L.e("tm==" + tm );
        new Thread(){
            @Override
            public void run() {
                if(App.logged_user.is_login()){
                    Connection con = null;
                    String sql;
                    ResultSet rs = null;
                    Statement mStmt;
                    try {
                        Class.forName("com.mysql.jdbc.Driver");
                        con = DriverManager
                                .getConnection(
                                        "jdbc:mysql://rdsz4kl5za13u8d0m3i8spublic.mysql.rds.aliyuncs.com:3306/chnspec",
                                        "spec3205722251", "spec3205722251");
                        mStmt = con.createStatement();
                        sql = "select *from "+ MySqlConsts.user_table+" where name ='"+App.logged_user.getName()+"'";
                        //sql="update "+ MySqlUtils.strTable+" set config='"+tm+login+"'  where name='"+ App.logged_user.getName()+"'";
                        L.e("sql=="+sql);
                        rs = mStmt.executeQuery(sql);
                        if(rs.next()){
                            L.e("es.next");
                            String tm2 = (String) rs.getObject("config");
                            if(tm2.endsWith("true")){
                                String[] strs = tm2.split("t");
                                String now = strs[0].toString();
                                if(!tm.equals(now)){
                                    mHandle.sendEmptyMessage(0);
                                }
                                L.e("strs[0].toString();=="+strs[0].toString());
                            }
                        }
                        // L.e("mStmt.execute(sql);=="+mStmt.executeUpdate(sql));
                    } catch (SQLException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }finally {
                        try {
                            if(con!=null){
                                con.close();
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                }
                super.run();
            }
        }.start();
    }

    Handler mHandle = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    Toast.makeText(mContext, "该账号已在其他设备上登录", Toast.LENGTH_LONG).show();
                    App.logged_user.setName("未登录");
                    App.logged_user.setIs_login(false);
                    App.logged_user.setPortrait_url("");
                    Intent intent1 = new Intent(mContext,MainActivity.class);
                    mContext.startActivity(intent1);
                    break;
                case 1:

                    break;
            }
            super.handleMessage(msg);
        }
    };
}
