package color.measurement.com.from_cp20.common.interfaze;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by wpc on 2017/5/19.
 */

public class CheckPermissionPresenterImp implements Permissionable {
    @Override
    public void checkPermissions(Activity context, String... permissions) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(context,
                Manifest.permission.BLUETOOTH_ADMIN)) {
            // Show an expanation to the user *asynchronously* -- don't block
            // this thread waiting for the user's response! After the user
            // sees the explanation, try again to request the permission.

        } else {
            // No explanation needed, we can request the permission.
            ActivityCompat.requestPermissions(context,
                    new String[]{Manifest.permission.READ_CONTACTS},
                    1);
            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
            // app-defined int constant. The callback method gets the
            // result of the request.
        }
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(context,
                        new String[]{Manifest.permission.BLUETOOTH_ADMIN},
                        1);
            }

    }
}
