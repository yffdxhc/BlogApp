package org.nuist.blogapp.custom;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ScrollView;

/**
 * 自定义的 ScrollView，提供在滚动到顶部或底部时的回弹效果。
 */
public class ReboundScrollView extends ScrollView {

    // 标志位，用于启用或禁用顶部回弹效果
    private boolean mEnableTopRebound = true;

    // 标志位，用于启用或禁用底部回弹效果
    private boolean mEnableBottomRebound = true;

    // 回弹结束事件的监听器
    private OnReboundEndListener mOnReboundEndListener;

    // ScrollView 中的内容视图
    private View mContentView;

    // 用于存储内容视图初始位置的 Rect
    private Rect mRect = new Rect();

    // 上一次触摸事件的 Y 坐标
    private int lastY;

    // 标志位，表示是否正在进行回弹
    private boolean rebound = false;

    // 回弹方向，<0 表示下部回弹，>0 表示上部回弹，0 表示不回弹
    private int reboundDirection = 0;

    /**
     * 构造函数
     * @param context 上下文
     */
    public ReboundScrollView(Context context) {
        super(context);
    }

    /**
     * 构造函数
     * @param context 上下文
     * @param attrs 属性集
     */
    public ReboundScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 构造函数
     * @param context 上下文
     * @param attrs 属性集
     * @param defStyleAttr 默认样式属性
     */
    public ReboundScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 在布局解析完毕后调用，获取内容视图
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mContentView = getChildAt(0);
    }

    /**
     * 在布局发生变化时调用，记录内容视图的初始位置
     * @param changed 布局是否发生变化
     * @param l 左边界
     * @param t 上边界
     * @param r 右边界
     * @param b 下边界
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (mContentView != null) {
            mRect.set(mContentView.getLeft(), mContentView.getTop(), mContentView.getRight(), mContentView.getBottom());
        }
    }

    /**
     * 设置回弹结束事件的监听器
     * @param listener 回弹结束事件的监听器
     * @return 当前对象
     */
    public ReboundScrollView setOnReboundEndListener(OnReboundEndListener listener) {
        this.mOnReboundEndListener = listener;
        return this;
    }

    /**
     * 启用或禁用顶部回弹效果
     * @param enable 是否启用顶部回弹效果
     * @return 当前对象
     */
    public ReboundScrollView setEnableTopRebound(boolean enable) {
        this.mEnableTopRebound = enable;
        return this;
    }

    /**
     * 启用或禁用底部回弹效果
     * @param enable 是否启用底部回弹效果
     * @return 当前对象
     */
    public ReboundScrollView setEnableBottomRebound(boolean enable) {
        this.mEnableBottomRebound = enable;
        return this;
    }

    /**
     * 处理触摸事件
     * @param ev 触摸事件
     * @return 是否处理了触摸事件
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (mContentView == null) {
            return super.dispatchTouchEvent(ev);
        }

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 记录触摸开始时的 Y 坐标
                lastY = (int) ev.getY();
                break;

            case MotionEvent.ACTION_MOVE:
                // 如果不在顶部或底部，则不处理
                if (!isScrollToTop() && !isScrollToBottom()) {
                    lastY = (int) ev.getY();
                    break;
                }

                // 计算触摸移动的距离
                int deltaY = (int) (ev.getY() - lastY);
                // 如果顶部回弹被禁用且下拉，或者底部回弹被禁用且上拉，则不处理
                if ((!mEnableTopRebound && deltaY > 0) || (!mEnableBottomRebound && deltaY < 0)) {
                    break;
                }

                // 计算回弹偏移量
                int offset = (int) (deltaY * 0.48);
                // 重新布局内容视图
                mContentView.layout(mRect.left, mRect.top + offset, mRect.right, mRect.bottom + offset);
                rebound = true;
                break;

            case MotionEvent.ACTION_UP:
                // 如果没有回弹，则不处理
                if (!rebound) break;

                // 计算回弹方向
                reboundDirection = mContentView.getTop() - mRect.top;
                // 创建回弹动画
                TranslateAnimation animation = new TranslateAnimation(0, 0, mContentView.getTop(), mRect.top);
                animation.setDuration(300);
                // 设置动画监听器
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {}

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        // 回弹结束时调用监听器
                        if (mOnReboundEndListener != null) {
                            if (reboundDirection > 0) {
                                mOnReboundEndListener.onReboundTopComplete();
                            }
                            if (reboundDirection < 0) {
                                mOnReboundEndListener.onReboundBottomComplete();
                            }
                        }
                        reboundDirection = 0;
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {}
                });

                // 启动回弹动画
                mContentView.startAnimation(animation);
                // 重置内容视图的位置
                mContentView.layout(mRect.left, mRect.top, mRect.right, mRect.bottom);
                rebound = false;
                break;
        }

        return super.dispatchTouchEvent(ev);
    }

    /**
     * 设置是否填充视口，默认为填充
     * @param fillViewport 是否填充视口
     */
    @Override
    public void setFillViewport(boolean fillViewport) {
        super.setFillViewport(true);
    }

    /**
     * 判断当前 ScrollView 是否处于顶部
     * @return 是否处于顶部
     */
    private boolean isScrollToTop() {
        return getScrollY() == 0;
    }

    /**
     * 判断当前 ScrollView 是否已滑到底部
     * @return 是否已滑到底部
     */
    private boolean isScrollToBottom() {
        return mContentView != null && mContentView.getHeight() <= getHeight() + getScrollY();
    }

    /**
     * 回弹结束事件的监听器接口
     */
    public interface OnReboundEndListener {
        /**
         * 回弹到顶部完成时调用
         */
        void onReboundTopComplete();

        /**
         * 回弹到底部完成时调用
         */
        void onReboundBottomComplete();
    }
}
