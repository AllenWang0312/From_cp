package color.measurement.com.from_cp20.manager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import color.measurement.com.from_cp20.module.App;
import color.measurement.com.from_cp20.util.utils.L;

/**
 * Created by cimcenter on 2017/5/5.
 */

public  class  UpLoadThread implements Runnable{
    String tm;
    String login;
    String table;
    public UpLoadThread(){

    }
    public UpLoadThread(String tm, String login,String table) {
        this.tm = tm;
        this.login = login;
        this.table = table;
    }

    public String getTm() {
        return tm;
    }

    public void setTm(String tm) {
        this.tm = tm;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    @Override
    public void run() {
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
            sql="update "+table+" set config='"+tm+login+"'  where name='"+ App.logged_user.getName()+"'";
            L.e("sql=="+sql);
            L.e("mStmt.execute(sql);=="+mStmt.executeUpdate(sql));
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
}