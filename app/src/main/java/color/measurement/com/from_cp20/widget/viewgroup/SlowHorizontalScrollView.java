package color.measurement.com.from_cp20.widget.viewgroup;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;

/**
 * Created by wpc on 2017/4/15.
 * 使用场景: horizontalscrollview 嵌套 recycleview 滑动传递不灵活
 *
 */

public class SlowHorizontalScrollView extends HorizontalScrollView {
    float x, y;

    public SlowHorizontalScrollView(Context context) {
        super(context);
    }

    public SlowHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SlowHorizontalScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public SlowHorizontalScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                x = ev.getX();
                y = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (Math.abs(ev.getX() - x)> Math.abs(ev.getY() - y)) {
                    return true;
                } else {
                    return false;
                }
        }
        return super.onInterceptTouchEvent(ev);
    }
}
