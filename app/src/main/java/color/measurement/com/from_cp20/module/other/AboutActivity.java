package color.measurement.com.from_cp20.module.other;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import color.measurement.com.from_cp20.R;
import color.measurement.com.from_cp20.base.BaseActivity;

/**
 * Created by wpc on 2017/3/3.
 */

public class AboutActivity extends BaseActivity {

    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.ctbl_about) CollapsingToolbarLayout mCtblAbout;
    @BindView(R.id.recyc_about) RecyclerView mRecycAbout;

    private ArrayList<FAQ> getFAQs() {
        ArrayList<FAQ> faqs = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            faqs.add(new FAQ("Question: Why are you so diao", "Response: No why"));
        }
        return faqs;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);

        mToolbar.setTitle("关于");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        mCtblAbout.setExpandedTitleColor(Color.GRAY);
//        mCtblAbout.setExpandedTitleColor(Color.RED);

        mRecycAbout.setLayoutManager(new LinearLayoutManager(this));
        MyAdapter adapter = new MyAdapter(getFAQs());
        TextView textView = new TextView(this);
        textView.setTextSize(100);
        textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        textView.setText("FAQ");
        adapter.setHeadview(textView);
        View foot = LayoutInflater.from(this).inflate(R.layout.foot_company_info_about, null);
        adapter.setFootview(foot);
        mRecycAbout.setAdapter(adapter);

    }

    class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        View headview;
        View footview;
        ArrayList<FAQ> faqs;

        int count;

        public MyAdapter(ArrayList<FAQ> faqs) {
            this.faqs = faqs;
            count = faqs.size();
        }

        public void setHeadview(View headview) {
            this.headview = headview;
            count += 1;
        }

        public void setFootview(View footview) {
            this.footview = footview;
            count += 1;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            //recyclerview.xml 简单的textview布局
            switch (viewType) {
                case 0:
                    return new HeadHolder(headview);
                case 1:
                    View view = View.inflate(AboutActivity.this, R.layout.recyclerview_item, null);
                    return new ItemHolder(view);
                case 2:
                    return new FootHolder(footview);
                default:
                    break;
            }
            return null;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            switch (getItemViewType(position)) {
                case 0:
                    break;
                case 1:
                    if (headview != null) {
                        position -= 1;
                    }
                    ItemHolder h = (ItemHolder) holder;
                    FAQ f = faqs.get(position);
                    h.question.setText(f.getQuestion());
                    h.response.setText(f.getResponse());
                    break;
                case 2:
                    break;
                default:
                    break;
            }
        }

        @Override
        public int getItemCount() {
            return count;
        }

        @Override
        public int getItemViewType(int position) {
            if (headview != null) {
                if (footview != null) {
                    if (position == 0) return 0;
                    if (position == count - 1) return 2;
                    return 1;
                } else {
                    if (position == 0) return 0;
                    return 1;
                }
            } else {
                if (footview != null) {
                    if (position == count - 1) return 2;
                    return 1;
                } else {
                    return 1;
                }
            }
        }
    }

    class HeadHolder extends RecyclerView.ViewHolder {

        public HeadHolder(View itemView) {
            super(itemView);
        }
    }

    class ItemHolder extends RecyclerView.ViewHolder {
        TextView question, response;

        public ItemHolder(View itemView) {
            super(itemView);
        }
    }

    class FootHolder extends RecyclerView.ViewHolder {

        public FootHolder(View itemView) {
            super(itemView);
        }
    }

}
