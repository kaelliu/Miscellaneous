package com.finch.kaeltest;

import java.io.File;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.webkit.JsResult;
public class MainActivity extends Activity {

	private ZhuanpanView panView = null;
	WebView myWebView;
	private class MyWebViewClient extends WebViewClient 
	{ 
	    @Override 
	    //show the web page in webview but not in web browser
	    public boolean shouldOverrideUrlLoading(WebView view, String url) { 
	        view.loadUrl (url); 
	        return true;
	    }
	}
	
	 public class WebAppInterface
	    {
	        Context mContext;

	        /** Instantiate the interface and set the context */
	        WebAppInterface(Context c)
	        {
	            mContext = c;
	        }

	        /** Show a toast from the web page */
	        // ���target ���ڵ���API 17������Ҫ��������ע��
	        @JavascriptInterface
	        public void showToast(String toast)
	        {
	            // Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
	            Toast.makeText(mContext, toast, Toast.LENGTH_LONG).show();
	        }
	    }
	public int progress = 0;
//	ProgressBar progressBar;
	ProgressDialog dialog ;//= ProgressDialog.show(MainActivity.this, "����", "����", true);
//	final Handler handler = new Handler() {
//		public void handleMessage(Message msg) {
//			dialog.setProgress(progress++);
//			if(progress>=100){
//				dialog.dismiss(); //�ر�progressdialog
////				Thread.currentThread().s;
//				onFakeLoadingOver();
//			}
//		}
//	};
	
	/** 
     * ��Handler������UI 
     */  
    private Handler handler = new Handler(){  
    @Override  
    public void handleMessage(Message msg) {  
        //�ر�ProgressDialog  
    	dialog.dismiss();  
    }}; 
    
	protected void onFakeLoadingOver(){

	}
	
	
	
	@SuppressLint({ "SetJavaScriptEnabled", "JavascriptInterface" })
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // �ޱ�����
//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // ��״̬��
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
//        		WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dialog = ProgressDialog.show(MainActivity.this, "waiting", "loading hardly", true);
        
        // test code
        Button btn = (Button) this.findViewById(R.id.start);
        btn.setEnabled(false);
//        progressBar = (ProgressBar) this.findViewById(R.id.progressBar1);
//        progressBar.setVisibility();
//      panView = new ZhuanpanView(this);
        
//      FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(  
//              FrameLayout.LayoutParams.WRAP_CONTENT,  
//              FrameLayout.LayoutParams.WRAP_CONTENT);  
//      this.addContentView(panView, params);
//      Button startButton=(Button) this.findViewById(R.id.start);
//      Button stopButton=(Button) this.findViewById(R.id.stop);
//      startButton.setOnClickListener(new OnClickListener() {	
//			public void onClick(View arg0) {
//				 panView.startRotate();
//			}
//		});
//      
//      stopButton.setOnClickListener(new OnClickListener() {
//      	public void onClick(View arg0) {
//				 panView.stopRotate();
//			}
//		});
      
      myWebView = (WebView) findViewById(R.id.webView1);
      myWebView.getSettings().setJavaScriptEnabled(true); 
     
      myWebView.setWebViewClient(new WebViewClient(){
    	  @Override
          public void onPageFinished(WebView view, String url) {
                  super.onPageFinished(view, url);
                  //ҳ���������,ȴ������ҳ����Ⱦ�����ʾ����
                  //WebChromeClient��progress==100ʱҲ��һ��
                  if (myWebView.getContentHeight() != 0) {
                          //���ʱ����ҳ����ʾ
                  }
          }
    	  //  animation �����࣬����interpolator
    	  /*
    	   * <set xmlns:android="http://schemas.android.com/apk/res/android"
			    android:ordering="sequentially" >
			    <objectAnimator
			    android:duration="3000"
			    android:interpolator="@android:anim/linear_interpolator"
			        android:propertyName="rotation"
			        android:repeatCount="infinite"
			        android:valueTo="360"
			        android:valueFrom="0" />
			</set>
			AnimatorSet solTeypAnim = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.solteypanim);
    		solTeypAnim.setTarget(solTeyp);
    		solTeypAnim.start();
    	   */
      });
      myWebView.setWebChromeClient(new WebChromeClient()
      {

          @Override
          public boolean onJsAlert(WebView view, String url, String message,
                  JsResult result)
          {
              // TODO Auto-generated method stub
              return super.onJsAlert(view, url, message, result);
          }
          @Override
          public void onProgressChanged(WebView view, int newProgress) {
        	  super.onProgressChanged(view, newProgress);
//        	  progressBar.setProgress(newProgress);
//        	  progressBar.postInvalidate();
        	  if (newProgress == 100) {
//        		  progressBar.setVisibility(View.GONE);
        		  // when loading over
        		  handler.sendEmptyMessage(0); 
        	  }
          }

      });
      
      // ��JavaScript����Android������
      // �Ƚ��������࣬��Ҫ���õ�Android����д���������public����
      // ���������WebView�����е�JavaScript����
      // ��һ��������һ���������룬��JS�������������������������󣬿��Ե�����������һЩ����
      myWebView.addJavascriptInterface(new WebAppInterface(this),
              "myInterfaceName");
      
      myWebView.loadUrl("file:///android_asset/raw/lottery.html"); 
        
        
//        Thread kaelThread = new Thread() {
//        	public void run() {
//        		while(true){
//        			if(progress>=100){
//        				break;
//        			}
//        			//���ﴦ���ʱ�����������ļ����صȵȡ�
//        			handler.sendEmptyMessage(0); //����handler
//        		}
//        	}
//        };
//        kaelThread.start();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}
