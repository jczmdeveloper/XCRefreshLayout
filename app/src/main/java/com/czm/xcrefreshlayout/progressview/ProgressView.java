package com.czm.xcrefreshlayout.progressview;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import com.czm.xcrefreshlayout.R;
import com.czm.xcrefreshlayout.progressview.controller.BallBeatController;
import com.czm.xcrefreshlayout.progressview.controller.BallClipRotateController;
import com.czm.xcrefreshlayout.progressview.controller.BallClipRotateMultipleController;
import com.czm.xcrefreshlayout.progressview.controller.BallClipRotatePulseController;
import com.czm.xcrefreshlayout.progressview.controller.BallGridBeatController;
import com.czm.xcrefreshlayout.progressview.controller.BallGridPulseController;
import com.czm.xcrefreshlayout.progressview.controller.BallPulseController;
import com.czm.xcrefreshlayout.progressview.controller.BallPulseRiseController;
import com.czm.xcrefreshlayout.progressview.controller.BallPulseSyncController;
import com.czm.xcrefreshlayout.progressview.controller.BallRotateController;
import com.czm.xcrefreshlayout.progressview.controller.BallScaleController;
import com.czm.xcrefreshlayout.progressview.controller.BallScaleMultipleController;
import com.czm.xcrefreshlayout.progressview.controller.BallScaleRippleController;
import com.czm.xcrefreshlayout.progressview.controller.BallScaleRippleMultipleController;
import com.czm.xcrefreshlayout.progressview.controller.BallSpinFadeLoaderController;
import com.czm.xcrefreshlayout.progressview.controller.BallTrianglePathController;
import com.czm.xcrefreshlayout.progressview.controller.BallZigZagController;
import com.czm.xcrefreshlayout.progressview.controller.BallZigZagDeflectController;
import com.czm.xcrefreshlayout.progressview.controller.CubeTransitionController;
import com.czm.xcrefreshlayout.progressview.controller.LineScaleController;
import com.czm.xcrefreshlayout.progressview.controller.LineScalePartyController;
import com.czm.xcrefreshlayout.progressview.controller.LineScalePulseOutController;
import com.czm.xcrefreshlayout.progressview.controller.LineScalePulseOutRapidController;
import com.czm.xcrefreshlayout.progressview.controller.LineSpinFadeLoaderController;
import com.czm.xcrefreshlayout.progressview.controller.Pacman;
import com.czm.xcrefreshlayout.progressview.controller.SemiCircleSpinController;
import com.czm.xcrefreshlayout.progressview.controller.SquareSpinController;
import com.czm.xcrefreshlayout.progressview.controller.TriangleSkewSpinController;


/**
 * Created by caizhiming on 2016/1/14.
 *
 .BallPulse,
 .BallGridPulse,
 .BallClipRotate,
 .BallClipRotatePulse,
 .SquareSpin,
 .BallClipRotateMultiple,
 .BallPulseRise,
 .BallRotate,
 .CubeTransition,
 .BallZigZag,
 .BallZigZagDeflect,
 .BallTrianglePath,
 .BallScale,
 .LineScale,
 .LineScaleParty,
 .BallScaleMultiple,
 .BallPulseSync,
 .BallBeat,
 .LineScalePulseOut,
 .LineScalePulseOutRapid,
 .BallScaleRipple,
 .BallScaleRippleMultiple,
 .BallSpinFadeLoader,
 .LineSpinFadeLoader,
 .TriangleSkewSpin,
 .Pacman,
 .BallGridBeat,
 .SemiCircleSpin
 *
 */
public class ProgressView extends View{


    //Sizes (with defaults in DP)
    public static final int DEFAULT_SIZE=30;

    //attrs
    int mStyleId;
    int mStyleColor;

    Paint mPaint;

    ProgressViewController mIndicatorController;

    private boolean mHasAnimation;


    public ProgressView(Context context) {
        super(context);
        init(null, 0);
    }

    public ProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public ProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ProgressView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs, defStyleAttr);
    }

    private void init(AttributeSet attrs, int defStyle) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.ProgressView);
        mStyleId =a.getInt(R.styleable.ProgressView_style, ProgressStyle.BallPulse);
        mStyleColor =a.getColor(R.styleable.ProgressView_style_color, Color.WHITE);
        a.recycle();
        mPaint=new Paint();
        mPaint.setColor(mStyleColor);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
        applyIndicator();
    }

    public void setStyleId(int indicatorId){
        mStyleId = indicatorId;
        applyIndicator();
    }

    public void setStyleColor(int color){
        mStyleColor = color;
        mPaint.setColor(mStyleColor);
        this.invalidate();
    }

    private void applyIndicator(){
        switch (mStyleId){
            case ProgressStyle.BallPulse:
                mIndicatorController=new BallPulseController();
                break;
            case ProgressStyle.BallGridPulse:
                mIndicatorController=new BallGridPulseController();
                break;
            case ProgressStyle.BallClipRotate:
                mIndicatorController=new BallClipRotateController();
                break;
            case ProgressStyle.BallClipRotatePulse:
                mIndicatorController=new BallClipRotatePulseController();
                break;
            case ProgressStyle.SquareSpin:
                mIndicatorController=new SquareSpinController();
                break;
            case ProgressStyle.BallClipRotateMultiple:
                mIndicatorController=new BallClipRotateMultipleController();
                break;
            case ProgressStyle.BallPulseRise:
                mIndicatorController=new BallPulseRiseController();
                break;
            case ProgressStyle.BallRotate:
                mIndicatorController=new BallRotateController();
                break;
            case ProgressStyle.CubeTransition:
                mIndicatorController=new CubeTransitionController();
                break;
            case ProgressStyle.BallZigZag:
                mIndicatorController=new BallZigZagController();
                break;
            case ProgressStyle.BallZigZagDeflect:
                mIndicatorController=new BallZigZagDeflectController();
                break;
            case ProgressStyle.BallTrianglePath:
                mIndicatorController=new BallTrianglePathController();
                break;
            case ProgressStyle.BallScale:
                mIndicatorController=new BallScaleController();
                break;
            case ProgressStyle.LineScale:
                mIndicatorController=new LineScaleController();
                break;
            case ProgressStyle.LineScaleParty:
                mIndicatorController=new LineScalePartyController();
                break;
            case ProgressStyle.BallScaleMultiple:
                mIndicatorController=new BallScaleMultipleController();
                break;
            case ProgressStyle.BallPulseSync:
                mIndicatorController=new BallPulseSyncController();
                break;
            case ProgressStyle.BallBeat:
                mIndicatorController=new BallBeatController();
                break;
            case ProgressStyle.LineScalePulseOut:
                mIndicatorController=new LineScalePulseOutController();
                break;
            case ProgressStyle.LineScalePulseOutRapid:
                mIndicatorController=new LineScalePulseOutRapidController();
                break;
            case ProgressStyle.BallScaleRipple:
                mIndicatorController=new BallScaleRippleController();
                break;
            case ProgressStyle.BallScaleRippleMultiple:
                mIndicatorController=new BallScaleRippleMultipleController();
                break;
            case ProgressStyle.BallSpinFadeLoader:
                mIndicatorController=new BallSpinFadeLoaderController();
                break;
            case ProgressStyle.LineSpinFadeLoader:
                mIndicatorController=new LineSpinFadeLoaderController();
                break;
            case ProgressStyle.TriangleSkewSpin:
                mIndicatorController=new TriangleSkewSpinController();
                break;
            case ProgressStyle.Pacman:
                mIndicatorController=new Pacman();
                break;
            case ProgressStyle.BallGridBeat:
                mIndicatorController=new BallGridBeatController();
                break;
            case ProgressStyle.SemiCircleSpin:
                mIndicatorController=new SemiCircleSpinController();
                break;
        }
        mIndicatorController.setTarget(this);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width  = measureDimension(dp2px(DEFAULT_SIZE), widthMeasureSpec);
        int height = measureDimension(dp2px(DEFAULT_SIZE), heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    private int measureDimension(int defaultSize,int measureSpec){
        int result = defaultSize;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else if (specMode == MeasureSpec.AT_MOST) {
            result = Math.min(defaultSize, specSize);
        } else {
            result = defaultSize;
        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawIndicator(canvas);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (!mHasAnimation){
            mHasAnimation=true;
            applyAnimation();
        }
    }

    @Override
    public void setVisibility(int v) {
        if (getVisibility() != v) {
            super.setVisibility(v);
            if (v == GONE || v == INVISIBLE) {
                mIndicatorController.setAnimationStatus(ProgressViewController.AnimStatus.END);
            } else {
                mIndicatorController.setAnimationStatus(ProgressViewController.AnimStatus.START);
            }
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mIndicatorController.setAnimationStatus(ProgressViewController.AnimStatus.CANCEL);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mIndicatorController.setAnimationStatus(ProgressViewController.AnimStatus.START);
    }

    void drawIndicator(Canvas canvas){
        mIndicatorController.draw(canvas, mPaint);
    }

    void applyAnimation(){
        mIndicatorController.initAnimation();
    }

    private int dp2px(int dpValue) {
        return (int) getContext().getResources().getDisplayMetrics().density * dpValue;
    }


}
