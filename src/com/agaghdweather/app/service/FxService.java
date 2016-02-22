package com.agaghdweather.app.service;

import com.agaghdweather.app.activity.NewWeatherActivity;
import com.agaghdweather.app.util.MyApplication;
import com.coolweather.app.R;

import android.app.Service;  

import android.content.Intent;  
import android.graphics.PixelFormat;  
import android.os.Binder;
import android.os.Handler;  
import android.os.IBinder;  
import android.util.Log;  
import android.view.Gravity;  
import android.view.LayoutInflater;  
import android.view.MotionEvent;  
import android.view.View;  
import android.view.WindowManager;  
import android.view.View.OnClickListener;  
import android.view.View.OnTouchListener;  
import android.view.WindowManager.LayoutParams;  
import android.widget.Button;  
import android.widget.LinearLayout;  
import android.widget.TextView;
import android.widget.Toast;  
  
public class FxService extends Service   
{  
  
    //���帡�����ڲ���  
    LinearLayout mFloatLayout;  
    WindowManager.LayoutParams wmParams;  
    //���������������ò��ֲ����Ķ���  
    WindowManager mWindowManager;  
      
    Button mFloatView;  
    //TextView mFloatText;
    
    private static final String TAG = "FxService";  
      
    @Override  
    public void onCreate()   
    {  
        // TODO Auto-generated method stub  
        super.onCreate();  
        Log.i(TAG, "oncreat");  
        createFloatView();        
    }  
  

  
    private void createFloatView()  
    {  
        wmParams = new WindowManager.LayoutParams();  
        //��ȡ����WindowManagerImpl.CompatModeWrapper  
        mWindowManager = (WindowManager)getApplication().getSystemService(getApplication().WINDOW_SERVICE);  
        Log.i(TAG, "mWindowManager--->" + mWindowManager);  
        //����window type  
        wmParams.type = LayoutParams.TYPE_PHONE;   
        //����ͼƬ��ʽ��Ч��Ϊ����͸��  
        wmParams.format = PixelFormat.RGBA_8888;   
        //���ø������ڲ��ɾ۽���ʵ�ֲ���������������������ɼ����ڵĲ�����  
        wmParams.flags = LayoutParams.FLAG_NOT_FOCUSABLE;        
        //������������ʾ��ͣ��λ��Ϊ����ö�  
        wmParams.gravity = Gravity.LEFT | Gravity.TOP;         
        // ����Ļ���Ͻ�Ϊԭ�㣬����x��y��ʼֵ�������gravity  
        wmParams.x = 250;  
        wmParams.y = 0;  
  
        //�����������ڳ�������    
        wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;  
        wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT; 
        
        //����������͸����
        wmParams.alpha = 100;
        
         /*// �����������ڳ������� 
        wmParams.width = 200; 
        wmParams.height = 80;*/  
     
        LayoutInflater inflater = LayoutInflater.from(getApplication());  
        //��ȡ����������ͼ���ڲ���  
        mFloatLayout = (LinearLayout) inflater.inflate(R.layout.float_layout, null);  
        //���mFloatLayout  
        mWindowManager.addView(mFloatLayout, wmParams);  
        //�������ڰ�ť  
        mFloatView = (Button)mFloatLayout.findViewById(R.id.float_id);  
        //��ȡҪ�ڸ���������ʾ��������Ϣ
       // mFloatText = (TextView) mFloatLayout.findViewById(R.id.float_text);
       
        
        mFloatLayout.measure(View.MeasureSpec.makeMeasureSpec(0,  
                View.MeasureSpec.UNSPECIFIED), View.MeasureSpec  
                .makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));  
        Log.i(TAG, "Width/2--->" + mFloatView.getMeasuredWidth()/2);  
        Log.i(TAG, "Height/2--->" + mFloatView.getMeasuredHeight()/2);  
        //���ü����������ڵĴ����ƶ�  
        mFloatView.setOnTouchListener(new OnTouchListener()   
        {  
              
            @Override  
            public boolean onTouch(View v, MotionEvent event)   
            {  
                // TODO Auto-generated method stub  
                //getRawX�Ǵ���λ���������Ļ�����꣬getX������ڰ�ť������  
                wmParams.x = (int) event.getRawX() - mFloatView.getMeasuredWidth()/2;  
                Log.i(TAG, "RawX" + event.getRawX());  
                Log.i(TAG, "X" + event.getX());  
                //��25Ϊ״̬���ĸ߶�  
                wmParams.y = (int) event.getRawY() - mFloatView.getMeasuredHeight()/2 - 25;  
                Log.i(TAG, "RawY" + event.getRawY());  
                Log.i(TAG, "Y" + event.getY());  
                 //ˢ��  
                mWindowManager.updateViewLayout(mFloatLayout, wmParams);  
                return false;  //�˴����뷵��false������OnClickListener��ȡ��������  
            }  
        });   
          
        mFloatView.setOnClickListener(new OnClickListener()   
        {  
              
            @Override  
            public void onClick(View v)   
            {  
                // TODO Auto-generated method stub  
                Intent intent = new Intent(MyApplication.getContext(),NewWeatherActivity.class);
                //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                //startActivity(intent);
            }  
        });  
    }  
      
    @Override  
    public void onDestroy()   
    {  
        // TODO Auto-generated method stub  
        super.onDestroy();  
        if(mFloatLayout != null)  
        {  
            //�Ƴ���������  
            mWindowManager.removeView(mFloatLayout);  
        }  
    }  
    
   /**
    * ��Binder����󶨷��� 
    */
    private ShowTextBinder showTextBinder = new ShowTextBinder();
    
    public class ShowTextBinder extends Binder {
	
    	public  void setFloatWindowText(String text) {
    		mFloatView.setText(text);
    	}
    }
    
    @Override
    public IBinder onBind(Intent intent) {
    	return showTextBinder;
    }
    
      
}  
