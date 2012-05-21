package com.main;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class DrawingPanel extends View implements OnTouchListener {

	private Canvas mCanvas;
	private Path mPath;
	private ArrayList<Paint> mPaints;
	private Paint mPaint;
	private ArrayList<Path> paths = new ArrayList<Path>();
	private Boolean clean = false;
	private Boolean toReplay = true;
	private int currentColor = Color.BLACK;
	private int strokeWidth = 6;
	private ArrayList<Integer> pathsByPaint;
	private ArrayList<Float> xs;
	private ArrayList<Float> ys;
	private ArrayList<Integer> colors;

	public DrawingPanel(Context context) {
		super(context);
		setFocusable(true);
		setFocusableInTouchMode(true);
		this.setOnTouchListener(this);

		xs = new ArrayList<Float>();
		ys = new ArrayList<Float>();
		colors = new ArrayList<Integer>();

		pathsByPaint = new ArrayList<Integer>();
		mPaints = new ArrayList<Paint>();
		mCanvas = new Canvas();
		mCanvas.drawColor(Color.WHITE);

		createPaint();
	}

	// cria uma nova instancia paint, que guardará os desenhos
	private void createPaint() {
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setDither(true);
		mPaint.setColor(currentColor);
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeJoin(Paint.Join.ROUND);
		mPaint.setStrokeCap(Paint.Cap.ROUND);
		mPaint.setStrokeWidth(strokeWidth);
		mPaints.add(mPaint);
		mPath = new Path();
		paths.add(mPath);
		pathsByPaint.add(1);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawColor(Color.WHITE);
		System.out.println("AKLFAJLKSFJHASHFKJASHF");
		// limpa o ecrã se a flag estiver ativa
		if (clean) {
			paths.clear();
			pathsByPaint.clear();
			mPaints.clear();
			createPaint();
			clean = false;
		} else {
			// desenha cada paint com a sua cor
			int acum = 0;

			for (int j = 0; j < mPaints.size(); j++) {
				for (int n = pathsByPaint.get(j); n > 0; n--) {

					canvas.drawPath(paths.get(acum++), mPaints.get(j));
				}
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
		if (toReplay) {
			xs.add(mX);
			ys.add(mY);
			colors.add(mPaint.getColor());
		}
	}

	private void touch_move(float x, float y) {
		float dx = Math.abs(x - mX);
		float dy = Math.abs(y - mY);
		if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
			mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
			mX = x;
			mY = y;
			if (toReplay) {
				xs.add(mX);
				ys.add(mY);
				colors.add(mPaint.getColor());
			}
		}
	}

	private void touch_up() {
		mPath.lineTo(mX, mY);
		// envia o path para o canvas de background
		mCanvas.drawPath(mPath, mPaint);
		// elimina o caminho atual, para não desenhar duas vezes o mesmo
		mPath = new Path();
		paths.add(mPath);
		pathsByPaint.set(pathsByPaint.size() - 1,
				pathsByPaint.get(pathsByPaint.size() - 1) + 1);
		if (toReplay) {
			xs.add(-1.0f);
			ys.add(-1.0f);
			colors.add(mPaint.getColor());
		}
	}

	public boolean onTouch(View arg0, MotionEvent event) {
		if (toReplay) {
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
		} else
			invalidate();
		return true;
	}

	/*
	 * @Override public boolean onKeyDown(int keyCode, KeyEvent event) { if
	 * (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) { //
	 * regista intenção de limpar o canvas na próxima atualização clean = true;
	 * this.invalidate(); } return true; }
	 */

	public void cleanCanvas() {
		clean = true;
		this.invalidate();
	}

	public void eraseMode() {
		currentColor = Color.WHITE;
		strokeWidth = 12;

		// é criado um novo paint com a cor a branco, que serve para apagar
		createPaint();
	}

	public void changeColor(int color) {
		currentColor = color;
		strokeWidth = 6;

		// é criado um novo paint com a nova cor seleccionada
		createPaint();
	}

	public void replay() {
		toReplay = false;
		touch_start(xs.get(0), ys.get(0));
		mPaint.setColor(colors.get(0));
		new Thread(new Runnable() {
			public void run() {
				for (int i = 1; i < xs.size(); i++) {
					mPaint.setColor(colors.get(i));
					if (xs.get(i) == -1.0f) {
						
						touch_up();
						if (i != xs.size() - 1)
						{
							if(colors.get(i+1) != mPaint.getColor())
								createPaint();
							touch_start(xs.get(i + 1), ys.get(i + 1));
						}
					} else {
						touch_move(xs.get(i), ys.get(i));
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
						}
					}
					DrawingPanel.this.postInvalidate();
				}
				toReplay = true;
			}
		}).start();
	}
}
