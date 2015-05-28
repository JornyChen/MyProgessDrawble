package hello.test.cn.progress;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by JornyChen
 * Email: zhaolong_chen@kingdee.com
 * Date: 15-5-25.
 * Project dialogTest
 */
public class MyView extends View {
    private ProgreeDrawable mProgreeDrawable;

    public MyView(Context context) {
        super(context);
        mProgreeDrawable = new ProgreeDrawable(context);
        setBackgroundDrawable(mProgreeDrawable);
    }

    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mProgreeDrawable = new ProgreeDrawable(context);
        setBackgroundDrawable(mProgreeDrawable);
    }

    public MyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mProgreeDrawable = new ProgreeDrawable(context);
        setBackgroundDrawable(mProgreeDrawable);
    }

    public void startAnimator() {
        if (mProgreeDrawable != null) {
            mProgreeDrawable.startAnimator();
        }
    }

    public void startCheck() {
        if (mProgreeDrawable != null) {
            mProgreeDrawable.startCheck();
        }

    }

    public void finishCheck() {
        if (mProgreeDrawable != null) {
            mProgreeDrawable.finish();
        }
    }

}
