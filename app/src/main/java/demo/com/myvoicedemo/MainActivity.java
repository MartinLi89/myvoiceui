package demo.com.myvoicedemo;

import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

	// Used to load the 'native-lib' library on application startup.
	static {
		System.loadLibrary("native-lib");
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Example of a call to a native method
		TextView tv = (TextView) findViewById(R.id.sample_text);
		tv.setText(stringFromJNI());


		ImageView iv_show = findViewById(R.id.iv_show);

		WaveDrawable drawable = new WaveDrawable();
		drawable.setFillColor(Color.parseColor("#000000"));
		drawable.setSwing(100);
		iv_show.setImageDrawable(drawable);

		setVoice(0);
	}


	Handler mhandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 0x11) {
//				setVoice((int) (Math.random() * 40));
				crm_wave1.setVoice((int) (Math.random() * 10)-1);
				crm_wave2.setVoice((int) (Math.random() * 20)+1);
				crm_wave3.setVoice((int) (Math.random() * 30)-1);
				mhandler.sendEmptyMessageDelayed(0x11, 1000);
			}

		}
	};


	@Override
	protected void onResume() {
		super.onResume();
		mhandler.sendEmptyMessage(0x11);
	}

	WaveView crm_wave1;
	WaveView crm_wave2;
	WaveView crm_wave3;
	public void setVoice(int voice) {
		 crm_wave1 = findViewById(R.id.crm_wave1);
		 crm_wave2 = findViewById(R.id.crm_wave2);
		 crm_wave3 = findViewById(R.id.crm_wave3);



	}

	/**
	 * A native method that is implemented by the 'native-lib' native library,
	 * which is packaged with this application.
	 */
	public native String stringFromJNI();
}
