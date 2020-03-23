package com.hx.wjbaseproject.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ProgressBar;

import com.hx.wjbaseproject.R;

/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:16/10/7 上午10:39
 * 描述:
 */
public class BGAProgressBar extends ProgressBar {
    private static final String TAG = BGAProgressBar.class.getSimpleName();

    private Paint mPaint;
    private Paint mSubTextPaint;
    private Paint mHeaderTextPaint;
    private Mode mMode;
    private int mTextColor;
    private int mTextSize;
    private int mTextMargin;
    private int mReachedColor;
    private int mReachedHeight;
    private int mUnReachedColor;
    private int mUnReachedHeight;
    private boolean mIsCapRounded;
    private boolean mIsHiddenText;

    private int mRadius;

    private int mMaxUnReachedEndX;
    private int mMaxStrokeWidth;

    private int mTextHeight;
    private int mSubTextHeight;
    private int mTextWidth;

    private RectF mArcRectF;
    private Rect mTextRect = new Rect();
    private Rect mSubTextRect = new Rect();
    private Rect mHeaderTextRect = new Rect();

    private String mText;
    private String mCenterText;
    private String mSubText;
    private int mSubTextSize;
    private String mHeaderText;
    private int mHeaderTextHeight;
    private int mHeaderTextSize;
    private int mHeaderTextColor;

    public BGAProgressBar(Context context) {
        this(context, null);
    }

    public BGAProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.progressBarStyle);
    }

    public BGAProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initDefaultAttrs(context);
        initCustomAttrs(context, attrs);

        mMaxStrokeWidth = Math.max(mReachedHeight, mUnReachedHeight);
        mHeaderTextPaint.setColor(mHeaderTextColor);
    }

    private void initDefaultAttrs(Context context) {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mSubTextPaint = new Paint(mPaint);
        mHeaderTextPaint = new Paint(mPaint);

        mMode = Mode.System;
        mTextColor = Color.parseColor("#70A800");
        mTextSize = BGAProgressBar.sp2px(context, 10);
        mSubTextSize = BGAProgressBar.sp2px(context, 8);
        mTextMargin = BGAProgressBar.dp2px(context, 4);
        mReachedColor = Color.parseColor("#70A800");
        mReachedHeight = BGAProgressBar.dp2px(context, 2);
        mUnReachedColor = Color.parseColor("#CCCCCC");
        mUnReachedHeight = BGAProgressBar.dp2px(context, 1);
        mHeaderTextSize = BGAProgressBar.sp2px(context, 8);
        mHeaderTextColor = Color.parseColor("#FFB53A");

        mIsCapRounded = false;
        mIsHiddenText = false;

        mRadius = BGAProgressBar.dp2px(context, 16);
    }

    private void initCustomAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.BGAProgressBar);
        final int N = typedArray.getIndexCount();
        for (int i = 0; i < N; i++) {
            initAttr(typedArray.getIndex(i), typedArray);
        }
        typedArray.recycle();
    }

    protected void initAttr(int attr, TypedArray typedArray) {
        if (attr == R.styleable.BGAProgressBar_bga_pb_mode) {
            int ordinal = typedArray.getInt(attr, Mode.System.ordinal());
            mMode = Mode.values()[ordinal];
        } else if (attr == R.styleable.BGAProgressBar_bga_pb_textColor) {
            mTextColor = typedArray.getColor(attr, mTextColor);
        } else if (attr == R.styleable.BGAProgressBar_bga_pb_textSize) {
            mTextSize = typedArray.getDimensionPixelOffset(attr, mTextSize);
        } else if (attr == R.styleable.BGAProgressBar_bga_pb_textMargin) {
            mTextMargin = typedArray.getDimensionPixelOffset(attr, mTextMargin);
        } else if (attr == R.styleable.BGAProgressBar_bga_pb_reachedColor) {
            mReachedColor = typedArray.getColor(attr, mReachedColor);
        } else if (attr == R.styleable.BGAProgressBar_bga_pb_reachedHeight) {
            mReachedHeight = typedArray.getDimensionPixelOffset(attr, mReachedHeight);
        } else if (attr == R.styleable.BGAProgressBar_bga_pb_unReachedColor) {
            mUnReachedColor = typedArray.getColor(attr, mUnReachedColor);
        } else if (attr == R.styleable.BGAProgressBar_bga_pb_unReachedHeight) {
            mUnReachedHeight = typedArray.getDimensionPixelOffset(attr, mUnReachedHeight);
        } else if (attr == R.styleable.BGAProgressBar_bga_pb_isCapRounded) {
            mIsCapRounded = typedArray.getBoolean(attr, mIsCapRounded);
            if (mIsCapRounded) {
                mPaint.setStrokeCap(Paint.Cap.ROUND);
            }
        } else if (attr == R.styleable.BGAProgressBar_bga_pb_isHiddenText) {
            mIsHiddenText = typedArray.getBoolean(attr, mIsHiddenText);
        } else if (attr == R.styleable.BGAProgressBar_bga_pb_radius) {
            mRadius = typedArray.getDimensionPixelOffset(attr, mRadius);
        } else if (attr == R.styleable.BGAProgressBar_bga_pb_sub_text) {
            mSubText = typedArray.getString(R.styleable.BGAProgressBar_bga_pb_sub_text);
        } else if (attr == R.styleable.BGAProgressBar_bga_pb_sub_text_size) {
            mSubTextSize = typedArray.getDimensionPixelOffset(R.styleable.BGAProgressBar_bga_pb_sub_text_size, mSubTextSize);
        } else if (attr == R.styleable.BGAProgressBar_bga_pb_header_text) {
            mHeaderText = typedArray.getString(R.styleable.BGAProgressBar_bga_pb_header_text);
        } else if (attr == R.styleable.BGAProgressBar_bga_pb_header_text_size) {
            mHeaderTextSize = typedArray.getDimensionPixelOffset(R.styleable.BGAProgressBar_bga_pb_header_text_size, mHeaderTextSize);
        } else if (attr == R.styleable.BGAProgressBar_bga_pb_header_text_color) {
            mHeaderTextColor = typedArray.getColor(R.styleable.BGAProgressBar_bga_pb_header_text_color, mHeaderTextColor);
        } else if (attr == R.styleable.BGAProgressBar_bga_pb_center_text) {
            mCenterText = typedArray.getString(R.styleable.BGAProgressBar_bga_pb_center_text);
        }
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mMode == Mode.System) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        } else if (mMode == Mode.Horizontal) {
            calculateTextWidthAndHeight();

            int width = MeasureSpec.getSize(widthMeasureSpec);

            int expectHeight = getPaddingTop() + getPaddingBottom();
            if (mIsHiddenText) {
                expectHeight += Math.max(mReachedHeight, mUnReachedHeight);
            } else {
                expectHeight += Math.max(mTextHeight, Math.max(mReachedHeight, mUnReachedHeight));
            }
            int height = resolveSize(expectHeight, heightMeasureSpec);
            setMeasuredDimension(width, height);

            mMaxUnReachedEndX = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
        } else if (mMode == Mode.Circle) {
            int expectSize = mRadius * 2 + mMaxStrokeWidth + getPaddingLeft() + getPaddingRight();
            int width = resolveSize(expectSize, widthMeasureSpec);
            int height = resolveSize(expectSize, heightMeasureSpec);
            expectSize = Math.min(width, height);

            mRadius = (expectSize - getPaddingLeft() - getPaddingRight() - mMaxStrokeWidth) / 2;
            if (mArcRectF == null) {
                mArcRectF = new RectF();
            }
            mArcRectF.set(0, 0, mRadius * 2, mRadius * 2);

            setMeasuredDimension(expectSize, expectSize);
        } else if (mMode == Mode.Comet) {
            // TODO
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        } else if (mMode == Mode.Wave) {
            // TODO
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        if (mMode == Mode.System) {
            super.onDraw(canvas);
        } else if (mMode == Mode.Horizontal) {
            onDrawHorizontal(canvas);
        } else if (mMode == Mode.Circle) {
            onDrawCircle(canvas);
        } else if (mMode == Mode.Comet) {
            // TODO
            super.onDraw(canvas);
        } else if (mMode == Mode.Wave) {
            // TODO
            super.onDraw(canvas);
        }
    }

    private void onDrawHorizontal(Canvas canvas) {
        canvas.save();
        canvas.translate(getPaddingLeft(), getMeasuredHeight() / 2);

        float reachedRatio = getProgress() * 1.0f / getMax();
        float reachedEndX = reachedRatio * mMaxUnReachedEndX;
        if (mIsHiddenText) {
            if (reachedEndX > mMaxUnReachedEndX) {
                reachedEndX = mMaxUnReachedEndX;
            }
            if (reachedEndX > 0) {
                mPaint.setColor(mReachedColor);
                mPaint.setStrokeWidth(mReachedHeight);
                mPaint.setStyle(Paint.Style.STROKE);
                canvas.drawLine(0, 0, reachedEndX, 0, mPaint);
            }

            float unReachedStartX = reachedEndX;
            if (mIsCapRounded) {
                unReachedStartX += (mReachedHeight + mUnReachedHeight) * 1.0f / 2;
            }
            if (unReachedStartX < mMaxUnReachedEndX) {
                mPaint.setColor(mUnReachedColor);
                mPaint.setStrokeWidth(mUnReachedHeight);
                mPaint.setStyle(Paint.Style.STROKE);
                canvas.drawLine(unReachedStartX, 0, mMaxUnReachedEndX, 0, mPaint);
            }
        } else {
            calculateTextWidthAndHeight();
            int maxReachedEndX = mMaxUnReachedEndX - mTextWidth - mTextMargin;
            if (reachedEndX > maxReachedEndX) {
                reachedEndX = maxReachedEndX;
            }
            if (reachedEndX > 0) {
                mPaint.setColor(mReachedColor);
                mPaint.setStrokeWidth(mReachedHeight);
                mPaint.setStyle(Paint.Style.STROKE);

                canvas.drawLine(0, 0, reachedEndX, 0, mPaint);
            }

            mPaint.setTextAlign(Paint.Align.LEFT);
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setColor(mTextColor);
            float textStartX = reachedEndX > 0 ? reachedEndX + mTextMargin : reachedEndX;
            canvas.drawText(mText, textStartX, mTextHeight / 2, mPaint);

            float unReachedStartX = textStartX + mTextWidth + mTextMargin;
            if (unReachedStartX < mMaxUnReachedEndX) {
                mPaint.setColor(mUnReachedColor);
                mPaint.setStrokeWidth(mUnReachedHeight);
                mPaint.setStyle(Paint.Style.STROKE);
                canvas.drawLine(unReachedStartX, 0, mMaxUnReachedEndX, 0, mPaint);
            }
        }

        canvas.restore();
    }

    private void onDrawCircle(Canvas canvas) {
        canvas.save();
        canvas.translate(getPaddingLeft() + mMaxStrokeWidth / 2, getPaddingTop() + mMaxStrokeWidth / 2);

        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(mUnReachedColor);
        mPaint.setStrokeWidth(mUnReachedHeight);
        canvas.drawCircle(mRadius, mRadius, mRadius, mPaint);

        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(mReachedColor);
        mPaint.setStrokeWidth(mReachedHeight);
        float sweepAngle = getProgress() * 1.0f / getMax() * 360;
        canvas.drawArc(mArcRectF, -90, sweepAngle, false, mPaint);

        if (!mIsHiddenText) {
            calculateTextWidthAndHeight();
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setColor(mTextColor);
            mPaint.setTextAlign(Paint.Align.CENTER);
            mPaint.setFakeBoldText(true);
            canvas.drawText(mText, mRadius, mRadius + dp2px(getContext(), 4), mPaint);

            mSubTextPaint.setTextAlign(Paint.Align.CENTER);
            mHeaderTextPaint.setTextAlign(Paint.Align.CENTER);
            if (!TextUtils.isEmpty(mSubText)) {
                canvas.drawText(mSubText, mRadius, mRadius + mTextHeight + dp2px(getContext(), 6), mSubTextPaint);
            }
            if (!TextUtils.isEmpty(mHeaderText)) {
                canvas.drawText(mHeaderText, mRadius, mRadius - mTextHeight - dp2px(getContext(), 4), mHeaderTextPaint);
            }
        }

        canvas.restore();
    }

    private void calculateTextWidthAndHeight() {
        //fix by Michael 修改参数溢出问题。
        //mText = String.format("%d", getProgress() * 100 / getMax()) + "%";
        if (!TextUtils.isEmpty(mCenterText)) {
            mText = mCenterText;
        }else {
            mText = String.format("%d", (int) (getProgress() * 1.0f / getMax() * 100)) + "%";
        }

        mPaint.setTextSize(mTextSize);
        mSubTextPaint.setTextSize(mSubTextSize);
        mHeaderTextPaint.setTextSize(mHeaderTextSize);
        mPaint.setStyle(Paint.Style.FILL);

        mPaint.getTextBounds(mText, 0, mText.length(), mTextRect);
        mTextWidth = mTextRect.width();
        mTextHeight = mTextRect.height();

        if (!TextUtils.isEmpty(mSubText)) {
            mSubTextPaint.getTextBounds(mSubText, 0, mSubText.length(), mSubTextRect);
            mSubTextHeight = mSubTextRect.height();
        }

        if (!TextUtils.isEmpty(mHeaderText)) {
            mHeaderTextPaint.getTextBounds(mHeaderText, 0, mHeaderText.length(), mHeaderTextRect);
            mHeaderTextHeight = mHeaderTextRect.height();
        }
    }

    public void setHeaderText(String headerText) {
        this.mHeaderText = headerText;
        invalidate();
    }

    public void setHeaderTextColor(int headerTextColor) {
        this.mHeaderTextColor = headerTextColor;
        mHeaderTextPaint.setColor(mHeaderTextColor);
        invalidate();
    }

    public void setReachedColor(int color) {
        this.mReachedColor = color;
        invalidate();
    }

    public void setUnReachedColor(int color) {
        this.mUnReachedColor = color;
        invalidate();
    }

    public void setCenterText(String centerText) {
        this.mCenterText = centerText;
        invalidate();
    }

    private enum Mode {
        System,
        Horizontal,
        Circle,
        Comet,
        Wave
    }

    public static int dp2px(Context context, float dpValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, context.getResources().getDisplayMetrics());
    }

    public static int sp2px(Context context, float spValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spValue, context.getResources().getDisplayMetrics());
    }
}