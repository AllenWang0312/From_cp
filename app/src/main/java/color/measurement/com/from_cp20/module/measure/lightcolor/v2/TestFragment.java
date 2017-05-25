package color.measurement.com.from_cp20.module.measure.lightcolor.v2;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import color.measurement.com.from_cp20.R;
import color.measurement.com.from_cp20.common.interfaze.OnItemClickListener;
import color.measurement.com.from_cp20.common.util.StringFormat;
import color.measurement.com.from_cp20.common.util.UiUtils;
import color.measurement.com.from_cp20.manager.res.ResConsts;
import color.measurement.com.from_cp20.manager.sp.SPConsts;
import color.measurement.com.from_cp20.manager.res.ResHelper;
import color.measurement.com.from_cp20.module.been.interfaze.CompareableData;
import color.measurement.com.from_cp20.module.been.data.LightColorData;
import color.measurement.com.from_cp20.module.been.wapper.GroupData;
import color.measurement.com.from_cp20.module.measure.GroupDisplayFragment;
import color.measurement.com.from_cp20.module.measure.lightcolor.bean.ResultAndDValues;
import color.measurement.com.from_cp20.util.utils.T;
import color.measurement.com.from_cp20.widget.FSL.DataProcesser;

import static color.measurement.com.from_cp20.manager.res.ResHelper.getSCGStitles;
import static color.measurement.com.from_cp20.manager.res.ResHelper.getSettings;

/**
 * Created by wpc on 2017/4/27.
 */
@SuppressLint("ValidFragment")
public class TestFragment extends GroupDisplayFragment implements Observer {

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                refeshViews();
            }
        }
    };
    //    protected  String[] titles;
    TestDataAdapter adapter;
    ArrayList<String> comp_titles;
    ArrayList<String> titles;

    GroupData<LightColorData> group;

    public TestFragment(SharedPreferences sp) {
        super(sp);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        mRecycTest.setLayoutManager(new LinearLayoutManager(mContext));

        return v;
    }

    public void refeshTables(LightColorData stand, LightColorData test) {
        int mod = sp.getInt(SPConsts.SIMPLE_DATA_MOD, 0);
        mLlLabTable.setVisibility(mod == 2 ? View.VISIBLE : View.GONE);
        mFslTable.setVisibility(mod == 1 ? View.VISIBLE : View.GONE);
        if (mod == 2 && group.hasSimple()) {
            refeshSC(stand, test);
        } else if (mod == 1) {
            refeshFSL(stand, test);
        }
    }

    @Override
    public void refeshViews() {
        Log.i("refeshViews", "testFragment");
        if (group != null) {
            initTitles();
            initStand();
            initTests();
            if (group.getTests() != null && group.getTests().size() > 0) {
                initDev(group.getStand(), group.getTests().get(group.getSelectIndex()));
            }
            refeshTables(group.getStand(),
                    group.getTests() == null || group.getTests().size() == 0 ?
                            null : group.getTests().get(group.getSelectIndex()));
        }
    }

    @Override
    public void initTitles() {
        mLlTitlesTest.removeAllViews();
        titles = ResHelper.getChecked_titles(mContext, sp, R.array.db_base_data, ResConsts.db_titles);
        comp_titles = ResHelper.getChecked_titles(mContext, sp, R.array.comparison_data, ResConsts.title_all);

        UiUtils.addTextViewToLinearLayout(mContext, mLlTitlesTest, "参数", null);
        for (String tit : titles) {
            UiUtils.addTextViewToLinearLayout(mContext, mLlTitlesTest, tit, null);
        }
    }

    @Override
    protected void initStand() {
        mLlStandTest.removeAllViews();
        if (group.getStand() != null) {
            LightColorData stand = group.getStand();
            if (stand != null) {
                HashMap<String, Object> result = stand.toHashMap(mContext);
                UiUtils.addTextViewToLinearLayout(mContext, mLlStandTest, "标样", null);
                for (String str : titles) {
                    Object d = result.get(str);
                    UiUtils.addTextViewToLinearLayout(mContext, mLlStandTest, d == null ? "---" : StringFormat.Object(d), null);
                }
            }
        }
    }

    @Override
    protected void initDev(CompareableData stand, CompareableData test) {
        //refesh div
        mLlDevTest.removeAllViews();
        UiUtils.addTextViewToLinearLayout(mContext, mLlDevTest, "差值", null);
        int index = group.getSelectIndex();
        Log.i("index", index + "");

        String[] scgs = getSCGStitles(mContext);
        HashMap<String, Float> sets = getSettings(comp_titles, sp);

        ResultAndDValues res = ResHelper.getWC(sp, comp_titles, scgs, sets, (LightColorData) stand, (LightColorData) test);

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

    @Override
    protected void initTests() {
        //        if (adapter == null) {
        if (group != null) {
            mRecycTest.setVisibility(View.VISIBLE);
            if (group.getTests() != null) {
                if (titles.size() > 0) {
                    if (group.getTests() != null) {
                        if (adapter == null) {
                            adapter = new TestDataAdapter(mContext, sp);
                            adapter.maxLength=10;
//                adapter = new TestDataAdapter(mContext, sp, titles, group.getTests(), group.getSelectIndex());
//                            if (adapter.getItemClickListener() == null) {
                                adapter.setItemClickListener(new OnItemClickListener() {
                                    @Override
                                    public void onItemClick(View view, int position) {
                                        adapter.setSelectIndex(position);
                                        group.setSelectIndex(position);
                                        refeshTables(group.getStand(), group.getTests().get(group.getSelectIndex()));
                                        initDev(group.getStand(), group.getTests().get(group.getSelectIndex()));
                                        adapter.notifyItemChanged(group.getSelectIndex());
                                        adapter.notifyItemChanged(group.getLastSelect());
                                    }
                                });
//                            }

                            mRecycTest.setAdapter(adapter);
//
                        }
//                        else {
//                            adapter.notifyItemInserted(0);
//                            adapter.notifyItemChanged(group.getSelectIndex());
//                            adapter.notifyItemChanged(group.getSelectIndex()+1);
////                            adapter.notifyAll();
////                            adapter.notifyDataSetChanged();
//
////                        adapter.setData(group.getTests(),group.getSelectIndex());
//                        }
                        adapter.refeshDatas( titles, group.getTests(), group.getSelectIndex());
                    }

//                    mRecycTest.scrollToPosition(group.getSelectIndex());
                } else {
                    T.showWarning(mContext, "未设置显示数据类型");
                }

            }
        } else {
            mRecycTest.setVisibility(View.GONE);
        }

    }


    void refeshSC(LightColorData stand, LightColorData test) {
        mVLightSc.setLab(stand.getResultData(sp).getHunter_Lab(), true);
        mVLightSc.setLab(test.getResultData(sp).getHunter_Lab(), false);
        mVScSc.setLab(stand.getResultData(sp).getHunter_Lab(), true);
        mVScSc.setLab(test.getResultData(sp).getHunter_Lab(), false);
    }

    void refeshFSL(LightColorData stand, LightColorData test) {
        DataProcesser.refeshFSL(stand == null ? null : stand.getSCI(),
                test == null ? null : test.getSCI(),
                mFslTable);
    }

    @Override
    public void update(Observable o, Object arg) {
        group = (GroupData<LightColorData>) o;
        mHandler.sendEmptyMessage(0);
        Log.i("update", "testFragment data update");
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            mHandler.sendEmptyMessage(0);
        }
    }


    public TestDataAdapter getAdapter() {
        return adapter;
    }

    public void setAdapter(TestDataAdapter adapter) {
        this.adapter = adapter;
    }

}
