package color.measurement.com.from_cp20.base;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.PersistableBundle;

/**
 * Created by wpc on 2017/2/27.
 */

public class ProgressDialogActivity extends BaseActivity {
    protected ProgressDialog dialog;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    public void showProgressDialog() {
        if (dialog == null) {
            dialog = new ProgressDialog(ProgressDialogActivity.this);
        }
        dialog.setCancelable(false);
        dialog.setTitle("请稍后");
        dialog.setMessage("载入中");
        dialog.show();
    }

    public void showProgressDialog(String title, String meg) {
        if (dialog == null) {
            dialog = new ProgressDialog(ProgressDialogActivity.this);
        }
        dialog.setCancelable(false);
        dialog.setTitle(title);
        dialog.setMessage(meg);
        dialog.show();
    }

    public void dismissProgressDialog() {
        if (dialog.isShowing()) {
            dialog.dismiss();
            return;
        }
//        T.showDeBug(this, "dismiss an unShowing dialog");
    }
}
