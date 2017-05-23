package color.measurement.com.from_cp20.module.measure.lustre;

import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;

import butterknife.OnClick;
import color.measurement.com.from_cp20.R;
import color.measurement.com.from_cp20.common.adapter.ViewPagerAdapter;
import color.measurement.com.from_cp20.common.interfaze.DismissCallback;
import color.measurement.com.from_cp20.manager.Ble_4.BlueToothManagerForBLE;
import color.measurement.com.from_cp20.manager.Ble_4.bean.BLE_Order;
import color.measurement.com.from_cp20.manager.NumEndedString;
import color.measurement.com.from_cp20.manager.res.ResConsts;
import color.measurement.com.from_cp20.manager.res.ResHelper;
import color.measurement.com.from_cp20.manager.sp.SPConsts;
import color.measurement.com.from_cp20.module.been.data.LustreData;
import color.measurement.com.from_cp20.module.been.wapper.GroupData;
import color.measurement.com.from_cp20.module.database.lightcolor.LightColorDBActivity;
import color.measurement.com.from_cp20.module.measure.MeasureActivity;
import color.measurement.com.from_cp20.module.measure.lightcolor.bean.ResultAndDValues;
import color.measurement.com.from_cp20.module.measure.lightcolor.v2.NameDialog;
import color.measurement.com.from_cp20.util.blankj.StringUtils;
import color.measurement.com.from_cp20.util.utils.T;

/**
 * Created by wpc on 2017/5/7.
 */

public class LustreActivity extends MeasureActivity {

    LustreData result_sim;
    LustreGroupTestFrag mGroupFrag;
    LustreStandTestFrag mStandFrag;
    Handler mHandler = new Handler();

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    protected void onResume() {
        Intent i = getIntent();
        bleAddress = i.getStringExtra("bleAddress");
        if (tableName == null) {
            tableName = i.getStringExtra("tableName");
        }
        standName = i.getStringExtra("standName");
        pageIndex = i.getIntExtra("page_index", -1);
        if (StringUtils.isEmpty(standName)) {
            mViewPager.setCurrentItem(0, false);
        } else {
            Cursor c = db.rawQuery("select * from " + tableName + " where stand_name = ?", new String[]{standName});
            if (c.moveToFirst()) {
                mGroupData.setStand(new LustreData(c));
            }
            c.close();
            mViewPager.setCurrentItem(1, false);
        }
        super.onResume();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = mContext.getSharedPreferences(SPConsts.PREFERENCE_LUSTRE, MODE_PRIVATE);
//        showProgressDialog("请稍后", "正在连接仪器");
//        mHandler.postDelayed(connectCallBack, 10000);
        mManagerForBLE = BlueToothManagerForBLE.getInstance(this);
//        mManagerForBLE.regeisterReceiver(this);
        initPages();
        mGroupData = new GroupData();
        mGroupData.addObserver(mStandFrag);
        mGroupData.addObserver(mGroupFrag);

        mToolbar.setTitle("光泽度");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void initPages() {
        mStandFrag = new LustreStandTestFrag(sp);
        mGroupFrag = new LustreGroupTestFrag(sp);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(mStandFrag, "stand_frag");
        adapter.addFragment(mGroupFrag, "group_frag");
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
                    setTitle("光泽度(简单模式)");
                    mMenu.findItem(R.id.simple_test).setChecked(true);
                    mGroupData.removeData();
                } else {
                    setTitle("光泽度(对比模式)");
                    mMenu.findItem(R.id.sc_test).setChecked(true);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private IntentFilter getintentflater() {
        IntentFilter intentFilter = new IntentFilter();
        return intentFilter;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        mManagerForBLE.unRegeisterReceiver(this);
        mManagerForBLE.v2manager.disconnect();
    }

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
                                if (db.rawQuery("select * from " + getTableName() + " where stand_name = ? and isStandard = ?", new String[]{name, "1"}).getCount() > 0) {
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
                mGroupFrag.getAdapter().notifyItemChanged(mGroupData.getSelectIndex());
            } else {
                T.showError(mContext, "请先测量标样");
            }
//            mGroupData = new GroupData();
//            mViewPager.setCurrentItem(0);
//            T.showSuccess(mContext, "保存成功");
        }
    }

    class TestTask extends AsyncTask {
        @Override
        protected void onPreExecute() {
            showProgressDialog("请稍后", "正在测量");
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Object[] params) {
            Log.i("check:", "lustre checked");
            byte[] result = mManagerForBLE.v2manager.whiteToSocket(BLE_Order.L.TEST_DATA, 18);
            result_sim = new LustreData(result);
            mHandler.removeCallbacks(timeoutCallback);
            if (mViewPager.getCurrentItem() == 0) {
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
                ArrayList<String> titles = ResHelper.getChecked_titles(mContext, sp, R.array.gzd_angles, ResConsts.lustre_title);
                ResultAndDValues resultAndDValues = ResHelper.getWC(
                        titles,
                        ResHelper.getSettings(titles, sp),
                        (LustreData) mGroupData.getStand(), result_sim
                );
                Boolean bol = ResHelper.getResultWithCheckItems(resultAndDValues);
                result_sim.setResult(bol);
                mGroupData.addTest(result_sim);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            dismissProgressDialog();
            super.onPostExecute(o);
        }
    }

    ;
    Runnable timeoutCallback = new Runnable() {
        @Override
        public void run() {
            dismissProgressDialog();
            testTask.cancel(true);
            T.showWarning(mContext, "请求超时,请确保蓝牙连接");
        }
    };
    TestTask testTask;

    @OnClick(R.id.ll4_check)
    void Test() {
        testTask = new TestTask();
        mHandler.postDelayed(timeoutCallback, 10000);
        testTask.execute();
    }

    Menu mMenu;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.lustre, menu);
        mMenu = menu;
        if (pageIndex != -1) {
            mViewPager.setCurrentItem(pageIndex, true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.setting_light_color:
                new SettingDialog(sp, new DismissCallback() {
                    @Override
                    public void dismiss() {
                        mStandFrag.initTitle();
                    }
                }).show(getFragmentManager(), "lustre_setting");
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
        }
        return true;
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
