package color.measurement.com.from_cp20.module.main;

import android.Manifest;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.orhanobut.logger.Logger;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import color.measurement.com.from_cp20.R;
import color.measurement.com.from_cp20.base.ProgressDialogActivity;
import color.measurement.com.from_cp20.common.imageloader.ImageLoaderProxy;
import color.measurement.com.from_cp20.common.util.ParcelUtil;
import color.measurement.com.from_cp20.manager.Ble_4.BleHelper;
import color.measurement.com.from_cp20.manager.Ble_4.BlueToothManagerForBLE;
import color.measurement.com.from_cp20.manager.db.DBConsts;
import color.measurement.com.from_cp20.manager.db.DBHelper;
import color.measurement.com.from_cp20.manager.db.MySqlConsts;
import color.measurement.com.from_cp20.manager.db.MySqlHelper;
import color.measurement.com.from_cp20.manager.ins.Instrument;
import color.measurement.com.from_cp20.manager.sp.SPConsts;
import color.measurement.com.from_cp20.module.App;
import color.measurement.com.from_cp20.module.been.AdvInfo;
import color.measurement.com.from_cp20.module.been.Ins;
import color.measurement.com.from_cp20.module.been.User;
import color.measurement.com.from_cp20.module.database.lightcolor.LightColorDBActivity;
import color.measurement.com.from_cp20.module.measure.lightcolor.v2.LightColor2Activity;
import color.measurement.com.from_cp20.module.measure.lightcolor.v2.LightColor4Activity;
import color.measurement.com.from_cp20.module.measure.lustre.LustreActivity;
import color.measurement.com.from_cp20.module.other.LoginActivity;
import color.measurement.com.from_cp20.module.other.UserInfoActivity;
import color.measurement.com.from_cp20.module.other.WebViewActivity;
import color.measurement.com.from_cp20.util.blankj.EncodeUtils;
import color.measurement.com.from_cp20.util.blankj.FileUtils;
import color.measurement.com.from_cp20.util.utils.L;
import color.measurement.com.from_cp20.util.utils.T;
import color.measurement.com.from_cp20.util.utils.TimeUtils;
import color.measurement.com.from_cp20.widget.CircleImageView;
import color.measurement.com.openfilehelper.FileAndPath;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static color.measurement.com.from_cp20.manager.Ble_4.BleHelper.removeBond;
import static color.measurement.com.from_cp20.manager.Ble_4.Ble_Consts.ACTION_DATA_AVAILABLE;
import static color.measurement.com.from_cp20.manager.Ble_4.Ble_Consts.ACTION_GATT_CONNECTED;
import static color.measurement.com.from_cp20.manager.Ble_4.Ble_Consts.ACTION_GATT_DISCONNECTED;
import static color.measurement.com.from_cp20.manager.Ble_4.Ble_Consts.ACTION_GATT_SERVICES_DISCOVERED;
import static color.measurement.com.from_cp20.manager.Ble_4.Ble_Consts.INIT_SUCCESS;
import static color.measurement.com.from_cp20.manager.ins.Instrument.TYPE_LIGHTCOLOR;
import static color.measurement.com.from_cp20.manager.ins.Instrument.getTypeWithInstrumentName;

public class MainActivity extends ProgressDialogActivity
        implements
//        NavigationView.OnNavigationItemSelectedListener,
        View.OnClickListener {

    private static final int REQUEST_LOCATION_PERMISSION = 3;
    final String Tag = "MainActivity";
    Context mContext;
    //    @BindView(R.id.drawer_layout) DrawerLayout mDrawerLayout;
//    @BindView(R.id.nav_view) NavigationView navigationView;//属于view  不属于viewgroup 内部视图不会被butterknife注解

    @BindView(R.id.recyc_main) RecyclerView mRecycMain;
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.abl_main) AppBarLayout mAppBarLayout;
    @BindView(R.id.banner_main) Banner mBanner;
    SearchView mSearchView;
    //    View NevHeaderView;
//   ImageView portrait;
//    TextView name, sign;
    @BindView(R.id.circ_iv_portrait) CircleImageView portrait;
    @BindView(R.id.iv_add) ImageView add_devices;
    @BindView(R.id.user_name_main) TextView name;
    //    View recycHeadView;
//   @BindView(R.id.banner) Banner mBanner;
    BlueToothManagerForBLE mManagerForBLE;
    MainMiniRecycAdapter adapter;
    //    @BindView(R.id.srl_main) SwipeRefreshLayout mSrlMain;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                adapter = new MainMiniRecycAdapter(R.layout.item_mini_instrument_recyc_main, mProductDevicess);
                adapter.setEmptyView(LayoutInflater.from(mContext).inflate(R.layout.empty_view, null));
                adapter.setOnRecyclerViewItemClickListener(onItemClicklistener);
//        adapter.setOnRecyclerViewItemLongClickListener(ItemLongClickListener);
                mRecycMain.setAdapter(adapter);
            } else if (msg.what == 2) {
                mBanner.setImages(imgUrls);
                mBanner.setOnBannerListener(new OnBannerListener() {
                    @Override
                    public void OnBannerClick(int position) {
                        Intent i = new Intent(MainActivity.this, WebViewActivity.class);
                        i.putExtra("url", webUrls.get(position));
                        startActivity(i);
                    }
                });
                mBanner.start();
                if (sp.getBoolean(SPConsts.LOGIN_GUIDE, true)) {//默认true
                    new MaterialShowcaseView.Builder(MainActivity.this)
                            .setTarget(portrait)
                            .setDismissTextColor(Color.GREEN)
                            .setDismissText("我知道了")
                            .setContentText("点击头像登录账户")
                            .setDelay(1000) // optional but starting animations immediately in onCreate can make them choppy
                            .singleUse("login_guide") // provide a unique ID used to ensure it is only shown once
                            .show();
                    sp.edit().putBoolean(SPConsts.LOGIN_GUIDE, false).commit();
                } else {
                    mAppBarLayout.setExpanded(false);
                }
            }
        }
    };

    ArrayList<Ins> mProductDevicess = new ArrayList<>();
    DBHelper dbHelper;
    SQLiteDatabase db;
    SharedPreferences sp;
    ArrayList<URL> imgUrls;
    ArrayList<String> titles;
    ArrayList<String> webUrls;
    MySqlHelper instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkPermissions();
        mContext = this;
        instance = MySqlHelper.getInstance(mContext);

        dbHelper = new DBHelper(mContext, DBConsts.DATE_BASE, null, 1);
        L.e("dbHelper==" + dbHelper);
        db = dbHelper.getWritableDatabase();
        sp = getSharedPreferences(SPConsts.PREFERENCE_APP_CONFIG, MODE_PRIVATE);

        setContentView(R.layout.activity_main);
        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
        ButterKnife.bind(this);
//        mSrlMain.setOnRefreshListener(new android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                mSrlMain.setRefreshing(true);
//                initRecycleView();
//                mSrlMain.setRefreshing(false);
//            }
//        });
        //App.logged_user = new User(name, pswd, StringUtils.isEmpty(portrait) ? null : portrait);
        /*if(App.has_login){
            Cursor cursor
        }*/
//        App.logged_user.saveToSQLite(db, DBConsts.USER_TAB_NAME);
        mContext = this;
        initUser();
        initViews();

    }

    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    1);
        }
        if (Build.VERSION.SDK_INT >= 23) {
            if (!isLocationOpen(getApplicationContext())) {
                Intent enableLocate = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(enableLocate, REQUEST_LOCATION_PERMISSION);
                return;
            }
        }

    }

    private void initUser() {
        SharedPreferences sp = getSharedPreferences(SPConsts.PREFERENCE_APP_CONFIG, MODE_PRIVATE);
        int id = sp.getInt("last_userId", -1);

        if (id == -1) {
            App.logged_user = new User();
        } else {
            Cursor cursor = db.query(DBConsts.USER_TAB_NAME, null, " service_id = ? ", new String[]{id + ""}, null, null, null);
            if (cursor.moveToFirst()) {
                App.logged_user = new User(cursor);
                App.logged_user.setIs_login(true);
                name.setText(App.logged_user.getName());
                ImageLoaderProxy.getInstance().displayImage(App.logged_user.getPortrait_url(), portrait, R.mipmap.caipu);
            }
        }

    }

    void initViews() {
        setSupportActionBar(mToolbar);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        mDrawerLayout.setDrawerListener(toggle);
//        toggle.syncState();
//        navigationView.setNavigationItemSelectedListener(this);

        if (App.logged_user.is_login()) {
            ImageLoaderProxy.getInstance().displayImage(App.logged_user.getPortrait_url(), portrait, R.mipmap.caipu);
            name.setText(App.logged_user.getName());
        }
        portrait.setOnClickListener(this);
        add_devices.setOnClickListener(this);

        Bitmap bitmap = new BitmapFactory().decodeResource(getResources(), R.mipmap.ic_launcher);
        RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(getResources(), bitmap);
        drawable.setCircular(true);
//        portrait.setImageDrawable(drawable);

        mBanner.setImageLoader(new ImageLoader() {
            @Override
            public void displayImage(Context context, Object path, ImageView imageView) {
//                ImageLoaderProxy.getInstance().displayImage(path.toString(),imageView,R.mipmap.adv_defult);
                Glide.with(MainActivity.this)
                        .load(path)
//                        .placeholder(R.mipmap.adv_defult)
//                        .crossFade()
//                        .override(600, 250)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(imageView);
            }
        });
        mBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        mBanner.setBannerAnimation(Transformer.DepthPage);
        mBanner.isAutoPlay(true);
        mBanner.setDelayTime(4000);
        initBannerDataFromMySql();
//        initBannerData();
        mRecycMain.setLayoutManager(new GridLayoutManager(this, 3));
        initRecycleView();
//        mRecycMain.setLayoutManager(new LinearLayoutManager(this));

    }


    private void initRecycleView() {
//        mProductDevicess = DBHelper.initProductDataFromDataBase(db, App.logged_user.getService_id());
        mProductDevicess = new ArrayList<>();
        new MySqlHelper.MySQLAsyTask(new Runnable() {
            @Override
            public void run() {
                try {
                    ResultSet set = instance.mStatement.executeQuery("select * from " + MySqlConsts.ins_table + " where userId = '" + App.logged_user.getService_id() + "'");
                    if (set.first()) {
                        do {
                            mProductDevicess.add(new Ins(set));
                        } while (set.next());
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                mHandler.sendEmptyMessage(0);
            }
        }, false).execute();
    }

    private void initBannerDataFromMySql() {
        final ArrayList<AdvInfo> advs = new ArrayList<>();
        imgUrls = new ArrayList<>();
        titles = new ArrayList<>();
        webUrls = new ArrayList<>();
        final MySqlHelper instance = MySqlHelper.getInstance(mContext);
        new MySqlHelper.MySQLAsyTask(new Runnable() {
            @Override
            public void run() {
                try {
                    ResultSet set = instance.mStatement.executeQuery("select * from " + MySqlConsts.hro_adv + " order by id desc limit 4");
                    if (set.first()) {
                        do {
                            advs.add(new AdvInfo(set));
                        } while (set.next());
                    }
                    for (AdvInfo info : advs) {
                        imgUrls.add(new URL(info.getImg_url()));
                        webUrls.add(info.getClick_url());
                    }
                    mHandler.sendEmptyMessage(2);
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        }, true).execute();
    }

    long last_back_click_time = 0;

    @Override
    public void onBackPressed() {
        long now = System.currentTimeMillis();
        if (now - last_back_click_time < 1000) {
            finish();
        } else {
            last_back_click_time = now;
            T.show(mContext, "双击退出");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.circ_iv_portrait:
                if (App.logged_user.is_login()) {
                    Intent intentUser = new Intent(this, UserInfoActivity.class);
                    startActivityForResult(intentUser, 10086);
                } else {
                    startActivityForResult(new Intent(this, LoginActivity.class), 10086);
                }
                break;
            case R.id.iv_add:
                if (mManagerForBLE.mBluetoothAdapter.isEnabled()) {
                    showAddDialog();
                } else {
                    mManagerForBLE.openBlueToothIfNeed();
                }
                break;
        }
    }

    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            Logger.i("onReceive", action);
            if (ACTION_GATT_CONNECTED.equals(action)) {
            } else if (ACTION_GATT_DISCONNECTED.equals(action)) {
            } else if (ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {

                mManagerForBLE.v4manager.isConnected = true;
                mContext.sendBroadcast(new Intent(INIT_SUCCESS));
            } else if (ACTION_DATA_AVAILABLE.equals(action)) {
            } else if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,
                        BluetoothAdapter.ERROR);
                switch (state) {
                    case BluetoothAdapter.STATE_OFF:
                        Log.d("aaa", "STATE_OFF 手机蓝牙关闭");
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        Log.d("aaa", "STATE_TURNING_OFF 手机蓝牙正在关闭");
                        break;
                    case BluetoothAdapter.STATE_ON:
                        Log.d("aaa", "STATE_ON 手机蓝牙开启");
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        Log.d("aaa", "STATE_TURNING_ON 手机蓝牙正在开启");
                        break;
                }
            }
        }
    };

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_GATT_CONNECTED);
        intentFilter.addAction(ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(ACTION_DATA_AVAILABLE);

        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);

        return intentFilter;
    }

    @Override
    protected void onResume() {
        mManagerForBLE = BlueToothManagerForBLE.getInstance(MainActivity.this);
//        mManagerForBLE.checkPermission(MainActivity.this);
//        mManagerForBLE.checkPermission(this);
//        mManagerForBLE.openBlueToothIfNeed();
        mBanner.startAutoPlay();
        if (App.logged_user.is_login()) {
            mManagerForBLE.openBlueToothIfNeed();

            if (sp.getBoolean(SPConsts.ADD_DEV_GUIDE, true)) {
                new MaterialShowcaseView.Builder(this)
                        .setTarget(add_devices)
                        .setDismissTextColor(Color.GREEN)
                        .setDismissText("我知道了")
                        .setContentText("点击这里添加一个设备")
                        .setDelay(1000) // optional but starting animations immediately in onCreate can make them choppy
                        .singleUse("add_device_gride") // provide a unique ID used to ensure it is only shown once
                        .show();
                sp.edit().putBoolean(SPConsts.ADD_DEV_GUIDE, false);
            }
        }
        super.onResume();
    }

    MenuItem tiem;

    @Override
    protected void onPause() {
        mBanner.stopAutoPlay();
        super.onPause();
    }

    private void saveProductDataToFile(BluetoothDevice products, String dir, String name) {
        FileUtils.createOrExistsDir(dir);
        File file = new File(dir, name);
        if (!file.exists()) {
            byte[] bytes = ParcelUtil.marshall(products);
            FileUtils.createFileByDeleteOldFile(file);
            FileUtils.saveByteToFile(file, bytes);
            Log.i("saveProductDataToFile:", "success");
        } else {
            Log.i("saveProductDataToFile:", "file exist");
        }
    }

    private void saveProductDataToFile(BluetoothDevice products, String abs) {
        int index = abs.lastIndexOf("/");
        String dir = abs.substring(0, index);
        String name = abs.substring(index + 1, abs.length());
        saveProductDataToFile(products, dir, name);
    }

    AddInstrumentDialog mAddInstrumentDialog;

    public void showAddDialog() {

        mManagerForBLE.v4manager.scanDevices(false);
//        mManagerForBLE.v2manager.st(false);
        if (mAddInstrumentDialog == null) {
            mAddInstrumentDialog = new AddInstrumentDialog(this, mManagerForBLE,
//                new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                //将device 写入文件  刷新recyclerview
////                new AsyThread(position).execute();
//                Ins ins = mAddInstrumentDialog.getV4Device(position);
//
//                if (db.rawQuery("select * from " + DBConsts.INS_TAB_NAME + " where userName = ? and address = ?", new String[]{App.logged_user.getName(), ins.getAddress()}).getCount() > 0) {
//
//                } else {
//                    String encoding_string = EncodeUtils.urlEncode(
//                            TimeUtils.getTimeWithFormat(TimeUtils.NO_BLANK)
//                    );
//                    int i = getTypeWithInstrumentName(ins.getBleName(), mContext);
//                    String tab_name = App.logged_user.getService_id() + Instrument.getTypeStringWithType(i) + encoding_string;
//                    ins.setDataTableName(tab_name);
//                    db.insert(DBConsts.INS_TAB_NAME, null, ins.getContentValue());
//
//                    switch (ins.getType()) {
//                        case 0:
//                            db.execSQL(DBConsts.createLightColorDataTableIfNotExist(tab_name));
//                            break;
//                        case 1:
//                            break;
//                        case 2:
//                            db.execSQL(DBConsts.createLustreDataTableIfNotExist(tab_name));
//                            break;
//                        case 3:
//                            break;
//                    }
//                }
//                String path = dbHelper.getDeviceFilePath(db, App.logged_user.getName(), ins.getAddress());
//                File f = new File(path);
//                if (!f.exists()) {
//                    saveProductDataToFile(ins.getDevice(), ins.getCachePath());
//                } else {
//                }
//                initRecycleView();
//            }
//        },
                    new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            if (App.logged_user.is_login()) {
                                final Ins ins = mAddInstrumentDialog.getV2Device(position);
                                BluetoothDevice device = ins.getDevice(mManagerForBLE.mBluetoothAdapter);
                                BleHelper.boundDeviceIfNeed(device);
                                if (ins.getType() <= 3) {
                                    if (InsideProducts(mProductDevicess, ins)) {
                                        T.showWarning(mContext, "仪器已添加");
                                    } else {
                                        String encoding_string = EncodeUtils.urlEncode(
                                                TimeUtils.getTimeWithFormat(TimeUtils.NO_BLANK)
                                        );
                                        int i = getTypeWithInstrumentName(ins.getBleName(), mContext);
                                        String tab_name = "user_id" + App.logged_user.getService_id() + Instrument.getTypeStringWithType(i) + encoding_string;
                                        ins.setDataTableName(tab_name);
                                        ins.setUserId(App.logged_user.getService_id());
                                        switch (i) {
                                            case 0:
                                                db.execSQL(DBConsts.createLightColorDataTableIfNotExist(tab_name));
                                                break;
                                            case 1:
                                                break;
                                            case 2:
                                                db.execSQL(DBConsts.createLustreDataTableIfNotExist(tab_name));
                                                break;
                                            case 3:
                                                break;
                                        }
                                    }
                                    new MySqlHelper.MySQLAsyTask(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                ResultSet set = instance.mStatement.executeQuery("select * from " + MySqlConsts.ins_table + " where userId = '" + App.logged_user.getService_id() + "' and address = '" + ins.getAddress() + "'");
                                                if (set.first()) {
                                                    Log.i("仪器" + ins.getAddress(), "数据库中已存在");
                                                } else {
                                                    MySqlHelper.insertDataToMySql(instance.mStatement, MySqlConsts.ins_table, ins, mContext);
                                                }
                                            } catch (SQLException e) {
                                                e.printStackTrace();
                                            }

                                        }
                                    }, false).execute();
//                                File f = new File(ins.getCachePath());
//                                if (!f.exists()) {
//                                    saveProductDataToFile(ins.getDevice(mManagerForBLE.mBluetoothAdapter), ins.getCachePath());
//                                } else {
//                                }
                                    initRecycleView();
                                } else {
                                    T.showWarning(mContext, "无法识别该设备");
                                }
                            } else {
                                T.show(mContext, "请先登录一个账号");
                            }
                        }
                    });
        }
        mAddInstrumentDialog.show(getFragmentManager(), "add_instrument_dialog");
    }

    private boolean InsideProducts(ArrayList<Ins> productDevicess, Ins ins) {
        for (Ins i : productDevicess) {
            if (i.getAddress().equals(ins.getAddress())) {
                return true;
            }
        }
        return false;
    }

    boolean scanable = false;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 10086) {
            if (resultCode == 20 || resultCode == 21 || resultCode == 22) {

            } else if (resultCode == 19) {
                App.logged_user=new User();
            }
            initRecycleView();
            ImageLoaderProxy.getInstance().displayImage(App.logged_user.getPortrait_url(), portrait, R.mipmap.caipu);
            name.setText(App.logged_user.getName());
        }
        if (requestCode == 1 && resultCode == 1) {
            scanable = true;
        }
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (!isLocationOpen(getApplicationContext())) {
                Toast.makeText(MainActivity.this, "安卓6.0系统要求：如果要使用蓝牙设备，必须打开位置！", Toast.LENGTH_SHORT).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    BaseQuickAdapter.OnRecyclerViewItemClickListener onItemClicklistener = new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
        @Override
        public void onItemClick(View view, int i) {
            checkPermissions();
            Ins info = mProductDevicess.get(i);
            if (info.getDevice_version() == 4) {
                BluetoothDevice d = info.getDevice(mManagerForBLE.mBluetoothAdapter);
                mManagerForBLE.v4manager.connectToDevice(d);
                switch (getTypeWithInstrumentName(info.getBleName(), mContext)) {
                    case TYPE_LIGHTCOLOR:
                        App.logged_user.setConnectIns(info);
                        Intent intent = new Intent(MainActivity.this, LightColor4Activity.class);
                        intent.putExtra("need_response", true);
                        intent.putExtra("bleName", info.getBleName());
                        intent.putExtra("bleAddress", info.getAddress());
                        startActivity(intent);
                        break;
                    default:
                        break;
                }
            } else if (info.getDevice_version() == 2) {//00001101-0000-1000-8000-00805F9B34FB
                new ConnectTask().execute(i);
            }
        }
    };
    BaseQuickAdapter.OnRecyclerViewItemLongClickListener ItemLongClickListener = new BaseQuickAdapter.OnRecyclerViewItemLongClickListener() {
        @Override
        public boolean onItemLongClick(View view, int i) {
            Ins ins = mProductDevicess.get(i);
            BluetoothDevice device = ins.getDevice(mManagerForBLE.mBluetoothAdapter);
            try {
                removeBond(device.getClass(), device);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }
    };
    BaseQuickAdapter.OnRecyclerViewItemLongClickListener mOnItemLongClickListener = new BaseQuickAdapter.OnRecyclerViewItemLongClickListener() {
        @Override
        public boolean onItemLongClick(View view, final int i) {
            new AlertDialog.Builder(mContext).setTitle("操作选项").setItems(new String[]{"查看数据", "删除"}, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case 0:
                            LightColorDBActivity.startWithIntent(mContext, mProductDevicess.get(i).getDataTableName());
                            break;
                        case 1:
                            new AlertDialog.Builder(mContext).setTitle("确认删除").setMessage("点击确认,删除仪器同时删除本地数据")
                                    .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String table_name = mProductDevicess.get(i).getDataTableName();
//                                            String address = mProductDevicess.get(i).getAddress();
//                                            Cursor c = db.rawQuery("select * from " + DBConsts.INS_TAB_NAME + " where userName = ? and address = ?", new String[]{App.logged_user.getName(), address});
//                                            if (c.getCount() == 1) {
//                                                c.moveToFirst();
//                                                String table_name = c.getString(c.getColumnIndex("dataTableName"));
                                            db.execSQL("drop table " + table_name);
//                                db.execSQL("delete from " + table_name);
                                            db.delete(DBConsts.INS_TAB_NAME, "dataTableName = ?", new String[]{table_name});
//                                            }
//                                          File f = new File(mProductDevicess.get(position).getCachePath());
//                                          if (f.exists()) {
//                                              f.delete();
//                                              T.showSuccess(mContext, "删除成功");
//                                          } else {
//                                              T.showWarning(mContext, "文件不存在");
//                                          }
                                            initRecycleView();
                                        }
                                    }).setNegativeButton("取消", null).create().show();
                            break;

                    }
                }
            }).create().show();
            return true;
        }
    };

    @Override
    protected void onDestroy() {
        db.close();
        unregisterReceiver(mGattUpdateReceiver);
        if (App.logged_user.is_login()) {
            SharedPreferences sp = this.getSharedPreferences(SPConsts.PREFERENCE_APP_CONFIG, MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putInt("last_userId", App.logged_user.getService_id());
            editor.commit();
        }
        super.onDestroy();
    }

    public String getInsDir() {
        return FileAndPath.APP_INS_CACHE_PATH + "/" + App.logged_user.getName();
    }

    String getFileName(BluetoothDevice ins, int version) {
        return ins.getName() + "_" + ins.getAddress() + "_" + version;
    }

    private void showShare() {
        ShareSDK.initSDK(this);
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间等使用
        oks.setTitle("标题");
        // titleUrl是标题的网络链接，QQ和QQ空间等使用
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我是分享文本");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");

// 启动分享GUI
        oks.show(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.i("location permission", "success");
                }
                return;
            }
            case 2:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.i("location permission", "success");
                }
                break;
        }
    }

    public static boolean isLocationOpen(final Context context) {
        LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        //gps定位
        boolean isGpsProvider = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        //网络定位
        boolean isNetWorkProvider = manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        return isGpsProvider || isNetWorkProvider;
    }

    class ConnectTask extends AsyncTask<Integer, Integer, Integer> {
        Ins info;
        boolean b;

        @Override
        protected void onPreExecute() {
            showProgressDialog("请稍后", "正在连接");
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(Integer... params) {
            mManagerForBLE.openBlueToothIfNeed();
            info = mProductDevicess.get(params[0]);
//            BluetoothDevice device = info.getDevice(mManagerForBLE.mBluetoothAdapter);
//            boundDeviceIfNeed(device);
            db.execSQL(DBConsts.createLightColorDataTableIfNotExist(info.getDataTableName()));
            mManagerForBLE.v2manager.disconnect();
            b = mManagerForBLE.v2manager.connectTo_2_Device(info.getAddress());
            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            dismissProgressDialog();
            if (b) {
                Intent i = null;
                if (info.getType() == 0) {
                    i = new Intent(MainActivity.this, LightColor2Activity.class);
                } else if (info.getType() == 2) {
                    i = new Intent(MainActivity.this, LustreActivity.class);
                }
                i.putExtra("tableName", info.getDataTableName());
                i.putExtra("bleName", info.getBleName());
                i.putExtra("bleAddress", info.getAddress());

                startActivity(i);
            } else {
                mManagerForBLE.v2manager.disconnect();
                T.showWarning(mContext, "连接失败,请稍后重试");
            }
            super.onPostExecute(integer);
        }
    }
}
