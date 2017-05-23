package color.measurement.com.openfilehelper;


/**
 * Created by wpc on 2016/11/18.
 */

public class FileAndPath {

    //包名 路径下文件会在 app卸载的时候删除  并且外界不可访问
    public static final String packagename = "color.measurement.com.from_cp20";

    public static final String APP_DATA_PATH = "/data/data/" + packagename;
    public static final String APP_INS_CACHE_PATH = APP_DATA_PATH + "/Ins";

    public static final String SDCARD_PATH = Util.getSDcardPath();

    public static final String SD_xj = SDCARD_PATH + "/xj_919";
    public static final String IMG = SD_xj + "/img";
    public static final String Excel = SD_xj + "/excel";

    public static final String IMG_NATIVE_DATA = SD_xj + "/imgCache";
    public static final String IMG_BAIBAN_NAME = "baiban";

}
