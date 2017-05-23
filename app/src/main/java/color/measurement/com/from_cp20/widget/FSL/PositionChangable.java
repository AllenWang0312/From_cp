package color.measurement.com.from_cp20.widget.FSL;

import android.graphics.Point;

/**
 * Created by wpc on 2016/12/17.
 */

public interface PositionChangable {
    Point TablePointToViewPoint(point p);
    point ViewPointToTablePoint(Point p);
}
