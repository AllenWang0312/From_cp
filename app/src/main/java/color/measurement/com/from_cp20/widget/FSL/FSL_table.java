package color.measurement.com.from_cp20.widget.FSL;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;

import java.util.ArrayList;

import color.measurement.com.from_cp20.R;
import color.measurement.com.from_cp20.common.util.StringFormat;

/**
 * Created by wpc on 2016/12/5.
 */

public class FSL_table extends CostomChart implements PositionChangable {

    enum TestMod {
        SCI, SCE, SCI_SCE
    }

    private TestMod mTestMod = TestMod.SCI;

    int t_sci, s_sci, t_sce, s_sce;
    boolean show_notice;

    private ArrayList<Line> mLines;
    private ArrayList<point> mselectPoints;

    private OnLongClickListener mOnLongClickListener;

    @Override
    public void setOnLongClickListener(OnLongClickListener onLongClickListener) {
        mOnLongClickListener = onLongClickListener;
    }

    public FSL_table(Context context) {
        this(context, null);
    }

    public FSL_table(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FSL_table(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray t = context.obtainStyledAttributes(attrs,
                R.styleable.FSL_table, 0, 0);
        y_unit = t.getString(R.styleable.FSL_table_unit);
        t_sci = t.getColor(R.styleable.FSL_table_t_sci_color, 0x88ff0000);
        s_sci = t.getColor(R.styleable.FSL_table_s_sci_color, 0x8800ff00);

        t_sce = t.getColor(R.styleable.FSL_table_t_sce_color, 0xff000000);
        s_sce = t.getColor(R.styleable.FSL_table_s_sce_color, 0xff000000);
        show_notice = t.getBoolean(R.styleable.FSL_table_notice_show, true);
        if (!show_notice) {
            paddingright = 40;
        }
        t.recycle();

    }


    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);

//        canvas.drawCircle(100, 100, 90, mPaint);
//        Log.i("onDraw",startPoint.x+""+ (startPoint.y - y_min_num * y_interval_length)+""+startPoint.x+""+ startPoint.y);
//        canvas.drawLine( startPoint.x, startPoint.y - y_min_num * y_interval_length,startPoint.x, startPoint.y, mPaint);
//        canvas.drawLine( startPoint.x + x_min_num * x_intervel_length, startPoint.y,startPoint.x, startPoint.y, mPaint);
        Line_Paint.setStyle(Paint.Style.FILL);

        Line_Paint.setAlpha(0x88);
        if (mLines != null) {
            for (int i = 0; i < mLines.size(); i++) {
                drawLine(canvas, mLines.get(i), i);
            }
        }
        Line_Paint.setAlpha(0xff);

        if (touchPoint != null) {
            Paint p = new Paint();
            p.setColor(Color.BLACK);

            canvas.drawLine(touchPoint.x, paddingtop, touchPoint.x, height - paddingbottom, p);
//            touchTablePoint.x
            p.setStyle(Paint.Style.STROKE);
            if (mLines != null && mLines.size() > 0) {
                mselectPoints = new ArrayList<>();
                for (Line l : mLines) {
                    ArrayList<point> points = l.getPoints();
                    point point = getNearestPoint(points, touchTablePoint.x);
                    mselectPoints.add(point);
                    Point viewp = TablePointToViewPoint(point);
                    canvas.drawCircle(viewp.x, viewp.y, 5, p);
                }
            }
        }

        if (show_notice) {
            Text_Paint.setStyle(Paint.Style.FILL_AND_STROKE);
            Text_Paint.setTextSize(tip_text_size*dip);


            if (mTestMod == TestMod.SCI | mTestMod == TestMod.SCI_SCE) {
                Text_Paint.setColor(s_sci);
                canvas.drawText("--S-SCI", tips_margin_left, tips_margin_top, Text_Paint);
                if (mselectPoints != null && mselectPoints.size() > 0) {
                    point p = mselectPoints.get(0);
                    canvas.drawText(p.x + "", width - paddingright, paddingtop, Text_Paint);
                    canvas.drawText(StringFormat.TwoDecimal(p.y), width - paddingright, paddingtop + 16 * dip, Text_Paint);
                }
                Text_Paint.setColor(t_sci);
                canvas.drawText("--T-SCI", tips_margin_left + tips_between, tips_margin_top, Text_Paint);
                if (mselectPoints != null && mselectPoints.size() > 1) {
                    for (int i = 1; i < mselectPoints.size(); i++) {
                        point p = mselectPoints.get(i);
                        canvas.drawText(p.x + "", width - paddingright, paddingtop + 36 * dip * i, Text_Paint);
                        canvas.drawText(StringFormat.TwoDecimal(p.y), width - paddingright, paddingtop + 36 * dip * i + 16 * dip, Text_Paint);
                    }

                }

            }

            if (mTestMod == TestMod.SCE | mTestMod == TestMod.SCI_SCE) {
                Text_Paint.setColor(s_sce);
                canvas.drawText("--S-SCE", tips_margin_left + tips_between * 2, tips_margin_top, Text_Paint);
                Text_Paint.setColor(t_sce);
                canvas.drawText("--T-SCE", tips_margin_left + tips_between * 3, tips_margin_top, Text_Paint);
            }
        }
    }

    ArrayList<point> selcetPoints;

    private point getNearestPoint(ArrayList<point> points, float x) {
        return points.get(getNearestIndex(points, x));
    }

    public static int getNearestIndex(ArrayList<point> datas, float com_num) {
        int index = 0;
        float distance = Integer.MAX_VALUE;
        for (int i = 0; i < datas.size(); i++) {
            float dis = Math.abs(com_num - datas.get(i).x);
            if (distance > dis) {
                distance = dis;
                index = i;
            }
        }
        return index;
    }

    private void drawLine(Canvas c, Line l, int index) {
        ArrayList<Point> points = translatePoints(l.getPoints());
        Path path = new Path();

        int color = l.getLine_color() != null ? l.getLine_color() : index == 0 ? s_sci : t_sci;
        Line_Paint.setStyle(Paint.Style.STROKE);
        Line_Paint.setColor(color);


        for (int i = 0; i < points.size(); i++) {
            Point p = points.get(i);
            if (i == 0) {
                path.moveTo(p.x, p.y);
            } else {
                path.lineTo(p.x, p.y);
            }
            Line_Paint.setStyle(Paint.Style.FILL);
            c.drawCircle(p.x, p.y, l.getPoint_r(), Line_Paint);
        }
        if (l.isConnect()) {
            Line_Paint.setColor(color);
            Line_Paint.setStyle(Paint.Style.STROKE);
            Line_Paint.setStrokeWidth(l.getLine_width());
            c.drawPath(path, Line_Paint);
        }
    }

    public void setY_Unit(String unit) {
        this.y_unit = unit;
        invalidate();
    }

    public void setTipsColors(int s_sci, int t_sci, int s_sce, int t_sce) {
        this.t_sci = t_sci;
        this.s_sci = s_sci;
        this.t_sce = t_sce;
        this.s_sce = s_sce;
        invalidate();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
//    public void setPoints(ArrayList<point> dataPoints) {
//        ArrayList<Point> translate_points = translatePoints(dataPoints);
//        Log.i("setPoints", dataPoints.toString());
//        for (int i = 0; i < translate_points.size(); i++) {
//            Point p = translate_points.get(i);
//            Log.i("p", p.toString());
//        }
//    }

    float x, y;
    Point touchPoint;
    point touchTablePoint;

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
//                Log.i("onTouchEvent", "ACTION_DOWN" + event.getDownTime());
//                hasClick = false;
//                downTime = event.getDownTime();
                break;
            case MotionEvent.ACTION_MOVE:
//                Log.i("onTouchEvent", "ACTION_MOVE" + event.getX() + "_" + event.getY() + "_" + event.getEventTime());
//                if (event.getEventTime() - downTime > 1000) {
//                    if (hasClick != true) {
//                        if (mOnLongClickListener != null) {
//                            mOnLongClickListener.onLongClick(this);
//                            hasClick = true;
//                        }
//                    }
//                }
                break;
            case MotionEvent.ACTION_UP:
//                Log.i("onTouchEvent", "ACTION_UP");
                break;
            default:
//                Log.i("onTouchEvent", event.getAction() + "");
                break;

        }
        x = event.getX();
        y = event.getY();
        if (x > paddingleft && x < width - paddingright && y > paddingtop && y < height - paddingbottom) {
            touchPoint = new Point((int) x, (int) y);
            touchTablePoint = ViewPointToTablePoint(touchPoint);
            invalidate();
        }
//        for(Line l:mLines){
//            l.getPoints().size();
//        }

        return true;
    }

    public void setLines(ArrayList<Line> lines) {
        this.mLines = lines;
        invalidate();
    }

    private ArrayList<Point> translatePoints(ArrayList<point> dataPoints) {
        ArrayList<Point> tran = new ArrayList<>();
        for (point p : dataPoints) {
            tran.add(TablePointToViewPoint(p));
        }
        return tran;
    }


    public int getX_end() {
        return x_end;
    }

    public int getX_begin() {
        return x_begin;
    }

    @Override
    public Point TablePointToViewPoint(point p) {
        return new Point((int) (startPoint.x + (p.x - x_begin) * x_density), (int) (startPoint.y - (p.y - y_begin) * y_density));
    }

    @Override
    public point ViewPointToTablePoint(Point p) {
        return new point(
                (p.x - paddingleft) / x_density + x_begin,
                y_end - (p.y - paddingtop) / y_density);
    }

    public void setMod(String mod) {
        if (mod.equals("SCI")) {
            mTestMod = TestMod.SCI;
        } else if (mod.equals("SCE")) {
            mTestMod = TestMod.SCE;
        } else {
            mTestMod = TestMod.SCI_SCE;
        }
        invalidate();
    }

    public void setPaddings(int left, int top, int right, int bottom) {
        paddingleft = left;
        paddingtop = top;
        paddingright = right;
        paddingbottom = bottom;
    }

    public void setTipsMargin(int tips_margin_left, int tips_margin_top, int tips_between) {
        this.tips_margin_left = tips_margin_left;
        this.tips_margin_top = tips_margin_top;
        this.tips_between = tips_between;
    }


    public void setText_size(int text_size, int tip_text_size) {
        this.text_size = text_size;
        this.tip_text_size = tip_text_size;
    }


    public static class Builder {

        Context mContext;
        int paddingleft, paddingright, paddingbottom, paddingtop;//px
        int tips_margin_table, tips_margin_top, tips_between;//px
        int text_size, tip_text_size;

        public Builder(Context context) {
            mContext = context;
        }

        public Builder setPadding(int left, int top, int right, int bottom) {
            paddingleft = left;
            paddingtop = top;
            paddingright = right;
            paddingbottom = bottom;
            return this;
        }

        public Builder setTipsMargin(int tips_margin_table, int tips_margin_top, int tips_between) {
            this.tips_margin_table = tips_margin_table;
            this.tips_margin_top = tips_margin_top;
            this.tips_between = tips_between;
            return this;
        }

        public Builder setText_size(int text_size, int tip_text_size) {
            this.text_size = text_size;
            this.tip_text_size = tip_text_size;
            return this;
        }

        public FSL_table create() {
            FSL_table table = new FSL_table(mContext);
            table.setPadding(paddingleft, paddingtop, paddingright, paddingbottom);
            table.setTipsMargin(tips_margin_table, tips_margin_top, tips_between);
            table.setText_size(text_size, tip_text_size);
            return table;
        }

    }
}
