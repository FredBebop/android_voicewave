package com.example.test;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

public class SquircleImageView extends AppCompatImageView {
//    private RectF mRect;
//    private Path mPath;
//    private float mRadius;
//
//    public SquircleImageView(Context context) {
//        this(context, null);
//    }
//
//    public SquircleImageView(Context context, AttributeSet attrs) {
//        this(context, attrs, 0);
//    }
//
//    public SquircleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//        getAttributes(context, attrs);
//        initView(context);
//    }
//
//    /**
//     * 获取属性
//     */
//    private void getAttributes(Context context, AttributeSet attrs) {
//        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.SquircleImageView);
//        mRadius = ta.getDimension(R.styleable.SquircleImageView_radius,-1);
//        ta.recycle();
//    }
//
//    /**
//     * 初始化
//     */
//    private void initView(Context context) {
//        mRect = new RectF();
//        mPath = new Path();
//        setScaleType(ImageView.ScaleType.FIT_CENTER);
//        setLayerType(LAYER_TYPE_SOFTWARE, null);        // 禁用硬件加速
//    }
//
//    @Override
//    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
//        super.onSizeChanged(w, h, oldw, oldh);
//        if (mRadius < 0) {
//            clipCircle(w, h);
//        } else {
//            clipRoundRect(w, h);
//        }
//    }
//
//    /**
//     * 圆角
//     */
//    private void clipRoundRect(int width, int height) {
//        mRect.left = 0;
//        mRect.top = 0;
//        mRect.right = width;
//        mRect.bottom = height;
//        mPath.addRoundRect(mRect, mRadius, mRadius, Path.Direction.CW);
//
//    }
//
//    /**
//     * 圆形
//     */
//    private void clipCircle(int width, int height) {
//        int radius = Math.min(width, height)/2;
//        mPath.addCircle(width/2, height/2, radius, Path.Direction.CW);
//    }
//
//    @Override
//    protected void onDraw(Canvas canvas) {
//        canvas.clipPath(mPath);
//        super.onDraw(canvas);
//    }
private Path clipPath, borderPath=new Path();
    private Paint clipPaint, borderPaint;

    public SquircleImageView(Context context){
        super(context);
        init();
    }

    public SquircleImageView(Context context, @Nullable AttributeSet attrs){
        super(context, attrs);
        init();
    }

    public SquircleImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr){
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        setScaleType(ScaleType.FIT_CENTER);
        setLayerType(LAYER_TYPE_HARDWARE, null);
        clipPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        clipPaint.setColor(0xFF000000);
        clipPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        borderPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        borderPaint.setColor(0x20000000);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh){
        super.onSizeChanged(w, h, oldw, oldh);
        clipPath=new Path();
        clipPath.moveTo(0.0f, 100.0f);
        clipPath.cubicTo(0.0f, 33.0f, 33.0f, 0.0f, 100.0f, 0.0f);
        clipPath.cubicTo(167.0f, 0.0f, 200.0f, 33.0f, 200.0f, 100.0f);
        clipPath.cubicTo(200.0f, 167.0f, 167.0f, 200.0f, 100.0f, 200.0f);
        clipPath.cubicTo(33.0f, 200.0f, 0.0f, 167.0f, 0.0f, 100.0f);
        clipPath.close();
        Matrix m=new Matrix();
        m.setScale(w/200f, h/200f, 0f, 0f);
        clipPath.transform(m);
        m.setScale((float)(w-dp(1))/w, (float)(w-dp(1))/h, w/2f, h/2f);
        clipPath.transform(m, borderPath);
        clipPath.toggleInverseFillType();
        borderPath.toggleInverseFillType();
    }

    public  int dp(float dp){
        return Math.round(dp* getResources().getDisplayMetrics().density);
    }

    @Override
    protected void dispatchDraw(Canvas canvas){
        super.dispatchDraw(canvas);
        if(clipPath!=null){
            canvas.drawPath(borderPath, borderPaint);
            canvas.drawPath(clipPath, clipPaint);
        }
    }
}
