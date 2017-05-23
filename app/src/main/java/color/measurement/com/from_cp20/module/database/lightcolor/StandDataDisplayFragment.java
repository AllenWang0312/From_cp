package color.measurement.com.from_cp20.module.database.lightcolor;//package color.measurement.com.from_cp20.module.database.lightcolor.stand;//package color.measurement.com.from_cp20.module.database.lightcolor.stand;//package color.measurement.com.from_cp20.module.database.lightcolor;//package color.measurement.com.from_cp20.module.measure.lightcolor.v2;//package color.measurement.com.from_cp20.module.measure.lightcolor.v1;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import color.measurement.com.from_cp20.R;
import color.measurement.com.from_cp20.base.BaseFragment;
import color.measurement.com.from_cp20.common.util.UiUtils;
import color.measurement.com.from_cp20.manager.db.DBHelper;
import color.measurement.com.from_cp20.manager.res.ResHelper;
import color.measurement.com.from_cp20.module.App;
import color.measurement.com.from_cp20.module.been.interfaze.CompareableData;
import color.measurement.com.from_cp20.module.been.wapper.GroupData;
import color.measurement.com.from_cp20.util.utils.T;

/**
 * 分光测色  数据库 fragment
 * Created by wpc on 2017/2/27.
 */
@SuppressLint("ValidFragment")
public class StandDataDisplayFragment extends BaseFragment {
    Context mContext;
    String table_name;
    ArrayList<String> titles;
    ArrayList<CompareableData> stands;
    ArrayList<GroupData<CompareableData>> groups;
    @BindView(R.id.ll_title_stand_db) LinearLayout mLlTitleStandDb;
    @BindView(R.id.recyc_stand_db) RecyclerView mRecycStandDb;
    @BindView(R.id.srl_refesh_stand_db) SwipeRefreshLayout mSrlRefeshStandDb;
    @BindView(R.id.hor_scroll_view) HorizontalScrollView mHorScrollView;
    //    @BindView(R.id.sp_differentiate_name) Spinner mSpDifferentiateName;
//    @BindView(R.id.refesh_bt) Button mRefeshBt;
//    @BindView(R.id.find_bt) Button mFindBt;
    Unbinder unbinder;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            refeshRecycle();
        }
    };


    SQLiteDatabase db;
    SharedPreferences sp;
    int title_array_id;
    int[] titles_ids;

    public StandDataDisplayFragment(SQLiteDatabase db, SharedPreferences sp, String select_table_name, int title_array_id, int[] titles_ids) {
        this.table_name = select_table_name;
        this.db = db;
        this.sp = sp;
        this.title_array_id = title_array_id;
        this.titles_ids = titles_ids;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_light_colour, container, false);
        unbinder = ButterKnife.bind(this, v);
        mContext = getActivity();

        setHasOptionsMenu(true);
        mRecycStandDb.setLayoutManager(new LinearLayoutManager(mContext));
        refeshRecycle();
        mSrlRefeshStandDb.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
        mSrlRefeshStandDb.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSrlRefeshStandDb.setRefreshing(true);
                mHandler.sendEmptyMessage(0);
            }
        });
        mRecycStandDb.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        return v;
    }

    StandDataRecycAdapter adapter;

    public StandDataRecycAdapter getAdapter() {
        return adapter;
    }

    public void setAdapter(StandDataRecycAdapter adapter) {
        this.adapter = adapter;
    }

    public void refeshRecycle() {
//      ArrayList<String>  table_names = DBHelper.getTableNames(db, 0, App.logged_user.getName());
//        table_name = table_names.get(0);
        groups = DBHelper.getGroups(db, table_name);
//        stands = DBHelper.getStandData(db, select_table_name);
        titles = ResHelper.getChecked_titles(mContext, sp, title_array_id, titles_ids);
        mLlTitleStandDb.removeAllViews();
        CheckBox cb = new CheckBox(mContext);
        cb.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//            }
//        });
        mLlTitleStandDb.addView(cb);
        for (String str : titles) {
            UiUtils.addTextViewToLinearLayout(mContext, mLlTitleStandDb, str, null);
        }
        stands = DBHelper.getStands(groups);
        if (stands != null) {
            adapter = new StandDataRecycAdapter(mContext, titles, stands);
            mRecycStandDb.setAdapter(adapter);
        }
        if (mSrlRefeshStandDb.isRefreshing()) {
            mSrlRefeshStandDb.setRefreshing(false);
        }
        T.showSuccess(mContext, "刷新成功");

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public String getSelect_table_name() {
        return table_name;
    }

    public void setSelect_table_name(String select_table_name) {
        this.table_name = select_table_name;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.db, menu);
        menu.findItem(R.id.updata_lc_db).setVisible(App.logged_user.is_login());
        menu.findItem(R.id.commit_lc_db).setVisible(App.logged_user.is_login());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public ArrayList<GroupData<CompareableData>> getGroups() {
        return groups;
    }

    public void setGroups(ArrayList<GroupData<CompareableData>> groups) {
        this.groups = groups;
    }
}
