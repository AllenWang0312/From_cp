package color.measurement.com.from_cp20.module.other;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.orhanobut.logger.Logger;

import color.measurement.com.from_cp20.R;
import color.measurement.com.from_cp20.base.BaseActivity;
import color.measurement.com.from_cp20.module.main.MainActivity;

/**
 * Created by wpc on 2017/3/24.
 */

public class WebViewActivity extends BaseActivity {
    String uri, onFinish;

    WebView mWebView;
    ProgressBar mPbWebview;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("webview", "oncreate");

        Intent i = getIntent();
        uri = i.getStringExtra("url");
        onFinish = i.getStringExtra("onFinish");
        Logger.i(uri + "-" + onFinish);
        Log.i("intent", i.toString());

        setContentView(R.layout.activity_webview);
        mPbWebview = (ProgressBar) findViewById(R.id.pb_webview);
        mWebView = (WebView) findViewById(R.id.webview_webview);


        mPbWebview.setMax(100);


        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                //设置加载进度条的进度
                if (newProgress == 100) {
                    mPbWebview.setVisibility(View.GONE);
                } else {
                    mPbWebview.setVisibility(View.VISIBLE);
                    mPbWebview.setProgress(newProgress);
                }
            }
        });
        mWebView.loadUrl(uri);
    }

    @Override
    public void onBackPressed() {
        //finish();
        //判断是否有历史记录,如果有就返回上一个网页
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            if (onFinish!=null&&onFinish.equals("MainActivity")) {
                this.finish();
                startActivity(new Intent(WebViewActivity.this, MainActivity.class));
            }
            //没有就作为正常的返回按钮返回上一个活动
            super.onBackPressed();
        }
    }
}
