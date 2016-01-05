package com.lm.android.gankapp.component.infiniteindicator.indicator;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewConfigurationCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import com.lm.android.gankapp.R;

import static android.graphics.Paint.ANTI_ALIAS_FLAG;

public class LineIndicator extends View implements PageIndicator {
    private static final int INVALID_POINTER = -1;

    private float mWidth;
    private float mNormalHeight;
    private float mSelectHeight;
    private final Paint mPaintNormal = new Paint(ANTI_ALIAS_FLAG);
    private final Paint mPaintSelect = new Paint(ANTI_ALIAS_FLAG);
    private ViewPager mViewPager;
    private ViewPager.OnPageChangeListener mListener;
    private int mCurrentPage;
    private int mSnapPage;
    private float mPageOffset;
    private int mScrollState;
    private boolean mSnap = true;

    private int mTouchSlop;
    private float mLastMotionX = -1;
    private int mActivePointerId = INVALID_POINTER;
    private boolean mIsDragging;
    private int mRealCount;

    public LineIndicator(Context context) {
        super(context);
        initView(context, null, R.attr.vpiCirclePageIndicatorStyle);
    }

    public LineIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs, R.attr.vpiCirclePageIndicatorStyle);
    }

    public LineIndicator(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context, attrs, defStyle);
    }

    private void initView(Context context, AttributeSet attrs, int defStyle) {
        if (isInEditMode()) return;

        //Load defaults from resources
        final Resources res = getResources();
        final int defaultFillColor = res.getColor(R.color.colorPrimary);
        final int defaultOrientation = res.getInteger(R.integer.default_circle_indicator_orientation);
        final int defaultStrokeColor = res.getColor(R.color.default_circle_indicator_stroke_color);

        //Retrieve styles attributes
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LinePageIndicator, defStyle, 0);
        mPaintNormal.setStyle(Style.FILL);
        mPaintNormal.setColor(a.getColor(R.styleable.LinePageIndicator_normalColor, defaultStrokeColor));
        mPaintSelect.setStyle(Style.FILL);
        mPaintSelect.setColor(a.getColor(R.styleable.LinePageIndicator_selectColor, defaultFillColor));
        mWidth = a.getDimension(R.styleable.LinePageIndicator_lineIndicatorWidth, 20);
        mNormalHeight = a.getDimension(R.styleable.LinePageIndicator_normalHeight, 4);
        mSelectHeight = a.getDimension(R.styleable.LinePageIndicator_selectHeight, 10);

        Drawable background = a.getDrawable(R.styleable.LinePageIndicator_android_background);
        if (background != null) {
            setBackgroundDrawable(background);
        }

        a.recycle();

        final ViewConfiguration configuration = ViewConfiguration.get(context);
        mTouchSlop = ViewConfigurationCompat.getScaledPagingTouchSlop(configuration);
    }

    public void setRealViewCount(int viewCount) {
        mRealCount = viewCount;
    }

    public void setFillColor(int fillColor) {
        mPaintSelect.setColor(fillColor);
        invalidate();
    }

    public int getFillColor() {
        return mPaintSelect.getColor();
    }

    public void setStrokeColor(int strokeColor) {
        mPaintNormal.setColor(strokeColor);
        invalidate();
    }

    public int getStrokeColor() {
        return mPaintNormal.getColor();
    }

    public void setStrokeWidth(float strokeWidth) {
        mPaintNormal.setStrokeWidth(strokeWidth);
        invalidate();
    }

    public float getStrokeWidth() {
        return mPaintNormal.getStrokeWidth();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mViewPager == null) {
            return;
        }
        final int count = ((RecyleAdapter) mViewPager.getAdapter()).getRealCount();
        if (count <= 1) {
            return;
        }

        int viewWidth;
        int paddingLeft;
        int paddingRight;
        int paddingTop;
        int paddingBottom;
        viewWidth = getWidth();
        paddingLeft = getPaddingLeft();
        paddingRight = getPaddingRight();
        paddingTop = getPaddingTop();
        paddingBottom = getPaddingBottom();


        final float itemWidth = mWidth * 1.5f;
        final float shortOffset = getHeight() - paddingBottom;
        float itemOffset = paddingLeft + mWidth;
        itemOffset += ((viewWidth - paddingLeft - paddingRight) / 2.0f) - ((count * itemWidth) / 2.0f);

        float dX;
        float dY;

        //Draw normal
        for (int iLoop = 0; iLoop < count; iLoop++) {
            float drawLong = itemOffset + (iLoop * itemWidth);
            dX = drawLong;
            dY = shortOffset;

            float left = dX - mWidth / 2.0f;
            float right = dX + mWidth / 2.0f;
            float top = dY - mNormalHeight;
            float bottom = dY;
            canvas.drawRect(left, top, right, bottom, mPaintNormal);
        }

        //Draw select
        float cx = mSnapPage % count * itemWidth;
        dX = itemOffset + cx;
        dY = shortOffset;

        float left = dX - mWidth / 2.0f;
        float right = dX + mWidth / 2.0f;
        float top = dY - mSelectHeight;
        float bottom = dY;
        canvas.drawRect(left, top, right, bottom, mPaintSelect);
    }

    public boolean onTouchEvent(MotionEvent ev) {
        if (super.onTouchEvent(ev)) {
            return true;
        }
        if ((mViewPager == null) || (mViewPager.getAdapter().getCount() == 0)) {
            return false;
        }

        final int action = ev.getAction() & MotionEventCompat.ACTION_MASK;
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mActivePointerId = MotionEventCompat.getPointerId(ev, 0);
                mLastMotionX = ev.getX();
                break;

            case MotionEvent.ACTION_MOVE: {
                final int activePointerIndex = MotionEventCompat.findPointerIndex(ev, mActivePointerId);
                final float x = MotionEventCompat.getX(ev, activePointerIndex);
                final float deltaX = x - mLastMotionX;

                if (!mIsDragging) {
                    if (Math.abs(deltaX) > mTouchSlop) {
                        mIsDragging = true;
                    }
                }

                if (mIsDragging) {
                    mLastMotionX = x;
                    if (mViewPager.isFakeDragging() || mViewPager.beginFakeDrag()) {
                        mViewPager.fakeDragBy(deltaX);
                    }
                }

                break;
            }

            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if (!mIsDragging) {
                    final int count = mViewPager.getAdapter().getCount();
                    final int width = getWidth();
                    final float halfWidth = width / 2f;
                    final float sixthWidth = width / 6f;

                    if ((mCurrentPage > 0) && (ev.getX() < halfWidth - sixthWidth)) {
                        if (action != MotionEvent.ACTION_CANCEL) {
                            mViewPager.setCurrentItem(mCurrentPage - 1);
                        }
                        return true;
                    } else if ((mCurrentPage < count - 1) && (ev.getX() > halfWidth + sixthWidth)) {
                        if (action != MotionEvent.ACTION_CANCEL) {
                            mViewPager.setCurrentItem(mCurrentPage + 1);
                        }
                        return true;
                    }
                }

                mIsDragging = false;
                mActivePointerId = INVALID_POINTER;
                if (mViewPager.isFakeDragging()) mViewPager.endFakeDrag();
                break;

            case MotionEventCompat.ACTION_POINTER_DOWN: {
                final int index = MotionEventCompat.getActionIndex(ev);
                mLastMotionX = MotionEventCompat.getX(ev, index);
                mActivePointerId = MotionEventCompat.getPointerId(ev, index);
                break;
            }

            case MotionEventCompat.ACTION_POINTER_UP:
                final int pointerIndex = MotionEventCompat.getActionIndex(ev);
                final int pointerId = MotionEventCompat.getPointerId(ev, pointerIndex);
                if (pointerId == mActivePointerId) {
                    final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
                    mActivePointerId = MotionEventCompat.getPointerId(ev, newPointerIndex);
                }
                mLastMotionX = MotionEventCompat.getX(ev, MotionEventCompat.findPointerIndex(ev, mActivePointerId));
                break;
        }

        return true;
    }

    @Override
    public void setViewPager(ViewPager view) {
        if (mViewPager == view) {
            return;
        }
        if (mViewPager != null) {
            mViewPager.setOnPageChangeListener(null);
        }
        if (view.getAdapter() == null) {
            throw new IllegalStateException("ViewPager does not have adapter instance.");
        }
        mViewPager = view;
        mViewPager.setOnPageChangeListener(this);
        invalidate();
    }

    @Override
    public void setViewPager(ViewPager view, int initialPosition) {
        setViewPager(view);
        setCurrentItem(initialPosition);
    }

    @Override
    public void setCurrentItem(int item) {
        if (mViewPager == null) {
            throw new IllegalStateException("ViewPager has not been bound.");
        }
        mViewPager.setCurrentItem(item);
        mCurrentPage = item;
        invalidate();
    }

    @Override
    public void notifyDataSetChanged() {
        invalidate();
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        mScrollState = state;

        if (mListener != null) {
            mListener.onPageScrollStateChanged(state);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        mCurrentPage = position;
        mPageOffset = positionOffset;
        invalidate();
        if (mListener != null) {
            mListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
        }
    }

    @Override
    public void onPageSelected(int position) {
        if (mSnap || mScrollState == ViewPager.SCROLL_STATE_IDLE) {
            mCurrentPage = position;
            mSnapPage = position;
            invalidate();
        }

        if (mListener != null) {
            mListener.onPageSelected(position);
        }
    }

    @Override
    public void setOnPageChangeListener(ViewPager.OnPageChangeListener listener) {
        mListener = listener;
    }

    /*
     * (non-Javadoc)
     *
     * @see android.view.View#onMeasure(int, int)
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureLong(widthMeasureSpec), measureShort(heightMeasureSpec));
    }

    /**
     * Determines the width of this view
     *
     * @param measureSpec A measureSpec packed into an int
     * @return The width of the view, honoring constraints from measureSpec
     */
    private int measureLong(int measureSpec) {
        int result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if ((specMode == MeasureSpec.EXACTLY) || (mViewPager == null)) {
            //We were told how big to be
            result = specSize;
        } else {
            //Calculate the width according the views count
            final int count = ((RecyleAdapter) mViewPager.getAdapter()).getRealCount();
            result = (int) (getPaddingLeft() + getPaddingRight()
                    + (count * 2 * mWidth) + (count - 1) * mWidth + 1);
            //Respect AT_MOST value if that was what is called for by measureSpec
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

    /**
     * Determines the height of this view
     *
     * @param measureSpec A measureSpec packed into an int
     * @return The height of the view, honoring constraints from measureSpec
     */
    private int measureShort(int measureSpec) {
        int result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            //We were told how big to be
            result = specSize;
        } else {
            //Measure the height
            result = (int) (2 * mWidth + getPaddingTop() + getPaddingBottom() + 1);
            //Respect AT_MOST value if that was what is called for by measureSpec
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }
}
