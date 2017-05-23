package color.measurement.com.from_cp20.widget.LAB;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Point;
import android.graphics.SweepGradient;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wpc on 2016/12/19.
 */

public class SC_table extends View {


    public SC_table(Context context) {
        super(context);
    }

    public SC_table(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SC_table(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    boolean isSC_mod = true;
    int y_ = 5, x_ = 20;
    int text_x, num_x, line_x;
    int x_paddingBottom, x_paddingTop, y_paddingRight = 20;
    int chart_height;
    int width, height;
    Paint mPaint;

    Point mPoint;
    float maxValue = 100f;
    float x_from = -maxValue, x_to = maxValue, y_from = -maxValue, y_to = maxValue;
    float x_interval, y_interval;
    float x_density, y_density;

    List<Double> stand;
    ArrayList<List<Double>> test;


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        width = canvas.getWidth();
        height = canvas.getHeight();
        if (showAble()) {
            float f = getMinAbsValue();
            x_from = -f;
            x_to = f;
            y_from = -f;
            y_to = f;
        }
        x_interval = (x_to - x_from) / 4;
        y_interval = (y_to - y_from) / 4;

        x_paddingBottom = height / 5;
        x_paddingTop = x_paddingBottom / 2;
        chart_height = height - x_paddingBottom - x_paddingTop;
        x_density = chart_height / (x_to - x_from);
        y_density = chart_height / (y_to - y_from);

        line_x = width - chart_height - y_paddingRight;
        text_x = line_x / 5;
        num_x = line_x * 2 / 5;
        mPoint = new Point(line_x, height - x_paddingBottom);

        mPaint = new Paint();
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
        mPaint.setTextSize(20);
        mPaint.setStrokeWidth(2);

        Path text_path = new Path();
        text_path.moveTo(text_x, height - x_paddingBottom - 100);
        text_path.lineTo(text_x, height - x_paddingBottom - 360);

        canvas.drawTextOnPath((isSC_mod ? "Δ" : "") + "b*(D65)[<1>]", text_path, 0, 0, mPaint);
        canvas.drawText((isSC_mod ? "Δ" : "") + "a*(D65)[<1>]", line_x + 100, height - 20, mPaint);

        canvas.drawText(x_from + "", num_x, height - x_paddingBottom + y_, mPaint);
        canvas.drawText(x_from + x_interval + "", num_x, height - x_paddingBottom - (chart_height / 4) + y_, mPaint);
        canvas.drawText(x_from + x_interval * 2 + "", num_x, height - x_paddingBottom - (chart_height / 2) + y_, mPaint);
        canvas.drawText(x_from + x_interval * 3 + "", num_x, height - x_paddingBottom - (chart_height / 4 * 3) + y_, mPaint);
        canvas.drawText(x_to + "", num_x, height - x_paddingBottom - chart_height + y_, mPaint);

        canvas.drawText(x_from + "", line_x - x_, height - x_paddingBottom + 30, mPaint);
        canvas.drawText(x_from + x_interval + "", line_x + (chart_height / 4) - x_, height - x_paddingBottom + 30, mPaint);
        canvas.drawText(x_from + x_interval * 2 + "", line_x + (chart_height / 2) - x_, height - x_paddingBottom + 30, mPaint);
        canvas.drawText(x_from + x_interval * 3 + "", line_x + (chart_height / 4 * 3) - x_, height - x_paddingBottom + 30, mPaint);
        canvas.drawText(x_to + "", line_x + chart_height - x_, height - x_paddingBottom + 20, mPaint);

        canvas.drawLine(line_x, height - x_paddingBottom, line_x, x_paddingTop, mPaint);
        canvas.drawLine(line_x + (chart_height / 4), height - x_paddingBottom, line_x + (chart_height / 4), x_paddingTop, mPaint);
        canvas.drawLine(line_x + (chart_height / 2), height - x_paddingBottom, line_x + (chart_height / 2), x_paddingTop, mPaint);
        canvas.drawLine(line_x + (chart_height / 4 * 3), height - x_paddingBottom, line_x + (chart_height / 4 * 3), x_paddingTop, mPaint);
        canvas.drawLine(line_x + chart_height, height - x_paddingBottom, line_x + chart_height, x_paddingTop, mPaint);


        canvas.drawLine(line_x, height - x_paddingBottom, width - y_paddingRight, height - x_paddingBottom, mPaint);
        canvas.drawLine(line_x, height - x_paddingBottom - (chart_height / 4), width - y_paddingRight, x_paddingTop + (chart_height / 4 * 3), mPaint);
        canvas.drawLine(line_x, height - x_paddingBottom - (chart_height / 2), width - y_paddingRight, x_paddingTop + (chart_height / 2), mPaint);
        canvas.drawLine(line_x, height - x_paddingBottom - (chart_height / 4 * 3), width - y_paddingRight, x_paddingTop + (chart_height / 4), mPaint);
        canvas.drawLine(line_x, height - x_paddingBottom - chart_height, width - y_paddingRight, x_paddingTop, mPaint);

        mPaint.setStyle(Paint.Style.STROKE);

        drawDotted(canvas);

        Paint p_s = new Paint();
        int[] SWEEP_GRADIENT_COLORS = new int[]{Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW, Color.RED};
        SweepGradient mColorShader = new SweepGradient(line_x + (chart_height / 2), x_paddingTop + (chart_height / 2), SWEEP_GRADIENT_COLORS, null);
        p_s.setShader(mColorShader);
        p_s.setAlpha(120);
//        LinearGradient vic = new LinearGradient(line_x + (chart_height / 2), x_paddingTop, line_x + (chart_height / 2), height - x_paddingBottom, new int[]{Color.YELLOW,Color.WHITE, Color.BLUE},null, Shader.TileMode.CLAMP);  //
//        p.setShader(vic);
//        canvas.drawCircle(line_x + (chart_height / 2), x_paddingTop + (chart_height / 2), chart_height / 2, p);
//        LinearGradient hrz = new LinearGradient(line_x, x_paddingTop + (chart_height / 2), width - y_paddingRight, x_paddingTop + (chart_height / 2),new int[]{ Color.GREEN, Color.WHITE,Color.RED},null, Shader.TileMode.CLAMP);  //
//        p.setShader(hrz);
        canvas.drawCircle(line_x + (chart_height / 2), x_paddingTop + (chart_height / 2), chart_height / 2, p_s);
//        Paint p_r = new Paint();
//        RadialGradient mRadiaShader = new RadialGradient((chart_height / 2), x_paddingTop + (chart_height / 2), chart_height / 2, 0xffffff, 0x000000, null);
//        p_r.setAlpha(120);
//        p_r.setShader(mRadiaShader);
//        canvas.drawCircle(line_x + (chart_height / 2), x_paddingTop + (chart_height / 2), chart_height / 2, p_r);


        if (stand != null && test != null) {
            Paint paint = new Paint();
            paint.setColor(Color.BLACK);
            paint.setStyle(Paint.Style.FILL);
            for (int i = 0; i < test.size(); i++) {
                double a = stand.get(1) - test.get(i).get(1);
                double b = stand.get(2) - test.get(i).get(2);
                canvas.drawCircle((float) (mPoint.x + (chart_height / 2) + (a * x_density)),
                        (float) (mPoint.y - (chart_height / 2) - (b * y_density))
                        , 5, paint);
            }
        }
    }

    float min = 2f;
    float num = maxValue / min;

    private float getMinAbsValue() {
        for (int i = 0; i < num; i++) {
            if (outside(i) && !outside(i + 1)) {
                return (i + 1) * min;
            }
        }
        return 100f;
    }

    private boolean outside(int i) {
        for (int j = 0; j < test.size(); j++) {
            {
                if (Math.abs(test.get(j).get(1) - stand.get(1)) > i * min) {
                    return true;
                }
                if (Math.abs(test.get(j).get(2) - stand.get(2)) > i * min) {
                    return true;
                }
            }
        }
        return false;
    }

    void drawDotted(Canvas c) {

        Path path = new Path();
        addXpath(path);
        addYpath(path);
        PathEffect effects = new DashPathEffect(new float[]{5, 5}, 1);
        mPaint.setPathEffect(effects);
//        c.drawLine(0, 40, 500, 40, mPaint);
        c.drawPath(path, mPaint);
    }

    private void addXpath(Path path) {
        for (int i = 0; i < 4; i++) {
            path.moveTo(mPoint.x, mPoint.y - (chart_height / 8) - (chart_height / 4) * i);
            path.lineTo(mPoint.x + chart_height, mPoint.y - (chart_height / 8) - (chart_height / 4) * i);
        }
    }

    private void addYpath(Path path) {
        for (int i = 0; i < 4; i++) {
            path.moveTo(mPoint.x + (chart_height / 8) + (chart_height / 4) * i, mPoint.y);
            path.lineTo(mPoint.x + (chart_height / 8) + (chart_height / 4) * i, x_paddingTop);
        }
    }

    public void clearLabs() {
        stand = null;
        test = new ArrayList<>();
    }

    public void setLab(List<Double> lab, boolean isStand) {
        if (isStand) {
            this.stand = lab;
            test = new ArrayList<>();
        } else {
            if (test == null) {
                test = new ArrayList<>();
            }
            this.test.add(lab);
        }
        if (showAble()) {
            invalidate();
        }
    }

    boolean showAble() {
        return stand != null && test != null && test.size() > 0;
    }

    public void setStandAndTest(List<Double> stand, @Nullable List<Double> test) {
        this.stand = stand;
        if (test != null  && test.size() > 0) {
            if(this.test!=null&&this.test.size()>0){
                this.test.clear();
            }else {
                this.test=new ArrayList<>();
            }
            this.test.add(test);
        }
        invalidate();
    }

    public void showGroupData(List<Double> stand, ArrayList<List<Double>> tests) {
        this.stand = stand;
        if (test == null) {
            test = new ArrayList<>();
        } else if (test.size() != 0) {
            test.clear();
        }
        this.test.addAll(tests);
        invalidate();
    }
}
