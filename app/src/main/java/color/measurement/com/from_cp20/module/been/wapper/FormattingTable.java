package color.measurement.com.from_cp20.module.been.wapper;

import java.util.ArrayList;
import java.util.HashMap;

import color.measurement.com.from_cp20.module.been.interfaze.CompareableData;

/**
 * Created by wpc on 2017/4/27.
 */

public  class FormattingTable<T extends CompareableData> {
    ArrayList<String> keys;
    HashMap<String, GroupData<T>> datas;

    public FormattingTable(ArrayList<String> keys, HashMap<String, GroupData<T>> datas) {
        this.keys = keys;
        this.datas = datas;
    }

    public ArrayList<String> getKeys() {
        return keys;
    }

    public void setKeys(ArrayList<String> keys) {
        this.keys = keys;
    }

    public HashMap<String, GroupData<T>> getDatas() {
        return datas;
    }

    public void setDatas(HashMap<String, GroupData<T>> datas) {
        this.datas = datas;
    }
}