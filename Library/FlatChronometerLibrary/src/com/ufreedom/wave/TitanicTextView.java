package com.ufreedom.wave;



import com.ufreedom.widget.R;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;



public class TitanicTextView extends TextView {

    public interface AnimationSetupCallback {
        public void onSetupAnimation(TitanicTextView titanicTextView);
    }

    // callback fired at first onSizeChanged
    private AnimationSetupCallback animationSetupCallback;
    // wave shader coordinates
    private float maskX, maskY;
    // if true, the shader will display the wave
    private boolean sinking;
    // true after the first onSizeChanged
    private boolean setUp;

    // shader containing a repeated wave
    private BitmapShader shaderBitmap;
    // shader matrix
    private Matrix shaderMatrix;
    // wave drawable
    private Drawable waveDrawable;
    // (getHeight() - waveHeight) / 2
    private float offsetY;

    public TitanicTextView(Context context) {
        super(context);
        init();
    }

    public TitanicTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TitanicTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        shaderMatrix = new Matrix();
    }

    public AnimationSetupCallback getAnimationSetupCallback() {
        return animationSetupCallback;
    }

    public void setAnimationSetupCallback(AnimationSetupCallback animationSetupCallback) {
        this.animationSetupCallback = animationSetupCallback;
    }

    public float getMaskX() {
        return maskX;
    }

    public void setMaskX(float maskX) {
        this.maskX = maskX;
        invalidate();
    }

    public float getMaskY() {
        return maskY;
    }

    public void setMaskY(float maskY) {
        this.maskY = maskY;
        invalidate();
    }

    public boolean isSinking() {
        return sinking;
    }

    public void setSinking(boolean sinking) {
        this.sinking = sinking;
    }

    public boolean isSetUp() {
        return setUp;
    }

    @Override
    public void setTextColor(int color) {
        super.setTextColor(color);
        createShader();
    }

    @Override
    public void setTextColor(ColorStateList colors) {
        super.setTextColor(colors);
        createShader();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        createShader();

        if (!setUp) {
            setUp = true;
            if (animationSetupCallback != null) {
                animationSetupCallback.onSetupAnimation(TitanicTextView.this);
            }
        }
    }

    /**
     * Create the shader
     * draw the wave with current color for a background
     * repeat the bitmap horizontally, and clamp colors vertically
     */
    private void createShader() {

        if (waveDrawable == null) {
        	waveDrawable = getResources().getDrawable(R.drawable.wave_blue);
        }

        int waveW = waveDrawable.getIntrinsicWidth();
        int waveH = waveDrawable.getIntrinsicHeight();

        Bitmap b = Bitmap.createBitmap(waveW, waveH, Bitmap.Config.ARGB_8888);
        
        Canvas c = new Canvas(b);
        
        //设置水波图形背景
        c.drawColor(getCurrentTextColor());

        waveDrawable.setBounds(0, 0, waveW, waveH);
        waveDrawable.draw(c);

        
        settestBitMap(b);
        shaderBitmap = new BitmapShader(b, Shader.TileMode.REPEAT, Shader.TileMode.CLAMP);
        
        getPaint().setShader(shaderBitmap);

        offsetY = (getHeight() - waveH) / 2;
    }

    private Bitmap testBitmap;
    public void settestBitMap(Bitmap bit){
    	this.testBitmap = bit;
    }
    public Bitmap getTestBitmap(){
    	return testBitmap;
    }
    
    
    
    @Override
    protected void onDraw(Canvas canvas) {

        // modify text paint shader according to sinking state
        if (sinking && shaderBitmap != null) {

            // first call after sinking, assign it to our paint
           if (getPaint().getShader() == null) {
                getPaint().setShader(shaderBitmap);
            }

            // translate shader accordingly to maskX maskY positions
            // maskY is affected by the offset to vertically center the wave
           
            //offsetY决定波纹上下移动的距离
  
            shaderMatrix.setTranslate(maskX, maskY + offsetY);

            // assign matrix to invalidate the shader
            shaderBitmap.setLocalMatrix(shaderMatrix);
        } else {
            getPaint().setShader(null);
        }

  
        super.onDraw(canvas);
    }
}
