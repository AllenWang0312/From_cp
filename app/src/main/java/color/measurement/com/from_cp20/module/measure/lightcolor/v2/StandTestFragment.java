package color.measurement.com.from_cp20.module.measure.lightcolor.v2;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import color.measurement.com.from_cp20.common.util.StringFormat;
import color.measurement.com.from_cp20.manager.res.ResConsts;
import color.measurement.com.from_cp20.manager.sp.SPConsts;
import color.measurement.com.from_cp20.manager.NumEndedString;
import color.measurement.com.from_cp20.manager.res.ResHelper;
import color.measurement.com.from_cp20.module.been.data.LightColorData;
import color.measurement.com.from_cp20.module.been.wapper.GroupData;
import color.measurement.com.from_cp20.module.measure.StandDisplayFragment;
import color.measurement.com.from_cp20.util.blankj.StringUtils;
import color.measurement.com.from_cp20.widget.FSL.DataProcesser;

/**
 * Created by wpc on 2017/3/30.
 */

@SuppressLint("ValidFragment")
public class StandTestFragment extends StandDisplayFragment implements Observer {

    String[] titles;
    GroupData<LightColorData> group;

    public StandTestFragment(SharedPreferences sp) {
        super(sp);
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                dismissViewIfNeed();
                initTitle();
                refeshName();
                mLlContentStandTest.removeAllViews();
                setResult(group.getStand());
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);

        initTitle();
        refeshName();
        dismissViewIfNeed();
        return v;
    }

    void setResult(@Nullable LightColorData LightColorData) {
        if (LightColorData != null) {
            HashMap<String, Double> results = LightColorData.getResultData(sp).toHashMap();
            for (String str : titles) {
                TextView result = new TextView(mContext);
                result.setText(StringFormat.TwoDecimal(results.get(str)));
                result.setTextSize(36);
                result.setGravity(Gravity.CENTER_HORIZONTAL);
                result.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                mLlContentStandTest.addView(result);
            }
        }
        dismissViewIfNeed();
        if (mFslTableStand.getVisibility() == View.VISIBLE) {
            DataProcesser.refeshFSL(LightColorData == null ? null : LightColorData.getSCI(), null, mFslTableStand);
        }

    }

    private void dismissViewIfNeed() {
        int stand_views_mod = sp.getInt(SPConsts.STAND_DATA_MOD,1);
//        T.showDeBug(mContext,stand_views_mod+"");
        if (stand_views_mod == 1) {
            mFslTableStand.setVisibility(View.VISIBLE);
        } else {
            mFslTableStand.setVisibility(View.GONE);
        }
    }


    void initTitle() {
        titles = ResHelper.getStandTitles(mContext, SPConsts.PREFERENCE_LIGHT_COLOR, ResConsts.stand_data, SPConsts.STAND_DATA_DISPLAY_TYPE);
        mLlTitleStandTest.removeAllViews();
        for (String str : titles) {
            TextView title = new TextView(mContext);
            title.setText(str + ":");
            title.setTextSize(36);
            title.setGravity(Gravity.CENTER_HORIZONTAL);
            title.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            mLlTitleStandTest.addView(title);
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        group = (GroupData<LightColorData>) o;
        mHandler.sendEmptyMessage(0);
    }

    public void refeshName() {
        String str = sp.getString(SPConsts.STAND_TARGET_NAME, "TargetName000");
        String tips = sp.getString(SPConsts.STAND_TARGET_TIPS, "TargetTips");
        NumEndedString name = NumEndedString.create(str);
        name.numIncreases();
        mNameStandTest.setVisibility(View.VISIBLE);
        mNameStandTest.setText(name.toString());
        if (!StringUtils.isEmpty(tips)) {
            mTipsStandTest.setVisibility(View.VISIBLE);
            mTipsStandTest.setText(tips);
        } else {
            mTipsStandTest.setVisibility(View.INVISIBLE);
        }
    }
    public void showNotice(boolean b){
        mTvTimeNoticestandTest.setVisibility(b ? View.VISIBLE : View.GONE);
    }

    public void refeshNotice(int index, int times) {
        mTvTimeNoticestandTest.setText("平均(" + index + "/" + times + ")");
    }
}
