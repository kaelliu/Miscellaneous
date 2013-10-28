package com.finch.kaeltest;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.View;

public class ZhuanpanView extends View implements Runnable{
	private Bitmap panpic;
	private Bitmap panhand;
	
	private Matrix panRotate = new Matrix();
	private Matrix panhandTrans = new Matrix();
	private int x = 0;
	private boolean ifRotate = false;
	
	public ZhuanpanView(Context context){//,AttributeSet attrs){
		super(context);//,attrs);
		Resources r=context.getResources();
		panhandTrans.setTranslate(180, 170-83);
		panpic = BitmapFactory.decodeStream(r.openRawResource(R.drawable.share_lottery));
		panhand = BitmapFactory.decodeStream(r.openRawResource(R.drawable.share_lottery_pointer));
		
		Thread thread=new Thread(this);
		thread.start();
	}
	
	@Override
	protected void onDraw(Canvas canvas){
		panRotate.setRotate(x,180,170);
		canvas.drawBitmap(panpic, panRotate, null);
		canvas.drawBitmap(panhand, panhandTrans, null);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true){
			if(ifRotate){
				this.x+=25;
				//这个函数强制UI线程刷新界面
				this.postInvalidate();
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	public void startRotate(){
		this.ifRotate=true;
	}

	public void stopRotate(){
		this.ifRotate=false;
	}
}
