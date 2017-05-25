package color.measurement.com.from_cp20.module.main;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import color.measurement.com.from_cp20.R;
import color.measurement.com.from_cp20.manager.ins.Instrument;
import color.measurement.com.from_cp20.module.been.Ins;

/**
 * Created by wpc on 2017/3/14.
 */

public class MainMiniRecycAdapter extends BaseQuickAdapter<Ins> {

    int[] resId = {R.drawable.lightcolor_ins, R.drawable.lustre_ins, R.drawable.chromatism_ins, R.drawable.raman_ins,R.drawable.ic_menu_gallery};

    public MainMiniRecycAdapter(int layoutResId, List<Ins> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Ins item) {

        helper.setText(R.id.tv_mini_item_main,
                Instrument.getTypeStringWithType(item.getType())
//                "分光测色仪"
        );
        helper.setImageResource(R.id.iv_mini_item_main, resId[item.getType()]);
    }
}
