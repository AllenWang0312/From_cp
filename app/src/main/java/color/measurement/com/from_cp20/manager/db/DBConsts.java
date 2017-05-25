package color.measurement.com.from_cp20.manager.db;


/**
 * Created by wpc on 2017/4/15.
 */

public class DBConsts {
    /**
     * DB数据结构
     * app-app.db
     * -仪器1[分光测色] table
     * -仪器2[分光测色] table
     * -仪器3[光泽度] table
     * -仪器4[色差] table
     */

    public static final String DATE_BASE = "app.db";
    public static final String USER_TAB_NAME = "user";
    public static final String INS_TAB_NAME = "ins";

    public static final String CREATE_USER_TABLE =
            "create table if not exists " + USER_TAB_NAME + "("
                    + "id integer primary key autoincrement,"
                    + "service_id int unique,"
                    + "name text,"
                    + "portrait text,"
                    + "password text,"
                    + "last_log_time bigint)";
    public static final String CREATE_INSTRUMENT_TABLE =
            "create table if not exists " + INS_TAB_NAME + "("
                    + "id integer primary key autoincrement,"
                    + "userId integer,"
                    + "type integer,"
                    + "bleName text,"
                    + "address text,"
                    + "deviceVersion integer,"
                    + "cacheAbsPath text,"
                    + "dataTableName text unique)";

    //name = username_address
    public static String createLightColorDataTableIfNotExist(String name) {
        return "create table if not exists " + name + "("
                + "id integer primary key autoincrement,"
                + "stand_name text,"
                + "name text,"
                + "tips text,"
                + "isStandard integer,"

                + "date text,"
                + "time text,"
                + "moment bigint unique,"

                + "result text,"

                + "SCI1 double," + "SCI2 double," + "SCI3 double," + "SCI4 double," + "SCI5 double," + "SCI6 double," + "SCI7 double," + "SCI8 double," + "SCI9 double," + "SCI10 double,"
                + "SCI11 double," + "SCI12 double," + "SCI13 double," + "SCI14 double," + "SCI15 double," + "SCI16 double," + "SCI17 double," + "SCI18 double," + "SCI19 double," + "SCI20 double,"
                + "SCI21 double," + "SCI22 double," + "SCI23 double," + "SCI24 double," + "SCI25 double," + "SCI26 double," + "SCI27 double," + "SCI28 double," + "SCI29 double," + "SCI30 double,"
                + "SCI31 double,"
//                + "SCI32 double," + "SCI33 double," + "SCI34 double," + "SCI35 double," + "SCI36 double,"
                + "light integer,"
                + "angle integer,"
                + "test_mod integer,"
                + "hasUpdata integer not null default 0" +
                ")";//1 i
    }

    public static String createLustreDataTableIfNotExist(String name) {
        return "create table if not exists " + name + "("
                + "id integer primary key autoincrement,"
                + "stand_name text,"
                + "name text,"
                + "tips text,"
                + "isStandard integer,"
                + "date text,"
                + "time text,"
                + "moment bigint unique,"
                + "result text,"
                + "gzd_20 double,"
                + "gzd_60 double,"
                + "gzd_80 double,"
                + "hasUpdata integer not null default 0"
                + ")";//1 i
    }
}
