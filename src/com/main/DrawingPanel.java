package com.main;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class DrawingPanel extends View implements OnTouchListener {

	private Canvas mCanvas;
	private Path mPath;
	private Paint mPaint;
	private ArrayList<Path> paths = new ArrayList<Path>();
	private Boolean clean = false;

	public DrawingPanel(Context context) {
		super(context);
		setFocusable(true);
		setFocusableInTouchMode(true);
		this.setOnTouchListener(this);

		createPaint();
		mCanvas = new Canvas();
		mCanvas.drawColor(Color.WHITE);
		mPath = new Path();
		paths.add(mPath);
	}
	
	//cria uma nova instancia paint, que guardará os desenhos
	private void createPaint() {
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setDither(true);
		mPaint.setColor(Color.BLUE);
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeJoin(Paint.Join.ROUND);
		mPaint.setStrokeCap(Paint.Cap.ROUND);
		mPaint.setStrokeWidth(6);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawColor(Color.WHITE);
		// limpa o ecrã se a flag estiver ativa
		if(clean) {
			createPaint();
			paths.clear();
			mPath = new Path();
			paths.add(mPath);
			clean = false;
		}
		else {
			for (Path p : paths) {
				canvas.drawPath(p, mPaint);
			}
		}
	}

	private float mX, mY;
	private static final float TOUCH_TOLERANCE = 4;

	private void touch_start(float x, float y) {
		mPath.reset();
		mPath.moveTo(x, y);
		mX = x;
		mY = y;
	}

	private void touch_move(float x, float y) {
		float dx = Math.abs(x - mX);
		float dy = Math.abs(y - mY);
		if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
			mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
			mX = x;
			mY = y;
		}
	}

	private void touch_up() {
		mPath.lineTo(mX, mY);
		// envia o path para o canvas de background 
		mCanvas.drawPath(mPath, mPaint);
		// elimina o caminho atual, para não desenhar duas vezes o mesmo
		mPath = new Path();
		paths.add(mPath);
	}

	public boolean onTouch(View arg0, MotionEvent event) {
		float x = event.getX();
		float y = event.getY();
		
		// switch para os eventos relacionados com o toque no ecrã
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			touch_start(x, y);
			invalidate();
			break;
		case MotionEvent.ACTION_MOVE:
			touch_move(x, y);
			invalidate();
			break;
		case MotionEvent.ACTION_UP:
			touch_up();
			invalidate();
			break;
		}
		return true;
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)  {
	    if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
	        // regista intenção de limpar o canvas na próxima atualização
	    	clean = true;
	    	this.invalidate();
	    }
	    return true;
	}
}
