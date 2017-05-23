package color.measurement.com.from_cp20.module.other;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import color.measurement.com.from_cp20.R;
import color.measurement.com.from_cp20.common.adapter.ShowAbleItem;
import color.measurement.com.from_cp20.common.adapter.TestItemCreater;
import color.measurement.com.from_cp20.common.adapter.TestViewPagerAdapter;
import color.measurement.com.from_cp20.widget.ViewPagerIndicater;

import static android.support.v4.view.ViewPager.*;

public class GuideActivity extends AppCompatActivity {

    @BindView(R.id.vp_guide) ViewPager mVpGuide;
    @BindView(R.id.vpi_guide) ViewPagerIndicater mVpiGuide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        ButterKnife.bind(this);

        ArrayList<ShowAbleItem> resid = new ArrayList<>();
        resid.add(new TestItemCreater("first", Color.BLUE));
        resid.add(new TestItemCreater("second", Color.GREEN));
        resid.add(new TestItemCreater("third", Color.RED));

        mVpGuide.setAdapter(new TestViewPagerAdapter(this, resid));
        mVpiGuide.setUpWithViewPager(mVpGuide);

        mVpGuide.setOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 2) {
                    Intent i=new Intent(GuideActivity.this, RegisterActivity.class);
                    i.putExtra("from","guide");
                    startActivity(i);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

}
