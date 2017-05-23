package color.measurement.com.from_cp20.module.measure;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import color.measurement.com.from_cp20.R;
import color.measurement.com.from_cp20.widget.FSL.FSL_table;

/**
 * Created by wpc on 2017/5/7.
 */
@SuppressLint("ValidFragment")
public abstract class StandDisplayFragment extends Fragment{
    protected SharedPreferences sp;
    public StandDisplayFragment(SharedPreferences sp){
        this.sp=sp;
    }
    protected  Context mContext;

    @BindView(R.id.fsl_table_stand)protected FSL_table mFslTableStand;


    @BindView(R.id.ll_title_stand_test)protected LinearLayout mLlTitleStandTest;
    @BindView(R.id.ll_content_stand_test)protected LinearLayout mLlContentStandTest;

    @BindView(R.id.name_stand_test)protected TextView mNameStandTest;
    @BindView(R.id.tips_stand_test)protected TextView mTipsStandTest;
    @BindView(R.id.tv_time_noticestand_test)protected TextView mTvTimeNoticestandTest;
    protected  Unbinder unbinder;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getActivity();
        View v = inflater.inflate(R.layout.fragment_stand_test, container, false);
        unbinder = ButterKnife.bind(this, v);

        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
