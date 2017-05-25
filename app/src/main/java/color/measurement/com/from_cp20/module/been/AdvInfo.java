package color.measurement.com.from_cp20.module.been;

import android.content.ContentValues;
import android.content.Context;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import color.measurement.com.from_cp20.module.been.interfaze.Data;

/**
 * Created by wpc on 2017/5/14.
 */

public class AdvInfo implements Data {
    int id;
    String title;
    String img_url;
    String click_url;

    public AdvInfo(String img_url, String click_url) {
        this.img_url = img_url;
        this.click_url = click_url;
    }
    public  AdvInfo (ResultSet set){
        try {
            this.id=set.getInt("id");
            this.title=set.getString("title");
            this.img_url=set.getString("url");
            this.click_url=set.getString("click_url");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getClick_url() {
        return click_url;
    }

    public void setClick_url(String click_url) {
        this.click_url = click_url;
    }

    @Override
    public void setServiceId(int id) {

    }

    @Override
    public ContentValues getContentValue() {
        return null;
    }

    @Override
    public HashMap<String, Object> toHashMapForMySql(Context context) {
        return null;
    }
}
