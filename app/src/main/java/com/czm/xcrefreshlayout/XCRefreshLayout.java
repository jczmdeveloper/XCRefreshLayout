package com.czm.xcrefreshlayout;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;

/**
 * Created by caizhiming on 2016/1/14.
 * 下拉刷新控件布局 XCRefreshLayout
 */
public class XCRefreshLayout extends FrameLayout{

    public static final String TAG = XCRefreshLayout.class.getSimpleName();

    private RefreshHeader mRefreshHeader;
    private View mChildView;
    private int mHeight;

    private DecelerateInterpolator mInterpolator = new DecelerateInterpolator(5);


    private float mHeaderHeight;
    private float mStartY;
    private float mCurY;
    private boolean mIsRefreshing;


    public static final int REFRESH_STATUS_PULL_REFRESH = 0;//下拉刷新;
    public static final int REFRESH_STATUS_RELEASE_REFRESH = 1;//放开立即刷新
    public static final int REFRESH_STATUS_REFRESHING = 2;// "正在刷新中...";
    public static final int REFRESH_STATUS_REFRESH_FINISH = 3;//"刷新完成";
    private int mRefreshStatus = REFRESH_STATUS_PULL_REFRESH;


    public XCRefreshLayout(Context context) {
        this(context, null, 0);
    }

    public XCRefreshLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public XCRefreshLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        if (getChildCount() > 1) {
            throw new RuntimeException("you can only attach one child view");
        }

        this.post(new Runnable() {
            @Override
            public void run() {
                mChildView = getChildAt(0);
                addHeaderView();
            }
        });
    }
    private void addHeaderView(){
        mRefreshHeader = new RefreshHeader(getContext());
        mHeaderHeight = mRefreshHeader.getHeaderHeight();
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
        params.gravity = Gravity.TOP;
        mRefreshHeader.setLayoutParams(params);
        mRefreshHeader.setBackgroundColor(Color.parseColor("#228B22"));
        addViewInternal(mRefreshHeader);
    }
    private void addViewInternal(@NonNull View child) {
        super.addView(child);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mHeight = getMeasuredHeight();
        Log.d("czm", "Height =" + mHeight);
    }

    @Override
    public void addView(View child) {
        if (getChildCount() >= 1) {
            throw new RuntimeException("you can only attach one child view");
        }
        super.addView(child);
        mChildView = child;
    }
    private boolean canChildScrollUp() {
        if (mChildView == null) {
            return false;
        }
        return ViewCompat.canScrollVertically(mChildView, -1);
    }
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (mIsRefreshing) {
            return true;
        }
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mStartY = ev.getY();
                mCurY = mStartY;
                break;
            case MotionEvent.ACTION_MOVE:
                float curY = ev.getY();
                float dy = curY - mStartY;
                Log.d("czm", "dy=" + dy);
                if (dy > 0 && !canChildScrollUp()) {
                    return true;
                }
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mIsRefreshing) {
            return super.onTouchEvent(event);
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                mCurY = event.getY();
                float dy = mCurY - mStartY;
                dy = Math.max(0, dy);
                if (mChildView != null) {
                    Log.d("czm", "dy/mHeight=" + (dy / mHeight));
                    float offsetY = mInterpolator.getInterpolation(dy / mHeight) * dy/3;
                    mChildView.setTranslationY(offsetY);
                    mRefreshHeader.getLayoutParams().height = (int) (offsetY+0.5f);
                    mRefreshHeader.requestLayout();
                    if(mChildView.getTranslationY() >= mHeaderHeight * 3/4){
                        mRefreshStatus = REFRESH_STATUS_RELEASE_REFRESH;
                        mRefreshHeader.updateRefreshStatus(mRefreshStatus);
                    }else{
                        mRefreshStatus = REFRESH_STATUS_PULL_REFRESH;
                        mRefreshHeader.updateRefreshStatus(mRefreshStatus);
                    }
                }
                return true;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if(mChildView != null){
                    if(mChildView.getTranslationY() >= mHeaderHeight *3/4){
                        mRefreshStatus = REFRESH_STATUS_REFRESHING;
                        mRefreshHeader.updateRefreshStatus(mRefreshStatus);
                        mIsRefreshing = true;
                        upToMiddleAnim();
                        if(mOnRefreshListener != null){
                            mOnRefreshListener.onRefreshing();
                        }
                    }else{
                        upToTopAnim();
                    }
                }
                return true;
            default:
                return super.onTouchEvent(event);
        }
    }
    private void upToMiddleAnim(){
        float offsetY = mChildView.getTranslationY();
        final ValueAnimator backToMiddleAnim = ValueAnimator.ofFloat(offsetY, 0);
        final float pullHeight = offsetY;
        backToMiddleAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float val = (float) animation.getAnimatedValue();
                val = mInterpolator.getInterpolation(val / pullHeight) * val;
                if (mChildView != null) {
                    mChildView.setTranslationY(val);
                }
                mRefreshHeader.getLayoutParams().height = (int) (val+0.5f);
                mRefreshHeader.requestLayout();
                Log.d("czm", "offsetY=" + mChildView.getTranslationY());
                if(mChildView.getTranslationY() <= mHeaderHeight * 3/4){
                    backToMiddleAnim.cancel();
                }
            }
        });
        backToMiddleAnim.setDuration((long) (offsetY * 600 / pullHeight));
        backToMiddleAnim.start();
    }
    private void upToTopAnim(){
        float offsetY = mChildView.getTranslationY();
        final ValueAnimator backToTopAnim = ValueAnimator.ofFloat(offsetY, 0);
        final float pullHeight = offsetY;
        backToTopAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float val = (float) animation.getAnimatedValue();
                val = mInterpolator.getInterpolation(val / pullHeight) * val;
                if (mChildView != null) {
                    mChildView.setTranslationY(val);
                }
                mRefreshHeader.getLayoutParams().height = (int) (val+0.5f);
                mRefreshHeader.requestLayout();
                Log.d("czm", "offsetY=" + mChildView.getTranslationY());
            }
        });
        backToTopAnim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mRefreshStatus = REFRESH_STATUS_PULL_REFRESH;
                mRefreshHeader.updateRefreshStatus(mRefreshStatus);

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        backToTopAnim.setDuration((long) (offsetY * 600 / pullHeight));
        backToTopAnim.start();
    }

    /**
     * test refreshing process
     */
    private void testRefreshing() {

    }
    public void refreshComplete(){
        this.postDelayed(new Runnable() {
            @Override
            public void run() {
                mIsRefreshing = false;
                mRefreshStatus = REFRESH_STATUS_REFRESH_FINISH;
                mRefreshHeader.updateRefreshStatus(mRefreshStatus);
                if(mOnRefreshListener != null){
                    mOnRefreshListener.onRefreshFinish();
                }
                upToTopAnim();
            }
        },300);
    }
    private OnRefreshListener mOnRefreshListener;
    public void setOnRefreshListener(OnRefreshListener listener){
        mOnRefreshListener = listener;
    }
    public interface OnRefreshListener{
        void onRefreshFinish();
        void onRefreshing();
    }
}
