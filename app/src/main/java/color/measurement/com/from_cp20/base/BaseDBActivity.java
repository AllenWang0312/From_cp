package color.measurement.com.from_cp20.base;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by wpc on 2017/4/27.
 */

public abstract class BaseDBActivity extends ProgressDialogActivity {
    protected Context mContext;
    protected SQLiteDatabase db;
    public abstract void initDataBase();
}
