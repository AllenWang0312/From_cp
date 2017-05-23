package color.measurement.com.from_cp20.module.other;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import color.measurement.com.from_cp20.R;
import color.measurement.com.from_cp20.module.main.MainActivity;

/**
 * Created by wpc on 2017/2/27.
 */

public class AdvertiseActivity extends AppCompatActivity {

    @BindView(R.id.img_advertise) ImageView img;
    @BindView(R.id.skip_splash) TextView mTextView;

    int delayTime = 5 * 1000;

    private CountDownTimer timer = new CountDownTimer(delayTime, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            mTextView.setText("跳过广告（" + millisUntilFinished / 1000 + ")");
        }

        @Override
        public void onFinish() {
            finish();
            startActivity(new Intent(AdvertiseActivity.this, MainActivity.class));
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advertise);
        ButterKnife.bind(this);

        timer.start();
        mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                timer.cancel();
                startActivity(new Intent(AdvertiseActivity.this, MainActivity.class));
            }
        });
//        ImageLoaderProxy.getInstance().displayImage(R.drawable.advert_1,img,R.drawable.ic_menu_slideshow);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();    timer.cancel();
                Intent i = new Intent(AdvertiseActivity.this, WebViewActivity.class);
                i.putExtra("url", "http://www.hzcaipu.com/");
                i.putExtra("onFinish", "MainActivity");
                startActivity(i);

            }
        });

    }
}
