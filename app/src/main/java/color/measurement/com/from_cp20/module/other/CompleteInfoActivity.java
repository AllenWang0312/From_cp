package color.measurement.com.from_cp20.module.other;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.SQLException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import color.measurement.com.from_cp20.R;
import color.measurement.com.from_cp20.base.ProgressDialogActivity;
import color.measurement.com.from_cp20.manager.db.MySqlConsts;
import color.measurement.com.from_cp20.util.blankj.StringUtils;
import color.measurement.com.from_cp20.util.utils.L;
import es.dmoral.toasty.Toasty;

public class CompleteInfoActivity extends ProgressDialogActivity {

    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.et_tel_comp) EditText mEtTelComp;
    @BindView(R.id.et_ename_comp) EditText mEtEnameComp;
    @BindView(R.id.et_birthday_comp) EditText mEtBirthdayComp;
    @BindView(R.id.et_city_comp) EditText mEtCityComp;
    @BindView(R.id.et_business_comp) EditText mEtBusinessComp;
    @BindView(R.id.et_sigh_comp) EditText mEtSighComp;
    @BindView(R.id.et_pswd_comp) EditText mEtPswdComp;
    @BindView(R.id.et_repswd_comp) EditText mEtRepswdComp;
    @BindView(R.id.bt_commit_comp) Button mBtCommitComp;

    @BindView(R.id.zhuce_pre) LinearLayout mLayout;
    private String mTel;
    private String mName;
    private String mPassword;
    private String mRepassword;
    private String mBirth;
    private String mCity;
    private String mJob;
    private String mSign;
    private Connection con = null;
    private String sql;
    private ResultSet rs = null;
    private boolean DifferSC = false;
    private String mNumber;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_info);
        mContext = this;
        ButterKnife.bind(this);
        mToolbar.setTitle("完善信息");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        mNumber = intent.getStringExtra("phone_number");

        mEtTelComp.setText(mNumber);
        mEtTelComp.setEnabled(false);//不可编辑
        mEtEnameComp.requestFocus();//获取焦点
        if (!wangluo()) {
            mHandler.sendEmptyMessage(3);
        }
        mEtBirthdayComp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar date = Calendar.getInstance();

                int c_year = date.get(Calendar.YEAR);
                int c_month = date.get(Calendar.MONTH);
                int c_day = date.get(Calendar.DAY_OF_MONTH);
                new DatePickerDialog(CompleteInfoActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
//                        app.select_mod = 0;
//                        checked_year = year;
//                        checked_mouth = month;
//                        checked_day = day;
                        mEtBirthdayComp.setText(year + "-" + (month + 1) + "-" + day);
                    }
                }, c_year, c_month, c_day).show();
            }

        });
    }


    private boolean wangluo() {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager
                .getActiveNetworkInfo();
        if (null != networkInfo && networkInfo.isConnected()) {
            Log.e("main", "当前网络名称：" + networkInfo.getTypeName());
            return true;
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.bt_commit_comp)
    void commit() {
        L.e("完成");
        if (!wangluo()) {
            mHandler.sendEmptyMessage(3);
            return;
        }
        //电话号码
        mTel = mEtTelComp.getText().toString().trim();
        //用户名
        mName = mEtEnameComp.getText().toString().trim();
        //密码
        mPassword = mEtPswdComp.getText().toString().trim();
        //密码
        mRepassword = mEtRepswdComp.getText().toString().trim();
        //生日
        mBirth = mEtBirthdayComp.getText().toString();
        //城市
        mCity = mEtCityComp.getText().toString();
        //行业
        mJob = mEtBusinessComp.getText().toString();
        //签名
        mSign = mEtSighComp.getText().toString();
        if (StringUtils.isEmpty(mTel) || StringUtils.isEmpty(mName) || StringUtils.isEmpty(mPassword) || StringUtils.isEmpty(mRepassword)) {
            Toast.makeText(this, "信息不完善", Toast.LENGTH_SHORT).show();
            return;
        } else {
            if (!mPassword.equals(mRepassword)) {
                Toast.makeText(this, "两次密码输入不匹配", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        mHandler.postDelayed(mRunnable,5000);
        new Thread() {
            @Override
            public void run() {
                mHandler.sendEmptyMessage(4);
                boolean IS_CUN = false;
                try {
                    Class.forName("com.mysql.jdbc.Driver");
                } catch (ClassNotFoundException e) {
                    // TODO Auto-generated catch block
                    mHandler.sendEmptyMessage(5);
                    e.printStackTrace();
                }
                try {
                    con = DriverManager
                            .getConnection(
                                    "jdbc:mysql://rdsz4kl5za13u8d0m3i8spublic.mysql.rds.aliyuncs.com:3306/chnspec",
                                    "spec3205722251", "spec3205722251");
                    L.e("con==" + con);
                    if (con != null) {// 初始化连接mysql
                        Statement stmt = con.createStatement();
                        stmt.executeUpdate(sql);
                        sql = "SELECT password from " + MySqlConsts.user_table+ " where name ='" + mName + "'";
                        L.e("spl==" + sql);
                        rs = stmt.executeQuery(sql);
                        if (rs.next()) {
                            IS_CUN = true;
                            L.e("cunzai ");
                        }
                        if (IS_CUN) {
                            mHandler.sendEmptyMessage(5);
                            mHandler.sendEmptyMessage(0);
                            return;
                        } else {
                            sql = String
                                    .format("insert into %s(login_way,tel,name,password,qq_token,qq_openid,weixin_token,weixin_openid,weibo_token,weibo_openid,age,birthday,address,sign,business,config) " +
                                                    "values(\'%s\',\'%s\',\'%s\',\'%s\',\'%s\',\'%s\',\'%s\',\'%s\',\'%s\',\'%s\',\'%s\',\'%s\',\'%s\',\'%s\',\'%s\',\'%s\') ",
                                            MySqlConsts.user_table, "1", mTel, mName, mPassword, "", "", "", "", "", "", "",
                                            mBirth, mCity, mSign, mJob, "");
                            stmt = con.createStatement();
                            mHandler.removeCallbacks(mRunnable);
                            stmt.executeUpdate(sql);
                            L.e("完成");
                            mHandler.sendEmptyMessage(1);
                        }
                    }
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    mHandler.sendEmptyMessage(5);
                    L.e("2");
                    e.printStackTrace();
                } catch (java.sql.SQLException e) {
                    mHandler.sendEmptyMessage(5);
                    e.printStackTrace();
                }
            }
        }.start();
    }

    Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            dismissProgressDialog();
            Toasty.error(mContext, "连接超时,请确保网络连接").show();
        }
    };

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    Toast.makeText(mContext, "此用户名已存在,请更改", Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    mLayout.setVisibility(View.GONE);
                    /*Intent intent = new Intent(mContext,LoginActivity.class);
                    startActivity(intent);*/
                    finish();
                    break;
                case 3:
                    Toast.makeText(mContext, "当前无网络,请检查网络", Toast.LENGTH_SHORT).show();
                    break;
                case 4:
                    showProgressDialog("请稍后","正在登录中...");
                    break;
                case 5:
                    dismissProgressDialog();
                    break;
            }
            super.handleMessage(msg);
        }
    };

}
