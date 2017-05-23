package color.measurement.com.openfilehelper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import static color.measurement.com.openfilehelper.Util.openFile;

/**
 * Created by wpc on 2017/2/11.
 */

public class OpenFileHelper {
    @SuppressLint("NewApi")
    public static void showChoseFileToPlayDialog(String dirPath,
                                                 String fileType, final Activity context) {
        if (new File(dirPath).exists()) {
            Log.i("目录存在", dirPath);
            final ArrayList<FileInfo> items = getFileInfoListWithDirPathAndEnd(
                    dirPath, fileType);
            if (items.size() == 0) {
                Toast.makeText(context, "文件夹为空", Toast.LENGTH_SHORT).show();
            } else {
                ChoseFileDialog dialog = new ChoseFileDialog(context, items,
                        new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> arg0,
                                                    View arg1, int arg2, long arg3) {
                                // TODO Auto-generated method stub
                                FileInfo fi = items.get(arg2);
                                String path = fi.getDirPath() + fi.getName();
                                openFile(context, new File(path));
                            }
                        }, null);
                dialog.show(context.getFragmentManager(), "chosefiledialog");
            }

            // if (MainActivity.videodatas == null
            // || MainActivity.videodatas.size() <= 0) {
            // Toast.makeText(SoftUpdateActivity.this, "路径下不存在视频",
            // Toast.LENGTH_SHORT).show();
            // } else {
            // MainActivity.videolist_layout
            // .setVisibility(View.VISIBLE);
            // }
        } else {
            Toast.makeText(context, "目录不存在", Toast.LENGTH_SHORT).show();
        }
    }

    public static ArrayList<FileInfo> getFileInfoListWithDirPathAndEnd(
            String path, String endwith) {
        ArrayList<FileInfo> vediolist = new ArrayList<FileInfo>();

        File file = new File(path);
        File[] subFile = file.listFiles();
        if (subFile != null) {
            if (subFile.length != 0) {
                for (int iFileLength = 0; iFileLength < subFile.length; iFileLength++) {
                    if (!subFile[iFileLength].isDirectory()) {
                        String filename = subFile[iFileLength].getName();
                        if (filename.trim().toLowerCase().endsWith(endwith)) {
                            vediolist.add(new FileInfo(filename, path, ""));
                        }
                    } else {
                        Log.i("getvediofilename", "文件目录有错");
                    }
                }
            }
        }
        return vediolist;
    }

}
