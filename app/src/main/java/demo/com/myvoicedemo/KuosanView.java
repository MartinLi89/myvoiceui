package demo.com.myvoicedemo;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author MartinLi 2018/9/11
 * 13:56
 */
public class KuosanView extends View {
	public KuosanView(Context context) {
		this(context, null);

	}

	public KuosanView(Context context, @Nullable AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public KuosanView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		this(context, attrs, defStyleAttr, 0);
	}

	public KuosanView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);


		initData();
	}

	private void initData() {



	}
}
