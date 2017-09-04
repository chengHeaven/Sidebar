package com.github.chengheaven.sidebar;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Heaven・Cheng Created on 17/7/17.
 */

public class SidebarView extends RelativeLayout {

    private OnSidebarItemClickListener onSidebarClickListener;

    private Context mContext;

    private final int NAVIGATION_LINE_WIDTH = (int) getResources().getDimension(R.dimen.sidebar_line_width);

    private float mSelectedTextSize;

    private float mUnSelectedTextSize;

    private List<SidebarBean> mSidebarList = new ArrayList<>();

    private List<View> mViewList = new ArrayList<>();

    private int mItemSelectedColor = -1;

    private int mItemUnSelectedColor;

    private boolean isItemSelectedColor = false;

    private boolean isItemUnSelectedColor = false;

    private int mItemSelectedTextColor;

    private int mItemUnSelectedTextColor;

    private boolean isItemSelectedTextColor = false;

    private boolean isItemUnSelectedTextColor = false;

    // 当前点击的 item index
    private static int mCurrentItem = 0;

    private int mNavigationWidth;

    private int mShadowHeight = 0;

    private boolean mShowText;

    private boolean isClickChangeBackgroundColor;

    private boolean mDisableShadow;

    private boolean isOrientation;

    private boolean mViewPagerSlide;

    private boolean isCustomFont = false;

    private boolean mWillNotRecreate = true;

    private FrameLayout mContainer;

    private View mBackgroundColorTemp;

    private ViewPager mViewPager;

    private Typeface mFont;

    private boolean isAnimator = true;

    private int mBaseBackgroundColor = 0;

    private boolean isSelectedItemClick = false;

    private int[] mItemGone = new int[]{};

    private int mInitializePadding = 100;

    private int mAnimatorPadding = 30;

    public SidebarView(Context context) {
        this(context, null);
    }

    public SidebarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SidebarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        if (attrs != null) {
            Resources res = getResources();

            TypedArray array = mContext.obtainStyledAttributes(attrs, R.styleable.SidebarView);
            mShowText = array.getBoolean(R.styleable.SidebarView_sidebar_with_text, true);
            isClickChangeBackgroundColor = array.getBoolean(R.styleable.SidebarView_sidebar_colored_background, true);
            mDisableShadow = array.getBoolean(R.styleable.SidebarView_sidebar_shadow, false);
            isOrientation = array.getBoolean(R.styleable.SidebarView_sidebar_tablet, false);
            mViewPagerSlide = array.getBoolean(R.styleable.SidebarView_sidebar_viewpager_slide, true);
            mItemSelectedColor = array.getColor(R.styleable.SidebarView_sidebar_selected_color, -1);
            mSelectedTextSize = array.getDimensionPixelSize(R.styleable.SidebarView_sidebar_selected_text_size, res.getDimensionPixelSize(R.dimen.sidebar_text_size_selected));
            mUnSelectedTextSize = array.getDimensionPixelSize(R.styleable.SidebarView_sidebar_unselected_text_size, res.getDimensionPixelSize(R.dimen.sidebar_text_size_unselected));

            array.recycle();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

//        mNavigationWidth = SidebarUtils.getActionbarSize(mContext);
        ViewGroup.LayoutParams params = getLayoutParams();
        if (isClickChangeBackgroundColor) {
            if (!isItemSelectedColor) {
                mItemSelectedColor = ContextCompat.getColor(mContext, R.color.colorWhite);
            }
            if (!isItemUnSelectedColor) {
                mItemUnSelectedColor = ContextCompat.getColor(mContext, R.color.colorPink);
            }
        } else {
            if (!isItemSelectedColor) {
                mItemSelectedColor = ContextCompat.getColor(mContext, R.color.white);
            }
            if (!isItemUnSelectedColor) {
                mItemUnSelectedColor = ContextCompat.getColor(mContext, R.color.colorGray);
            }
        }
        if (isOrientation) {
            params.width = mNavigationWidth + NAVIGATION_LINE_WIDTH;
            params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        } else {
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            params.height = mDisableShadow ? mNavigationWidth : mNavigationWidth + mShadowHeight;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                setElevation(getResources().getDimension(R.dimen.sidebar_elevation));
            }
        }
        setLayoutParams(params);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        if (mWillNotRecreate)
            removeAllViews();
//        if (mCurrentItem < 0 || mCurrentItem > (mSidebarList.size() - 1)) {
//            throw new IndexOutOfBoundsException(mCurrentItem < 0 ? "Position must be 0 or greater than 0, current is " + mCurrentItem
//                    : "Position must be less or equivalent than items size, items size is " + (mSidebarList.size() - 1) + " current is " + mCurrentItem);
//        }
        if (mSidebarList.size() == 0) {
            throw new NullPointerException("You need at least one item");
        }
        LayoutParams containerParams, params, lineParams;
        int white = ContextCompat.getColor(mContext, R.color.white);
        mBackgroundColorTemp = new View(mContext);
        mViewList.clear();
        int itemWidth;
        int itemHeight;
        if (isOrientation) {
            itemWidth = LayoutParams.MATCH_PARENT;
            itemHeight = mNavigationWidth;
        } else {
            itemWidth = getWidth() / (mSidebarList.size() - mItemGone.length);
            itemHeight = LayoutParams.MATCH_PARENT;
        }
        mContainer = new FrameLayout(mContext);
        View shadow = new View(mContext);
        View line = new View(mContext);
        LinearLayout items = new LinearLayout(mContext);
        items.setOrientation(isOrientation ? LinearLayout.VERTICAL : LinearLayout.HORIZONTAL);
        LayoutParams shadowParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mShadowHeight);

        if (!isClickChangeBackgroundColor){
            items.setBackgroundColor(mBaseBackgroundColor);
        }

        if (isOrientation) {
            line.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorGray));
            containerParams = new LayoutParams(mNavigationWidth, ViewGroup.LayoutParams.MATCH_PARENT);
            lineParams = new LayoutParams(NAVIGATION_LINE_WIDTH, ViewGroup.LayoutParams.MATCH_PARENT);
            lineParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            params = new LayoutParams(mNavigationWidth, ViewGroup.LayoutParams.MATCH_PARENT);
//            items.setPadding(0, itemHeight / 2, 0, 0);
            items.setPadding(0, 0, 0, 0);
            addView(line, lineParams);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                LayoutParams backgroundLayoutParams = new LayoutParams(
                        mNavigationWidth, ViewGroup.LayoutParams.MATCH_PARENT);
                backgroundLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                mContainer.addView(mBackgroundColorTemp, backgroundLayoutParams);
            }
        } else {
            params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mNavigationWidth);
            containerParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mNavigationWidth);
            shadowParams.addRule(RelativeLayout.ABOVE, mContainer.getId());
            shadow.setBackgroundResource(R.drawable.shadow);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                LayoutParams backgroundLayoutParams = new LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, mNavigationWidth);
                backgroundLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                mContainer.addView(mBackgroundColorTemp, backgroundLayoutParams);
            }
        }
        containerParams.addRule(isOrientation ? RelativeLayout.ALIGN_PARENT_LEFT : RelativeLayout.ALIGN_PARENT_BOTTOM);
        addView(shadow, shadowParams);
        addView(mContainer, containerParams);
        if (!isClickChangeBackgroundColor) {
            mContainer.setBackgroundColor(mBaseBackgroundColor);
        } else {
            mContainer.setBackgroundColor(mSidebarList.get(0).getColor());
        }
        mContainer.addView(items, params);
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        for (int i = 0; i < mSidebarList.size(); i++) {
            final int index = i;

            if (!isClickChangeBackgroundColor) {
                mSidebarList.get(i).setColor(white);
            }

            final View view = inflater.inflate(R.layout.sidebar, this, false);
            ImageView icon = (ImageView) view.findViewById(R.id.sidebar_item_icon);
            TextView title = (TextView) view.findViewById(R.id.sidebar_item_title);

            if (mItemGone.length != 0) {

                for (int itemGone : mItemGone) {

                    if (itemGone >= mSidebarList.size()) {
                        throw new IllegalArgumentException("The input value is out of range!");
                    }

                    if (i == itemGone) {
                        view.setVisibility(GONE);
                    }
                }
            }

            if (isCustomFont) {
                title.setTypeface(mFont);
            }

            mViewList.add(view);

            if (mSidebarList.get(i).getImageResourceSelected() != 0) {
                if (i == mCurrentItem) {
                    title.setTextColor(isItemSelectedTextColor ? mItemSelectedTextColor : mItemSelectedColor);
                    icon.setImageResource(mSidebarList.get(i).getImageResourceSelected());
                    icon.setColorFilter(mItemSelectedColor);
                } else {
                    title.setTextColor(isItemUnSelectedTextColor ? mItemUnSelectedTextColor : mItemUnSelectedColor);
                    icon.setImageResource(mSidebarList.get(i).getImageResource());
                    icon.setColorFilter(mItemUnSelectedColor);
                }
            } else {
                icon.setImageResource(mSidebarList.get(i).getImageResource());
                title.setTextColor(isItemUnSelectedTextColor ? mItemUnSelectedTextColor : mItemUnSelectedColor);
                icon.setColorFilter(i == mCurrentItem ? mItemSelectedColor : mItemUnSelectedColor);
            }

            if (i == mCurrentItem) {
                title.setTextColor(isItemSelectedTextColor ? mItemSelectedTextColor : mItemUnSelectedColor);
            }

            if (isAnimator) {

                if (isOrientation) {
                    view.setPadding(getPaddingLeft(), getPaddingTop(), getPaddingRight() + mInitializePadding, getPaddingBottom());
                } else {
                    view.setPadding(getPaddingLeft(), getPaddingTop() + mInitializePadding, getPaddingRight(), getPaddingBottom());
                }

                if (i == mCurrentItem) {
                    mContainer.setBackgroundColor(mSidebarList.get(index).getColor());
                    title.setTextColor(isItemSelectedTextColor ? mItemSelectedTextColor : mItemSelectedColor);
                    icon.setScaleX((float) 1.1);
                    icon.setScaleY((float) 1.1);

                    if (isOrientation) {
                        SidebarUtils.changeRightPadding(view, getPaddingRight() + mAnimatorPadding, getPaddingRight() - mAnimatorPadding);
                    } else {
                        SidebarUtils.changeViewTopPadding(view, getPaddingTop() + mAnimatorPadding, getPaddingTop() - mAnimatorPadding);
                    }
                }

                title.setTextSize(TypedValue.COMPLEX_UNIT_PX, i == mCurrentItem ? mSelectedTextSize : mShowText ? mUnSelectedTextSize : 0);
            }


            if (mShowText) {
                title.setVisibility(VISIBLE);
            } else {
                title.setVisibility(GONE);
            }
            title.setText(mSidebarList.get(i).getTitle());
            LayoutParams itemParams = new LayoutParams(itemWidth, itemHeight);
            items.addView(view, itemParams);
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    onSidebarClick(index);
                }
            });
        }
    }

    private void onSidebarClick(final int itemIndex) {

        if (mCurrentItem == itemIndex) {
            if (onSidebarClickListener != null && isSelectedItemClick) {
                onSidebarClickListener.onSidebarItemClick(itemIndex);
            }
            return;
        }

        int centerX;
        int centerY;
        for (int i = 0; i < mViewList.size(); i++) {
            View view = mViewList.get(i).findViewById(R.id.sidebar_container);
            final TextView title = (TextView) view.findViewById(R.id.sidebar_item_title);
            final ImageView icon = (ImageView) view.findViewById(R.id.sidebar_item_icon);

            if (mSidebarList.get(i).getImageResource() != 0) {
                icon.setImageResource((mSidebarList.get(i).getImageResource()));
            }
            SidebarUtils.changeImageColorFilter(isAnimator, icon, i == mCurrentItem ? mItemSelectedColor : mItemUnSelectedColor, mItemUnSelectedColor);
            SidebarUtils.changeTextColor(isAnimator, title, i == mCurrentItem ? mItemSelectedTextColor : mItemUnSelectedTextColor, mItemUnSelectedTextColor);


            if (view.getRootView().getMeasuredHeight() - icon.getMeasuredWidth() < mAnimatorPadding) {
                throw new IllegalArgumentException("The input value is out of range!");
            }

            if (isAnimator) {
                if (i == mCurrentItem) {
                    if (isOrientation) {
                        icon.setColorFilter(R.color.itemClickColor);
                        SidebarUtils.changeRightPadding(view, getPaddingRight() - mAnimatorPadding, getPaddingRight() + mInitializePadding);
                    } else {
                        SidebarUtils.changeViewTopPadding(view, getPaddingTop() - mAnimatorPadding, getPaddingTop() + mInitializePadding);
                    }

                    SidebarUtils.changeTextColor(isAnimator, title, isItemSelectedTextColor ? mItemSelectedTextColor : mItemSelectedColor, isItemUnSelectedTextColor ? mItemUnSelectedTextColor : mItemUnSelectedColor);
//                    title.setTextColor(isItemUnSelectedTextColor ? mItemUnSelectedTextColor : mItemUnSelectedColor);
                    SidebarUtils.changeTextSize(title, mSelectedTextSize, mShowText ? mUnSelectedTextSize : 0);


                    icon.animate()
                            .setDuration(150)
                            .scaleX((float) 0.9)
                            .scaleY((float) 0.9)
                            .start();
                }
            } else {
                title.setTextColor(isItemUnSelectedTextColor ? mItemUnSelectedTextColor : mItemUnSelectedColor);
                SidebarUtils.changeTextColor(isAnimator, title, i == mCurrentItem ? mItemSelectedTextColor : mItemUnSelectedTextColor, mItemUnSelectedTextColor);

            }

            if (i == itemIndex) {

                if (mSidebarList.get(i).getImageResourceSelected() != 0) {
                    icon.setImageResource((mSidebarList.get(i).getImageResourceSelected()));
                }
                SidebarUtils.changeImageColorFilter(isAnimator, icon, mItemUnSelectedColor, mItemSelectedColor);

                if (isAnimator) {
                    SidebarUtils.changeTextColor(isAnimator, title, isItemUnSelectedTextColor ? mItemUnSelectedTextColor : mItemUnSelectedColor, isItemSelectedTextColor ? mItemSelectedTextColor : mItemSelectedColor);
                    SidebarUtils.changeTextSize(title, mShowText ? mUnSelectedTextSize : 0, mSelectedTextSize);

                    if (isOrientation) {
                        SidebarUtils.changeRightPadding(view, getPaddingRight() + mAnimatorPadding, getPaddingRight() - mAnimatorPadding);
                    } else {
                        SidebarUtils.changeViewTopPadding(view, getPaddingTop() + mAnimatorPadding, getPaddingTop() - mAnimatorPadding);
                    }

                    icon.animate()
                            .setDuration(150)
                            .scaleX((float) 1.1)
                            .scaleY((float) 1.1)
                            .start();
                } else {
                    title.setTextColor(isItemSelectedTextColor ? mItemSelectedTextColor : mItemUnSelectedColor);
                }

                if (isOrientation) {
                    centerX = mViewList.get(itemIndex).getWidth() / 2;
                    centerY = (int) mViewList.get(itemIndex).getY() + mViewList.get(itemIndex).getHeight() / 2;
                } else {
                    centerX = (int) mViewList.get(itemIndex).getX() + mViewList.get(itemIndex).getWidth() / 2;
                    centerY = mViewList.get(itemIndex).getHeight() / 2;
                }

                int finalRadius = Math.max(getWidth(), getHeight());
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    mBackgroundColorTemp.setBackgroundColor(mSidebarList.get(itemIndex).getColor());
                    if (isAnimator) {
                        Animator changeBackgroundColor = ViewAnimationUtils.createCircularReveal(mBackgroundColorTemp, centerX, centerY, 0, finalRadius);
                        changeBackgroundColor.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                mContainer.setBackgroundColor(mSidebarList.get(itemIndex).getColor());
                            }
                        });
                        changeBackgroundColor.start();
                    }
                } else {
                    SidebarUtils.changeViewBackgroundColor(isAnimator, mContainer, mSidebarList.get(mCurrentItem).getColor(), mSidebarList.get(itemIndex).getColor());
                }

                if (onSidebarClickListener != null) {
                    onSidebarClickListener.onSidebarItemClick(itemIndex);
                }
            }

            if (mViewPager != null) {
                mViewPager.setCurrentItem(itemIndex, mViewPagerSlide);
            }
        }
        mCurrentItem = itemIndex;
    }

    /**
     * Creates a connection between this navigation view and a ViewPager
     * mCurrentItem default value set 0
     *
     * @param pager          pager to connect to
     * @param colorResources color resources for every item in the ViewPager adapter
     * @param imageResources images resources for every item in the ViewPager adapter
     */

    public void setUpWithViewPager(ViewPager pager, int[] colorResources, int[] imageResources) {
        this.mViewPager = pager;
        if (pager.getAdapter().getCount() != colorResources.length || pager.getAdapter().getCount() != imageResources.length)
            throw new IllegalArgumentException("colorResources and imageResources must be equal to the ViewPager items : " + pager.getAdapter().getCount());

        for (int i = 0; i < pager.getAdapter().getCount(); i++) {
            addTab(new SidebarBean(pager.getAdapter().getPageTitle(i).toString(), colorResources[i], imageResources[i]));
        }
        mCurrentItem = 0;
    }

    /**
     * Add item for Sidebar Tab
     *
     * @param item item to add
     */
    public void addTab(SidebarBean item) {
        mSidebarList.add(item);
    }

    /**
     * Sidebar orientation, default orientation is horizontal
     */
    public void orientationIsVertical() {
        isOrientation = true;
    }

    /**
     * Change text visibility
     *
     * @param mShowText disable or enable item text
     */
    public void isShowText(boolean mShowText) {
        this.mShowText = mShowText;
    }

    /**
     * Item Selected Color
     *
     * @param itemSelectedColor Selected item color
     */
    public void setItemSelectedColor(int itemSelectedColor) {
        isItemSelectedColor = true;
        this.mItemSelectedColor = itemSelectedColor;
    }

    /**
     * Item unSelected Color
     *
     * @param itemUnSelectedColor unSelected item color
     */
    public void setItemUnSelectedColor(int itemUnSelectedColor) {
        isItemUnSelectedColor = true;
        this.mItemUnSelectedColor = itemUnSelectedColor;
    }

    public void setItemSelectedTextColor(int itemSelectedTextColor) {
        isItemSelectedTextColor = true;
        this.mItemSelectedTextColor = itemSelectedTextColor;
    }

    public void setItemUnSelectedTextColor(int itemUnSelectedTextColor) {
        isItemUnSelectedTextColor = true;
        mItemUnSelectedTextColor = itemUnSelectedTextColor;
    }

    /**
     * With this Sidebar background will be white
     *
     * @param isClickChangeBackgroundColor disable or enable background color
     */
    public void isClickChangeBackgroundColor(boolean isClickChangeBackgroundColor) {
        this.isClickChangeBackgroundColor = isClickChangeBackgroundColor;
    }

    /**
     * Change tab programmatically
     *
     * @param position selected tab position
     */
    public void selectTab(int position) {
        onSidebarClick(position);
        mCurrentItem = position;
    }

    /**
     * Disable shadow of SidebarView
     */
    public void disableShadow() {
        mDisableShadow = true;
    }

    /**
     * Disable slide animation when using ViewPager
     */
    public void disableViewPagerSlide() {
        mViewPagerSlide = false;
    }

    /**
     * Change Selected text size
     *
     * @param mSelectedTextSize size
     */
    public void setSelectedTextSize(float mSelectedTextSize) {
        this.mSelectedTextSize = mSelectedTextSize;
    }

    /**
     * Change unSelected text size
     *
     * @param mUnSelectedTextSize size
     */
    public void setUnSelectedTextSize(float mUnSelectedTextSize) {
        this.mUnSelectedTextSize = mUnSelectedTextSize;
    }

    /**
     * Setup interface for item onClick
     */
    public void setOnSidebarClickListener(OnSidebarItemClickListener
                                                  onSidebarClickListener) {
        this.onSidebarClickListener = onSidebarClickListener;
    }

    /**
     * Returns the item that is currently selected
     *
     * @return Currently selected item
     */
    public int getCurrentItem() {
        return mCurrentItem;
    }

    /**
     * If your activity/fragment will not recreate
     * you can call this method
     *
     * @param willNotRecreate set true if will not recreate
     */
    public void WillNotRecreate(boolean willNotRecreate) {
        this.mWillNotRecreate = willNotRecreate;
    }

    public boolean isAnimator() {
        return isAnimator;
    }

    /**
     * Set animate, default value is true
     */
    public void isAnimator(boolean animator) {
        isAnimator = animator;
    }

    /**
     * Set custom font for item texts
     *
     * @param font custom font
     */
    public void setFont(Typeface font) {
        isCustomFont = true;
        this.mFont = font;
    }

    /**
     * Index by default selected, if don't set it, mCurrentItem default value is 0
     */
    public void setDefaultSelectItemOfIndex(int index) {
        mCurrentItem = index;
    }

    /**
     * Set Sidebar width
     *
     * @param dp: unit is dp
     */
    public void setSidebarWidth(int dp) {
        this.mNavigationWidth = SidebarUtils.dip2px(mContext, dp);
    }

    /**
     * Set Sidebar background color
     *
     * @param baseBackgroundColor color
     */
    public void setBaseBackgroundColor(int baseBackgroundColor) {
        this.mBaseBackgroundColor = baseBackgroundColor;
    }

    /**
     * Response Selected click event, default value is false
     */
    public void isSelectedItemClick(boolean isSelectedItemClick) {
        this.isSelectedItemClick = isSelectedItemClick;
    }

    /**
     * One item is gone
     */
    public void setItemGone(int[] itemGone) {
        this.mItemGone = itemGone;
    }

    /**
     * Set initialize padding, default padding value is 10
     */
    public void setInitializePadding(int initializePadding) {
        this.mInitializePadding = initializePadding;
    }

    /**
     * Set animator padding, default padding value is 30
     */
    public void setAnimatorPadding(int animatorPadding) {
        this.mAnimatorPadding = animatorPadding;
    }

    /**
     * Set shadow height, default value is 0
     */
    public void setShadowHeight(int shadowHeight) {
        this.mShadowHeight = shadowHeight;
    }

    /**
     * get item text size on selected status
     *
     * @return selected size
     */
    public float getSelectedTextSize() {
        return mSelectedTextSize;
    }

    /**
     * get item text size on unSelected status
     *
     * @return unSelected size
     */
    public float getUnSelectedTextSize() {
        return mUnSelectedTextSize;
    }


    public SidebarBean getItem(int position) {
        onSidebarClick(position);
        return mSidebarList.get(position);
    }
}
