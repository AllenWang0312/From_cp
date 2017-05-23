package color.measurement.com.from_cp20.common.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by wpc on 2017/4/19.
 */

public class ViewPagerIndicater extends View {


    public ViewPagerIndicater(Context context) {
        this(context, null);
    }

    public ViewPagerIndicater(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ViewPagerIndicater(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

//        this.setOrientation(LinearLayout.HORIZONTAL);
//        this.setGravity(Gravity.CENTER);

    }

    public void setUpWithViewPager(ViewPager viewPager) {
        PagerAdapter pagerAdapter = viewPager.getAdapter();
        item_count = pagerAdapter.getCount();

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                select_index = position;
                progress = positionOffset;
                invalidate();
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        Log.i("viewpagerindicater init",item_count+"_");
    }

    int width, height;
    int cent_x, cent_y;

    int item_count;
    int select_index;
    float progress;

    int child_margin = 100;//px



    @Override
    protected void onDraw(Canvas canvas) {
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);

        width = canvas.getWidth();
        height = canvas.getHeight();
        cent_x = width / 2;
        cent_y = height / 2;

        p.setColor(Color.WHITE);

        for (int i = 0; i <= item_count - 1; i++) {
            canvas.drawCircle((int) (cent_x - ((item_count - 1) / 2.0f-i) * child_margin), cent_y,10, p);
        }

        p.setColor(Color.GRAY);
        canvas.drawCircle(cent_x - (((item_count - 1) / 2.0f) - ((select_index + progress-1) * child_margin)), cent_y,10, p);
        super.onDraw(canvas);
    }
}
