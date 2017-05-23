package color.measurement.com.from_cp20.module.information;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import color.measurement.com.from_cp20.R;

public class NewsDetiaslActivity extends AppCompatActivity {

    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.web_text) WebView mWebView;
    @BindView(R.id.ivImage) ImageView mImageView;
    @BindView(R.id.collapsing_toolbar) CollapsingToolbarLayout mCollapsingToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detiasl);
        ButterKnife.bind(this);
        mCollapsingToolbar.setTitle("新闻详情");
        mCollapsingToolbar.setExpandedTitleColor(Color.GRAY);
        mCollapsingToolbar.setCollapsedTitleTextColor(Color.WHITE);

        mToolbar.setTitle("新闻详情");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Bundle bundle = this.getIntent().getExtras();
        final ArrayList<String> data = bundle.getStringArrayList("data");
        Log.i("url", data.get(0));

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        mWebView.loadUrl(data.get(1));

        Glide.with(this).
                load(data.get(0)).
                error(R.drawable.ic_extension_white_24dp).
                fitCenter().
                into(mImageView);
    }
}
