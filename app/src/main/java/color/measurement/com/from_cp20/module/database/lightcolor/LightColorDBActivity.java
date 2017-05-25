package color.measurement.com.from_cp20.module.database.lightcolor;//package color.measurement.com.from_cp20.module.database;//package color.measurement.com.from_cp20.module.database;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.sql.SQLException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import color.measurement.com.from_cp20.R;
import color.measurement.com.from_cp20.base.BaseDBActivity;
import color.measurement.com.from_cp20.common.adapter.ViewPagerAdapter;
import color.measurement.com.from_cp20.common.interfaze.DismissCallback;
import color.measurement.com.from_cp20.manager.Ble_4.BlueToothManagerForBLE;
import color.measurement.com.from_cp20.manager.db.DBConsts;
import color.measurement.com.from_cp20.manager.db.DBHelper;
import color.measurement.com.from_cp20.manager.db.MySqlConsts;
import color.measurement.com.from_cp20.manager.db.MySqlHelper;
import color.measurement.com.from_cp20.manager.ins.Instrument;
import color.measurement.com.from_cp20.manager.res.ResConsts;
import color.measurement.com.from_cp20.manager.sp.SPConsts;
import color.measurement.com.from_cp20.module.been.Ins;
import color.measurement.com.from_cp20.module.been.data.LightColorData;
import color.measurement.com.from_cp20.module.been.data.LustreData;
import color.measurement.com.from_cp20.module.been.interfaze.CompareableData;
import color.measurement.com.from_cp20.module.been.wapper.GroupData;
import color.measurement.com.from_cp20.module.measure.lightcolor.v2.LightColor2Activity;
import color.measurement.com.from_cp20.module.measure.lightcolor.v2.TestDataAdapter;
import color.measurement.com.from_cp20.module.measure.lustre.LustreActivity;
import color.measurement.com.from_cp20.util.utils.L;
import color.measurement.com.from_cp20.widget.CustomViewPager;
import es.dmoral.toasty.Toasty;

import static color.measurement.com.from_cp20.manager.db.MySqlConsts.MySql_light_color;
import static color.measurement.com.from_cp20.manager.db.MySqlConsts.MySql_lustre;

//import color.measurement.com.from_cp20.common.widget.TableFragment;

/**
 * Created by wpc on 2017/2/27.
 */
public class LightColorDBActivity extends BaseDBActivity {


    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.vp_instrument) CustomViewPager mVpInstrument;

    ViewPagerAdapter madapter;
    //    StandTableFragment stand_table;
    StandDataDisplayFragment mStandDataDisplayFragment;
    DBGroupFragment mGroupDisplayFragment;
    BlueToothManagerForBLE mManagerForBLE;

    String table_name;
    int type;
    SharedPreferences stand_sp, test_sp;


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    int data_type_id, comp_title_id;
    int[] arrays_ids, comp_titles;

    @Override
    protected void onResume() {
        showProgressDialog("请稍候", "数据加载中");
        Intent i = getIntent();
        table_name = i.getStringExtra("table_name");

        type = Instrument.tableType(table_name);

        switch (type) {
            case 0:
                stand_sp = getSharedPreferences(SPConsts.PREFERENCE_LIGHT_COLOR_DB_STAND, MODE_PRIVATE);
                test_sp = getSharedPreferences(SPConsts.PREFERENCE_LIGHT_COLOR_DB_TEST, MODE_PRIVATE);
                data_type_id = R.array.db_base_data;
                arrays_ids = ResConsts.db_titles;
                comp_title_id = R.array.comparison_data;
                comp_titles = ResConsts.title_all;
                break;
            case 2:
                stand_sp = getSharedPreferences(SPConsts.PREFERENCE_LUSTRE, MODE_PRIVATE);
                test_sp = getSharedPreferences(SPConsts.PREFERENCE_LUSTRE, MODE_PRIVATE);
                data_type_id = R.array.gzd_angles_test;
                arrays_ids = ResConsts.lustre_title_test;
                comp_title_id = R.array.gzd_angles;
                comp_titles = ResConsts.lustre_title;
                break;
        }

        madapter = new ViewPagerAdapter(getSupportFragmentManager());
        mStandDataDisplayFragment = new StandDataDisplayFragment(db, stand_sp, table_name, data_type_id, arrays_ids);
        mGroupDisplayFragment = new DBGroupFragment(test_sp, type, data_type_id, arrays_ids, comp_title_id, comp_titles);

        madapter.addFragment(mStandDataDisplayFragment, "标样数据");
        madapter.addFragment(mGroupDisplayFragment, "试样数据");
//        madapter.addFragment(stand_table, "stand_table");
        mVpInstrument.setAdapter(madapter);
//        mManagerForBLE.regeisterReceiver(this);
        dismissProgressDialog();
        super.onResume();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_db);
        ButterKnife.bind(this);
        mManagerForBLE = BlueToothManagerForBLE.getInstance(LightColorDBActivity.this);
        mToolbar.setTitle("数据库");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initDataBase();
//        mTlInstrument.setupWithViewPager(mVpInstrument);
//        mTlInstrument.setClickable(false);
        mVpInstrument.setPagingEnabled(false);
        instance = MySqlHelper.getInstance(mContext);
    }

    public static void startWithIntent(Context context, String table_name) {
        Intent i = new Intent(context, LightColorDBActivity.class);
        i.putExtra("table_name", table_name);
        context.startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    MySqlHelper instance;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.refesh_lc_db:
                // showAnimate(item);
                break;
            case R.id.updata_lc_db:
                new MySqlHelper.MySQLAsyTask(new Runnable() {
                    @Override
                    public void run() {
//                        String table_name = mStandDataDisplayFragment.getSelect_table_name();
                        MySqlHelper.downloadDataFromMySql(instance.mStatement, table_name, db);
                    }
                }, true).execute();
                mStandDataDisplayFragment.mHandler.sendEmptyMessage(0);
                break;
            case R.id.commit_lc_db://上传
                new MySqlHelper.MySQLAsyTask(new Runnable() {
                    @Override
                    public void run() {
                        try {
//                            String table_name = mStandDataDisplayFragment.getSelect_table_name();
//                            ResultSet set = instance.mStatement.executeQuery("SELECT table_name FROM chnspec WHERE table_name = " + table_name);
//                            if (set.getFetchSize() > 0) {
//                                L.i("表已存在", table_name);
//                            } else {
                            Cursor table_c = db.rawQuery("select * from " + DBConsts.INS_TAB_NAME + " where dataTableName = ? ", new String[]{table_name});
                            if (table_c.getCount() == 1) {
                                table_c.moveToFirst();
                                Ins ins = new Ins(table_c);
                                MySqlHelper.insertDataToMySql(instance.mStatement, MySqlConsts.ins_table, ins,mContext);
                            }
                            if (type == 0)
                                instance.mStatement.execute(String.format(MySql_light_color, table_name));
                            if (type == 2)
                                instance.mStatement.execute(String.format(MySql_lustre, table_name));
                            L.i("创建成功", table_name);
//                            }
                            Cursor c = db.rawQuery("select * from " + table_name + " where hasUpdata = ?", new String[]{"0"});
                            if (c.moveToFirst()) {
                                do {
                                    CompareableData lc = null;
                                    if (type == 0) {
                                        lc = new LightColorData(c);
                                    } else if (type == 2) {
                                        lc = new LustreData(c);
                                    }
                                    MySqlHelper.insertDataToMySql(instance.mStatement, table_name, lc,mContext);
                                    ContentValues cv = new ContentValues();
                                    long moment = c.getLong(c.getColumnIndex("moment"));
                                    cv.put("hasUpdata", 1);
                                    db.update(table_name, cv, "moment = ?", new String[]{String.valueOf(moment)});
                                } while (c.moveToNext());
                            }

                        } catch (SQLException e) {
                            L.i("创建失败");
                            e.printStackTrace();
                        }
                    }
                }, true).execute();
                break;
            case R.id.db_view_group:
                Integer selected = mStandDataDisplayFragment.getAdapter().getChecked_item_index();
                if (selected == null || selected == -1) {
                    Toasty.warning(mContext, "请选择一条数据").show();
                } else {
                    GroupData<CompareableData> group = mStandDataDisplayFragment.getGroups().get(selected);
                    if (group.getTests() != null && group.getTests().size() > 0) {

                        mGroupDisplayFragment.setGroupData(
//                                ResHelper.getChecked_titles(mContext, SPConsts.PREFERENCE_LIGHT_COLOR_DB_TEST, ResHelper.stand_data_type, ResHelper.db_titles),
                                group);
                        mVpInstrument.setCurrentItem(1, true);
                    } else {
                        Toasty.warning(mContext, "该标样没有试样数据").show();
                    }

                }
                break;
            case R.id.db_delete:
                AlertDialog dialog = new AlertDialog.Builder(mContext)
                        .setTitle("是否删除")
                        .setMessage("点击确定,删除选中条目,对应试样也会被删除")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Integer check = mStandDataDisplayFragment.getAdapter().getChecked_item_index();
                                ArrayList<GroupData<CompareableData>> groups = mStandDataDisplayFragment.getGroups();
                                groups.get(check).remove(db, mStandDataDisplayFragment.getSelect_table_name(), groups.get(check).getStand().getStand_name());
                                mStandDataDisplayFragment.refeshRecycle();
                            }
                        }).setNegativeButton("取消", null).create();
                dialog.show();
                break;
            case R.id.db_expore:
                Integer check = mStandDataDisplayFragment.getAdapter().getChecked_item_index();
                if (check != null && check != -1) {
                    Intent i = null;
                    if (type == 0) i = new Intent(LightColorDBActivity.this, LightColor2Activity.class);
                    if (type == 2) i = new Intent(LightColorDBActivity.this, LustreActivity.class);

//                    Cursor c = db.rawQuery("select * from " + DBConsts.INS_TAB_NAME + " where dataTableName = ?", new String[]{mStandDataDisplayFragment.getSelect_table_name()});
//                    if (c.moveToFirst()) {
//                        String path = c.getString(c.getColumnIndex("cacheAbsPath"));
//                        Ins ins = Ins.initProductFromFile(path, mContext);
//                        BluetoothDevice device = ins.getDevice();
//                        mManagerForBLE.disconnect();

//                        Boolean b = null;
//                        if (type == 0) mManagerForBLE.v4manager.connectToDevice(this, device);
//                        if (type == 2)
//                            b = mManagerForBLE.v2manager.connectTo_2_Device(this, device);
//                        if (b == null || b == true) {
//                            if (mManagerForBLE.isEnable()) {
//                            App.logged_user.setConnectIns(ins);
//                            i.putExtra("bleName", device.getName());
                            i.putExtra("tableName", mStandDataDisplayFragment.getSelect_table_name());
                            i.putExtra("standName", mStandDataDisplayFragment.getGroups().get(check).getStand().getStand_name());
                            i.putExtra("page_index", 1);
                            startActivity(i);
                            finish();
//                            } else {
//                                mManagerForBLE.openBlueTooth();
//                            }
//                        }
//                    }

                } else {
                    Toasty.warning(mContext, "请选择一条数据").show();
                }

                break;
            case R.id.db_data_setting:

                new DisplayDataTypeCheckDialog(mContext, new DismissCallback() {
                    @Override
                    public void dismiss() {
                        mStandDataDisplayFragment.refeshRecycle();
                    }
                }, stand_sp, data_type_id
                ).show(getSupportFragmentManager(), "test_type_check");
                break;
            case R.id.db_test_delete:
                GroupData<CompareableData> groupData = mGroupDisplayFragment.getGroup();
                TestDataAdapter adapter = mGroupDisplayFragment.getAdapter();
                groupData.remove(db, mStandDataDisplayFragment.getSelect_table_name(), groupData.getTests().get(groupData.getSelectIndex()).getMoment());
                groupData.getTests().remove(groupData.getSelectIndex());
                adapter.setSelectIndex(0);
                mGroupDisplayFragment.refeshViews();
                break;
            case R.id.db_test_data_setting:
                new DisplayDataTypeCheckDialog(mContext, new DismissCallback() {
                    @Override
                    public void dismiss() {
                        mGroupDisplayFragment.refeshViews();
                    }
                }, test_sp, data_type_id).show(getSupportFragmentManager(), "stand_type_check");
                break;

            case R.id.db_test_fsl:
                SharedPreferences.Editor editor = getSharedPreferences(SPConsts.PREFERENCE_LIGHT_COLOR_DB_TEST, MODE_PRIVATE).edit();
                editor.putInt(SPConsts.SIMPLE_DATA_MOD, 1);
                editor.commit();
                mGroupDisplayFragment.refeshViews();
                break;
            case R.id.db_test_sc:
                SharedPreferences.Editor e = getSharedPreferences(SPConsts.PREFERENCE_LIGHT_COLOR_DB_TEST, MODE_PRIVATE).edit();
                e.putInt(SPConsts.SIMPLE_DATA_MOD, 2);
                e.commit();
                mGroupDisplayFragment.refeshViews();
                break;
        }
        return true;
    }

    @Override
    public void initDataBase() {
        mContext = this;
        db = new DBHelper(mContext, DBConsts.DATE_BASE, null, 1).getWritableDatabase();
    }

    @Override
    public void onBackPressed() {
        if (mVpInstrument.getCurrentItem() == 1) {
            mVpInstrument.setCurrentItem(0, true);
        }
//        else {
//            super.onBackPressed();
//        }
        else {
            if(type==0){
                Intent i = new Intent(LightColorDBActivity.this, LightColor2Activity.class);
                i.putExtra("page_index", 0);
                startActivity(i);
                finish();
            }else if(type==2){
                Intent i = new Intent(LightColorDBActivity.this, LustreActivity.class);
                i.putExtra("page_index", 0);
                startActivity(i);
                finish();
            }

        }

    }
}
