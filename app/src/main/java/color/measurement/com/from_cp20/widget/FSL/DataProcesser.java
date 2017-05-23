package color.measurement.com.from_cp20.widget.FSL;

import android.support.annotation.Nullable;

import java.util.ArrayList;

/**
 * Created by wpc on 2017/4/19.
 */

public class DataProcesser {

    //chart 默认值
    public static final   int point_r = 5;
    public static final  int line_width = 2;

    static ArrayList<ArrayList<point>> points = new ArrayList<>();

    public static void refeshFSL(@Nullable ArrayList<Double> stand_datas, @Nullable ArrayList<Double> test_data, FSL_table fsl_table) {
        points.clear();
        if (stand_datas != null) {
            points.add(getPoints(stand_datas, fsl_table));
        }
        if (test_data != null) {
            points.add(getPoints(test_data, fsl_table));
        }
        refeshLines(fsl_table);
    }

    public static void refeshLines(FSL_table fsl_table) {
        ArrayList<Line> lines = new ArrayList<>();
        for (int i = 0; i < points.size(); i++) {
            lines.add(new Line(true, point_r, line_width, points.get(i)));
        }
        fsl_table.setLines(lines);
    }

    static ArrayList<point> getPoints(@Nullable ArrayList<Double> datas, FSL_table fsl_table) {
        ArrayList<point> points = new ArrayList<>();
        if (datas != null) {
            for (int i = 0; i < datas.size(); i++) {
                float x = (fsl_table.getX_end() - fsl_table.getX_begin()) / (datas.size() - 1);
                points.add(new point((fsl_table.getX_begin() + x * i),datas.get(i).floatValue()));
            }
        }

        return points;
    }
}
