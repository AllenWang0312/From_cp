package color.measurement.com.from_cp20.module.information;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import color.measurement.com.from_cp20.R;
import color.measurement.com.from_cp20.module.information.javaBean.NewslistBean;
import color.measurement.com.from_cp20.common.backgroundcreater.RandomBgCreater;

/**
 * Created by wpc on 2017/2/16.
 */

public class NewsAdapter extends RecyclerArrayAdapter<NewslistBean> {

    Context mContext;
    RandomBgCreater mRandomBgCreater;

    public NewsAdapter(Context context, RandomBgCreater randomBgCreater) {
        super(context);
        mContext = context;
        mRandomBgCreater = randomBgCreater;
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new NewsViewHolder(parent);
    }

    public class NewsViewHolder extends BaseViewHolder<NewslistBean> {
        private TextView mTv_name;
        private ImageView mImg_face;
        private TextView mTv_sign;
        private TextView mDescription;
        private CardView mCardView;

        public NewsViewHolder(ViewGroup parent) {
            super(parent, R.layout.news_recycler_item);
            mTv_name = $(R.id.person_name);
            mTv_sign = $(R.id.person_sign);
            mImg_face = $(R.id.person_face);
            mDescription = $(R.id.news_description);
            mCardView = $(R.id.cv_homepage);

        }

        @Override
        public void setData(final NewslistBean data) {
            mTv_name.setText(data.getTitle());
            mTv_sign.setText(data.getCtime());
            mDescription.setText(data.getDescription());
            mCardView.setBackground(mRandomBgCreater.getBackground(0, 0.3f));
            Glide.with(mContext)
                    .load(data.getPicUrl())
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.drawable.ic_arrow_upward_white_24dp)

                    .centerCrop()
                    .into(mImg_face);
        }


    }
}
