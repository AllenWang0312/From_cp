package color.measurement.com.from_cp20.module.database.lightcolor;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;

import color.measurement.com.from_cp20.R;
import color.measurement.com.from_cp20.common.interfaze.OnItemClickListener;
import color.measurement.com.from_cp20.common.util.StringFormat;
import color.measurement.com.from_cp20.common.util.UiUtils;
import color.measurement.com.from_cp20.manager.res.ResHelper;
import color.measurement.com.from_cp20.manager.sp.SPConsts;
import color.measurement.com.from_cp20.module.been.data.LightColorData;
import color.measurement.com.from_cp20.module.been.data.LustreData;
import color.measurement.com.from_cp20.module.been.interfaze.CompareableData;
import color.measurement.com.from_cp20.module.been.wapper.GroupData;
import color.measurement.com.from_cp20.module.measure.GroupDisplayFragment;
import color.measurement.com.from_cp20.module.measure.lightcolor.bean.ResultAndDValues;
import color.measurement.com.from_cp20.module.measure.lightcolor.v2.TestDataAdapter;
import color.measurement.com.from_cp20.util.utils.T;
import color.measurement.com.from_cp20.widget.FSL.DataProcesser;

import static color.measurement.com.from_cp20.manager.res.ResHelper.getSCGStitles;
import static color.measurement.com.from_cp20.manager.res.ResHelper.getSettings;

/**
 * Created by wpc on 2017/4/27.
 */
@SuppressLint("ValidFragment")
public class DBGroupFragment extends GroupDisplayFragment {
    int type;
    int title_array_id;
    int[] titles_ids;
    int comp_array_id;
    int[] comp_ids;

    public DBGroupFragment(SharedPreferences sp, int type,
                           int title_array_id, int[] titles_ids,
                           int comp_array_id, int[] comp_ids) {
        super(sp);
        this.type = type;
        this.title_array_id = title_array_id;
        this.titles_ids = titles_ids;
        this.comp_array_id = comp_array_id;
        this.comp_ids = comp_ids;
    }

    ArrayList<String> comp_titles;
    ArrayList<String> titles;
    TestDataAdapter adapter;
    GroupData<CompareableData> group;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void initTitles() {
        titles = ResHelper.getChecked_titles(mContext, sp, title_array_id, titles_ids);
        comp_titles = ResHelper.getChecked_titles(mContext, sp, comp_array_id, comp_ids);

        mLlTitlesTest.removeAllViews();
        UiUtils.addTextViewToLinearLayout(mContext, mLlTitlesTest, "参数", null);
        for (String tit : titles) {
            UiUtils.addTextViewToLinearLayout(mContext, mLlTitlesTest, tit, null);
        }
    }

    @Override
    protected void initStand() {
        if (group != null) {
            if (group.getStand() != null) {
                CompareableData stand = group.getStand();
                if (stand != null) {
                    mLlStandTest.removeAllViews();
                    HashMap<String, Object> result = stand.toHashMap(mContext);
                    UiUtils.addTextViewToLinearLayout(mContext, mLlStandTest, "标", null);
                    for (String str : titles) {
                        Object d = result.get(str);
                        UiUtils.addTextViewToLinearLayout(mContext, mLlStandTest, d == null ? "---" : StringFormat.Object(d), null);
                    }
                }
            }
        }

    }

    @Override
    protected void initDev(CompareableData stand, CompareableData test) {
        //refesh div
        if (group != null) {
            mLlDevTest.removeAllViews();
            UiUtils.addTextViewToLinearLayout(mContext, mLlDevTest, "△", null);
            ResultAndDValues res = null;
            HashMap<String, Float> sets = getSettings(comp_titles, sp);
            if (type == 0) {
                String[] scgs = getSCGStitles(mContext);
                res = ResHelper.getWC(sp, comp_titles, scgs, sets, (LightColorData) stand, (LightColorData) test);
            } else if (type == 2) {
                res = ResHelper.getWC(comp_titles, sets, (LustreData) stand, (LustreData) test);
            }

            HashMap<String, Boolean> hege = res.getResult();
            HashMap<String, Double> result = res.getData();

            for (String str : titles) {
                if (comp_titles.contains(str)) {
                    Double d = result.get(str);
                    Boolean b = hege.get(str);
                    UiUtils.addTextViewToLinearLayout(mContext, mLlDevTest, d == null ? "---" : StringFormat.TwoDecimal(d), b ? Color.GREEN : Color.RED);
                } else {
                    UiUtils.addTextViewToLinearLayout(mContext, mLlDevTest, "---", null);
                }
            }
        }

    }

    @Override
    protected void initTests() {
        if (group != null) {
            if (group.getTests() != null && group.getTests().size() > 0) {
                if (titles.size() > 0) {
                    if (adapter == null) {
                        mRecycTest.setLayoutManager(new LinearLayoutManager(mContext));
                        adapter = new TestDataAdapter(mContext, sp);
//                adapter = new TestDataAdapter(mContext, sp, titles, group.getTests(), group.getSelectIndex());
                        adapter.setItemClickListener(new OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                adapter.setSelectIndex(position);
                                group.setSelectIndex(position);
                                refeshTables();
                                adapter.notifyItemChanged(group.getLastSelect());
                                adapter.notifyItemChanged(group.getSelectIndex());
                            }
                        });
                        mRecycTest.setAdapter(adapter);
                    }
//                    else {
//                        adapter.notifyDataSetChanged();
//                    }
                    adapter.refeshDatas(titles, group.getTests(), group.getSelectIndex());
//                    mRecycTest.scrollToPosition(group.getSelectIndex());
                } else {
                    T.showWarning(mContext, "未设置显示数据类型");
                }
            }
            initDev(group.getStand(), group.getTests().get(group.getSelectIndex()));
        }
    }

    public void refeshTables() {
        int mod = sp.getInt(SPConsts.SIMPLE_DATA_MOD, 0);
        mFslTable.setVisibility(mod == 1 ? View.VISIBLE : View.GONE);
        mLlLabTable.setVisibility(mod == 2 ? View.VISIBLE : View.GONE);
        if (group.hasSimple()) {
            if (mod == 2) {
                refeshSC();
            } else if (mod == 1) {
                refeshFSL();
            }
        }
    }


    @Override
    public void refeshViews() {
        refeshTables();
        initTitles();
        initStand();
        initTests();
    }

    void refeshSC() {
        LightColorData stand = (LightColorData) group.getStand();
        LightColorData test = (LightColorData) group.getTests().get(group.getSelectIndex());

        mVLightSc.setLab(stand.getResultData(sp).getHunter_Lab(), true);
        mVLightSc.setLab(test.getResultData(sp).getHunter_Lab(), false);
        mVScSc.setLab(stand.getResultData(sp).getHunter_Lab(), true);
        mVScSc.setLab(test.getResultData(sp).getHunter_Lab(), false);
    }

    void refeshFSL() {
        LightColorData stand = (LightColorData) group.getStand();
        LightColorData test = (LightColorData) group.getTests().get(group.getSelectIndex());
        DataProcesser.refeshFSL(stand.getSCI(), test.getSCI(), mFslTable);
    }

    void setGroupData(
            GroupData<CompareableData> groupData) {
//        this.titles=titles;
        group = groupData;
        refeshViews();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.db_test, menu);
    }

    public ArrayList<String> getTitles() {
        return titles;
    }

    public void setTitles(ArrayList<String> titles) {
        this.titles = titles;
    }

    public TestDataAdapter getAdapter() {
        return adapter;
    }

    public void setAdapter(TestDataAdapter adapter) {
        this.adapter = adapter;
    }

    public GroupData<CompareableData> getGroup() {
        return group;
    }

    public void setGroup(GroupData<CompareableData> group) {
        this.group = group;
    }
}
