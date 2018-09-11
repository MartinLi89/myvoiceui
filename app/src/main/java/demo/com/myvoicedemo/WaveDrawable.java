package demo.com.myvoicedemo;

import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.view.animation.LinearInterpolator;

/**
 * Created by ex-keayuan001 on 2018/4/4.
 *
 * @author ex-keayuan001
 */
public class WaveDrawable extends Drawable implements Drawable.Callback, Animatable {

	/**
	 * 振幅
	 */
	private int A = 30;

	/**
	 * 初相
	 */
	private float φ = -(float) (Math.PI / 2);

	/**
	 * 波形移动的速度
	 */
	private float waveSpeed = 0.05f;

	/**
	 * 角速度
	 */
	private double ω;
	private float periodPercent = 0.5f;

	private Path mPath;
	private Paint mPaint;

	private int mWidth;
	private int mHeight;

	private boolean isFillTop;

	private ValueAnimator valueAnimator;

	public WaveDrawable() {
		initPaint();
		initAnimation();
	}

	private void initPaint() {
		mPath = new Path();
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaint.setAntiAlias(true);
		mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
	}

	public void setFillTop(boolean isTop) {
		isFillTop = isTop;
	}

	public void setFillColor(int color) {
		mPaint.setColor(color);
	}

	public void setSwing(int px) {
		A = px;
	}

	public void setWaveSpeed(float speed) {
		waveSpeed = speed;
	}

	/**
	 * @param period (0,1]
	 */
	public void setPeriod(float period) {
		periodPercent = period;
		ω = Math.PI / mWidth / periodPercent;
	}

	@Override
	protected void onBoundsChange(Rect bounds) {
		mWidth = bounds.width();
		mHeight = bounds.height();
		ω = Math.PI / mWidth / periodPercent;
		initAnimation();
	}

	@Override
	public void draw(Canvas canvas) {
		fillTop(canvas, isFillTop);
	}

	@Override
	public int getIntrinsicWidth() {
		return mWidth;
	}

	@Override
	public int getIntrinsicHeight() {
		return mHeight;
	}

	/**
	 * 填充波浪上面部分
	 */
	private void fillTop(Canvas canvas, boolean isTop) {
		float y = mHeight;

		mPath.reset();
		mPath.moveTo(0, isTop ? mWidth : mHeight);

		for (int x = 0; x <= mWidth; x += 20) {
			y = (float) (A * Math.sin(ω * x + φ) + A);
			mPath.lineTo(x, isTop ? mHeight - y : y);
		}

		mPath.lineTo(mWidth, isTop ? mHeight - y : y);
		mPath.lineTo(mWidth, isTop ? 0 : mHeight);
		mPath.lineTo(0, isTop ? 0 : mHeight);
		mPath.close();

		canvas.drawPath(mPath, mPaint);
		φ += waveSpeed;
	}

	private void initAnimation() {
		valueAnimator = ValueAnimator.ofInt(0, mWidth);
		valueAnimator.setDuration(1000);
		valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
		valueAnimator.setInterpolator(new LinearInterpolator());
		valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				invalidateSelf();
			}
		});
	}

	@Override
	public void setAlpha(int alpha) {
		mPaint.setAlpha(alpha);
	}

	@Override
	public void setColorFilter(ColorFilter cf) {
		mPaint.setColorFilter(cf);
	}

	@Override
	public int getOpacity() {
		return PixelFormat.TRANSLUCENT;
	}

	@Override
	public void invalidateDrawable(Drawable who) {
		if (getCallback() != null) {
			getCallback().invalidateDrawable(this);
		}
	}

	@Override
	public void scheduleDrawable(Drawable who, Runnable what, long when) {
		if (getCallback() != null) {
			getCallback().scheduleDrawable(this, what, when);
		}
	}

	@Override
	public void unscheduleDrawable(Drawable who, Runnable what) {
		if (getCallback() != null) {
			getCallback().unscheduleDrawable(this, what);
		}
	}

	@Override
	public void start() {
		if (valueAnimator != null) {
			valueAnimator.start();
		}
	}

	@Override
	public void stop() {
		if (valueAnimator != null) {
			valueAnimator.cancel();
		}
	}

	@Override
	public boolean isRunning() {
		return valueAnimator.isRunning();
	}
}
