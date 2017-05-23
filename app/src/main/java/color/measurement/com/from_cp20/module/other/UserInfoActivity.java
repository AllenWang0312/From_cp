package color.measurement.com.from_cp20.module.other;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import butterknife.BindView;
import butterknife.ButterKnife;
import color.measurement.com.from_cp20.R;
import color.measurement.com.from_cp20.base.BaseActivity;
import color.measurement.com.from_cp20.common.imageloader.ImageLoaderProxy;
import color.measurement.com.from_cp20.manager.db.DBConsts;
import color.measurement.com.from_cp20.manager.db.DBHelper;
import color.measurement.com.from_cp20.manager.db.MySqlConsts;
import color.measurement.com.from_cp20.manager.sp.SPConsts;
import color.measurement.com.from_cp20.module.App;
import color.measurement.com.from_cp20.module.other.tools.MyDialogFragment;
import color.measurement.com.from_cp20.util.utils.L;
import color.measurement.com.from_cp20.widget.CircleImageView;

/**
 * Created by wpc on 2017/4/6.
 * 个人详情页
 */

public class UserInfoActivity extends BaseActivity implements View.OnClickListener, MyDialogFragment.LoginInputListener {

    private Toolbar mToolbarGlobal;
    private Context mContext;
    @BindView(R.id.zhu_xiao)
    Button mButton;
    @BindView(R.id.message_head)
    CircleImageView mImageView;
    @BindView(R.id.message_name)
    TextView mTextName;
    @BindView(R.id.message_number)
    TextView mTextNumber;
    @BindView(R.id.message_birth)
    TextView mTextBirth;
    @BindView(R.id.message_job)
    TextView mTextJob;
    @BindView(R.id.message_city)
    TextView mTextCity;
    @BindView(R.id.message_sign)
    TextView mTextSign;
    @BindView(R.id.xiugai_name)
    RelativeLayout xiugaiName;
    @BindView(R.id.xiugai_chengshi)
    RelativeLayout xiugaiChengshi;
    @BindView(R.id.xiugai_hangye)
    RelativeLayout xiugaiHangey;
    @BindView(R.id.xiugai_qianming)
    RelativeLayout xiugaiQIanming;
    @BindView(R.id.xiugai_shengri)
    RelativeLayout xiugaiShengri;

    @BindView(R.id.xiugai_mima)
    RelativeLayout xiugaiMima;
    private String mName;
    private String mImageUrl;
    private ResultSet mRs;
    private Connection mConn;
    private Statement mStmt;
    private String sql;
    private String mTel;
    private String mBirth;
    private String mAddress;
    private String mSign;
    private String mBusiness;
    private int mWay;
    DBHelper dbHelper;
    SQLiteDatabase db;
    private String mName2;
    private int weizhi;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfo2);
        ButterKnife.bind(this);
        L.e("oncreate");
        mContext = this;
        initView();
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(mContext, R.string.shifou_zhuxiao,
                        R.string.queding, R.string.quxiao, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SharedPreferences sp2 = getSharedPreferences(SPConsts.PREFERENCE_APP_CONFIG, MODE_PRIVATE);
                                final String tm = sp2.getString("tm", "");
                                final String name = App.logged_user.getName();
                                final String login = "false";
                                new Thread() {
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
                                            sql = "update " + MySqlConsts.user_table + " set config='" + tm + login + "'  where name='" + name + "'";
                                            L.e("sql==" + sql);
                                            L.e("mStmt.execute(sql);==" + mStmt.executeUpdate(sql));
                                        } catch (SQLException e) {
                                            e.printStackTrace();
                                        } catch (ClassNotFoundException e) {
                                            e.printStackTrace();
                                        } finally {
                                            try {
                                                if (con != null) {
                                                    con.close();
                                                }
                                            } catch (SQLException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                        super.run();
                                    }
                                }.start();
                                Intent intent2 = new Intent();
                                setResult(19, intent2);
                                finish();
                            }
                        }, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }, true);
            }
        });
    }

    private void initView() {
        xiugaiName.setOnClickListener(this);
        xiugaiChengshi.setOnClickListener(this);
        xiugaiHangey.setOnClickListener(this);
        xiugaiQIanming.setOnClickListener(this);
        xiugaiShengri.setOnClickListener(this);
        xiugaiMima.setOnClickListener(this);
        mToolbarGlobal = (Toolbar) findViewById(R.id.toolbar);
        mToolbarGlobal.setTitle("个人信息");
        setSupportActionBar(mToolbarGlobal);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        dbHelper = new DBHelper(mContext, DBConsts.DATE_BASE, null, 1);
        db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query(DBConsts.USER_TAB_NAME, null, " name = ? ", new String[]{App.logged_user.getName()}, null, null, null);
        if (cursor.moveToFirst()) {
            mName2 = cursor.getString(cursor.getColumnIndex("name"));
            String imageUrl = cursor.getString(cursor.getColumnIndex("portrait"));
            mTextName.setText(mName2);
            L.e("imageUrl==" + imageUrl);
            ImageLoaderProxy.getInstance().displayImage(imageUrl, mImageView, R.mipmap.caipu);
            mThread.start();
        }
    }

    Thread mThread = new Thread() {
        @Override
        public void run() {
            ConnectivityManager connectivityManager = (ConnectivityManager)
                    getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager
                    .getActiveNetworkInfo();
            if (null != networkInfo && networkInfo.isConnected()) {
                Log.e("main", "当前网络名称：" + networkInfo.getTypeName());
            } else {
                Log.e("main", "没有可用网络");
                Message msg = new Message();
                msg.what = 5;
                mHandler.sendMessage(msg);
                return;
            }
            L.e("thread");
            try {
                Class.forName("com.mysql.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            mRs = null;
            //获取连接
            mConn = null;
            try {
                mConn = DriverManager
                        .getConnection(
                                "jdbc:mysql://rdsz4kl5za13u8d0m3i8spublic.mysql.rds.aliyuncs.com:3306/chnspec",
                                "spec3205722251", "spec3205722251");
                mStmt = mConn.createStatement();
                L.e("mstem==" + mStmt);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (mConn != null) {// 初始化连接mysql
                Statement stmt = null;
                try {
                    stmt = mConn.createStatement();
                    sql = "SELECT * from " + MySqlConsts.user_table + " where name ='" + mName2 + "'";
                    L.e("spl==" + sql);
                    mRs = stmt.executeQuery(sql);
                    if (mRs.next()) {
                        L.e("9999");
                        mTel = (String) mRs.getObject("tel");
                        mBirth = (String) mRs.getObject("birthday");
                        mAddress = (String) mRs.getObject("address");
                        mSign = (String) mRs.getObject("sign");
                        mBusiness = (String) mRs.getObject("business");
                        mHandler.sendEmptyMessage(1);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    if (mConn != null) {
                        try {
                            mConn.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            super.run();
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    mTextNumber.setText(mTel.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2") + "   ");
                    mTextBirth.setText(mBirth);
                    mTextCity.setText(mAddress);
                    mTextJob.setText(mBusiness);
                    mTextSign.setText(mSign);
                    break;
                case 5:
                    Toast.makeText(UserInfoActivity.this, "当前无网络,请检查网络", Toast.LENGTH_SHORT).show();
                    break;
                case 10:
                    Toast.makeText(UserInfoActivity.this, "此为qq账号,不能修改密码", Toast.LENGTH_SHORT).show();
                    break;
                case 11:
                    Toast.makeText(UserInfoActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private android.support.v7.app.AlertDialog showDialog(
            Context context, int message, int sureText,
            int cancelText, DialogInterface.OnClickListener sureListener,
            DialogInterface.OnClickListener cancelListener, boolean cancelable) {
        android.support.v7.app.AlertDialog dialog =
                new android.support.v7.app.AlertDialog.Builder(context).setTitle("提示").
                        setMessage(message).setPositiveButton(sureText,
                        sureListener).setNegativeButton(cancelText, cancelListener).
                        setCancelable(cancelable).show();
        return dialog;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.xiugai_name:
                weizhi = 1;
                MyDialogFragment dialog = new MyDialogFragment("填写姓名", App.logged_user.getName());
                dialog.show(getFragmentManager(), "nameDialog");
                break;
            case R.id.xiugai_chengshi:
                weizhi = 2;
                MyDialogFragment dialog2 = new MyDialogFragment("填写城市", mTextCity.getText().toString());
                dialog2.show(getFragmentManager(), "cityDialog");
                break;
            case R.id.xiugai_hangye:
                weizhi = 3;
                MyDialogFragment dialog3 = new MyDialogFragment("填写行业", mTextJob.getText().toString());
                dialog3.show(getFragmentManager(), "jobDialog");
                break;
            case R.id.xiugai_qianming:
                weizhi = 4;
                MyDialogFragment dialog4 = new MyDialogFragment("填写签名", mTextSign.getText().toString());
                dialog4.show(getFragmentManager(), "signDialog");
                break;
            case R.id.xiugai_shengri:
                weizhi = 5;
                MyDialogFragment dialog5 = new MyDialogFragment("填写生日", mTextBirth.getText().toString());
                dialog5.show(getFragmentManager(), "birthDialog");
                break;
            case R.id.xiugai_mima:
                if(!App.logged_user.getPortrait_url().equals("")){
                    mHandler.sendEmptyMessage(10);
                    return;
                }
                weizhi = 6;
                MyDialogFragment dialog6 = new MyDialogFragment("修改密码", "");
                dialog6.show(getFragmentManager(), "mimaDialog");
                break;
        }
    }

    @Override
    public void onLoginInputComplete(String username) {
        switch (weizhi) {
            case 1:
                mTextName.setText(username);
                dbHelper = new DBHelper(mContext, DBConsts.DATE_BASE, null, 1);
                db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("name", username);
                // 目标数据表|要更新的数据|更新条件语句|更新条件参数列表
                db.update(DBConsts.USER_TAB_NAME, values, "name=?", new String[] { App.logged_user.getName() });
                db.close();
                App.logged_user.setName(username);
                mHandler.sendEmptyMessage(11);
                break;
            case 2:
                mTextCity.setText(username);
                mHandler.sendEmptyMessage(11);
                break;
            case 3:
                mTextJob.setText(username);
                mHandler.sendEmptyMessage(11);
                break;
            case 4:
                mTextSign.setText(username);
                mHandler.sendEmptyMessage(11);
                break;
            case 5:
                mTextBirth.setText(username);
                mHandler.sendEmptyMessage(11);
                break;
            case 6:
                mHandler.sendEmptyMessage(11);
                break;
        }
        L.e("username==" + username);
    }
}
