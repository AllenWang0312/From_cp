package color.measurement.com.from_cp20.module.Test;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import color.measurement.com.from_cp20.R;
import color.measurement.com.from_cp20.base.BaseActivity;
import color.measurement.com.from_cp20.util.utils.T;

import static android.widget.RadioGroup.OnClickListener;

/**
 * Created by wpc on 2017/3/24.
 */

public class TestActivity extends BaseActivity {


    RadioGroup rg;
    private OnClickListener mListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            T.showError(TestActivity.this, ((RadioButton) v).getText() + "");
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        float den = this.getResources().getDisplayMetrics().density;

        rg = (RadioGroup) findViewById(R.id.rg_test);
        for (int i = 0; i < 10; i++) {
            RadioButton rb = new RadioButton(this);
            rb.setOnClickListener(mListener);
            rb.setText(i + "");
//            rb.setBackground(null);
//            rb.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.radio_bg_selecter));
            rb.setLayoutParams(new LinearLayout.LayoutParams((int) (100 * den), (int) (60 * den)));
            rg.addView(rb);
        }
    }

}
