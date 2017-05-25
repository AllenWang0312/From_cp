package color.measurement.com.from_cp20.module.been;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.HashMap;

import color.measurement.com.from_cp20.module.been.interfaze.Data;

/**
 * Created by wpc on 2017/4/1.
 */

public class User implements Data {


    Integer service_id=-1;
    boolean is_login =false;
    int login_way, age;
    String tel, name = "未登录", password, portrait_url, birthday, address, sign, business;
    String qq_token, qq_openid, weixin_token, weixin_openid, weibo_token, weibo_openid;
    Ins connectIns;
    String tel_id;
    Long last_log_time;
    public User() {

    }
    public User(String nickname, String headAddress, int way) {
        this.name = nickname;
        portrait_url = headAddress;
        login_way = way;
    }
    public User(Integer service_id, String name, String portrait_url, int login_way) {
        this(name, portrait_url, login_way);
        this.portrait_url = portrait_url;
    }

    public Long getLast_log_time() {
        return last_log_time;
    }

    public void setLast_log_time(Long last_log_time) {
        this.last_log_time = last_log_time;
    }

    public User(Cursor c){
        this.service_id=c.getInt(c.getColumnIndex("service_id"));
        this.name=c.getString(c.getColumnIndex("name"));
        this.portrait_url=c.getString(c.getColumnIndex("portrait"));
        this.password=c.getString(c.getColumnIndex("password"));
        this.last_log_time=c.getLong(c.getColumnIndex("last_log_time"))  ;
    }
//    public User(String name, String password, @Nullable String portrait_url, boolean is_login) {
//        this.name = name;
//        if (portrait_url != null) {
//            this.portrait_url = portrait_url;
//        }
//        this.password = password;
//        this.is_login = is_login;
//    }

    @Override
    public ContentValues getContentValue() {
        ContentValues cv = new ContentValues();
        cv.put("service_id", service_id);
        cv.put("name", getName());
        cv.put("password", getPassword());
        cv.put("portrait", portrait_url);
        cv.put("last_log_time",last_log_time);
        return cv;
    }

    @Override
    public HashMap<String, Object> toHashMapForMySql(Context context) {
        HashMap<String, Object> values = new HashMap<>();
        values.put("login_way", login_way);
        values.put("tel", tel);
        values.put("name", name);
        values.put("password", password);
        values.put("qq_token", qq_token);
        values.put("qq_openid", qq_openid);
        values.put("weixin_token", weixin_token);
        values.put("weixin_openid", weixin_openid);
        values.put("weibo_token", weibo_token);
        values.put("weibo_openid", weibo_openid);
        values.put("age", age);
        values.put("birthday", birthday);
        values.put("address", address);
        values.put("sign", sign);
        values.put("business", business);

        values.put("tel_id", tel_id);

        values.put("has_login", is_login);
        return values;
    }

    public void saveToSQLite(SQLiteDatabase db, String table_name) {
        db.insert(table_name, null, getContentValue());
    }

    @Override
    public void setServiceId(int id) {
        this.service_id = id;
    }

    public User(Builder builder) {
        this.service_id = builder.service_id;
        this.is_login = builder.is_login;
        this.login_way = builder.login_way;
        this.age = builder.age;
        this.tel = builder.tel;
        this.name = builder.name;
        this.password = builder.password;
        this.portrait_url = builder.portrait_url;
        this.birthday = builder.birthday;
        this.address = builder.address;
        this.sign = builder.sign;
        this.business = builder.business;
        this.qq_token = builder.qq_token;
        this.qq_openid = builder.qq_openid;
        this.weixin_token = builder.weixin_token;
        this.weixin_openid = builder.weixin_openid;
        this.weibo_token = builder.weibo_token;
        this.weibo_openid = builder.weibo_openid;
    }

    public static class Builder {
        Integer service_id;
        boolean is_login;
        int login_way, age;
        String tel, name = "未登录", password, portrait_url, birthday, address, sign, business, config;
        String qq_token, qq_openid, weixin_token, weixin_openid, weibo_token, weibo_openid;

        public Builder setService_id(Integer service_id) {
            this.service_id = service_id;
            return this;
        }

        public Builder setIs_login(boolean is_login) {
            this.is_login = is_login;
            return this;
        }

        public Builder setLogin_way(int login_way) {
            this.login_way = login_way;
            return this;
        }

        public Builder setAge(int age) {
            this.age = age;
            return this;
        }

        public Builder setTel(String tel) {
            this.tel = tel;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setPassword(String password) {
            this.password = password;
            return this;
        }

        public Builder setPortrait_url(String portrait_url) {
            this.portrait_url = portrait_url;
            return this;
        }

        public Builder setBirthday(String birthday) {
            this.birthday = birthday;
            return this;
        }

        public Builder setAddress(String address) {
            this.address = address;
            return this;
        }

        public Builder setSign(String sign) {
            this.sign = sign;
            return this;
        }

        public Builder setBusiness(String business) {
            this.business = business;
            return this;
        }

        public Builder setConfig(String config) {
            this.config = config;
            return this;
        }

        public Builder setQq_token(String qq_token) {
            this.qq_token = qq_token;
            return this;
        }

        public Builder setQq_openid(String qq_openid) {
            this.qq_openid = qq_openid;
            return this;
        }

        public Builder setWeixin_token(String weixin_token) {
            this.weixin_token = weixin_token;
            return this;
        }

        public Builder setWeixin_openid(String weixin_openid) {
            this.weixin_openid = weixin_openid;
            return this;
        }

        public Builder setWeibo_token(String weibo_token) {
            this.weibo_token = weibo_token;
            return this;
        }

        public Builder setWeibo_openid(String weibo_openid) {
            this.weibo_openid = weibo_openid;
            return this;
        }

        public User create() {
            return new User(this);
        }
    }

    public boolean is_login() {
        return is_login;
    }

    public void setIs_login(boolean is_login) {
        this.is_login = is_login;
    }

    public String getPortrait_url() {
        return portrait_url;
    }

    public void setPortrait_url(String portrait_url) {
        this.portrait_url = portrait_url;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        if (password != null) {
            return password;
        }
        return "null";
    }

    public Integer getService_id() {
        return service_id;
    }

    public void setService_id(Integer service_id) {
        this.service_id = service_id;
    }

    public int getLogin_way() {
        return login_way;
    }

    public void setLogin_way(int login_way) {
        this.login_way = login_way;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getBusiness() {
        return business;
    }

    public void setBusiness(String business) {
        this.business = business;
    }

    public String getQq_token() {
        return qq_token;
    }

    public void setQq_token(String qq_token) {
        this.qq_token = qq_token;
    }

    public String getQq_openid() {
        return qq_openid;
    }

    public void setQq_openid(String qq_openid) {
        this.qq_openid = qq_openid;
    }

    public String getWeixin_token() {
        return weixin_token;
    }

    public void setWeixin_token(String weixin_token) {
        this.weixin_token = weixin_token;
    }

    public String getWeixin_openid() {
        return weixin_openid;
    }

    public void setWeixin_openid(String weixin_openid) {
        this.weixin_openid = weixin_openid;
    }

    public String getWeibo_token() {
        return weibo_token;
    }

    public void setWeibo_token(String weibo_token) {
        this.weibo_token = weibo_token;
    }

    public String getWeibo_openid() {
        return weibo_openid;
    }

    public void setWeibo_openid(String weibo_openid) {
        this.weibo_openid = weibo_openid;
    }

    public String getTel_id() {
        return tel_id;
    }

    public void setTel_id(String tel_id) {
        this.tel_id = tel_id;
    }

    public Ins getConnectIns() {
        return connectIns;
    }

    public void setConnectIns(Ins connectIns) {
        this.connectIns = connectIns;
    }

}
