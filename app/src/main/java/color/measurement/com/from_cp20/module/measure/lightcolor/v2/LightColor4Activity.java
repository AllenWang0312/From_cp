package color.measurement.com.from_cp20.module.measure.lightcolor.v2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import com.orhanobut.logger.Logger;
import java.util.ArrayList;
import butterknife.OnClick;
import color.measurement.com.from_cp20.R;
import color.measurement.com.from_cp20.common.adapter.ViewPagerAdapter;
import color.measurement.com.from_cp20.manager.Ble_4.BlueToothManagerForBLE;
import color.measurement.com.from_cp20.manager.Ble_4.bean.BLE_Order;
import color.measurement.com.from_cp20.manager.Ble_4.bean.LightColorDataPackager;
import color.measurement.com.from_cp20.manager.NumEndedString;
import color.measurement.com.from_cp20.manager.res.ResConsts;
import color.measurement.com.from_cp20.manager.res.ResHelper;
import color.measurement.com.from_cp20.manager.sp.SPConsts;
import color.measurement.com.from_cp20.module.been.data.LightColorData;
import color.measurement.com.from_cp20.module.been.wapper.GroupData;
import color.measurement.com.from_cp20.module.database.lightcolor.LightColorDBActivity;
import color.measurement.com.from_cp20.module.measure.MeasureActivity;
import color.measurement.com.from_cp20.module.measure.lightcolor.bean.ResultAndDValues;
import color.measurement.com.from_cp20.util.blankj.StringUtils;
import color.measurement.com.from_cp20.util.java.ArrayUtil;
import color.measurement.com.from_cp20.util.utils.T;
import es.dmoral.toasty.Toasty;

import static color.measurement.com.from_cp20.manager.Ble_4.Ble_Consts.BLACK_TEXT_FAILD;
import static color.measurement.com.from_cp20.manager.Ble_4.Ble_Consts.BLACK_TEXT_SUCCESS;
import static color.measurement.com.from_cp20.manager.Ble_4.Ble_Consts.GET_POWER;
import static color.measurement.com.from_cp20.manager.Ble_4.Ble_Consts.INIT_SUCCESS;
import static color.measurement.com.from_cp20.manager.Ble_4.Ble_Consts.RECEIVE_TEST_DATA;
import static color.measurement.com.from_cp20.manager.Ble_4.Ble_Consts.RECEIVE_WRONG_DATA;
import static color.measurement.com.from_cp20.manager.Ble_4.Ble_Consts.WHITE_TEXT_FAILD;
import static color.measurement.com.from_cp20.manager.Ble_4.Ble_Consts.WHITE_TEXT_SUCCESS;


public class LightColor4Activity extends MeasureActivity {

    LightColorData result_sim;
    TestFragment mTestFragment;
    StandTestFragment mStandTestFragment;
    private android.os.Handler mHandler=new Handler();

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String str = intent.getAction();
            Logger.i(str);
            switch (intent.getAction()) {
                case INIT_SUCCESS:
                    dismissProgressDialog();
                    mHandler.removeCallbacks(connectCallBack);
                    break;
                case RECEIVE_TEST_DATA:
//                    if (mGroupData == null) {
//                        mGroupData = new GroupData();
//                    }
                    mHandler.removeCallbacks(progressCallBack);
                    LightColorDataPackager.LightColorResultData result = (LightColorDataPackager.LightColorResultData) mManagerForBLE.v4manager.getDataPackager().getResultData();
                    ArrayList<Double> SCI = result.getSCI();
                    result_sim = new LightColorData(SCI, sp.getInt(SPConsts.TEST_MOD, 0), sp.getInt(SPConsts.LIGHT_SET, 0), sp.getInt(SPConsts.ANGLE_SET, 0));

                    mStandTestFragment.showNotice(showTimes);
                    if (standard == null) {
                        stand_temp.add(SCI);
                        mStandTestFragment.refeshNotice(stand_temp.size(), times);
                        if (stand_temp.size() == times) {
                            mGroupData.setStand(new LightColorData(ArrayUtil.getListAverage(stand_temp), sp.getInt(SPConsts.TEST_MOD, 0), sp.getInt(SPConsts.LIGHT_SET, 0), sp.getInt(SPConsts.ANGLE_SET, 0)));
                            stand_temp = new ArrayList<>();
                        }

                    } else {
                        if (standard) {
                            result_sim.setStandard(true);
                            mGroupData.setStand(result_sim);
                        } else {
                            result_sim.setStandard(false);
                            if (sp.getBoolean(SPConsts.SIMPLE_AUTO_NAME, true)) {
                                result_sim.setName(sp.getString(SPConsts.SIMPLE_TARGET_NAME, "default_name") + String.format("%03d", mGroupData.getTests().size() + 1));
                            }
                            if (mGroupData.getStand() != null && mGroupData.getStand().getStand_name() != null) {
                                result_sim.setStand_name(mGroupData.getStand().getStand_name());
                            }
                            ResultAndDValues resultAndDValues = ResHelper.getWC(sp,
                                    ResHelper.getChecked_titles(context, sp, R.array.comparison_data, ResConsts.title_all),
                                    ResHelper.getSCGStitles(context),
                                    ResHelper.getSettings(ResHelper.getChecked_titles(context, sp, R.array.comparison_data, ResConsts.title_all), sp),
                                    (LightColorData) mGroupData.getStand(), result_sim
                            );
                            Boolean bol = ResHelper.getResultWithCheckItems(resultAndDValues);
//                            Boolean b = comple(getCompare_titles(LightColorActivity.this), gd.getStand().getResultData().toHashMap(), result_sim.getResultData().toHashMap(), getRCSet());
                            result_sim.setResult(bol);
                            mGroupData.addTest(result_sim);
                        }
                    }

                    break;
                case BLACK_TEXT_FAILD:
                    Toasty.error(mContext, "黑校准失败").show();
                    break;
                case BLACK_TEXT_SUCCESS:
                    Toasty.success(mContext, "黑校准成功").show();
                    break;
                case WHITE_TEXT_FAILD:
                    Toasty.error(mContext, "白校准失败").show();
                    break;
                case WHITE_TEXT_SUCCESS:
                    Toasty.success(mContext, "白校准成功").show();
                    break;
                case RECEIVE_WRONG_DATA:
                    Toasty.error(mContext, "返回数据格式不正确").show();
                    break;
                case GET_POWER:
//              refeshInfo(InstrumentConfig.model_number,InstrumentConfig.power, InstrumentConfig.instrument_id);
//                            BlueToothManagerForBLE.power + "0%",new String(BlueToothManagerForBLE.instrumentId)
                    break;
                default:
                    break;
            }
            dismissProgressDialog();
        }
    };


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent i = getIntent();
        need_response = i.getBooleanExtra("need_response", false);
        bleAddress = i.getStringExtra("bleAddress");
        tableName = i.getStringExtra("tableName");
        standName = i.getStringExtra("standName");
        pageIndex = i.getIntExtra("page_index", -1);

        mManagerForBLE = BlueToothManagerForBLE.getInstance(this);
        if (need_response == true) {
            showProgressDialog("请稍后", "正在连接仪器");
            mHandler.postDelayed(connectCallBack, 15000);
        } else {
            if (StringUtils.isEmpty(standName)) {
                mViewPager.setCurrentItem(0, false);
                mGroupData.removeData();
            } else {
                Cursor c = db.rawQuery("select * from " + tableName + " where stand_name = ?", new String[]{standName});
                if (c.moveToFirst()) {
                    mGroupData.setStand(new LightColorData(c));
                }
                c.close();
                mViewPager.setCurrentItem(1, false);
            }
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = mContext.getSharedPreferences(SPConsts.PREFERENCE_LIGHT_COLOR, MODE_PRIVATE);

        registerReceiver(mBroadcastReceiver, getintentflater());
//        refeshFeatureNames();
//        mManagerForBLE.regeisterReceiver(this);

        initPages();
        mGroupData = new GroupData();
        mGroupData.addObserver(mTestFragment);
        mGroupData.addObserver(mStandTestFragment);

        mToolbar.setTitle("分光测色");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public void initPages() {
        mTestFragment = new TestFragment(sp);
        mStandTestFragment = new StandTestFragment(sp);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(mStandTestFragment, "stand_test_fragment");
        adapter.addFragment(mTestFragment, "test_fragment");
        mViewPager.setAdapter(adapter);
        mViewPager.setOffscreenPageLimit(0);
        mViewPager.setPagingEnabled(false);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    setTitle("分光测色(简单模式)");
                    mMenu.findItem(R.id.simple_test).setChecked(true);
                    mGroupData.removeData();
                } else {
                    setTitle("分光测色(色差模式)");
                    mMenu.findItem(R.id.sc_test).setChecked(true);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

    }

//    private void refeshFeatureNames() {
////        name_feature = DataNamedHelper.getNames(DBHelper.getStands(db, tableName));
//    }

    private IntentFilter getintentflater() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(INIT_SUCCESS);
        intentFilter.addAction(RECEIVE_WRONG_DATA);
        intentFilter.addAction(RECEIVE_TEST_DATA);
        intentFilter.addAction(BLACK_TEXT_SUCCESS);
        intentFilter.addAction(BLACK_TEXT_FAILD);
        intentFilter.addAction(WHITE_TEXT_SUCCESS);
        intentFilter.addAction(WHITE_TEXT_FAILD);

        return intentFilter;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBroadcastReceiver);
        mManagerForBLE.v4manager.disconnect();
    }
//    @OnClick(R.id.ll2_right)
//    void right() {
//        if (mGroupData == null || mGroupData.getStand() == null) {
//            T.showError(mContext, "请先测量试样");
//        } else {
//            final NameDialog log = new NameDialog();
//            log.setPositiveButton(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    mGroupData.getStand().setName(log.getEt().getText().toString());
//                    mViewPager.setCurrentItem(1, true);
//                }
//            });
//            log.show(getFragmentManager(), "namedialog");
//        }
//
//    }

    String name = null, tips = null;

    @OnClick(R.id.ll3_save)
    void save() {
        if (mViewPager.getCurrentItem() == 0) {
            if (mGroupData.getStand() != null) {
                if (!mGroupData.getStand().isHasSaved()) {
                    boolean isAutoName = sp.getBoolean(SPConsts.STAND_AUTO_NAME, true);
                    final NameDialog dialog;
                    if (isAutoName) {
                        name = sp.getString(SPConsts.STAND_TARGET_NAME, "TargetName000");
                        tips = sp.getString(SPConsts.STAND_TARGET_TIPS, "TargetTips");
                        dialog = new NameDialog(NumEndedString.create(name), tips);
                    } else {
                        dialog = new NameDialog(NumEndedString.create(mGroupData.getStand().getStand_name()), mGroupData.getStand().getTips());
                    }

                    dialog.setListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            name = dialog.et_name.getText().toString();
                            tips = dialog.et_tips.getText().toString();
                            if (StringUtils.isEmpty(name)) {
                                T.showWarning(mContext, "请正确填写");
                            } else {
                                if (db.rawQuery("select * from " + getTableName() + " where stand_name = ?", new String[]{name}).getCount() > 0) {
                                    T.showWarning(mContext, "名称重复");
                                } else {
                                    rememberNameAndTips(name, tips);
                                    insertStand(name, tips);
                                    dialog.dismiss();
                                }
                            }
                        }
                    }, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            name = dialog.et_name.getText().toString();
                            if (!StringUtils.isEmpty(name)) {
                                mViewPager.setCurrentItem(1, true);
                            }
                        }
                    });
                    dialog.show(getFragmentManager(), "name_dialog");
                } else {
                    T.showWarning(mContext, "该数据已存在,请勿重复保存");
                }

            } else {
                T.showWarning(mContext, "请先测量数据");
            }


        } else {
            if (mGroupData.getTests() != null || mGroupData.getTests().size() > 0) {
                mGroupData.saveSimple(db, getTableName(), mGroupData.getSelectIndex());
                mTestFragment.getAdapter().notifyItemChanged(mGroupData.getSelectIndex());
            } else {
                T.showWarning(mContext, "请先测量标样");
            }

//            mGroupData = new GroupData();
//            mViewPager.setCurrentItem(0);
//            T.showSuccess(mContext, "保存成功");
        }
    }

    ArrayList<ArrayList<Double>> stand_temp = new ArrayList<>();//平均数据

    boolean showTimes;
    int times;//平均次数  现在第几次

    @OnClick(R.id.ll4_check)
    void Test() {
        showProgressDialog("请稍后", "正在测试...");
        if (mViewPager.getCurrentItem() == 0) {
            times = sp.getInt(SPConsts.AVERAGE_TIMES, 0) + 1;
            if (times > 1) {
                showTimes = true;
                if (stand_temp.size() == 0) {
                    mGroupData.removeData();
                }
                standard = null;
            } else {
                showTimes = false;
                standard = true;
            }

        } else {
            standard = false;
        }
        mManagerForBLE.v4manager.writeToCharacteristic(BLE_Order.LC.TEST_DATA);
        mHandler.postDelayed(progressCallBack
                , 5000);
    }

    Menu mMenu;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.light_color, menu);
        mMenu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.simple_test:
                mViewPager.setCurrentItem(0, true);
                break;
            case R.id.sc_test:
                if (mGroupData.getStand() != null) {
                    if (mGroupData.getStand().isHasSaved()) {
                        mViewPager.setCurrentItem(1, true);
                    } else {
                        save();
                    }
                } else {
                    T.showWarning(mContext, "请先测量标样数据");
                }

                break;
            case R.id.data_light_color:
                LightColorDBActivity.startWithIntent(this, getTableName());
                break;
            case R.id.setting_light_color:
                new SettingDialog(mViewPager.getCurrentItem(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        initSettings();
                        mGroupData.hasChange();
                    }
                }).show(getFragmentManager(), "setting");
                break;
//            case R.id.rc_setting_light_color:
//              SetRCDialog dialog=  new SetRCDialog(mContext, ResHelper.getChecked_titles(mContext));
//                dialog.setOnDismissCallBack(new DismissCallback() {
//                    @Override
//                    public void dismiss() {
//                        mTestFragment.refeshViews();
//                    }
//                });
//               dialog.show(getFragmentManager(), "setRC");
//                break;
//            case R.id.info_light_color:
//                new InsInfoDialog().show(getFragmentManager(), "info");
//                break;
        }
        return true;
    }

    private void initSettings() {
        sp = mContext.getSharedPreferences(SPConsts.PREFERENCE_LIGHT_COLOR, MODE_PRIVATE);
    }

    @Override
    public void onBackPressed() {
        if (mViewPager.getCurrentItem() == 1) {
            mViewPager.setCurrentItem(0, true);
        } else {
            super.onBackPressed();
        }
    }
}
