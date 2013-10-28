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
	        // 如果target 大于等于API 17，则需要加上如下注解
	        @JavascriptInterface
	        public void showToast(String toast)
	        {
	            // Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
	            Toast.makeText(mContext, toast, Toast.LENGTH_LONG).show();
	        }
	    }
	public int progress = 0;
//	ProgressBar progressBar;
	ProgressDialog dialog ;//= ProgressDialog.show(MainActivity.this, "标题", "正文", true);
//	final Handler handler = new Handler() {
//		public void handleMessage(Message msg) {
//			dialog.setProgress(progress++);
//			if(progress>=100){
//				dialog.dismiss(); //关闭progressdialog
////				Thread.currentThread().s;
//				onFakeLoadingOver();
//			}
//		}
//	};
	
	/** 
     * 用Handler来更新UI 
     */  
    private Handler handler = new Handler(){  
    @Override  
    public void handleMessage(Message msg) {  
        //关闭ProgressDialog  
    	dialog.dismiss();  
    }}; 
    
	protected void onFakeLoadingOver(){

	}
	
	
	
	@SuppressLint({ "SetJavaScriptEnabled", "JavascriptInterface" })
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 无标题栏
//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 无状态栏
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
                  //页面下载完毕,却不代表页面渲染完毕显示出来
                  //WebChromeClient中progress==100时也是一样
                  if (myWebView.getContentHeight() != 0) {
                          //这个时候网页才显示
                  }
          }
    	  //  animation 动画类，缓动interpolator
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
      
      // 用JavaScript调用Android函数：
      // 先建立桥梁类，将要调用的Android代码写入桥梁类的public函数
      // 绑定桥梁类和WebView中运行的JavaScript代码
      // 将一个对象起一个别名传入，在JS代码中用这个别名代替这个对象，可以调用这个对象的一些方法
      myWebView.addJavascriptInterface(new WebAppInterface(this),
              "myInterfaceName");
      
      myWebView.loadUrl("file:///android_asset/raw/lottery.html"); 
        
        
//        Thread kaelThread = new Thread() {
//        	public void run() {
//        		while(true){
//        			if(progress>=100){
//        				break;
//        			}
//        			//这里处理耗时操作，比如文件下载等等。
//        			handler.sendEmptyMessage(0); //告诉handler
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
