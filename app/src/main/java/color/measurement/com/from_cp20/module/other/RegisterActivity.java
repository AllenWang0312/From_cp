package color.measurement.com.from_cp20.module.other;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import color.measurement.com.from_cp20.R;
import color.measurement.com.from_cp20.base.BaseActivity;
import color.measurement.com.from_cp20.common.util.CodeUtils;
import color.measurement.com.from_cp20.third.AppKeyContants;
import color.measurement.com.from_cp20.manager.db.MySqlConsts;
import color.measurement.com.from_cp20.module.main.MainActivity;
import color.measurement.com.from_cp20.util.utils.L;
import color.measurement.com.from_cp20.util.utils.T;

/**
 * Created by wpc on 2017/2/27.
 */

public class RegisterActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.et_tel_sign) EditText mEtTelSign;

    @BindView(R.id.bt_get_yzm_sign) Button mBtGetYzmSign;
    @BindView(R.id.et_yzm_sign) EditText mEtYzmSign;
    @BindView(R.id.bt_login_sign) Button mBtLoginSign;
    @BindView(R.id.iv_auth_code_regist) ImageView mIvAuthCodeRegist;
    @BindView(R.id.tv_notice_rigest) TextView mTvNoticeRigest;
    @BindView(R.id.et_tx_yzm) EditText mEtTxYzm;


    private static final int SUBMIT_VERIFICATION_CODE_COMPLETE = 1;
    private static final int GET_VERIFICATION_CODE_COMPLETE = 2;
    private static final int RESULT_ERROR = 3;

    private Context mContext;
    boolean flag = false;
    private ResultSet mRs;
    private Connection mConn;
    private Statement mStmt;
    private String sql;
    CodeUtils mCodeUtils;
    String from;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        Intent i = getIntent();
        from = i.getStringExtra("from");

        mToolbar.setTitle("注册");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mCodeUtils = CodeUtils.getInstance();
        initBitmap();
        initNoticeText();
        L.e("oncreate");
        SMSSDK.initSDK(this, AppKeyContants.SMS_APPKEY, AppKeyContants.SMS_APPSECRET);
        //注册回调
        SMSSDK.registerEventHandler(eh);
        mContext = this;
        mIvAuthCodeRegist.setOnClickListener(this);
        mBtGetYzmSign.setOnClickListener(this);
        mBtLoginSign.setOnClickListener(this);
        new Thread() {
            @Override
            public void run() {
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
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }.start();
        if (!wangluo()) {
            mHandler.sendEmptyMessage(2);
        }
    }

    private void initNoticeText() {
        String content = "点击立即注册,即表示您同意并愿意遵守彩谱用户协议和隐私政策";
        SpannableString spanText = new SpannableString(content);
        spanText.setSpan(new ForegroundColorSpan(Color.BLUE), 2, 6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spanText.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                T.show(mContext, "点击了 用户协议");
            }
        }, 20, 24, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spanText.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                T.show(mContext, "点击了 隐私政策");
            }
        }, 25, 29, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mTvNoticeRigest.setMovementMethod(LinkMovementMethod.getInstance());
        mTvNoticeRigest.setText(spanText);
    }

    private void initBitmap() {
        Bitmap bitmap = mCodeUtils.createBitmap();
        mIvAuthCodeRegist.setImageBitmap(bitmap);
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

    private EventHandler eh = new EventHandler() {
        @Override
        public void afterEvent(int event, int result, Object data) {
            if (result == SMSSDK.RESULT_COMPLETE) {
                // 回调完成
                L.e("回调完成");
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                    L.e("提交验证码成功");
                    handler.sendEmptyMessage(GET_VERIFICATION_CODE_COMPLETE);
                } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                    L.e("获取验证码成功");
                    handler.sendEmptyMessage(SUBMIT_VERIFICATION_CODE_COMPLETE);
                } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
                    L.e("返回支持发送验证码的国家列表");
                }
            } else if (result == SMSSDK.RESULT_ERROR) {
                Throwable throwable = (Throwable) data;
                throwable.printStackTrace();
                JSONObject object;
                try {
                    object = new JSONObject(throwable.getMessage());
                    Message msg = new Message();
                    Bundle bundle = new Bundle();
                    bundle.putInt("status", object.optInt("status"));// 错误代码
                    bundle.putString("detail", object.optString("detail"));// 错误描述
                    msg.setData(bundle);
                    msg.what = RESULT_ERROR;
                    handler.sendMessage(msg);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    };

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUBMIT_VERIFICATION_CODE_COMPLETE:
                    Toast.makeText(mContext, "获取成功", Toast.LENGTH_SHORT)
                            .show();
                    break;
                case GET_VERIFICATION_CODE_COMPLETE:
                    new Thread() {
                        @Override
                        public void run() {
                            //插入数据（手机号） 建表
                            Intent intent = new Intent(mContext, CompleteInfoActivity.class);
                            intent.putExtra("phone_number", mEtTelSign.getText().toString());
                            finish();
                            startActivity(intent);
                            super.run();
                        }
                    }.start();
                    Toast.makeText(mContext, "提交成功", Toast.LENGTH_SHORT)
                            .show();
                    break;
                case RESULT_ERROR:
                    Toast.makeText(
                            mContext,
                            msg.getData().getInt("status")
                                    + msg.getData().getString("detail"),
                            Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //取消注册回调
        SMSSDK.unregisterEventHandler(eh);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (from != null && from.equals("guide")) {
                finish();
                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
            }
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (!wangluo()) {
            mHandler.sendEmptyMessage(2);
        }
        if (v.getId() == R.id.bt_get_yzm_sign) {//获取验证码
            L.e("main", "bt");
            if(mEtTxYzm.getText().toString().equals(CodeUtils.getInstance().mBuilder.toString())){
                new Thread() {
                    @Override
                    public void run() {
                        boolean IS_ZHU = false;
                        try {
                            Class.forName("com.mysql.jdbc.Driver");
                        } catch (ClassNotFoundException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        try {
                            mConn = DriverManager
                                    .getConnection(
                                            "jdbc:mysql://rdsz4kl5za13u8d0m3i8spublic.mysql.rds.aliyuncs.com:3306/chnspec",
                                            "spec3205722251", "spec3205722251");
                            L.e("con==" + mConn);
                            if (mConn != null) {// 初始化连接mysql
                                Statement stmt = mConn.createStatement();
                                String sql = "SELECT COUNT(*) FROM " + MySqlConsts.user_table;
                                L.e("spl==" + sql);
                                stmt.executeQuery(sql);
                                L.e("表存在");
                                sql = "SELECT password from " +MySqlConsts.user_table + " where login_way = '1'and tel =" + mEtTelSign.getText().toString().trim() + "";
                                L.e("spl==" + sql);
                                mRs = stmt.executeQuery(sql);
                                if (mRs.next()) {
                                    IS_ZHU = true;
                                }
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        if (!IS_ZHU) {
                            L.e("mCodeUtils.getCode()=="+mCodeUtils.getCode());
                            if(mEtTxYzm.getText().toString().equalsIgnoreCase(mCodeUtils.getCode())){
                                if (isMobileNO(mEtTelSign.getText().toString().trim())) {
                                    //获取验证
                                    L.e("accoutEdit.getText().toString().trim()==" + mEtTelSign.getText().toString().trim());
                                    mHandler.sendEmptyMessage(1);
                                } else {
                                    mHandler.sendEmptyMessage(4);
                                }
                            }else {
                                mHandler.sendEmptyMessage(5);
                            }
                        } else {
                            mHandler.sendEmptyMessage(3);
                        }
                        super.run();
                    }
                }.start();
            }else {
                T.showError(mContext,"图形验证失败");
            }

        } else if (v.getId() == R.id.bt_login_sign) {//注册
            L.e("main", "bt2");
            /*Intent intent = new Intent(mContext,CompleteInfoActivity.class);
            intent.putExtra("phone_number",mEtTelSign.getText().toString());
            finish();
            startActivity(intent);*/
            SMSSDK.submitVerificationCode("86", mEtTelSign.getText().toString().trim(), mEtYzmSign.getText().toString().trim());
        } else if (v.getId() == R.id.iv_auth_code_regist) {
            initBitmap();
        }

    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {

            } else if (msg.what == 1) {
                SMSSDK.getVerificationCode("86", mEtTelSign.getText().toString().trim());
            } else if (msg.what == 2) {
                Toast.makeText(mContext, "当前无网络,请检查网络", Toast.LENGTH_SHORT).show();
            } else if (msg.what == 3) {
                Toast.makeText(mContext, "此号码已注册,请登录", Toast.LENGTH_SHORT).show();
            } else if (msg.what == 4) {
                Toast.makeText(mContext, "请填写正确的号码", Toast.LENGTH_SHORT).show();
            }else if (msg.what == 5){
                T.show(mContext,"请正确填写图形验证码");
            }
            super.handleMessage(msg);
        }
    };

    private boolean isMobileNO(String mobiles) {
        String telRegex = "[1][358]\\d{9}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobiles)) return false;
        else return mobiles.matches(telRegex);
    }


}
