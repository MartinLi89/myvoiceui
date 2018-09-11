package demo.com.myvoicedemo;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;

/**
 * @author MartinLi 2018/9/10
 * <p>
 * 做了音频柱状动效 只是个假的
 * 15:05
 */
public class TiaoVoice extends View {
	private Paint fillPaint;//圆的画笔
	private Path circle;
	private Paint linePaint;//竖线的画笔
	private int color;
	private int background_color;
	private int speed;
	private int lintWidth;
	private Path linePath;

	public TiaoVoice(Context context) {
		this(context, null);

	}

	public TiaoVoice(Context context, @Nullable AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public TiaoVoice(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		this(context, attrs, defStyleAttr, 0);
	}

	public TiaoVoice(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.tiaovoice);

		if (typedArray != null) {
			initParams(context, typedArray);
			typedArray.recycle();
			typedArray = null;
		}


		initData();

	}

	private void initParams(Context context, TypedArray typedArray) {
		color = typedArray.getColor(R.styleable.tiaovoice_tv_color, Color.parseColor("#ffffff"));
		background_color = typedArray.getColor(R.styleable.tiaovoice_tv_background_color, Color.parseColor("#ff0000"));
		speed = typedArray.getInt(R.styleable.tiaovoice_tv_speed, 200);
		lintWidth = typedArray.getInt(R.styleable.tiaovoice_tv_lint_width, 40);
	}


	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		int measuredWidth = getMeasuredWidth();

		int measuredHeight = getMeasuredHeight();
		initCircle(measuredWidth, measuredHeight);
		initLine(measuredWidth, measuredHeight);
	}

	ArrayList<MyLine> lines = new ArrayList<>();

	private void initLine(int width, int height) {

		lines.clear();

		float cx = width * 0.5f;
		float cy = height * 0.5f;

		float dartex = cx * 0.4f;//线的间距
		float dartey = height * 0.25f;//线的高度


		MyLine myLine1 = new MyLine(cx, (cy + dartey), (dartey));

		MyLine myLine2 = new MyLine(cx - dartex, (cy + dartey), (cy - dartey));
		MyLine myLine3 = new MyLine(cx + dartex, (cy + dartey), (cy - dartey));

		lines.add(myLine1);
		lines.add(myLine2);
		lines.add(myLine3);
	}

	private void initData() {

		fillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

		fillPaint.setStrokeWidth(1);
		fillPaint.setStyle(Paint.Style.FILL);

		fillPaint.setColor(background_color);


		linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		linePaint.setColor(color);
		linePaint.setStrokeWidth(lintWidth);
		linePaint.setStyle(Paint.Style.FILL_AND_STROKE);
	}

	private void initCircle(int width, int height) {

		circle = new Path();
		float cx = width * 0.5f;
		float cy = height * 0.5f;
		int radius = (int) Math.min(cx, cy);
		circle.addCircle(cx, cy, radius, Path.Direction.CCW);
	}


	class MyLine {

		float originX;
		float originY;


		float finishX;
		float finishY;

		public MyLine(float x, float y, float fy) {
			originX = x;
			originY = y;

			finishX = x;
			finishY = fy;
		}

		/**
		 * 获取线的长度
		 *
		 * @return
		 */
		public float getline() {
			return (float) Math.sqrt((originX - finishX) * (originX - finishX)
					+ (originY - finishY) * (originY - finishY));
		}

	}


	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);
		canvas.drawPath(circle, fillPaint);//画圆

		if (linePath == null) {
			linePath = new Path();

		} else {
			linePath.reset();
		}

		for (MyLine line : lines) {
			linePath.moveTo(line.originX, line.originY);

			float lineHeith = line.getline();

			linePath.lineTo(line.finishX, line.finishY + (float) (lineHeith * Math.random())
			);
		}

		canvas.drawPath(linePath, linePaint);
		postInvalidateDelayed(speed);

	}


}
