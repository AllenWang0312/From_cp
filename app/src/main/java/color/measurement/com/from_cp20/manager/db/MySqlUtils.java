package color.measurement.com.from_cp20.manager.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import color.measurement.com.from_cp20.manager.UpLoadThread;
import color.measurement.com.from_cp20.util.utils.L;

/**
 * Created by cimcenter on 2017/4/13.
 */

public class MySqlUtils {

    private static Connection mConn;
    private static Statement mStmt;
    private static String sql;
    private static ResultSet mRs;
    private static boolean IS_ZHU = false;



    public static  void updata(String tm,String table,String login){
        UpLoadThread upLoadThread = new UpLoadThread();
        upLoadThread.setTm(tm);
        upLoadThread.setTable(table);
        upLoadThread.setLogin(login);
        Thread thread = new Thread(upLoadThread);
        thread.start();
    }

    /**
     * 获取连接
     */
    public static  void  getConnect(final String tel,
                                    final String name,final String password,
                                    final String qq_token,final String qq_openid,
                                    final String weixin_token,final String weixin_openid,
                                    final String weibo_token,final String weibo_openid,final
                                    String age,final String birthday,final String address,final
                                    String sign,final String business,final String config){
        new Thread(){
            @Override
            public void run() {
                try {
                    Class.forName("com.mysql.jdbc.Driver");
                } catch (ClassNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                //获取连接
                mConn = null;
                try {
                    mConn = DriverManager.getConnection(MySqlConsts.DB_URL, MySqlConsts.DB_name,MySqlConsts.DB_pswd);
                    mStmt = mConn.createStatement();
                    L.e("mstem=="+mStmt);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                //插入数据（手机号） 建表
                if(mConn!= null){
                    sql = String.format(MySqlConsts.create_user_table, MySqlConsts.user_table);
                    try {
                        mStmt.executeUpdate(sql);

                        String sql = "SELECT COUNT(*) FROM "+ MySqlConsts.user_table;
//                        L.e("spl=="+sql);
                        mStmt.executeQuery(sql);
//                        L.e("表存在");

                        sql = "SELECT * from "+MySqlConsts.user_table+" where qq_token ='"+qq_token+"'";
                        L.e("spl=="+sql);
                        mRs= mStmt.executeQuery(sql);
                        if(mRs.next()){
                            IS_ZHU = true;
                            L.e("IS_ZHU000=="+IS_ZHU);
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                if(IS_ZHU){
                    return;
                }else {
                    try {
                        sql = String
                                .format("insert into %s(login_way,tel,name,password,qq_token,qq_openid,weixin_token,weixin_openid,weibo_token,weibo_openid,age,birthday,address,sign,business,config) " +
                                                "values(\'%s\',\'%s\',\'%s\',\'%s\',\'%s\',\'%s\',\'%s\',\'%s\',\'%s\',\'%s\',\'%s\',\'%s\',\'%s\',\'%s\',\'%s\',\'%s\') ",
                                        MySqlConsts.user_table, "2","",name, "", qq_token,qq_openid,"","","","","",
                                        birthday, address, sign, business,config);
                        mStmt.executeUpdate(sql);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }



}
