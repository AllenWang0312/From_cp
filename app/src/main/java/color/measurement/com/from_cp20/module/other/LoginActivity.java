package color.measurement.com.from_cp20.module.other;

import android.app.AlertDialog;
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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import color.measurement.com.from_cp20.R;
import color.measurement.com.from_cp20.base.ProgressDialogActivity;
import color.measurement.com.from_cp20.manager.db.DBConsts;
import color.measurement.com.from_cp20.manager.db.DBHelper;
import color.measurement.com.from_cp20.manager.db.MySqlConsts;
import color.measurement.com.from_cp20.manager.db.MySqlHelper;
import color.measurement.com.from_cp20.manager.sp.SPConsts;
import color.measurement.com.from_cp20.module.App;
import color.measurement.com.from_cp20.module.been.User;
import color.measurement.com.from_cp20.third.AccessTokenKeeper;
import color.measurement.com.from_cp20.third.AppKeyContants;
import color.measurement.com.from_cp20.util.blankj.StringUtils;
import color.measurement.com.from_cp20.util.utils.L;
import es.dmoral.toasty.Toasty;

import static color.measurement.com.from_cp20.module.other.LoginConsts.NAME;
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
    private Tencent mMTencent;
    private UserInfo qqInfo;//qq

    private AuthInfo mAuthInfo;//weibo
    private Oauth2AccessToken mAccessToken;
    private SsoHandler mSsoHandler;
    //weixin
    private IWXAPI api;
    SharedPreferences sp;
    DBHelper dbHelper;
    SQLiteDatabase db;
    private boolean is_QQ;

    MySqlHelper instance;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        sp = getSharedPreferences(SPConsts.PREFERENCE_APP_CONFIG, MODE_PRIVATE);
        mContext = this;
        dbHelper = new DBHelper(mContext, DBConsts.DATE_BASE, null, 1);
        db = dbHelper.getWritableDatabase();
        instance = MySqlHelper.getInstance(mContext);

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
            // TODO Auto-generated method stub
            // 从 Bundle 中解析 Token
            mAccessToken = Oauth2AccessToken.parseAccessToken(values);
            if (mAccessToken.isSessionValid()) {
                String nickname = String.valueOf(values
                        .get("com.sina.weibo.intent.extra.NICK_NAME"));
                String access_token = String.valueOf(values
                        .get("com.sina.weibo.intent.extra.access_token"));
                String uid = String.valueOf(values
                        .get("com.sina.weibo.intent.extra.uid"));
                String _weibo_transaction = String.valueOf(values
                        .get("com.sina.weibo.intent.extra._weibo_transaction"));
                // 保存 Token 到 SharedPreferences
                AccessTokenKeeper.writeAccessToken(LoginActivity.this,
                        mAccessToken);
                Toast.makeText(LoginActivity.this, "授权成功", Toast.LENGTH_SHORT)
                        .show();
                String headAddress = String.valueOf(values.get("com.sina.weibo.intent.extra.USER_ICON"));
                App.logged_user = new User(nickname, headAddress, WEIBO);
                App.logged_user.setIs_login(true);
                App.logged_user.saveToSQLite(db, DBConsts.USER_TAB_NAME);

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

    String openID = null;
    private String mAccessToken1;

    private class BaseUiListener implements IUiListener {
        @Override
        public void onComplete(Object response) {
            JSONObject jo = (JSONObject) response;
            int ret = 0;
            try {
                ret = jo.getInt("ret");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (ret == 0) {
                Toast.makeText(mContext, "登录成功",
                        Toast.LENGTH_LONG).show();

                try {
                    openID = jo.getString("openid");
                    mAccessToken1 = jo.getString("access_token");
                    String expires = jo.getString("expires_in");
                    mMTencent.setOpenId(openID);
                    mMTencent.setAccessToken(mAccessToken1, expires);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                qqInfo = new UserInfo(LoginActivity.this, mMTencent.getQQToken());
                qqInfo.getUserInfo(getQQinfoListener);
//                doComplete((JSONObject) response);
            }

        }
//
//        protected void doComplete(JSONObject values) {
//
//        }

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

    }

    private IUiListener getQQinfoListener = new IUiListener() {
        @Override
        public void onComplete(Object response) {
            try {
                JSONObject jsonObject = (JSONObject) response;
                String headImage = (String) jsonObject.get("figureurl_qq_2");
                String name = jsonObject.getString("nickname");
                String gender = jsonObject.getString("gender");
                String province = jsonObject.getString("province");
                String city = jsonObject.getString("city");
                App.logged_user = new User(name, headImage, QQ);
                App.logged_user.setAddress(province + city);
                App.logged_user.setQq_token(mAccessToken1);
                App.logged_user.setQq_openid(openID);
                App.logged_user.setTel_id(App.tel_id);

                new MySqlHelper.MySQLAsyTask(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            ResultSet set = instance.mStatement.executeQuery("select * from " + MySqlConsts.user_table + " where qq_openid = '" + openID + "'");
                            if (set.first()) {
                                instance.mStatement.executeUpdate("update " + MySqlConsts.user_table + " set qq_token = '" + mAccessToken1 + "' where qq_openid = '" + openID + "'");
                            } else {
                                MySqlHelper.insertDataToMySql(instance.mStatement,
                                        MySqlConsts.user_table,
                                        App.logged_user, mContext);
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                        try {
                            ResultSet rs = instance.mStatement.executeQuery("select * from " + MySqlConsts.user_table + " where qq_openid = '" + openID + "'");
                            if (rs.first()) {
                                App.logged_user.setService_id(rs.getInt("ID"));
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        App.logged_user.setIs_login(true);
                        App.logged_user.setLast_log_time(System.currentTimeMillis());
                        App.logged_user.saveToSQLite(db, DBConsts.USER_TAB_NAME);
                    }
                }, true).execute();

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
        final String name = mEtNameLogin.getText().toString().trim();
        final String password = mEtPasswordLogin.getText().toString().trim();

        if (StringUtils.isEmpty(name) || StringUtils.isEmpty(password)) {
            mHandler.sendEmptyMessage(2);
            return;
        }
        mHandler.postDelayed(mRunnable, 5000);
        new MySqlHelper.MySQLAsyTask(new Runnable() {
            @Override
            public void run() {

                try {
                    if (color.measurement.com.from_cp20.common.util.StringUtils.isMobileNO(name)) {//手机号
                        ResultSet rs = instance.mStatement.executeQuery("SELECT * from " + MySqlConsts.user_table + " where tel = '" + name + "' and password = '" + password + "'");
                        if (rs.first()) {
                            App.logged_user = new User(rs.getInt("ID"), name, "", TEL);
                            App.logged_user.setIs_login(true);
                            App.logged_user.saveToSQLite(db, DBConsts.USER_TAB_NAME);
                            mHandler.sendEmptyMessage(1);
                            mHandler.removeCallbacks(mRunnable);
                        } else {
                            mHandler.sendEmptyMessage(0);
                        }
                    } else {//用户名
                        ResultSet rs = instance.mStatement.executeQuery("SELECT password from " + MySqlConsts.user_table + "  where name ='" + name + "' and password = '" + password + "'");
                        if (rs.first()) {
                            App.logged_user = new User(rs.getInt("ID"), name, "", NAME);
                            App.logged_user.setIs_login(true);
                            App.logged_user.saveToSQLite(db, DBConsts.USER_TAB_NAME);
                            mHandler.sendEmptyMessage(1);
                            mHandler.removeCallbacks(mRunnable);
                        } else {
                            mHandler.sendEmptyMessage(0);
                        }
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }, true).execute();
    }

    private void turnMainActivity(int requestCode) {
        new MySqlHelper.MySQLAsyTask(new Runnable() {
            @Override
            public void run() {
                try {
                    ResultSet rs = instance.mStatement.executeQuery("select * from " + MySqlConsts.user_table + " where ID = '" + App.logged_user.getService_id() + "'");
                    if (rs.next()) {
                        String tel_id = rs.getString("tel_id");
                        if (!App.tel_id.equals(tel_id)) {
                            //提示是否强制登录
                            mHandler.sendEmptyMessage(8);
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
                }
            }
        }, true).execute();

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
                case 1:
                    //注册账号登录
                    is_QQ = false;
                    turnMainActivity(20);
                    break;
                case 2:
                    Toast.makeText(LoginActivity.this, "账号或密码不能为空", Toast.LENGTH_SHORT).show();
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
                    new AlertDialog.Builder(mContext).setTitle("提示").
                            setMessage(R.string.shifou_qiangzhi).setPositiveButton(R.string.queding,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mHandler.sendEmptyMessage(9);
                                }
                            }).setNegativeButton(R.string.quxiao, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dismissProgressDialog();
                        }
                    }).setCancelable(true).show();

                    break;
                case 9:
                    new MySqlHelper.MySQLAsyTask(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                instance.mStatement.executeUpdate("update " + MySqlConsts.user_table + " set tel_id = '" + App.tel_id + "' where ID = '" + App.logged_user.getService_id() + "'");
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                    }, true).execute();

//                    new Thread(new MySqlUtils.UpLoadThread(App.tel_id, MySqlConsts.user_table, "true")).start();
//                    App.logged_user = new User(loginName, is_QQ ? "" : mEtPasswordLogin.getText().toString().trim(), loginImageUrl, true);
//                    App.logged_user.saveToSQLite(db, DBConsts.USER_TAB_NAME);
                    Intent intent2 = new Intent();
                    setResult(20, intent2);
//                    App.logged_user.setName(loginName);
//                    App.logged_user.setIs_login(true);
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
