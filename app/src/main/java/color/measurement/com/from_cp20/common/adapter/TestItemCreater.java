package color.measurement.com.from_cp20.common.adapter;

import android.view.View;

/**
 * Created by wpc on 2017/4/20.
 */

public class TestItemCreater implements ShowAbleItem {
    String title;
    Integer color;
    Integer img_res_id;
    View mView;

    public TestItemCreater(String title, Integer color){
        this.title = title;
        this.color = color;
    }

    public TestItemCreater(Integer color, Integer img_res_id) {
        this.color = color;
        this.img_res_id = img_res_id;
    }

    public TestItemCreater(View view) {
        mView = view;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public Integer getBackgroundColor() {
        return color;
    }

    @Override
    public Integer getImageResId() {
        return img_res_id;
    }

    @Override
    public View getItemView() {
        return mView;
    }
}
