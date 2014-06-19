package com.ufreedom.widget;




import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Handler;
import android.text.format.DateUtils;
import android.util.AttributeSet;
import android.view.View;



public class FlatChronometer extends View {
	

	
	private static final String TAG = "CircularCounter";
	
	/**
	 * View starts at 6 o'clock
	 */
	private static float START_DEGREES_FROM_TOP = -90;
	private static float START_DEGREES_FROM_BOTTOM = 90;
	private static float START_DEGREES_FROM_RIGHT = 0;
	private static float START_DEGREES_FROM_LEFT = 180;
	
	
	/**
	 * Default background
	 */
	private int mBackgroundCenter;
	private int mBackgroundRadius;
	
	

	/*
	 * 设定倒计时的时间
	 */
	
	private int hour=0;
	private int minutes=0;
	private int seconds=0;
	private int millisecond = 0;
	
	
	
	private int totalHour=0;
	private int totalMinutes=0;
	private int totalSeconds = 0;
	private int totalMillisecond = 0;
	
	
	
	
	
	private int currentSeconds;
	private int progress = 0;
	
	
	private boolean isStart;
	private boolean isEnd;

    public  static final int  MODEL_INCREASE = 1;
    public static final int MODEL_DECREASE = 0;
    
	
	private int circularModel = MODEL_INCREASE;
	
	private boolean isDecreasePaintInit;
	
	
	/**
	 * Range of view
	 */
	private int mRange;
	
	
	/**
	 * Thickness of flows
	 */
	private float mOutermosWidth;
	private float mMiddleWidth;
	private float mBenmostWidth;
	
	
	/**
	 * Size of text
	 */
	private float mTextSize;
	private float mMetricSize;
	
	
	/**
	 * Color of bars
	 */
	private int mOutermosColor;
	private int mMiddleColor;
	private int mBenmostColor;
	
	
	/**
	 * Color of text
	 */
	private int mTextColor = -1;
	private int mBackgroundColor;

	
	/**
	 * Paint objects
	 */
	private Paint mOutermosPaint;
	private Paint mMiddlePaint;
	private Paint mBenmostPaint;
	
	
	private Paint mBackgroundPaint;
	private Paint mTextPaint;
	private Paint mMetricPaint;
	
	private Paint mMiddleEraserPaint;
	private Paint mBenmostEraserPaint;
	
	
	/**
	 * Bounds of each flow
	 */
	private RectF mOutermosBounds;
	private RectF mMiddleBounds;
	private RectF mBenmostBounds;
	
	
	/**
	 * Text position
	 */
	private float mTextPosY;
	private float mMetricPosY;
	private float mMetricPaddingY;

	
	/**
	 * Metric in use
	 */
	private String mMetricText;
	
	
	/*
	 * Typeface of text
	 */
	private Typeface mTypeface;
	
	
	private onChronometerStateChangeListener onChronometerStateChangeListener;
	
	/**
	 * Handler to update the view
	 */
//	private SpeedHandler mSpinHandler;
	
	public void setChronometerStateChangeListener(onChronometerStateChangeListener l){
		onChronometerStateChangeListener = l;
	}
	
	
	
	
	public interface onChronometerStateChangeListener{
		void startChronometer(FlatChronometer view);
		void updateChronometerState(FlatChronometer view,int elapseSeconds);
		void stopChronometer(FlatChronometer view);
		
	}
	
	void startChronometer(){
		if(onChronometerStateChangeListener != null)
			onChronometerStateChangeListener.startChronometer(this);
	}
	
	void stopChronometer(){
		if(onChronometerStateChangeListener != null)
			onChronometerStateChangeListener.stopChronometer(this);
		
	}
	
	void updateCountDownState(int elapseSeconds){
		if(onChronometerStateChangeListener != null)
			onChronometerStateChangeListener.updateChronometerState(this, elapseSeconds);
	}
	
	
	
	
	
	@SuppressLint("Recycle")
	public FlatChronometer(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context.obtainStyledAttributes(attrs, R.styleable.CircularMeter));
	}
	

	
	
	
	/**
	 * Setting up variables on attach
	 */
	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
	//	mSpinHandler = new SpeedHandler(this);
		setupBounds();
		setupPaints();
		setupTextPosition();
	}
	
	
	/**
	 * Free variables on detached
	 */
	@Override
	protected void onDetachedFromWindow(){
		super.onDetachedFromWindow();
		
	//	mSpinHandler = null;
		mOutermosPaint = null;
		mOutermosBounds = null;
		mMiddlePaint = null;
		mMiddleBounds = null;
		mBackgroundPaint = null;
		mTextPaint = null;
		mMetricPaint = null;
		if(circularModel == MODEL_DECREASE){
			mMiddleEraserPaint = null;
			mBenmostEraserPaint = null;
		}
	}
	
	
	
	/**
	 * Set up paint variables to be used in onDraw method
	 */
	private void setupPaints() {
		
		mOutermosPaint = new Paint();
		mOutermosPaint.setColor(mOutermosColor);
		mOutermosPaint.setAntiAlias(true);
		mOutermosPaint.setStyle(Style.STROKE);
		mOutermosPaint.setStrokeWidth(mOutermosWidth);
        
		mMiddlePaint = new Paint();
		mMiddlePaint.setColor(mMiddleColor);
		mMiddlePaint.setAntiAlias(true);
		mMiddlePaint.setStyle(Style.STROKE);
		mMiddlePaint.setStrokeWidth(mMiddleWidth);
		
		mBenmostPaint = new Paint();
		mBenmostPaint.setColor(mBenmostColor);
		mBenmostPaint.setAntiAlias(true);
		mBenmostPaint.setStyle(Style.STROKE);
		mBenmostPaint.setStrokeWidth(mBenmostWidth);
		
		if(circularModel == MODEL_DECREASE){
			initDecreasePaint();
		}
		
		
        mBackgroundPaint = new Paint();
        mBackgroundPaint.setColor(mBackgroundColor);
        mBackgroundPaint.setAntiAlias(true);
        mBackgroundPaint.setStyle(Style.FILL);
        
		mTextPaint = new Paint();
		mTextPaint.setColor(mTextColor);
		mTextPaint.setStyle(Style.FILL);
		mTextPaint.setAntiAlias(true);
		mTextPaint.setTextSize(mTextSize);
		mTextPaint .setTypeface(mTypeface);
		mTextPaint.setTextAlign(Align.CENTER);
		
        mMetricPaint = new Paint();
        mMetricPaint.setColor(mTextColor);
        mMetricPaint.setStyle(Style.FILL);
        mMetricPaint.setAntiAlias(true);
        mMetricPaint.setTextSize(mMetricSize);
        mMetricPaint .setTypeface(mTypeface);
        mMetricPaint.setTextAlign(Align.CENTER);
	}
	
	
	private void initDecreasePaint(){
		mMiddleEraserPaint = new Paint();
		mMiddleEraserPaint.setColor(mBackgroundColor);
		mMiddleEraserPaint.setAntiAlias(true);
		mMiddleEraserPaint.setStyle(Style.STROKE);
		mMiddleEraserPaint.setStrokeWidth(mMiddleWidth);
		
		mBenmostEraserPaint = new Paint();
		mBenmostEraserPaint.setColor(mBackgroundColor);
		mBenmostEraserPaint.setAntiAlias(true);
		mBenmostEraserPaint.setStyle(Style.STROKE);
		mBenmostEraserPaint.setStrokeWidth(mBenmostWidth);
		isDecreasePaintInit = true;


	}

	/**
	 * Set the bounds of the bars.
	 */
	private void setupBounds() {
		
		mBackgroundCenter = this.getLayoutParams().width/2;
		mBackgroundRadius = mBackgroundCenter - this.getPaddingTop();
		
		mOutermosBounds = new RectF(this.getPaddingTop() + mOutermosWidth/2,
				this.getPaddingLeft() + mOutermosWidth/2,
                this.getLayoutParams().width - this.getPaddingRight() - mOutermosWidth/2,
                this.getLayoutParams().height - this.getPaddingBottom() - mOutermosWidth/2);	
		
		mMiddleBounds = new RectF(this.getPaddingTop() + mMiddleWidth/2 + mOutermosWidth,
				this.getPaddingLeft() + mMiddleWidth/2 + mOutermosWidth,
                this.getLayoutParams().width - this.getPaddingRight() - mMiddleWidth/2 - mOutermosWidth,
                this.getLayoutParams().height - this.getPaddingBottom() - mMiddleWidth/2 - mOutermosWidth);
		
		mBenmostBounds = new RectF(this.getPaddingTop() + mBenmostWidth/2 +  mMiddleWidth + mOutermosWidth,
				this.getPaddingLeft() + mBenmostWidth/2 + mMiddleWidth + mOutermosWidth,
                this.getLayoutParams().width - this.getPaddingRight() - mBenmostWidth/2 - mMiddleWidth - mOutermosWidth,
                this.getLayoutParams().height - this.getPaddingBottom() - mBenmostWidth/2 - mMiddleWidth - mOutermosWidth);
	}


	
	/**
	 * Setting up text position
	 */
	private void setupTextPosition() {
		Rect textBounds = new Rect();  
		mTextPaint.getTextBounds("1", 0, 1, textBounds);  
		mTextPosY = mOutermosBounds.centerY() + (textBounds.height() / 2f);
		mMetricPosY = mTextPosY + mMetricPaddingY;
	}

	
	
	/**
	 * Parse the attributes passed to the view
	 * and default values.
	 */
	private void init(TypedArray a) {
		
		
		mTextSize = a.getDimension(R.styleable.CircularMeter_textSize, getResources().getDimension(R.dimen.textSize));
		mTextColor = a.getColor(R.styleable.CircularMeter_textColor, mTextColor);
	    
	    mMetricSize = a.getDimension(R.styleable.CircularMeter_metricSize, getResources().getDimension(R.dimen.metricSize));
	    mMetricText = a.getString(R.styleable.CircularMeter_metricText);
	    mMetricPaddingY = getResources().getDimension(R.dimen.metricPaddingY);
	   
	    mRange = a.getInt(R.styleable.CircularMeter_range, 100);
		
	    mOutermosWidth = getResources().getDimension(R.dimen.width);
		mMiddleWidth = getResources().getDimension(R.dimen.width);
		mBenmostWidth = getResources().getDimension(R.dimen.width);
		
		mOutermosColor = -1213350;
		mMiddleColor = -7747644;
		mBenmostColor = -1;
		
	
		
		String aux = a.getString(R.styleable.CircularMeter_typeface);
		if (aux != null)
			mTypeface = Typeface.createFromAsset(this.getResources().getAssets(), aux);
	}


	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		canvas.drawCircle(mBackgroundCenter, mBackgroundCenter, mBackgroundRadius, mBackgroundPaint);
		canvas.drawArc(mBenmostBounds, START_DEGREES_FROM_TOP,360, false, mBenmostPaint);
		
		
		
		
		switch (circularModel) {
		case MODEL_INCREASE:
			
			canvas.drawArc(mMiddleBounds, START_DEGREES_FROM_TOP,((float)progress / 60)*360, false, mMiddlePaint);
			
		//	canvas.drawText(DateUtils.formatElapsedTime(currentSeconds), mOutermosBounds.centerX(), mTextPosY, mTextPaint);
			
			
			
			break;

        case MODEL_DECREASE:
        	
        	
        	if(!isDecreasePaintInit){
        		initDecreasePaint();
        	}
        	
        	canvas.drawArc(mMiddleBounds, START_DEGREES_FROM_TOP,360, false, mMiddlePaint);  	
        	canvas.drawArc(mMiddleBounds, START_DEGREES_FROM_TOP,-((float)progress / 60)*360, false, mMiddleEraserPaint);
        	
     	
        	
        	
			break;
		}
		

		
		
    	if( isEnd == true ){	
    		
    		canvas.drawText("时间到!", mOutermosBounds.centerX(), mTextPosY, mTextPaint);
    		stopChronometer();
    	}else{
    		canvas.drawText(DateUtils.formatElapsedTime(currentSeconds), mOutermosBounds.centerX(), mTextPosY, mTextPaint);
    		canvas.drawText(mMetricText, mOutermosBounds.centerX(), mMetricPosY, mMetricPaint);
    	}
		
    	
    	
    	
		//最外圈
	//	canvas.drawArc(mOutermosBounds, START_DEGREES_FROM_TOP, ( (float)eProgress/ innerCircleMaxValue ) * 360, false, mOutermosPaint);
		
		
		
	}

	

	

	
	
	protected void setCurrenValue(int progress){
		invalidate();
		this.progress = progress;
	}
	
	
	protected void setCurrenValue(){
		invalidate();	
	}

	Handler handler = new Handler() {
		
		public void handleMessage(android.os.Message msg) {

			switch (circularModel) {
			case MODEL_INCREASE:
				
				if (currentSeconds   < totalSeconds){
					
					invalidate();
							
					++currentSeconds;			
					handler.sendEmptyMessageDelayed(progress++, 1000);
								
					if(progress == 60){
						 progress = 0; 		 
					  }
				}
				
				if(currentSeconds == totalSeconds)
					  isEnd = true;
				
				updateCountDownState(currentSeconds);
				
				break;

			case MODEL_DECREASE:
	
				if(currentSeconds > 0){		
					invalidate();
	    	
					currentSeconds--;			
				    handler.sendEmptyMessageDelayed(progress++, 1000);
				    
				    if(progress == 60){
				    	 progress = 0; 	    	 
				    }
				}
				
				if(currentSeconds == 0)
					isEnd = true;
				
				updateCountDownState(totalSeconds - currentSeconds);
				break;
			}
			
			
		
		};
	};
	

	
	
	
	
	


	public void start(){
		 
		 countTotalSeonds(); 
		 
		 
		 currentSeconds = circularModel == MODEL_DECREASE ? totalSeconds : 0;
	 
		 invalidate();
		 startChronometer();
		 
		 handler.sendEmptyMessageDelayed(currentSeconds,1000);	
		 isStart = true;
		 
		 
		 
	}
	
	
	
	public void drawView(){
		setCurrenValue(0);
	}

	
	
	private int countTotalSeonds(){
		
		totalMinutes = totalMinutes+minutes+hour * 60;
		totalSeconds = totalMinutes * 60;
	
		return totalSeconds;
		
		
	}
	

	/*
	 * Setters
	 */
	
	
	
	
	

	/**
	 * @param Outermos Circle Width the mOutermosWidth to set
	 */
	public FlatChronometer setOutermosWidth(float mOutermosWidth) {
		this.mOutermosWidth = mOutermosWidth;
		return this;
	}


	/**
	 * @param Middle Circle Width the mMiddleWidth to set
	 */
	public FlatChronometer setMiddleWidth(float mMiddleWidth) {
		this.mMiddleWidth = mMiddleWidth;
		return this;

	}


	/**
	 * @param Benmost Circle Width the mBenmostWidth to set
	 */
	public FlatChronometer setBenmostWidth(float mBenmostWidth) {
		this.mBenmostWidth = mBenmostWidth;
		return this;

	}



	/**
	 * @param Outermos Circle Color the mOutermosColor to set
	 */
	public FlatChronometer setOutermosColor(int mOutermosColor) {
		this.mOutermosColor = mOutermosColor;
		return this;

	}



	/**
	 * @param  Middle Circle Color the mMiddleColor to set
	 */
	public FlatChronometer setMiddleColor(int mMiddleColor) {
		this.mMiddleColor = mMiddleColor;
		return this;

	}



	/**
	 * @param Benmost Circle Color the mBenmostColor to set
	 */
	public FlatChronometer setBenmostColor(int mBenmostColor) {
		this.mBenmostColor = mBenmostColor;
		return this;

	}



	
	



	




	

	
	
	
	
	
	
	public FlatChronometer setTotalSeconds(int seconds){
		this.totalSeconds = seconds;
		return this;

		
	}
	
	
	public FlatChronometer setTotalMinute(int minutes){
		totalMinutes = minutes;
		return this;
	}
	
	
	
	public FlatChronometer setHour(int hour){
		this.hour = hour;
		return this;
	}
	
	public FlatChronometer setMinute(int minutes){
		this.minutes = minutes;
		return this;
	}
	
	
	public FlatChronometer setSeconds(int seconds){
		this.seconds = seconds;
		return this;
	}
	
	public FlatChronometer setMillisecond(int millisecond){
		this.millisecond = millisecond;
		return this;
	}
	
	
	
	
	public FlatChronometer setCircularModel(int model){
	    circularModel = model;
		return this;
	}
	
	public FlatChronometer setRange(int range){
		mRange = range;
		return this;
	}
	

	
	
	
	

	public FlatChronometer setTextSize(float size) {
		mTextSize = size;
		return this;
	}

	public FlatChronometer setMetricSize(float size) {
		mMetricSize = size;
		return this;
	}



	public FlatChronometer setTextColor(int color) {
		mTextColor = color;
		return this;
	}
	
	public FlatChronometer setMetricText(String text){
		mMetricText = text;
		return this;
	}

	@Override
	public void setBackgroundColor(int color) {
		mBackgroundColor = color;
	}

	public FlatChronometer setTypeface(Typeface typeface) {
		mTypeface = typeface;
		return this;
	}

	

//	/**
//	 * Handles display invalidates
//	 */
//	private static class SpeedHandler extends Handler {
//
//        private CircularCounter act;
//        
//        public SpeedHandler(CircularCounter act) {
//            super();
//            this.act = act;
//        }
//
//		@Override
//		public void handleMessage(Message msg) {
//			act.invalidate();
//			super.handleMessage(msg);
//		}
//
//    }
	
	
	
}
