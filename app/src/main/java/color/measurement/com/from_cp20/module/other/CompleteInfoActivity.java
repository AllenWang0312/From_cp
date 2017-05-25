package color.measurement.com.from_cp20.module.other;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
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
import android.widget.Toast;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import color.measurement.com.from_cp20.R;
import color.measurement.com.from_cp20.base.ProgressDialogActivity;
import color.measurement.com.from_cp20.manager.db.MySqlConsts;
import color.measurement.com.from_cp20.manager.db.MySqlHelper;
import color.measurement.com.from_cp20.module.been.User;
import color.measurement.com.from_cp20.util.blankj.StringUtils;
import color.measurement.com.from_cp20.util.utils.L;
import color.measurement.com.from_cp20.util.utils.T;

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

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    T.showSuccess(mContext, "注册成功");
                    Intent i = new Intent(CompleteInfoActivity.this, LoginActivity.class);
                    i.putExtra("username", mEtEnameComp.getText().toString().trim());
                    startActivity(i);
                    finish();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    private String mNumber;
    private Context mContext;
    MySqlHelper instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_info);
        mContext = this;
        ButterKnife.bind(this);
        instance = MySqlHelper.getInstance(mContext);

        mToolbar.setTitle("完善信息");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        mNumber = intent.getStringExtra("phone_number");

        mEtTelComp.setText(mNumber);
        mEtTelComp.setEnabled(false);//不可编辑
        mEtEnameComp.requestFocus();//获取焦点

        if (!wangluo()) {
            Toast.makeText(mContext, "当前无网络,请检查网络", Toast.LENGTH_SHORT).show();
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
//        if (!wangluo()) {
//            Toast.makeText(mContext, "当前无网络,请检查网络", Toast.LENGTH_SHORT).show();
//            return;
//        }
        final String mTel = mEtTelComp.getText().toString().trim();
        final String mName = mEtEnameComp.getText().toString().trim();
        final String mPassword = mEtPswdComp.getText().toString().trim();
        final String mRepassword = mEtRepswdComp.getText().toString().trim();
        final String mBirth = mEtBirthdayComp.getText().toString();
        final String mCity = mEtCityComp.getText().toString();
        final String mJob = mEtBusinessComp.getText().toString();
        final String mSign = mEtSighComp.getText().toString();

        if (StringUtils.isEmpty(mTel) || StringUtils.isEmpty(mName) || StringUtils.isEmpty(mPassword) || StringUtils.isEmpty(mRepassword)) {
            Toast.makeText(this, "信息不完善", Toast.LENGTH_SHORT).show();
            return;
        } else {
            if (!mPassword.equals(mRepassword)) {
                Toast.makeText(this, "两次密码输入不匹配", Toast.LENGTH_SHORT).show();
                return;
            }
        }
//        mHandler.postDelayed(mRunnable, 5000);
        new MySqlHelper.MySQLAsyTask(new Runnable() {
            @Override
            public void run() {
                User user = new User.Builder().setLogin_way(1).setTel(mTel).setName(mName).setPassword(mPassword).setBirthday(mBirth).setAddress(mCity).setSign(mSign).setBusiness(mJob).create();
                MySqlHelper.insertDataToMySql(instance.mStatement, MySqlConsts.user_table, user, mContext);
                mHandler.sendEmptyMessage(0);
            }
        }, true).execute();

//        new Thread() {
//            @Override
//            public void run() {
//                try {
//                    Class.forName("com.mysql.jdbc.Driver");
//                } catch (ClassNotFoundException e) {
//                    // TODO Auto-generated catch block
//                    dismissProgressDialog();
//                    e.printStackTrace();
//                }
//                try {
//                    con = DriverManager
//                            .getConnection(
//                                    "jdbc:mysql://rdsz4kl5za13u8d0m3i8spublic.mysql.rds.aliyuncs.com:3306/chnspec",
//                                    "spec3205722251", "spec3205722251");
//                    if (con != null) {// 初始化连接mysql
//                        Statement stmt = con.createStatement();
//                        rs = stmt.executeQuery("SELECT password from " + MySqlConsts.user_table + " where name ='" + mName + "'");
//                        if (rs.next()) {
//                        }
//                        if (IS_CUN) {
//                            dismissProgressDialog();
//                            Toast.makeText(mContext, "此用户名已存在,请更改", Toast.LENGTH_SHORT).show();
//                            return;
//                        } else {
//                            stmt = con.createStatement();
//
//                            mHandler.removeCallbacks(mRunnable);
////                            finish();
//                        }
//                    }
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                } catch (java.sql.SQLException e) {
//                    dismissProgressDialog();
//                    e.printStackTrace();
//                }
//            }
//        }.start();
//        dismissProgressDialog();
    }

//    Runnable mRunnable = new Runnable() {
//        @Override
//        public void run() {
//            dismissProgressDialog();
//            Toasty.error(mContext, "连接超时,请确保网络连接").show();
//        }
//    };

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

}
