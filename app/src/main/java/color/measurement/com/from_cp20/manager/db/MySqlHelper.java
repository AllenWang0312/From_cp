package color.measurement.com.from_cp20.manager.db;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import color.measurement.com.from_cp20.manager.ins.Instrument;
import color.measurement.com.from_cp20.module.been.data.LightColorData;
import color.measurement.com.from_cp20.module.been.data.LustreData;
import color.measurement.com.from_cp20.module.been.interfaze.Data;

import static color.measurement.com.from_cp20.manager.db.MySqlConsts.DB_URL;
import static color.measurement.com.from_cp20.manager.db.MySqlConsts.DB_name;
import static color.measurement.com.from_cp20.manager.db.MySqlConsts.DB_pswd;

/**
 * Created by wpc on 2017/5/4.
 */

public class MySqlHelper {

    static Context mContext;

    static private ProgressDialog mProgressDialog;
    private static MySqlHelper instance;

    private MySqlHelper() {

    }

    public static MySqlHelper getInstance(Context context) {
        if (instance == null) {
            instance = new MySqlHelper();
        }
        if (mContext != context) {
            mContext = context;
            mProgressDialog = new ProgressDialog(context);
            mProgressDialog.setTitle("请稍后");
            mProgressDialog.setMessage("数据加载中");
        }
        return instance;
    }

    public static Connection mConnection;
    public static Statement mStatement;

    public static class MySQLAsyTask extends AsyncTask {
        Runnable run;
        boolean show;

        public MySQLAsyTask(Runnable run, boolean showprogress) {
            this.run = run;
            show = showprogress;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (show) {
                mProgressDialog.show();
            }
        }

        @Override
        protected Object doInBackground(Object[] params) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
            } catch (ClassNotFoundException e) {
            }
            try {
                mConnection = DriverManager.getConnection(DB_URL, DB_name, DB_pswd);
                mStatement = mConnection.createStatement();
                run.run();

            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            if (mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
            super.onPostExecute(o);
        }

        @Override
        protected void onProgressUpdate(Object[] values) {
            super.onProgressUpdate(values);
        }
    }

    //从mysql下载同名表  需要添加其他been类型
    public static void downloadDataFromMySql(Statement statement, String table_name, SQLiteDatabase db) {
        ArrayList<Data> datas = new ArrayList<>();
        int type = Instrument.tableType(table_name);
        try {
            ResultSet set = statement.executeQuery("select * from " + table_name);
            if (set.first()) {
                do {
                    switch (type) {
                        case 0:
                            datas.add(new LightColorData(set));
                            break;
                        case 2:
                            datas.add(new LustreData(set));
                            break;
                    }

                } while (set.next());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        for (Data d : datas) {
            db.insert(table_name, null, d.getContentValue());
        }
    }

    public static void insertDataToMySql(Statement statement, String table_name, Data data, Context context) {
        StringBuffer cloum = new StringBuffer(), values = new StringBuffer();
        Iterator iter = data.toHashMapForMySql(context).entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<String, Object> entry = (Map.Entry) iter.next();
            String key = entry.getKey();
            Object value = entry.getValue();
            cloum.append(key);
            if (value instanceof String) {
                values.append("'" + value + "'");
            } else {
                values.append(value);
            }

            if (iter.hasNext()) {
                cloum.append(",");
                values.append(",");
            }
        }
        try {
//            int id = statement.executeUpdate("insert " + table_name + "(" + cloum.toString() + ")"
//                    + "values(" + values.toString() + ")");
//            return id;
            statement.execute("insert " + table_name + "(" + cloum.toString() + ")"
                    + "values(" + values.toString() + ")");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
