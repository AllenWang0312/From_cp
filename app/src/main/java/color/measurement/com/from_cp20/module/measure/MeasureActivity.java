package color.measurement.com.from_cp20.module.measure;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import color.measurement.com.from_cp20.R;
import color.measurement.com.from_cp20.base.ProgressDialogActivity;
import color.measurement.com.from_cp20.manager.Ble_4.BlueToothManagerForBLE;
import color.measurement.com.from_cp20.manager.db.DBConsts;
import color.measurement.com.from_cp20.manager.db.DBHelper;
import color.measurement.com.from_cp20.manager.sp.SPConsts;
import color.measurement.com.from_cp20.module.App;
import color.measurement.com.from_cp20.module.been.interfaze.CompareableData;
import color.measurement.com.from_cp20.module.been.wapper.GroupData;
import color.measurement.com.from_cp20.widget.CustomViewPager;
import es.dmoral.toasty.Toasty;

/**
 * Created by wpc on 2017/5/7.
 */

public abstract class MeasureActivity<T extends CompareableData> extends ProgressDialogActivity {

    @BindView(R.id.toolbar) protected Toolbar mToolbar;
    @BindView(R.id.viewPager) protected CustomViewPager mViewPager;
    //    @BindView(R.id.ll1_left) LinearLayout mLl1Left;
//    @BindView(R.id.ll2_right) LinearLayout mLl2Right;
    @BindView(R.id.ll3_save) protected LinearLayout mLl3Save;
    @BindView(R.id.ll4_check) protected LinearLayout mLl4Check;

    protected GroupData<T> mGroupData;

    protected Context mContext;
    protected SharedPreferences sp;
    protected SQLiteDatabase db;
    protected boolean need_response = false;
    protected String tableName;
    protected String bleAddress;
    protected  String settingStr;

    protected int pageIndex;
    protected String standName;

    protected Boolean standard;


    protected BlueToothManagerForBLE mManagerForBLE;
    protected Runnable connectCallBack = new Runnable() {
        @Override
        public void run() {
            dismissProgressDialog();
            Toasty.error(mContext, "蓝牙连接失败").show();
            finish();
        }
    };
    protected Runnable progressCallBack = new Runnable() {
        @Override
        public void run() {
            dismissProgressDialog();
            Toasty.error(mContext, "传输超时,请确保蓝牙连接").show();
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_light_color2);
        ButterKnife.bind(this);
        db = new DBHelper(this, DBConsts.DATE_BASE, null, 1).getWritableDatabase();
        mContext = this;
    }

    public abstract void initPages();

    protected void rememberNameAndTips(String name, String tips) {
        SharedPreferences.Editor edit = sp.edit();
        edit.putString(SPConsts.STAND_TARGET_NAME, name);
        edit.putString(SPConsts.STAND_TARGET_TIPS, tips);
        edit.commit();
    }

    protected String getTableName() {
        if (tableName != null) {
            return tableName;
        }
        Cursor c = db.query(DBConsts.INS_TAB_NAME, new String[]{"userName", "address", "dataTableName"}, "userName = ? and address = ?", new String[]{App.logged_user.getName(), bleAddress}, null, null, null);
        if (c.moveToFirst()) {
            do {
                tableName = c.getString(c.getColumnIndex("dataTableName"));
                return tableName;
            } while (c.moveToNext());
        }
        c.close();
        return null;
    }

    protected void insertStand(String name, String tips) {

        if (!mGroupData.getStand().isHasSaved()) {
            mGroupData.getStand().setName(name);
            mGroupData.getStand().setTips(tips);
            mGroupData.saveStand(db, getTableName());
            color.measurement.com.from_cp20.util.utils.T.showSuccess(mContext, "保存成功");
        } else {
            color.measurement.com.from_cp20.util.utils.T.showWarning(mContext, "数据已存在,请勿重复保存");
        }
    }


}
