package com.agaghdweather.app.activity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import ofs.ahd.dii.AdManager;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.coolweather.app.R;


public class SplashActivity  extends Activity {
	
	private RelativeLayout splashLayout;
	
	
	private TextView seasonText;
	private TextView seasonWord;
	private int mMonth;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView (R.layout.splash);
		
		splashLayout = (RelativeLayout) findViewById(R.id.splash);
		seasonText =  (TextView) findViewById (R.id.season);
		seasonWord = (TextView) findViewById (R.id.season_keyword);
		
		/**
		 * ��ȡ��ǰ���������趨��ӭ���汳��
		 */
		 SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM"); 
		 String date=sdf.format(new java.util.Date()); 
		 
		// final Calendar c = Calendar.getInstance();
		 
		// mMonth = c.get(Calendar.MONTH);//��ȡ��ǰ�·� 
		 
		 char c[] = date.toCharArray();
		 String dataMonth = new String(c, 5, 2);
		 mMonth = Integer.parseInt(dataMonth);
		 
		 /**
		  * ��������Ϊ����
		  */
		 AssetManager mgr=getAssets();//�õ�AssetManager
		 Typeface tf=Typeface.createFromAsset(mgr, "fonts/stkaiti.TTF");//����·���õ�Typeface
		 seasonText.setTypeface(tf);
		 seasonWord.setTypeface(tf);
		 
		 switch (mMonth) {
		 case 3:
		 case 4:
		 case 5:
			 splashLayout.setBackgroundResource(R.drawable.spring);
			 seasonText.setText("��");
			 seasonWord.setText("��������");
			 
			 break;
		 case 6:
		 case 7:
		 case 8:
			 splashLayout.setBackgroundResource(R.drawable.summer);
			 seasonText.setText("��");
			 seasonWord.setText("��������");
			 break;
		 case 9:
		 case 10:
		 case 11:
			 splashLayout.setBackgroundResource(R.drawable.autumn);
			 seasonText.setText("��");
			 seasonWord.setText("һҶ֪��");
			 break;
		 case 12:
		 case 1:
		 case 2:
			 splashLayout.setBackgroundResource(R.drawable.winter);
			 seasonText.setText("��");
			 seasonWord.setText("�����ݷ�");
			 break;
		 default:
			 break;
	
		 }
		
		 //���׹�� ��ʼ��Ӧ����Ϣ
		 
		 AdManager.getInstance(this).init("4c62e52a585e61e9", "526b72930ae45e92", false);
		 
		mHandler.sendEmptyMessageDelayed(GOTO_MAIN_ACTIVITY, 3000);//3����ת
		//Toast.makeText(this, "HHHHH " + mMonth , Toast.LENGTH_LONG).show();
		
	}
	
	 private static final int GOTO_MAIN_ACTIVITY = 0;
	    private Handler mHandler = new Handler(){
	        public void handleMessage(android.os.Message msg) {
	 
	            switch (msg.what) {
	                case GOTO_MAIN_ACTIVITY:
	                    Intent intent = new Intent();
	                    intent.setClass(SplashActivity.this, ChooseAreaActivity.class);
	                    startActivity(intent);
	                    finish();
	                    break;
	 
	                default:
	                    break;
	            }
	        }
	    };
	

}
