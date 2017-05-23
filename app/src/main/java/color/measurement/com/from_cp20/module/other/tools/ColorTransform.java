package color.measurement.com.from_cp20.module.other.tools;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import color.measurement.com.from_cp20.R;

/**
 * Created by wpc on 2017/4/5.
 */

@SuppressLint("ValidFragment")
public class ColorTransform extends Fragment {
    Context mContext;
    @BindView(R.id.sp_from_trans) Spinner mSpFromTrans;
    @BindView(R.id.sp_to_trans) Spinner mSpToTrans;
    @BindView(R.id.ll_left_transform) LinearLayout mLlLeftTransform;
    @BindView(R.id.ll_right_transform) LinearLayout mLlRightTransform;
    @BindView(R.id.sp_light_trans) Spinner mSpLightTrans;
    @BindView(R.id.sp_angle_trans) Spinner mSpAngleTrans;
    @BindView(R.id.bt_trans) Button mBtTrans;
    Unbinder unbinder;

    public ColorTransform() {

    }

    public ColorTransform(Context context) {
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_color_transform, container, false);
        unbinder = ButterKnife.bind(this, view);
        mSpFromTrans.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        inflater.inflate(R.menu.color_transform, menu);
//    }

}
