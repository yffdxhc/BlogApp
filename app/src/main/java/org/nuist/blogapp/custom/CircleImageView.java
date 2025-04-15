package org.nuist.blogapp.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;

import org.nuist.blogapp.R;

/**
 * 自定义ImageView，实现圆角
 */
public class CircleImageView extends AppCompatImageView {

    private Paint mImagePaint;
    private Paint mBorderPaint;
    private BitmapShader mBitmapShader;
    private Matrix mShaderMatrix;

    private float mBorderWidth;
    private int mBorderColor;
    private ImageView.ScaleType mScaleType;

    private boolean mSetupPending;
    private int mViewWidth;
    private int mViewHeight;

    public CircleImageView(Context context) {
        super(context);
        init(null);
    }

    public CircleImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        mShaderMatrix = new Matrix();
        mImagePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBorderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        // 默认值
        mBorderWidth = 0;
        mBorderColor = Color.WHITE;
        mScaleType = ImageView.ScaleType.CENTER_CROP;

        // 必须设置为MATRIX模式
        super.setScaleType(ImageView.ScaleType.MATRIX);

        // 处理自定义属性
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(
                    attrs,
                    R.styleable.CircleImageView
            );

            mBorderWidth = a.getDimension(
                    R.styleable.CircleImageView_border_width,
                    0
            );
            mBorderColor = a.getColor(
                    R.styleable.CircleImageView_border_color,
                    Color.WHITE
            );

            int scaleTypeValue = a.getInt(
                    R.styleable.CircleImageView_scale_type,
                    0
            );
            mScaleType = convertScaleType(scaleTypeValue);

            a.recycle();
        }

        // 初始化画笔
        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBorderPaint.setColor(mBorderColor);
        mBorderPaint.setStrokeWidth(mBorderWidth);
    }

    private ImageView.ScaleType convertScaleType(int value) {
        switch (value) {
            case 1: return ImageView.ScaleType.CENTER_INSIDE;
            case 2: return ImageView.ScaleType.FIT_CENTER;
            default: return ImageView.ScaleType.CENTER_CROP;
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mViewWidth = w;
        mViewHeight = h;
        setup();
    }

    @Override
    public void setScaleType(ImageView.ScaleType scaleType) {
        if (scaleType == null) {
            throw new NullPointerException("ScaleType cannot be null");
        }

        if (mScaleType != scaleType) {
            mScaleType = scaleType;
            setup();
            invalidate();
        }
    }

    @Override
    @NonNull
    public ImageView.ScaleType getScaleType() {
        return mScaleType;
    }

    @Override
    public void setImageResource(int resId) {
        super.setImageResource(resId);
        setup();
    }

    @Override
    public void setImageURI(Uri uri) {
        super.setImageURI(uri);
        setup();
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
        setup();
    }

    private void setup() {
        if (mViewWidth == 0 || mViewHeight == 0) {
            mSetupPending = true;
            return;
        }

        Drawable drawable = getDrawable();
        if (drawable == null) {
            mBitmapShader = null;
            invalidate();
            return;
        }

        Bitmap bitmap = getBitmapFromDrawable(drawable);
        if (bitmap == null) {
            mBitmapShader = null;
            invalidate();
            return;
        }

        mBitmapShader = new BitmapShader(
                bitmap,
                Shader.TileMode.CLAMP,
                Shader.TileMode.CLAMP
        );
        mImagePaint.setShader(mBitmapShader);

        updateShaderMatrix();
        invalidate();
    }

    private void updateShaderMatrix() {
        mShaderMatrix.set(null);

        Bitmap bitmap = ((BitmapDrawable) getDrawable()).getBitmap();
        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();

        int viewWidth = mViewWidth - getPaddingLeft() - getPaddingRight();
        int viewHeight = mViewHeight - getPaddingTop() - getPaddingBottom();

        float scale = 1;
        float dx = 0;
        float dy = 0;

        switch (mScaleType) {
            case CENTER_CROP:
                float widthScale = viewWidth / (float) bitmapWidth;
                float heightScale = viewHeight / (float) bitmapHeight;
                scale = Math.max(widthScale, heightScale);
                break;

            case CENTER_INSIDE:
                float fitScale = Math.min(
                        viewWidth / (float) bitmapWidth,
                        viewHeight / (float) bitmapHeight
                );
                scale = Math.min(1, fitScale);
                break;

            case FIT_CENTER:
                scale = Math.min(
                        viewWidth / (float) bitmapWidth,
                        viewHeight / (float) bitmapHeight
                );
                break;

            default:
                throw new IllegalArgumentException("Unsupported scale type");
        }

        mShaderMatrix.setScale(scale, scale);

        // 计算居中偏移
        dx = (viewWidth - bitmapWidth * scale) / 2f;
        dy = (viewHeight - bitmapHeight * scale) / 2f;

        // 应用偏移（含padding）
        mShaderMatrix.postTranslate(
                dx + getPaddingLeft(),
                dy + getPaddingTop()
        );

        mBitmapShader.setLocalMatrix(mShaderMatrix);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mBitmapShader == null) return;

        float centerX = mViewWidth / 2f;
        float centerY = mViewHeight / 2f;
        float radius = Math.min(centerX, centerY) - mBorderWidth / 2f;

        canvas.drawCircle(centerX, centerY, radius, mImagePaint);

        if (mBorderWidth > 0) {
            canvas.drawCircle(centerX, centerY, radius, mBorderPaint);
        }
    }

    private Bitmap getBitmapFromDrawable(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        try {
            Bitmap bitmap = Bitmap.createBitmap(
                    drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight(),
                    Bitmap.Config.ARGB_8888
            );
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return bitmap;
        } catch (Exception e) {
            return null;
        }
    }

    public void setBorderWidth(float width) {
        mBorderWidth = width;
        mBorderPaint.setStrokeWidth(width);
        invalidate();
    }

    public void setBorderColor(int color) {
        mBorderColor = color;
        mBorderPaint.setColor(color);
        invalidate();
    }
}