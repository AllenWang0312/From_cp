package color.measurement.com.from_cp20.module.measure.lustre;

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
import color.measurement.com.from_cp20.manager.res.ResHelper;
import color.measurement.com.from_cp20.module.been.data.LustreData;
import color.measurement.com.from_cp20.module.been.interfaze.CompareableData;
import color.measurement.com.from_cp20.module.been.wapper.GroupData;
import color.measurement.com.from_cp20.module.measure.GroupDisplayFragment;
import color.measurement.com.from_cp20.module.measure.lightcolor.bean.ResultAndDValues;
import color.measurement.com.from_cp20.module.measure.lightcolor.v2.TestDataAdapter;

import static color.measurement.com.from_cp20.manager.res.ResHelper.getSettings;

/**
 * Created by wpc on 2017/5/7.
 */
@SuppressLint("ValidFragment")
public class LustreGroupTestFrag extends GroupDisplayFragment implements Observer {

    public LustreGroupTestFrag(SharedPreferences sp) {
        super(sp);
    }
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                refeshViews();
            }
        }
    };

    public TestDataAdapter getAdapter() {
        return adapter;
    }

    TestDataAdapter adapter;
    ArrayList<String> titles;
    ArrayList<String> comp_titles;
    GroupData<LustreData> group;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        mRecycTest.setLayoutManager(new LinearLayoutManager(mContext));
        return v;
    }

    @Override
    public void refeshViews() {
        Log.i("refeshViews", "testFragment");
        if (group != null) {
            initTitles();
            initStand();
            initTests();
            if (group.getTests().size() > 0) {
                initDev(group.getStand(),group.getTests().get(group.getSelectIndex()));
            }
        }
    }

    @Override
    public void initTitles() {
        mLlTitlesTest.removeAllViews();
        titles = ResHelper.getChecked_titles(mContext,sp, R.array.gzd_angles_test, ResConsts.lustre_title_test);
        comp_titles = ResHelper.getChecked_titles(mContext, sp, R.array.gzd_angles, ResConsts.lustre_title);
        UiUtils.addTextViewToLinearLayout(mContext, mLlTitlesTest, "参数", null);
        for (String tit : titles) {
            UiUtils.addTextViewToLinearLayout(mContext, mLlTitlesTest, tit, null);
        }
    }

    @Override
    protected void initStand() {
        mLlStandTest.removeAllViews();
        if (group.getStand() != null) {
            LustreData stand = group.getStand();
            if (stand != null) {
                HashMap<String, Object> result = stand.toHashMap(mContext);
                UiUtils.addTextViewToLinearLayout(mContext, mLlStandTest, "标", null);
                for (String str : titles) {
                    Object d = result.get(str);
                    UiUtils.addTextViewToLinearLayout(mContext, mLlStandTest, d == null ? "---" : StringFormat.Object(d), null);
                }
            }
        }
    }

    @Override
    protected void initDev(CompareableData stand,CompareableData test) {
        mLlDevTest.removeAllViews();
        UiUtils.addTextViewToLinearLayout(mContext, mLlDevTest, "△", null);
        int index = group.getSelectIndex();
        Log.i("index", index + "");
        HashMap<String, Float> sets = getSettings(comp_titles, sp);
        ResultAndDValues res = ResHelper.getWC(comp_titles, sets,(LustreData) stand, (LustreData)test);
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
        if (group != null) {
            mRecycTest.setVisibility(View.VISIBLE);
            if (group.getTests() != null) {
//                if (titles.size() > 0) {
                    if(adapter==null){
                        adapter = new TestDataAdapter(mContext, sp);
//                adapter = new TestDataAdapter(mContext, sp, titles, group.getTests(), group.getSelectIndex());

                        adapter.setItemClickListener(new OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                adapter.setSelectIndex(position);
                                group.setSelectIndex(position);
                                adapter.notifyItemChanged(group.getSelectIndex());
                                adapter.notifyItemChanged(group.getLastSelect());
                            }
                        });
                        mRecycTest.setAdapter(adapter);
                    }
// else {
//                        adapter.notifyDataSetChanged();
//                        adapter.setData(group.getTests(),group.getSelectIndex());
//                    }
                adapter.refeshDatas( titles, group.getTests(),group.getSelectIndex());
//                    mRecycTest.scrollToPosition(group.getSelectIndex());
//                } else {
//                    T.showWarning(mContext, "未设置显示数据类型");
//                }
            }
        } else {
            mRecycTest.setVisibility(View.GONE);
        }
    }


    @Override
    public void update(Observable o, Object arg) {
        group = (GroupData<LustreData>) o;
        mHandler.sendEmptyMessage(0);
        Log.i("update", "testFragment data update");
    }
}
