package com.zhengxyou.progressdemo;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

/**
 * 父布局建议加    android:clipChildren="false"
 *
 * @author zhengxyou
 * @version 1.0 2017年8月28日
 */
public class FractionalNumberProgressBarView extends View {
    private int mMax = 100;//最大值
    private int mProgress = 0;//当前进度
    private float mStrokeWidth = 3;//dp
    private float mMaxTextSize = 14;//sp
    private float mProgressTextSize = 12;
    private float mLineCircleRadius = 3;//dp

    private int mFinishColor = Color.WHITE;
    private int mUnFinishColor = 0X80ffffff;//白色50%透明度
    private int mCircleColor = Color.BLUE;
    private int mCirclrShadowColor = Color.WHITE;
    private int mMaxTextColor = Color.WHITE;
    private int mProgressTextColor = Color.WHITE;

    private float mCircleRadius;
    private float mCircleShadowRadius;

    private TextPaint mProgressTextPaint;
    private float mProgressTextWidth;
    private float mProgressTextHeight;

    private TextPaint mMaxTextPaint;
    private float mMaxTextWidth;
    private float mMaxTextHeight;

    private Paint mStrokePaint;
    private Paint mcirclePaint;
    private Paint mLinePaint;
    private Path path;

    private String mMaxString;

    private RectF oval;

    private static final String INSTANCE_STATE = "saved_instance";
    private static final String INSTANCE_STROKE_WIDTH = "stroke_width";
    private static final String INSTANCE_PROGRESS = "progress";
    private static final String INSTANCE_MAX = "max";
    private static final String INSTANCE_FINISHED_STROKE_COLOR = "finished_stroke_color";
    private static final String INSTANCE_UNFINISHED_STROKE_COLOR = "unfinished_stroke_color";
    private static final String INSTANCE_MAX_TEXTSIZE = "max_text_size";
    private static final String INSTANCE_PROGRESS_TEXTSIZE = "progress_text_size";
    private static final String INSTANCE_PROGRESS_TEXTCOLOR = "progress_text_color";
    private static final String INSTANCE_MAX_TEXTCOLOR = "max_text_color";
    private static final String INSTANCE_CIRCLE_COLOR = "circle_color";
    private static final String INSTANCE_CIRCLE_SHADOW_COLOR = "circle_shadow_color";


    public FractionalNumberProgressBarView(Context context) {
        super(context);
        init(null, 0, context);
    }

    public FractionalNumberProgressBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0, context);
    }

    public FractionalNumberProgressBarView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle, context);
    }

    private void init(AttributeSet attrs, int defStyle, Context context) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.FractionalNumberProgressBarView, defStyle, 0);

        mMax = a.getInteger(
                R.styleable.FractionalNumberProgressBarView_max, mMax);

        mProgress = a.getInteger(
                R.styleable.FractionalNumberProgressBarView_progress, mProgress);

        mStrokeWidth = a.getDimension(
                R.styleable.FractionalNumberProgressBarView_strokeWidth,
                DpPxUtils.dp2px(context, mStrokeWidth));

        mMaxTextSize = a.getDimension(R.styleable.FractionalNumberProgressBarView_maxValueSize,
                DpPxUtils.dp2px(context, mMaxTextSize));

        mProgressTextSize = a.getDimension(R.styleable.FractionalNumberProgressBarView_progressValueSize,
                DpPxUtils.dp2px(context, mProgressTextSize));

        mFinishColor = a.getColor(
                R.styleable.FractionalNumberProgressBarView_finishColor,
                mFinishColor);

        mUnFinishColor = a.getColor(
                R.styleable.FractionalNumberProgressBarView_unFinishColor,
                mUnFinishColor);

        mCircleColor = a.getColor(
                R.styleable.FractionalNumberProgressBarView_progressCircleColor,
                mCircleColor);

        mCirclrShadowColor = a.getColor(
                R.styleable.FractionalNumberProgressBarView_progressCircleShadowColor,
                mCirclrShadowColor);

        mMaxTextColor = a.getColor(
                R.styleable.FractionalNumberProgressBarView_maxValueColor,
                mMaxTextColor);

        mProgressTextColor = a.getColor(
                R.styleable.FractionalNumberProgressBarView_progressValueColor,
                mProgressTextColor);

        mCircleRadius = mStrokeWidth / 2;
        mCircleShadowRadius = mStrokeWidth;
        mMaxString = String.valueOf(getMax());
        mLineCircleRadius = DpPxUtils.dp2px(context, mLineCircleRadius);
        a.recycle();

        initPaint();
    }

    private void initPaint() {
        mMaxTextPaint = new TextPaint();
        mMaxTextPaint.setColor(mMaxTextColor);
        mMaxTextPaint.setTextSize(mMaxTextSize);
        mMaxTextPaint.setAntiAlias(true);
        mMaxTextPaint.setTextAlign(Paint.Align.RIGHT);

        mProgressTextPaint = new TextPaint();
        mProgressTextPaint.setColor(mProgressTextColor);
        mProgressTextPaint.setTextSize(mProgressTextSize);
        mProgressTextPaint.setAntiAlias(true);
        mProgressTextPaint.setTextAlign(Paint.Align.CENTER);

        mStrokePaint = new Paint();
        mStrokePaint.setStrokeWidth(mStrokeWidth);
        mStrokePaint.setAntiAlias(true);
        mStrokePaint.setStyle(Paint.Style.STROKE);
        mStrokePaint.setStrokeCap(Paint.Cap.ROUND);

        mcirclePaint = new Paint();
        mcirclePaint.setAntiAlias(true);
        mcirclePaint.setStyle(Paint.Style.FILL);

        mLinePaint = new Paint();
        mLinePaint.setColor(mProgressTextColor);
        mLinePaint.setAntiAlias(true);
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setStrokeWidth(2f);

        path = new Path();
        oval = new RectF();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void invalidateTextPaintAndMeasurements() {
        mMaxTextWidth = mMaxTextPaint.measureText(mMaxString);
        mMaxTextHeight = mMaxTextPaint.descent() + mMaxTextPaint.ascent();

        mProgressTextWidth = mProgressTextPaint.measureText(String.valueOf(getProgress()));
        mProgressTextHeight = mProgressTextPaint.descent() + mProgressTextPaint.ascent();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        invalidateTextPaintAndMeasurements();
        float centerY = getHeight() / 2;

        float progressFloat = getWidth() * getProgress() / (float) getMax();

        //未完成
        mStrokePaint.setColor(mUnFinishColor);
        canvas.drawLine(0, centerY, getWidth(), centerY, mStrokePaint);

        //已完成
        mStrokePaint.setColor(mFinishColor);
        canvas.drawLine(0, centerY, progressFloat, centerY, mStrokePaint);

        //圆形进度
        //阴影
        mcirclePaint.setColor(mCirclrShadowColor);
        canvas.drawCircle(progressFloat, centerY, mCircleShadowRadius, mcirclePaint);
        //进度圆
        mcirclePaint.setColor(mCircleColor);
        canvas.drawCircle(progressFloat, centerY, mCircleRadius, mcirclePaint);

        //底部最大值
        canvas.drawText(mMaxString, getWidth(), centerY + mStrokeWidth - mMaxTextHeight * 2, mMaxTextPaint);

        //进度值
        float progressTextCenterY = centerY - mStrokeWidth * 2 + mProgressTextHeight;
        canvas.drawText(String.valueOf(mProgress), progressFloat, progressTextCenterY, mProgressTextPaint);

        path.reset();
        //进度值边框
        float halfWith = mProgressTextWidth / 2;
        float sideLength = mProgressTextWidth / 4;//三角形边长
        float halfSideLength = sideLength / 2;
        float bottomLineY = progressTextCenterY - mProgressTextHeight / 2;
        //底部线
        path.moveTo(progressFloat - halfWith - mLineCircleRadius, bottomLineY);
        path.lineTo(progressFloat - halfSideLength, bottomLineY);
        path.lineTo(progressFloat, bottomLineY + halfSideLength);
        path.lineTo(progressFloat + halfSideLength, bottomLineY);
        path.lineTo(progressFloat + halfWith + mLineCircleRadius, bottomLineY);

        float centerRightBottomCircleX = progressFloat + halfWith + mLineCircleRadius;
        float centerRightBottomCircleY = bottomLineY - mLineCircleRadius;

        oval.set(centerRightBottomCircleX - mLineCircleRadius
                , centerRightBottomCircleY - mLineCircleRadius
                , centerRightBottomCircleX + mLineCircleRadius
                , centerRightBottomCircleY + mLineCircleRadius);
        path.addArc(oval, 0, 90);

        //右边边框
        float rightY = centerRightBottomCircleY - mLineCircleRadius + mProgressTextHeight;
        float rightX = centerRightBottomCircleX + mLineCircleRadius;
        path.moveTo(rightX, centerRightBottomCircleY);
        path.lineTo(rightX, rightY);

        oval.set(centerRightBottomCircleX - mLineCircleRadius
                , rightY - mLineCircleRadius
                , centerRightBottomCircleX + mLineCircleRadius
                , rightY + mLineCircleRadius);
        path.addArc(oval, 270, 90);

        //顶部边框
        float leftX = progressFloat - halfWith - mLineCircleRadius;

        path.moveTo(centerRightBottomCircleX, rightY - mLineCircleRadius);
        path.lineTo(leftX, rightY - mLineCircleRadius);

        //左上圆
        oval.set(leftX - mLineCircleRadius
                , rightY - mLineCircleRadius
                , leftX + mLineCircleRadius
                , rightY + mLineCircleRadius);
        path.addArc(oval, 180, 90);

        //左边线
        float leftBottomY = bottomLineY - mLineCircleRadius;
        path.moveTo(leftX - mLineCircleRadius, rightY);
        path.lineTo(leftX - mLineCircleRadius, leftBottomY);

        //左下圆
        oval.set(leftX - mLineCircleRadius
                , leftBottomY - mLineCircleRadius
                , leftX + mLineCircleRadius
                , leftBottomY + mLineCircleRadius);
        path.addArc(oval, 90, 90);

        canvas.drawPath(path, mLinePaint);
    }

    public int getMax() {
        return mMax;
    }

    public void setMax(int mMax) {
        if (mMax > 0) {
            this.mMax = mMax;
            mMaxString = String.valueOf(getMax());
            invalidate();
        }
    }

    public int getProgress() {
        return mProgress;
    }

    public void setProgress(int mProgress) {
        this.mProgress = mProgress;
        if (this.mProgress > getMax()) {
//            this.mProgress %= getMax();
            this.mProgress = getMax();
        }
        invalidate();
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        final Bundle bundle = new Bundle();
        bundle.putParcelable(INSTANCE_STATE, super.onSaveInstanceState());
        bundle.putFloat(INSTANCE_STROKE_WIDTH, mStrokeWidth);
        bundle.putInt(INSTANCE_PROGRESS, getProgress());
        bundle.putInt(INSTANCE_MAX, getMax());
        bundle.putInt(INSTANCE_FINISHED_STROKE_COLOR, mFinishColor);
        bundle.putInt(INSTANCE_UNFINISHED_STROKE_COLOR, mUnFinishColor);
        bundle.putFloat(INSTANCE_MAX_TEXTSIZE, mMaxTextSize);
        bundle.putFloat(INSTANCE_PROGRESS_TEXTSIZE, mProgressTextSize);
        bundle.putInt(INSTANCE_PROGRESS_TEXTCOLOR, mProgressTextColor);
        bundle.putInt(INSTANCE_MAX_TEXTCOLOR, mMaxTextColor);
        bundle.putInt(INSTANCE_CIRCLE_COLOR, mCircleColor);
        bundle.putInt(INSTANCE_CIRCLE_SHADOW_COLOR, mCirclrShadowColor);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            final Bundle bundle = (Bundle) state;
            mStrokeWidth = bundle.getFloat(INSTANCE_STROKE_WIDTH);
            setMax(bundle.getInt(INSTANCE_MAX));
            setProgress(bundle.getInt(INSTANCE_PROGRESS));
            mFinishColor = bundle.getInt(INSTANCE_FINISHED_STROKE_COLOR);
            mUnFinishColor = bundle.getInt(INSTANCE_UNFINISHED_STROKE_COLOR);
            mMaxTextSize = bundle.getFloat(INSTANCE_MAX_TEXTSIZE);
            mProgressTextSize = bundle.getFloat(INSTANCE_PROGRESS_TEXTSIZE);
            mProgressTextColor = bundle.getInt(INSTANCE_PROGRESS_TEXTCOLOR);
            mMaxTextColor = bundle.getInt(INSTANCE_MAX_TEXTCOLOR);
            mCircleColor = bundle.getInt(INSTANCE_CIRCLE_COLOR);
            mCirclrShadowColor = bundle.getInt(INSTANCE_CIRCLE_SHADOW_COLOR);
            super.onRestoreInstanceState(bundle.getParcelable(INSTANCE_STATE));
            return;
        }
        super.onRestoreInstanceState(state);
    }
}