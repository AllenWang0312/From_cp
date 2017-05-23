package color.measurement.com.from_cp20.widget.FSL;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Point;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by wpc on 2017/5/2.
 */

public class CostomChart extends View {

    int width, height;
    int paddingleft = 28, paddingright = 40, paddingbottom =24, paddingtop = 24;//dp

    int tips_margin_top = 16, tips_margin_left = 24, tips_between = 48;//dp

    int text_size = 12, tip_text_size = 12;//px

    int x_begin = 400, y_begin = 0, x_end = 700, y_end = 100;
    int x_min_num = 5, y_min_num = 10;

    Point startPoint;
    String x_unit, y_unit;
    Paint Line_Paint;
    Paint Text_Paint;

    int x_interval, y_interval;
    int x_length, y_length, x_intervel_length, y_interval_length;
    float x_density, y_density;
    float dip;
    public CostomChart(Context context) {
        this(context, null);
    }

    public CostomChart(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CostomChart(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        dip = getContext().getResources().getDisplayMetrics().density;
        paddingleft *= dip;
        paddingright *= dip;
        paddingbottom *= dip;
        paddingtop *= dip;//dp
        tips_margin_top *= dip;
        tips_margin_left *= dip;
        tips_between *= dip;//dp
    }



    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Line_Paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        Text_Paint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        mPaint.setColor(0xff111111);
//        mPaint.setAntiAlias(false);
//        mPaint.setStyle(Paint.Style.STROKE);
//        mPaint.setStrokeWidth(5);

        width = canvas.getWidth();
        height = canvas.getHeight();

        x_length = width - paddingleft - paddingright;
        y_length = height - paddingbottom - paddingtop;

        x_intervel_length = x_length / x_min_num;
        y_interval_length = y_length / y_min_num;

        x_interval = (x_end - x_begin) / x_min_num;
        y_interval = (y_end - y_begin) / y_min_num;

        x_density = x_intervel_length / (float) x_interval;
        y_density = y_interval_length / (float) y_interval;

        startPoint = new Point(paddingleft, height - paddingbottom);

        Text_Paint.setStyle(Paint.Style.FILL);
        Text_Paint.setTextSize(text_size*dip);
        drawXText(canvas);
        drawYText(canvas);

        Line_Paint.setStyle(Paint.Style.STROKE);
        Line_Paint.setAntiAlias(true);
        Line_Paint.setStrokeWidth(3);
        drawXYLines(canvas);

        Line_Paint.setStrokeWidth(1);
        drawDotted(canvas);
        Line_Paint.setColor(0xff000000);
    }

    int text_x_x_excursion = 16, text_y_x_excursion = 16;//dp
    int text_x_y_excursion = 12, text_y_y_excursion = 100;

    void drawXYLines(Canvas c) {
        c.drawLine(startPoint.x, startPoint.y, startPoint.x, startPoint.y - y_length, Line_Paint);
        c.drawLine(startPoint.x, startPoint.y, startPoint.x + x_length, startPoint.y, Line_Paint);
    }

    void drawDotted(Canvas c) {
        Path path = new Path();
        addXpath(path);
        addYpath(path);
        PathEffect effects = new DashPathEffect(new float[]{5, 5}, 1);
        Line_Paint.setPathEffect(effects);
//        c.drawLine(0, 40, 500, 40, mPaint);
        c.drawPath(path, Line_Paint);
        Line_Paint.setPathEffect(null);
    }

    private void addXpath(Path path) {
        for (int i = 1; i < y_min_num; i++) {
            path.moveTo(startPoint.x, startPoint.y - y_interval_length * i);
            path.lineTo(startPoint.x + x_length, startPoint.y - y_interval_length * i);
        }
    }

    private void addYpath(Path path) {
        for (int i = 1; i < x_min_num; i++) {
            path.moveTo(startPoint.x + x_intervel_length * i, startPoint.y);
            path.lineTo(startPoint.x + x_intervel_length * i, startPoint.y - y_length);
        }
    }
    private void drawXText(Canvas canvas) {

        for (int i = 0; i < x_min_num + 1; i++) {
            canvas.drawText(x_begin + i * x_interval + y_unit, (startPoint.x - text_x_x_excursion*dip + x_intervel_length * i), (height - paddingbottom + text_x_y_excursion*dip), Text_Paint);
        }
    }

    private void drawYText(Canvas canvas) {
        for (int i = 0; i < y_min_num; i++) {
            canvas.drawText(0 + i * y_interval + "",
                    (startPoint.x - text_y_x_excursion*dip),
                    (height - paddingbottom - y_interval_length * i),
                    Text_Paint);
        }
    }
}
