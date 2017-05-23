package color.measurement.com.from_cp20.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * Created by wpc on 2017/3/3.
 */

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.i("Life cyc onCreate",this.getLocalClassName());
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        Log.i("Life cyc onResume",this.getLocalClassName());
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.i("Life cyc onPause",this.getLocalClassName());
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        Log.i("Life cyc onDestroy",this.getLocalClassName());
        super.onDestroy();
    }
}
