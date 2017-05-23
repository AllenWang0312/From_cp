package color.measurement.com.from_cp20.module.other;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.tencent.connect.UserInfo;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import color.measurement.com.from_cp20.third.AccessTokenKeeper;
import color.measurement.com.from_cp20.third.AppKeyContants;
import color.measurement.com.from_cp20.manager.db.DBConsts;
import color.measurement.com.from_cp20.manager.db.DBHelper;
import color.measurement.com.from_cp20.manager.db.MySqlConsts;
import color.measurement.com.from_cp20.manager.sp.SPConsts;
import color.measurement.com.from_cp20.R;
import color.measurement.com.from_cp20.base.ProgressDialogActivity;
import color.measurement.com.from_cp20.manager.db.MySqlUtils;
import color.measurement.com.from_cp20.module.App;
import color.measurement.com.from_cp20.module.been.User;
import color.measurement.com.from_cp20.util.blankj.StringUtils;
import color.measurement.com.from_cp20.util.utils.L;
import es.dmoral.toasty.Toasty;

import static color.measurement.com.from_cp20.module.other.LoginConsts.QQ;
import static color.measurement.com.from_cp20.module.other.LoginConsts.SCOPE;
import static color.measurement.com.from_cp20.module.other.LoginConsts.TEL;
import static color.measurement.com.from_cp20.module.other.LoginConsts.WEIBO;

public class LoginActivity extends ProgressDialogActivity implements View.OnClickListener,
        PlatformActionListener, Handler.Callback {


    Context mContext;
    @BindView(R.id.toolbar) Toolbar mToolbarGlobal;
    @BindView(R.id.et_name_login) EditText mEtNameLogin;
    @BindView(R.id.et_password_login) EditText mEtPasswordLogin;
    @BindView(R.id.login_login) Button mLoginLogin;

    @BindView(R.id.social_qq_login) ImageView mSocialQqLogin;
    @BindView(R.id.social_weichat_login) ImageView mSocialWeichatLogin;
    @BindView(R.id.social_weibo_login) ImageView mSocialWeiboLogin;
    @BindView(R.id.login_check) CheckBox mCheckBox;
    @BindView(R.id.login_pre) LinearLayout mLayout;
    @BindView(R.id.zhuce_new) TextView textZhuce;
    @BindView(R.id.mima_check) CheckBox mimaCheckBox;

    private Connection con = null;
    private String sql;
    private ResultSet rs = null;
    private String mPass;
    private String mName;

    private String loginName;
    private String loginImageUrl;
    private int login_way;

    private Tencent mMTencent;
    private UserInfo qqInfo;//qq

    private AuthInfo mAuthInfo;//weibo
    private Oauth2AccessToken mAccessToken;
    private SsoHandler mSsoHandler;
    //weixin
    private IWXAPI api;
    String openID = null;
    private String mAccessToken1;
    SharedPreferences sp;
    DBHelper dbHelper;
    SQLiteDatabase db;
    private boolean is_QQ;
    private Statement mStmt;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        sp = getSharedPreferences(SPConsts.PREFERENCE_APP_CONFIG, MODE_PRIVATE);
        mContext = this;

        textZhuce.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        mMTencent = Tencent.createInstance(AppKeyContants.QQ_APPID, this.getApplicationContext());
        String nameSp = sp.getString("username", "");
        String passSp = sp.getString("password", "");
        if (!StringUtils.isEmpty(nameSp)) {
            mEtNameLogin.setText(nameSp);
        }
        if (!StringUtils.isEmpty(passSp)) {
            mEtPasswordLogin.setText(passSp);
        }
        mEtNameLogin.setSelection(mEtNameLogin.getText().length());
        mEtPasswordLogin.setSelection(mEtPasswordLogin.getText().length());
        setSupportActionBar(mToolbarGlobal);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initView();
//        initWeibo();
//        initWEIXI();
    }

    private void initWEIXI() {
        api = WXAPIFactory.createWXAPI(mContext, AppKeyContants.WEIXI_APPID, true);
        api.registerApp(AppKeyContants.WEIXI_APPID);
    }

    @Override
    protected void onResume() {
        L.e("login_onResume");
        mEtNameLogin.setSelection(mEtNameLogin.getText().length());
        mEtPasswordLogin.setSelection(mEtPasswordLogin.getText().length());
        super.onResume();
    }

    private void initView() {
        textZhuce.setOnClickListener(this);
        mLoginLogin.setOnClickListener(this);
        mSocialWeiboLogin.setOnClickListener(this);
        mSocialQqLogin.setOnClickListener(this);
        mSocialWeichatLogin.setOnClickListener(this);
        mimaCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mEtPasswordLogin.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    mEtPasswordLogin.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                mEtPasswordLogin.setSelection(mEtPasswordLogin.getText().length());
            }
        });
    }

    private void initWeibo() {
        // 初始化授权类对象，将应用的信息保存
        mAuthInfo = new AuthInfo(this, AppKeyContants.WEIBO_APPKEY,
                AppKeyContants.WEIBO_WEB, SCOPE);
        mSsoHandler = new SsoHandler(LoginActivity.this, mAuthInfo);
        // 第一次启动本应用，AccessToken 不可用
        mAccessToken = AccessTokenKeeper.readAccessToken(this);
    }

    @Override
    protected void onDestroy() {
        mMTencent.logout(this);
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
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
        switch (v.getId()) {
            case R.id.social_qq_login://qq登录
                login2();
                break;
            case R.id.social_weibo_login://微博登录
                L.e("weibologin");
                showProgressDialog("请稍后", "正在登录中...");
                weiboLogin();
                break;
            case R.id.login_login://注册账号登录
                zhuLogin();
                break;
            case R.id.social_weichat_login://微信登录
                final SendAuth.Req req = new SendAuth.Req();
                req.scope = "snsapi_userinfo";
                req.state = "carjob_wx_login";
                api.sendReq(req);
                break;
            case R.id.zhuce_new://跳转到注册页面
                Intent intent = new Intent(mContext, RegisterActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void weiboLogin() {
        mSsoHandler.authorize(new AuthListener());
    }


    @Override
    public boolean handleMessage(Message msg) {
        String text = getString(R.string.logining, msg.obj);
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {

    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {

    }

    @Override
    public void onCancel(Platform platform, int i) {

    }

    /**
     * 微博认证授权回调类。 1. SSO 授权时，需要在 {@link #onActivityResult} 中调用
     * {@link SsoHandler#authorizeCallBack} 后， 该回调才会被执行。 2. 非 SSO
     * 授权时，当授权结束后，该回调就会被执行。 当授权成功后，请保存该 access_token、expires_in、uid 等信息到
     * SharedPreferences 中。
     */
    class AuthListener implements WeiboAuthListener {
        @Override
        public void onCancel() {
            // TODO Auto-generated method stub
            Toast.makeText(LoginActivity.this, "取消授权", Toast.LENGTH_LONG)
                    .show();
        }

        @Override
        public void onComplete(Bundle values) {
            L.e("weibo 回调");
            // TODO Auto-generated method stub
            // 从 Bundle 中解析 Token
            mAccessToken = Oauth2AccessToken.parseAccessToken(values);
            L.e("value===" + values.toString());
            if (mAccessToken.isSessionValid()) {
                String nickname = String.valueOf(values
                        .get("com.sina.weibo.intent.extra.NICK_NAME"));
                L.e("nickname==" + nickname);
                String access_token = String.valueOf(values
                        .get("com.sina.weibo.intent.extra.access_token"));
                L.e("access_token==" + access_token);

                String uid = String.valueOf(values
                        .get("com.sina.weibo.intent.extra.uid"));
                L.e("uid==" + uid);

                String _weibo_transaction = String.valueOf(values
                        .get("com.sina.weibo.intent.extra._weibo_transaction"));
                L.e("_weibo_transaction==" + _weibo_transaction);
                // 保存 Token 到 SharedPreferences
                AccessTokenKeeper.writeAccessToken(LoginActivity.this,
                        mAccessToken);
                Toast.makeText(LoginActivity.this, "授权成功", Toast.LENGTH_SHORT)
                        .show();
                String headAddress = String.valueOf(values.get("com.sina.weibo.intent.extra.USER_ICON"));
                L.e("headAddress==" + headAddress);
                loginName = nickname;//微博
                loginImageUrl = headAddress;
                login_way = WEIBO;
                mHandler.sendEmptyMessage(6);
            } else {
                // 以下几种情况，您会收到 Code：
                // 1. 当您未在平台上注册的应用程序的包名与签名时；
                // 2. 当您注册的应用程序包名与签名不正确时；
                // 3. 当您在平台上注册的包名和签名与您当前测试的应用的包名和签名不匹配时。
                String code = values.getString("code");
                String message = "授权失败";
                if (!TextUtils.isEmpty(code)) {
                    message = message + "\nObtained the code: " + code;
                }
                Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG)
                        .show();
            }
        }

        @Override
        public void onWeiboException(WeiboException e) {
            // TODO Auto-generated method stub
            Toast.makeText(LoginActivity.this,
                    "Auth exception : " + e.getMessage(), Toast.LENGTH_LONG)
                    .show();
        }

    }

    private class BaseUiListener implements IUiListener {
        protected void doComplete(JSONObject values) {

        }

        @Override
        public void onComplete(Object response) {
            L.e("onComplete:");
            L.e(response.toString());
            JSONObject jo = (JSONObject) response;
            int ret = 0;
            try {
                ret = jo.getInt("ret");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            L.e("json=" + String.valueOf(jo));
            if (ret == 0) {
                Toast.makeText(mContext, "登录成功",
                        Toast.LENGTH_LONG).show();

                try {
                    openID = jo.getString("openid");
                    mAccessToken1 = jo.getString("access_token");
                    String expires = jo.getString("expires_in");
                    L.e("qq openID==" + openID);
                    mMTencent.setOpenId(openID);
                    mMTencent.setAccessToken(mAccessToken1, expires);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                getuserInfo();
                doComplete((JSONObject) response);
            }
        }

        @Override
        public void onError(UiError e) {
            L.e("onError:", "code:" + e.errorCode + ", msg:"
                    + e.errorMessage + ", detail:" + e.errorDetail);
        }

        @Override
        public void onCancel() {
            L.e("onCancel", "");
        }
    }


    //qq登录
    public void login2() {
        mMTencent = Tencent.createInstance("1106082920", this.getApplicationContext());
        L.e("mMTencent==" + mMTencent);
        if (!mMTencent.isSessionValid()) {
            showProgressDialog("请稍后", "正在登录中...");
            mMTencent.login(this, "get_simple_userinfo,add_topic", new BaseUiListener());
        }
    }

    private void getuserInfo() {
        qqInfo = new UserInfo(LoginActivity.this, mMTencent.getQQToken());
        qqInfo.getUserInfo(getQQinfoListener);
    }

    private IUiListener getQQinfoListener = new IUiListener() {
        @Override
        public void onComplete(Object response) {
            try {
                JSONObject jsonObject = (JSONObject) response;
                L.e("qq用户信息", jsonObject.toString());
                String headImage = (String) jsonObject.get("figureurl_qq_2");
                String name = jsonObject.getString("nickname");
                String gender = jsonObject.getString("gender");
                String province = jsonObject.getString("province");
                String city = jsonObject.getString("city");
                loginImageUrl = headImage;
                loginName = name;//qq登录
                login_way = QQ;
                MySqlUtils.getConnect("", name, "", mAccessToken1, openID, "", "", "", "", "", "", province + city, "", "", "");
                mHandler.sendEmptyMessage(7);
                //处理自己需要的信息
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError(UiError uiError) {

        }

        @Override
        public void onCancel() {

        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        L.e("resultCode==" + resultCode);
        L.e("requestCode==" + requestCode);
        dismissProgressDialog();
        if (resultCode == -1 && requestCode == 11101) {
            Tencent.onActivityResultData(requestCode, resultCode, data, new BaseUiListener());
        }

        if (mSsoHandler != null) {
            L.e("mSohandle");
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
        super.onActivityResult(requestCode, resultCode, data);
        // 重要：发起 SSO 登陆的 Activity 必须重写 onActivityResults
    }


    Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            dismissProgressDialog();
            Toasty.error(mContext, "连接超时,请确保网络连接").show();
        }
    };

    private void zhuLogin() {
        L.e("login");
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("username", mEtNameLogin.getText().toString().trim());
        editor.putString("password", mEtPasswordLogin.getText().toString().trim());
        editor.commit();
        final String name = mEtNameLogin.getText().toString().trim();
        final String password = mEtPasswordLogin.getText().toString().trim();
        if (StringUtils.isEmpty(name) || StringUtils.isEmpty(password)) {
            mHandler.sendEmptyMessage(2);
            return;
        }
        showProgressDialog("请稍后", "正在登录中...");
        mHandler.postDelayed(mRunnable, 5000);
        new Thread() {
            @Override
            public void run() {
                boolean IS_SUCCESS = false;
                try {
                    Class.forName("com.mysql.jdbc.Driver");
                } catch (ClassNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                try {
                    con = DriverManager
                            .getConnection(
                                    "jdbc:mysql://rdsz4kl5za13u8d0m3i8spublic.mysql.rds.aliyuncs.com:3306/chnspec",
                                    "spec3205722251", "spec3205722251");
                    L.e("con==" + con);
                    if (con != null) {// 初始化连接mysql
                        mStmt = con.createStatement();
                        String sql = "SELECT COUNT(*) FROM " + MySqlConsts.user_table;
                        L.e("spl==" + sql);
                        mStmt.executeQuery(sql);
                        L.e("表存在");
                        if (color.measurement.com.from_cp20.common.util.StringUtils.isMobileNO(name)) {//手机号
                            sql = "SELECT password from " + MySqlConsts.user_table + " where login_way = '1' and tel =" + name + "";
                            L.e("spl==" + sql);
                            rs = mStmt.executeQuery(sql);
                            if (rs.next()) {
                                mPass = (String) rs.getObject("password");
                            }
                            L.e("mpass==" + mPass);
                            sql = "SELECT name from " + MySqlConsts.user_table + " where tel =" + name + "";
                            rs = mStmt.executeQuery(sql);
                            if (rs.next()) {
                                mName = (String) rs.getObject("name");
                            }
                            L.e("mName==" + mName);
                            if (!mPass.equals("") && mPass.equals(password)) {
                                L.e("账号密码都正确  登录");
                                IS_SUCCESS = true;
                                loginName = mName;
                                loginImageUrl = "";
                                login_way = TEL;
                            }
                        } else {//用户名
                            sql = "SELECT password from " + MySqlConsts.user_table + "  where login_way = '1' and name ='" + name + "'";
                            L.e("spl==" + sql);
                            rs = mStmt.executeQuery(sql);
                            if (rs.next()) {
                                mPass = (String) rs.getObject("password");
                            }
                            L.e("mpass==" + mPass);
                            if (mPass != null && !mPass.equals("") && mPass.equals(password)) {
                                L.e("账号密码都正确  登录");
                                loginName = name;
                                loginImageUrl = "";
                                login_way = TEL;
                                IS_SUCCESS = true;
                            }
                        }
                        L.e("1");
                        L.e("IS_SUCCESS==" + IS_SUCCESS);
                        if (IS_SUCCESS) {
                            mHandler.sendEmptyMessage(1);
                            mHandler.removeCallbacks(mRunnable);
                        } else {
                            mHandler.sendEmptyMessage(0);
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                super.run();
            }
        }.start();
    }

    private void turnMainActivity(int requestCode) {
        SharedPreferences sp = getSharedPreferences(SPConsts.PREFERENCE_APP_CONFIG, MODE_PRIVATE);
        final String tm = sp.getString("tm", "");
        new Thread() {
            @Override
            public void run() {
                sql = "select * from " + MySqlConsts.user_table + " where name = '" + loginName + "'";
                try {
                    if (mStmt == null) {
                        try {
                            Class.forName("com.mysql.jdbc.Driver");
                            con = DriverManager
                                    .getConnection(
                                            "jdbc:mysql://rdsz4kl5za13u8d0m3i8spublic.mysql.rds.aliyuncs.com:3306/chnspec",
                                            "spec3205722251", "spec3205722251");
                            mStmt = con.createStatement();
                        } catch (SQLException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } catch (ClassNotFoundException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                    rs = mStmt.executeQuery(sql);
                    if (rs.next()) {
                        String config = (String) rs.getObject("config");
                        if (config.endsWith("true")) {
                            String[] strs = config.split("t");
                            String now = strs[0].toString();
                            if (!tm.equals(now)) {
                                //提示是否强制登录
                                mHandler.sendEmptyMessage(8);
                            } else {
                                //正常登录
                                mHandler.sendEmptyMessage(9);
                            }
                            L.e("strs[0].toString();==" + strs[0].toString());
                        } else {
                            //正常登录
                            mHandler.sendEmptyMessage(9);
                        }
                    } else {
                        //正常登录
                        mHandler.sendEmptyMessage(9);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    dismissProgressDialog();
                }
                super.run();
            }
        }.start();
        //先判断此账号是否登录
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    dismissProgressDialog();
                    mHandler.removeCallbacks(mRunnable);
                    Toast.makeText(LoginActivity.this, "账号或密码不正确", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(LoginActivity.this, "账号或密码不能为空", Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    //注册账号登录
                    is_QQ = false;
                    turnMainActivity(20);
                    break;
                case 5:
                    Toast.makeText(LoginActivity.this, "当前无网络,请检查网络", Toast.LENGTH_SHORT).show();
                    break;
                case 6:
                    //微博登录
                    dismissProgressDialog();
                    turnMainActivity(21);
                    break;
                case 7:
                    //qq登录
                    is_QQ = true;
                    turnMainActivity(22);
                    break;
                case 8:
                    showDialog2(mContext, R.string.shifou_qiangzhi,
                            R.string.queding, R.string.quxiao, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mHandler.sendEmptyMessage(9);
                                }
                            }, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dismissProgressDialog();
                                }
                            }, true);
                    break;
                case 9:
                    SharedPreferences sp = getSharedPreferences(SPConsts.PREFERENCE_APP_CONFIG, MODE_PRIVATE);
                    final String tm = sp.getString("tm", "");
                    dbHelper = new DBHelper(mContext, DBConsts.DATE_BASE, null, 1);
                    db = dbHelper.getWritableDatabase();
                    MySqlUtils.updata(tm, MySqlConsts.user_table, "true");
                    App.logged_user = new User(loginName, is_QQ ? "" : mEtPasswordLogin.getText().toString().trim(), loginImageUrl, true);
                    App.logged_user.saveToSQLite(db, DBConsts.USER_TAB_NAME);
                    Intent intent2 = new Intent();
                    setResult(20, intent2);
                    App.logged_user.setName(loginName);
                    App.logged_user.setIs_login(true);
                    mHandler.sendEmptyMessage(10);
                    finish();
                    break;
                case 10:
                    Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                    break;

            }
            super.handleMessage(msg);
        }
    };

    private android.support.v7.app.AlertDialog showDialog2(
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
//    private boolean isMobileNO(String mobiles) {
//        String telRegex = "[1][358]\\d{9}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
//        if (TextUtils.isEmpty(mobiles)) return false;
//        else return mobiles.matches(telRegex);
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.login_register_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
       /* if (item.getItemId() == R.id.first_menu_login) {
            //跳转到注册页面
            Intent intent = new Intent(mContext, RegisterActivity.class);
            startActivity(intent);
        }*/
        return super.onOptionsItemSelected(item);
    }

    public void showProgressDialog(String title, String meg) {
        if (dialog == null) {
            dialog = new ProgressDialog(mContext);
        }
        dialog.setCancelable(false);
        dialog.setTitle(title);
        dialog.setMessage(meg);
        dialog.show();
    }

    public void dismissProgressDialog() {
        if (dialog.isShowing()) {
            dialog.dismiss();
            return;
        }
    }


}
