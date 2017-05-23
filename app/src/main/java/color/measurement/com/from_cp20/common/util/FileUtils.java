package color.measurement.com.from_cp20.common.util;

import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by wpc on 2017/4/14.
 */

public class FileUtils {
    //包名 路径下文件会在 app卸载的时候删除  并且外界不可访问
    public static final String packagename = "color.measurement.com.from_cp20";

    public static final String APP_DATA_PATH = "/data/data/" + packagename;

    public static final String APP_INS_CACHE_PATH = APP_DATA_PATH + "/Ins";

    public static final String SDCARD_PATH = Environment.getExternalStorageDirectory().getPath();

//    public static final String SD_xj = SDCARD_PATH + "/xj_919";
//    public static final String IMG = SD_xj + "/img";
//    public static final String Excel = SD_xj + "/excel";
//
//    public static final String IMG_NATIVE_DATA = SD_xj + "/imgCache";
//    public static final String IMG_BAIBAN_NAME = "baiban";


    public static boolean writeByteArrayToFile(byte[] bytes, String dirPath, String filename) {
        createOrExistsDir(dirPath);
        createFileIfNotExist(dirPath,filename);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(new File(dirPath,filename));
            fos.write(bytes);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                fos.close();
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
    }
    public static boolean createOrExistsDir(String dirPath) {
        return createOrExistsDir(getFileByPath(dirPath));
    }
    public static File getFileByPath(String filePath) {
        return StringUtils.isSpace(filePath) ? null : new File(filePath);
    }

    public static boolean createOrExistsDir(File file) {
        // 如果存在，是目录则返回true，是文件则返回false，不存在则返回是否创建成功
        return file != null && (file.exists() ? file.isDirectory() : file.mkdirs());
    }

    private static void createFileIfNotExist(String dirPath, String filename){
        File file=new File(dirPath,filename);
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
