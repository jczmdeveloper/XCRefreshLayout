package com.czm.xcrefreshlayout.progressview.controller;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import com.czm.xcrefreshlayout.progressview.ProgressViewController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by caizhiming on 2016/1/14.
 */
public class SemiCircleSpinController extends ProgressViewController {


    @Override
    public void draw(Canvas canvas, Paint paint) {
        RectF rectF=new RectF(0,0,getWidth(),getHeight());
        canvas.drawArc(rectF,-60,120,false,paint);
    }

    @Override
    public List<Animator> createAnimation() {
        List<Animator> animators=new ArrayList<>();
        ObjectAnimator rotateAnim=ObjectAnimator.ofFloat(getTarget(),"rotation",0,180,360);
        rotateAnim.setDuration(600);
        rotateAnim.setRepeatCount(-1);
        rotateAnim.start();
        animators.add(rotateAnim);
        return animators;
    }


}
