package demo.com.myvoicedemo;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author MartinLi 2018/9/10
 * 10:12
 */
public class WaveView extends View {
	private static final String TAG = "WaveView";
	private volatile int width = 0;
	private volatile int height = 0;
	private Path shapePath;
	private volatile int step = 20;
	private Paint fillPaint;

	private volatile double omega;//波长
	private volatile double phi;//初始相位
	private volatile int delta = -2;//每次位移角度，大于0向左，小于0 向右
	private volatile boolean fillTop = true;
	private volatile int waveColor;

	public WaveView(Context context) {
		this(context, null);
	}

	public WaveView(Context context, @Nullable AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public WaveView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		this(context, attrs, defStyleAttr, 0);
//		TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.wave);
//		initParams(context, typedArray);
//		typedArray.recycle();
	}

	@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
	public WaveView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.wave);
		initParams(context, typedArray);
		typedArray.recycle();
	}


	private void initParams(Context context, TypedArray typedArray) {

		waveColor = typedArray.getColor(R.styleable.wave_color, Color.parseColor("#3958AA"));
		fillTop = typedArray.getInt(R.styleable.wave_fill_mode, 1) == 1;// 默认绘制上层
		delta = typedArray.getInt(R.styleable.wave_speed, -2); //默认向右移动
		omega = typedArray.getFloat(R.styleable.wave_omega, 3 * 1.0f / 4);// 角频率
		phi = typedArray.getInt(R.styleable.wave_phi, 0) * Math.PI / 180 + Math.PI / 2 * -1;// 初始相位角


		initPaint();

	}

	private void initPaint() {
		fillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		fillPaint.setStrokeWidth(1);
		fillPaint.setStyle(Paint.Style.FILL);
		fillPaint.setColor(waveColor);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		if (shapePath == null) {
			shapePath = new Path();
		} else {
			shapePath.reset();
		}

		if (fillTop) {

			shapePath.moveTo(0, 0);
		} else {
			shapePath.moveTo(0, height + 1);
		}
		for (int x = 0; x <= width; x += step) {
			double angle = x * 1.0f / width * 2 * Math.PI;// 弧度
			double y = height / 2 * Math.sin(angle * omega + phi);
			shapePath.lineTo(x, (float) y + height / 2);
		}

		if (fillTop) {
			shapePath.lineTo(width, 0);
		} else {
			shapePath.lineTo(width, height + 1);
		}
		canvas.drawPath(shapePath, fillPaint);

		postInvalidateDelayed(30); // 每秒刷新40次
		addPhi();
	}

	/**
	 * 增加相位角
	 */
	private void addPhi() {
		int temp_delta = delta;
		phi += temp_delta * Math.PI / 180;

		if (phi > Math.PI * 2)
			phi -= Math.PI * 2;
		else if (phi < Math.PI * -2)
			phi += Math.PI * 2;
	}


	/**
	 * @param speed 绝对值越大 跑的波速越快 大于0向左，小于0 向右
	 */
	public void setVoice(int speed) {
		if (delta < 0) {
			delta = -Math.abs(speed);

		} else {
			delta = Math.abs(speed);
		}

	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		/**
		 * 组件测量
		 * 布局里指定的size使用布局的size
		 * 属性里指定的size使用属性的size
		 * 都没有指定，使用默认size
		 */
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int widthSize = 0;
		if (widthMode == MeasureSpec.EXACTLY) {
			widthSize = MeasureSpec.getSize(widthMeasureSpec);
			width = widthSize;
		} else if (widthMode == MeasureSpec.AT_MOST) {
			widthSize = width;
		}
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int heightSize = 0;
		if (heightMode == MeasureSpec.EXACTLY) {
			heightSize = MeasureSpec.getSize(heightMeasureSpec);
			height = heightSize;
		} else if (heightMode == MeasureSpec.AT_MOST) {
			heightSize = height;
		}
		setMeasuredDimension(widthSize, heightSize);
	}
}
