package color.measurement.com.from_cp20.module.measure;//package color.measurement.com.from_cp20.module.measure.lightcolor.v2;//package color.measurement.com.from_cp20.module.measure.lightcolor.v2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import color.measurement.com.from_cp20.R;
import color.measurement.com.from_cp20.module.been.interfaze.CompareableData;
import color.measurement.com.from_cp20.widget.FSL.FSL_table;
import color.measurement.com.from_cp20.widget.LAB.Light_table;
import color.measurement.com.from_cp20.widget.LAB.SC_table;

/**
 * Created by wpc on 2017/3/31.
 */
@SuppressLint("ValidFragment")
public abstract class GroupDisplayFragment extends Fragment {

    protected Context mContext;

    @BindView(R.id.ll_titles_test)protected LinearLayout mLlTitlesTest;
    @BindView(R.id.ll_stand_test)protected LinearLayout mLlStandTest;
    @BindView(R.id.ll_dev_test)protected LinearLayout mLlDevTest;
    @BindView(R.id.recyc_test)protected RecyclerView mRecycTest;

    @BindView(R.id.ll_lab_table) protected LinearLayout mLlLabTable;

    @BindView(R.id.v_light_sc) protected Light_table mVLightSc;
    @BindView(R.id.v_sc_sc) protected SC_table mVScSc;

    @BindView(R.id.fsl_table)protected FSL_table mFslTable;

    Unbinder unbinder;
    protected SharedPreferences sp;

    public GroupDisplayFragment(SharedPreferences sp) {
        this.sp = sp;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getActivity();
        View     v = inflater.inflate(R.layout.fragment_test, container, false);
        unbinder = ButterKnife.bind(this, v);
        initTitles();
        return v;
    }

    protected abstract void initDev(CompareableData stand,CompareableData test);

    protected abstract void initTests();

    public abstract void initTitles();

    public abstract void refeshViews();

    protected abstract void initStand();



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
