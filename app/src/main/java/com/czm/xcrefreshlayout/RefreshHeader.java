package com.czm.xcrefreshlayout;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.czm.util.DensityUtil;
import com.czm.xcrefreshlayout.progressview.ProgressStyle;
import com.czm.xcrefreshlayout.progressview.ProgressView;

/**
 * Created by caizhiming on 2016/1/14.
 */
public class RefreshHeader extends LinearLayout {

    private TextView mStatusTextView;
    private ProgressView mProgressView;
    private ImageView mStatusImageView;
    private Animation mRotateUpAnim;
    private Animation mRotateDownAnim;
    private float mHeaderHeight = 0;
    private String[] mTxtStatus = new String[]
            {
                    "下拉刷新", "釋放立即刷新",
                    "正在刷新...", "刷新完成"
            };
    private int mStatus = XCRefreshLayout.REFRESH_STATUS_PULL_REFRESH;
    private static final int ROTATE_ANIM_DURATION = 200;
    public RefreshHeader(Context context) {
        this(context, null);
    }

    public RefreshHeader(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefreshHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    public float getHeaderHeight() {
        return this.mHeaderHeight;
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        mHeaderHeight = DensityUtil.dip2px(context, 80);
        //初始化自己
        LinearLayout.LayoutParams lpRoot = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        this.setOrientation(LinearLayout.HORIZONTAL);
        this.setGravity(Gravity.CENTER_HORIZONTAL);
        this.setLayoutParams(lpRoot);

        //add progress view
        mProgressView = new ProgressView(context);
        mProgressView.setStyleColor(0xFFFFFFFF);
        mProgressView.setStyleId(ProgressStyle.BallSpinFadeLoader);
        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(
                DensityUtil.dip2px(context, 20), DensityUtil.dip2px(context, 20));
        lp2.gravity = Gravity.BOTTOM;
        lp2.bottomMargin = (int) (mHeaderHeight / 5);
        this.addView(mProgressView, lp2);
        mProgressView.setVisibility(View.GONE);

        //add arrow ImageView
        mStatusImageView = new ImageView(context);
        mStatusImageView.setImageResource(R.drawable.arrow_down);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                DensityUtil.dip2px(context, 20), DensityUtil.dip2px(context, 20));
        lp.gravity = Gravity.BOTTOM;
        lp.bottomMargin = (int) (mHeaderHeight / 5);
        this.addView(mStatusImageView, lp);

        //add refresh text status TextView
        mStatusTextView = new TextView(context);
        mStatusTextView.setText(mTxtStatus[0]);
        mStatusTextView.setTextColor(Color.WHITE);
        lp = new LinearLayout.LayoutParams(
                DensityUtil.dip2px(context, 100), LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.BOTTOM;
        lp.leftMargin = DensityUtil.dip2px(context, 5);
        lp.bottomMargin = (int) (mHeaderHeight / 10);
        mStatusTextView.setGravity(Gravity.CENTER);
        this.addView(mStatusTextView, lp);

        mRotateUpAnim = new RotateAnimation(0.0f, -180.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        mRotateUpAnim.setDuration(ROTATE_ANIM_DURATION);
        mRotateUpAnim.setFillAfter(true);
        mRotateDownAnim = new RotateAnimation(-180.0f, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        mRotateDownAnim.setDuration(ROTATE_ANIM_DURATION);
        mRotateDownAnim.setFillAfter(true);

    }

    public void updateRefreshStatus(int status) {
        mStatusTextView.setText(mTxtStatus[status]);
        if(status == XCRefreshLayout.REFRESH_STATUS_RELEASE_REFRESH
                && mStatus == XCRefreshLayout.REFRESH_STATUS_PULL_REFRESH){
            mStatusImageView.clearAnimation();
            mStatusImageView.startAnimation(mRotateUpAnim);
        }
        if(status == XCRefreshLayout.REFRESH_STATUS_PULL_REFRESH
                && mStatus == XCRefreshLayout.REFRESH_STATUS_RELEASE_REFRESH){
            mStatusImageView.clearAnimation();
            mStatusImageView.startAnimation(mRotateDownAnim);
        }
        if(status == XCRefreshLayout.REFRESH_STATUS_REFRESHING){
            mStatusImageView.clearAnimation();
            mStatusImageView.setVisibility(View.GONE);
            mProgressView.setVisibility(View.VISIBLE);
        }
        if(status == XCRefreshLayout.REFRESH_STATUS_REFRESH_FINISH){
            mProgressView.setVisibility(View.GONE);

        }
        if(status == XCRefreshLayout.REFRESH_STATUS_PULL_REFRESH
                && mStatus == XCRefreshLayout.REFRESH_STATUS_REFRESH_FINISH){
            mStatusImageView.setVisibility(View.VISIBLE);
            mStatusImageView.startAnimation(mRotateDownAnim);
        }
        mStatus = status;
    }
}
