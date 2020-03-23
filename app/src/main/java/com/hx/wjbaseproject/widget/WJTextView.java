package com.hx.wjbaseproject.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.TextView;

import com.hx.wjbaseproject.R;
import com.hx.wjbaseproject.util.ConvertUtils;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

@SuppressLint("AppCompatCustomView")
public class WJTextView extends TextView {

    private int mSolidColor = Color.TRANSPARENT;//填充颜色
    private int mBorderColor = Color.TRANSPARENT;//边框颜色
    private float mBorderWidth = 0;//边框宽度
    private float mCornerRadius = 0;//圆角大小

    private float mTopLeftRadius = 0;
    private float mTopRightRadius = 0;
    private float mBottomLeftRadius = 0;
    private float mBottomRightRadius = 0;

    private int mGradientStartColor, mGradientEndColor,mGradientCenterColor;
    private Integer mTextGradientStartColor, mTextGradientEndColor;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public WJTextView(Context context) {
        this(context, null);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public WJTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public WJTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, 0,0);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public WJTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.WJTextView, defStyleAttr, 0);
        if (attributes != null) {

            mSolidColor = attributes.getColor(R.styleable.WJTextView_wj_solid_color, Color.TRANSPARENT);
            mBorderColor = attributes.getColor(R.styleable.WJTextView_wj_border_color, Color.TRANSPARENT);
            mBorderWidth = attributes.getDimension(R.styleable.WJTextView_wj_border_width, 0);
            mCornerRadius = attributes.getDimension(R.styleable.WJTextView_wj_corner_radius, 0);
            mTopLeftRadius = attributes.getDimension(R.styleable.WJTextView_wj_top_left_radius, 0);
            mTopRightRadius = attributes.getDimension(R.styleable.WJTextView_wj_top_right_radius, 0);
            mBottomLeftRadius = attributes.getDimension(R.styleable.WJTextView_wj_bottom_left_radius, 0);
            mBottomRightRadius = attributes.getDimension(R.styleable.WJTextView_wj_bottom_right_radius, 0);
            mGradientStartColor = attributes.getColor(R.styleable.WJTextView_wj_gradient_start_color, Color.TRANSPARENT);
            mGradientCenterColor = attributes.getColor(R.styleable.WJTextView_wj_gradient_center_color, Color.TRANSPARENT);
            mGradientEndColor = attributes.getColor(R.styleable.WJTextView_wj_gradient_end_color, Color.TRANSPARENT);
            attributes.recycle();

            GradientDrawable gd = new GradientDrawable();//创建drawable
            if (mCornerRadius != 0) {
                gd.setCornerRadius(mCornerRadius);
            }else {
                //1、2两个参数表示左上角，3、4表示右上角，5、6表示右下角，7、8表示左下角
                gd.setCornerRadii(new float[] { mTopLeftRadius,
                        mTopLeftRadius, mTopRightRadius, mTopRightRadius, mBottomRightRadius,
                        mBottomRightRadius , mBottomLeftRadius, mBottomLeftRadius});
            }
            if (mGradientStartColor != Color.TRANSPARENT) {
                if (mGradientCenterColor != Color.TRANSPARENT) {
                    gd.setColors(new int[] { mGradientStartColor,
                            mGradientCenterColor, mGradientEndColor});
                }else {
                    gd.setColors(new int[] { mGradientStartColor, mGradientEndColor});
                }
                gd.setOrientation(GradientDrawable.Orientation.LEFT_RIGHT);
            }else {
                gd.setColor(mSolidColor);
            }
            if (mBorderWidth > 0) {
                gd.setStroke((int) mBorderWidth, mBorderColor);
            }

            this.setBackground(gd);
        }
    }

    /**
     * 设置渐变颜色
     * @param startColor 开始颜色
     * @param endColor   结束颜色
     */
    public void setGradientColor(int startColor, int endColor){
        this.mGradientStartColor = startColor;
        this.mGradientEndColor = endColor;
        GradientDrawable myGrad = (GradientDrawable) getBackground();
        myGrad.setColors(new int[] { mGradientStartColor, mGradientEndColor});
        myGrad.setOrientation(GradientDrawable.Orientation.LEFT_RIGHT);
        this.setBackground(myGrad);
    }

    /**
     * 设置渐变颜色
     * @param startColor 开始颜色
     * @param centerColor 中间颜色
     * @param endColor   结束颜色
     */
    public void setGradientColor(int startColor, int centerColor, int endColor){
        this.mGradientStartColor = startColor;
        this.mGradientCenterColor = centerColor;
        this.mGradientEndColor = endColor;
        GradientDrawable myGrad = (GradientDrawable) getBackground();
        myGrad.setColors(new int[] { mGradientStartColor,
                mGradientCenterColor, mGradientEndColor});
        myGrad.setOrientation(GradientDrawable.Orientation.LEFT_RIGHT);
        this.setBackground(myGrad);
    }

    public void setCornerRadius(float radius) {
        GradientDrawable myGrad = (GradientDrawable) getBackground();
        myGrad.setCornerRadius(ConvertUtils.dp2px(getContext(),radius));
        this.setBackground(myGrad);

    }

    public void setCornerRadii(float topLeftRadius, float topRightRadius, float btmRightRadius, float btmLeftRadius){

        mTopLeftRadius = ConvertUtils.dp2px(getContext(),topLeftRadius);
        mTopRightRadius = ConvertUtils.dp2px(getContext(),topRightRadius);
        mBottomRightRadius = ConvertUtils.dp2px(getContext(),btmRightRadius);
        mBottomLeftRadius = ConvertUtils.dp2px(getContext(),btmLeftRadius);

        GradientDrawable myGrad = (GradientDrawable) getBackground();
        //1、2两个参数表示左上角，3、4表示右上角，5、6表示右下角，7、8表示左下角
        myGrad.setCornerRadii(new float[] { mTopLeftRadius,
                mTopLeftRadius, mTopRightRadius, mTopRightRadius, mBottomRightRadius,
                mBottomRightRadius , mBottomLeftRadius, mBottomLeftRadius});
        this.setBackground(myGrad);
    }

    public void setSolidColor(int color){
        this.mSolidColor = color;
        GradientDrawable myGrad = (GradientDrawable) getBackground();
        myGrad.setColor(color);

    }

    public void setBorderColor(int color){
        this.mBorderColor = color;
        GradientDrawable myGrad = (GradientDrawable) getBackground();
        myGrad.setStroke((int) mBorderWidth, mBorderColor);
    }

    public void setTextGradientColor(Integer startColor, Integer endColor) {
        mTextGradientStartColor = startColor;
        mTextGradientEndColor = endColor;
    }

//    private LinearGradient mLinearGradient;
//    private Paint mPaint;
//    private int mViewWidth = 0;
//    private Rect mTextBound = new Rect();
//    @Override
//    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
//        if (mTextGradientStartColor != null && mTextGradientEndColor != null) {
//            mViewWidth = getMeasuredWidth();
//            mPaint = getPaint();
//            String mTipText = getText().toString();
//            mPaint.getTextBounds(mTipText, 0, mTipText.length(), mTextBound);
//            mLinearGradient = new LinearGradient(0, 0, mViewWidth, 0,
//                    new int[]{mTextGradientStartColor, mTextGradientEndColor},
//                    null, Shader.TileMode.REPEAT);
//            mPaint.setShader(mLinearGradient);
//            canvas.drawText(mTipText, getMeasuredWidth() / 2 - mTextBound.width() / 2, getMeasuredHeight() / 2 + mTextBound.height() / 2, mPaint);
//        }
//    }
}
