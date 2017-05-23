package color.measurement.com.from_cp20.module.measure.lustre;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import color.measurement.com.from_cp20.R;
import color.measurement.com.from_cp20.common.util.StringFormat;
import color.measurement.com.from_cp20.manager.NumEndedString;
import color.measurement.com.from_cp20.manager.res.ResConsts;
import color.measurement.com.from_cp20.manager.res.ResHelper;
import color.measurement.com.from_cp20.manager.sp.SPConsts;
import color.measurement.com.from_cp20.module.been.data.LustreData;
import color.measurement.com.from_cp20.module.been.wapper.GroupData;
import color.measurement.com.from_cp20.module.measure.StandDisplayFragment;
import color.measurement.com.from_cp20.util.blankj.StringUtils;

/**
 * Created by wpc on 2017/5/7.
 */
@SuppressLint("ValidFragment")
public class LustreStandTestFrag extends StandDisplayFragment implements Observer {
    GroupData<LustreData> group;
    private ArrayList<String> titles;

    public LustreStandTestFrag(SharedPreferences sp) {
        super(sp);
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
//                dismissViewIfNeed();
                initTitle();
                setResult(group.getStand());
                refeshName();

            }
        }
    };


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        initTitle();
        refeshName();
        return v;
    }

    void initTitle() {
        titles = ResHelper.getChecked_titles(mContext,sp, R.array.gzd_angles, ResConsts.lustre_title);
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

    void setResult(@Nullable LustreData lustre) {
        mLlContentStandTest.removeAllViews();
        if (lustre != null) {
            HashMap<String, Object> results = lustre.toHashMap(mContext);
            for (String str : titles) {
                TextView result = new TextView(mContext);
                result.setText(StringFormat.Object(results.get(str)));
                result.setTextSize(36);
                result.setGravity(Gravity.CENTER_HORIZONTAL);
                result.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                mLlContentStandTest.addView(result);
            }
        }
//        dismissViewIfNeed();
//        if (mFslTableStand.getVisibility() == View.VISIBLE) {
//            DataProcesser.refeshFSL(lustre == null ? null : lustre.getSCI(), null, mFslTableStand);
//        }

    }

//    private void dismissViewIfNeed() {
//        int stand_views_mod = sp.getInt(SPConsts.STAND_DATA_MOD, 0);
////        T.showDeBug(mContext,stand_views_mod+"");
//        if (stand_views_mod == 1) {
//            mFslTableStand.setVisibility(View.VISIBLE);
//        } else {
//            mFslTableStand.setVisibility(View.GONE);
//        }
//    }


    public void refeshName() {
            String str = sp.getString(SPConsts.STAND_TARGET_NAME, "TargetName000");
            String tips = sp.getString(SPConsts.STAND_TARGET_TIPS, "");
            NumEndedString name = NumEndedString.create(str);
            name.numIncreases();
            mNameStandTest.setText(name.toString());
            if (!StringUtils.isEmpty(tips)) {
                mTipsStandTest.setText(tips);
            } else {
                mTipsStandTest.setVisibility(View.GONE);
            }
    }

    public void refeshNotice(boolean b, int index, int times) {
        mTvTimeNoticestandTest.setVisibility(b ? View.VISIBLE : View.GONE);
        mTvTimeNoticestandTest.setText("平均(" + index + "/" + times + ")");
    }

    @Override
    public void update(Observable o, Object arg) {
        group = (GroupData<LustreData>) o;
        mHandler.sendEmptyMessage(0);
    }
}
