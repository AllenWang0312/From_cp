package color.measurement.com.from_cp20.module.been.interfaze;

import android.content.Context;
import android.database.Cursor;

import java.util.HashMap;

/**
 * Created by wpc on 2017/4/27.
 */

public abstract class CompareableData implements Data {
    private boolean hasSaved;

    int id; //当前表中的id;
    protected String stand_name;
    protected String name;
    protected String tips;
    protected String date;
    protected String time;
    protected long moment;
    protected boolean isStandard;//是否是标样
    protected String result;//-1 标样没有结果  0 不合格  1 合格

    protected CompareableData() {
    }


    public void setResult(Boolean bol) {
        this.result = bol == null ? "" : bol ? "合格" : "不合格";
    }
    public void setName(String name) {
        this.name = name;
        if (isStandard) {
            stand_name = name;
        }
    }

    public CompareableData (Cursor c){
        this.stand_name = c.getString(c.getColumnIndex("stand_name"));
        this.id = c.getInt(c.getColumnIndex("id"));
        this.name = c.getString(c.getColumnIndex("name"));
        this.tips = c.getString(c.getColumnIndex("tips"));
        this.isStandard = c.getInt(c.getColumnIndex("isStandard")) == 1;
        this.date = c.getString(c.getColumnIndex("date"));
        this.time = c.getString(c.getColumnIndex("time"));
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStand_name() {
        return stand_name;
    }

    public void setStand_name(String stand_name) {
        this.stand_name = stand_name;
    }

    public String getName() {
        return name;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public long getMoment() {
        return moment;
    }

    public void setMoment(long moment) {
        this.moment = moment;
    }

    public boolean isStandard() {
        return isStandard;
    }

    public void setStandard(boolean standard) {
        isStandard = standard;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public boolean isHasSaved() {
        return hasSaved;
    }

    public void setHasSaved(boolean hasSaved) {
        this.hasSaved = hasSaved;
    }

    public abstract HashMap<String, Object> toHashMap(Context context);
}
