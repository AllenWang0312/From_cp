package color.measurement.com.from_cp20.module.database.lightcolor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import color.measurement.com.from_cp20.R;
import color.measurement.com.from_cp20.common.widget.table.TableFragment;
import color.measurement.com.from_cp20.manager.db.DBHelper;
import color.measurement.com.from_cp20.manager.res.ResConsts;
import color.measurement.com.from_cp20.manager.res.ResHelper;
import color.measurement.com.from_cp20.module.been.interfaze.CompareableData;
import color.measurement.com.from_cp20.module.been.wapper.GroupData;

//import color.measurement.com.from_cp20.common.widget.TableFragment;

/**
 * Created by wpc on 2017/5/3.
 */
@SuppressLint("ValidFragment")
public class StandTableFragment extends TableFragment {

    Context mContext;
    //    ArrayList<String> table_names;
    String select_table_name;
    ArrayList<String> titles;
    ArrayList<GroupData<CompareableData>> groups;
    ArrayList<CompareableData> stands;

    SQLiteDatabase db;
    SharedPreferences sp;

    public StandTableFragment(SQLiteDatabase db, SharedPreferences sharedPreferences, String table_name) {
        this.db = db;
        this.sp = sharedPreferences;
        select_table_name = table_name;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
//        table_names = DBHelper.getTableNames(db, 0);
//        select_table_name = table_names.get(0);
        groups = DBHelper.getGroups(db, select_table_name);
//        stands = DBHelper.getStandData(db, select_table_name);
        titles = ResHelper.getChecked_titles(mContext, sp, R.array.db_base_data, ResConsts.db_titles);
        stands = getStands(groups);
        setNames(getNames(stands));
        setContent(titles, stands);
        //        setCheckBoxCheckedListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//
//            }
//        });
        return v;
    }

    private ArrayList<CompareableData> getStands(ArrayList<GroupData<CompareableData>> groups) {
        ArrayList<CompareableData> stands = new ArrayList<>();
        for (GroupData<CompareableData> group : groups) {
            stands.add(group.getStand());
        }
        return stands;
    }

    private ArrayList<String> getNames(ArrayList<CompareableData> stands) {
        ArrayList<String> names = new ArrayList<>();
        for (CompareableData lc : stands) {
            names.add(lc.getStand_name());
        }
        return names;
    }
}
