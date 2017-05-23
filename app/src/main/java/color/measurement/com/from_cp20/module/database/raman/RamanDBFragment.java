package color.measurement.com.from_cp20.module.database.raman;//package color.measurement.com.from_cp20.module.measure.raman;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import color.measurement.com.from_cp20.R;
import color.measurement.com.from_cp20.base.BaseFragment;

/**
 * Created by wpc on 2017/2/27.
 */
@SuppressLint("ValidFragment")
public class RamanDBFragment extends BaseFragment {

    @BindView(R.id.rcyc_raman_db_left) RecyclerView mRcycRamanDbLeft;
    @BindView(R.id.fl_raman_db_right) FrameLayout mFlRamanDbRight;

    ArrayList<String> titles;
    int selectIndex = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_ramanm, container, false);
        ButterKnife.bind(this, v);
        initTitles();


        mRcycRamanDbLeft.setLayoutManager(new LinearLayoutManager(getActivity()));

//        mFlRamanDbRight

        return v;
    }

    private void initTitles() {
        titles = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            titles.add("title" + i);
        }
    }
}
