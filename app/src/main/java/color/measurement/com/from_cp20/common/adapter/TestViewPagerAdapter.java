package color.measurement.com.from_cp20.common.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;

/**
 * Created by wpc on 2017/4/20.
 */

public class TestViewPagerAdapter extends PagerAdapter {

    Context mContext;
    ArrayList<ShowAbleItem> datas;

    //ArrayList<View> views;
    public TestViewPagerAdapter(Context context, ArrayList<ShowAbleItem> datas) {
        this.datas = datas;
        mContext = context;
//        views=new ArrayList<>();
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View v = null;
        ShowAbleItem dats = datas.get(position);
        if (dats.getBackgroundColor() != null) {
            v = new View(mContext);
            v.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            v.setBackgroundColor(dats.getBackgroundColor());
        } else if (dats.getImageResId() != null) {
            v = new ImageView(mContext);
            v.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            ((ImageView) v).setImageResource(dats.getImageResId());
            v.setBackgroundColor(dats.getBackgroundColor());
        } else if (dats.getItemView() != null) {
            v = dats.getItemView();
        }
        container.addView(v);
        return v;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return datas.get(position).getTitle();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
