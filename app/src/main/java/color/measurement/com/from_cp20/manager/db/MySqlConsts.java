package color.measurement.com.from_cp20.manager.db;

/**
 * Created by wpc on 2017/5/9.
 */

public class MySqlConsts {

    public static final String DB_URL = "jdbc:mysql://rdsz4kl5za13u8d0m3i8spublic.mysql.rds.aliyuncs.com:3306/chnspec";
    public static final String DB_name = "spec3205722251";
    public static final String DB_pswd = "spec3205722251";

    public static final String user_table = "table_user";
    public static final String ins_table = "table_ins";
    public static final String hro_adv = "hro_adv_info";
    public static final String splash_adv = "app_splash_adv_info";


    public static final String create_user_table = "CREATE TABLE IF NOT EXISTS %s (ID INT(4)NOT NULL UNIQUE AUTO_INCREMENT ," +
            "login_way VARCHAR(20),tel VARCHAR(20),name VARCHAR(20),password VARCHAR(20)," +
            "qq_token VARCHAR(20),qq_openid VARCHAR(20),weixin_token VARCHAR(20)," +
            "weixin_openid VARCHAR(20),weibo_token VARCHAR(20),weibo_openid VARCHAR(20)," +
            "age VARCHAR(20),birthday VARCHAR(20),address VARCHAR(20)" +
            ",sign VARCHAR(20),business VARCHAR(20),config VARCHAR(300))";


    public static final String MySql_light_color = "create table if not exists %s("
            + "id int(4) not null primary key auto_increment,"
            + "stand_name char(20) not null,"
            + "name char(20),"
            + "tips char(20),"
            + "isStandard int(4),"
            + "date char(20),"
            + "time char(20),"
            + "moment bigint(8) unique,"
            + "result char(20),"
            + "SCI1 double(16,4)," + "SCI2 double(16,4)," + "SCI3 double(16,4)," + "SCI4 double(16,4)," + "SCI5 double(16,4)," + "SCI6 double(16,4)," + "SCI7 double(16,4)," + "SCI8 double(16,4)," + "SCI9 double(16,4)," + "SCI10 double(16,4),"
            + "SCI11 double(16,4)," + "SCI12 double(16,4)," + "SCI13 double(16,4)," + "SCI14 double(16,4)," + "SCI15 double(16,4)," + "SCI16 double(16,4)," + "SCI17 double(16,4)," + "SCI18 double(16,4)," + "SCI19 double(16,4)," + "SCI20 double(16,4),"
            + "SCI21 double(16,4)," + "SCI22 double(16,4)," + "SCI23 double(16,4)," + "SCI24 double(16,4)," + "SCI25 double(16,4)," + "SCI26 double(16,4)," + "SCI27 double(16,4)," + "SCI28 double(16,4)," + "SCI29 double(16,4)," + "SCI30 double(16,4),"
            + "SCI31 double(16,4),"
            + "light int(4),"
            + "angle int(4),"
            + "test_mod int(4)" +
            ")";
    public static final String MySql_lustre = "create table if not exists %s("
            + "id int(4) not null primary key auto_increment,"
            + "stand_name char(20) not null,"
            + "name char(20),"
            + "tips char(20),"
            + "isStandard int(4),"
            + "date char(20),"
            + "time char(20),"
            + "moment bigint(8) unique,"
            + "result char(20),"
            + "gzd_20 double(16,4),"
            + "gzd_60 double(16,4),"
            + "gzd_80 double(16,4)"
            + ")";

}
