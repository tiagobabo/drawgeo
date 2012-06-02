package pt.drawgeo.canvas;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import pt.drawgeo.utility.Configurations;
import pt.drawgeo.utility.Connection;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.AsyncTask;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class DrawingPanel extends View implements OnTouchListener  {

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
	private float mX, mY;
	private int defaultstrokewidth = 6;
	private int erasestrokewidth = 20;
	private static final float TOUCH_TOLERANCE = 4;
	private Dialog dialog;
	private Context mContext;

	private float xden;
	private float yden;
	
	private float xdenDraw;
	private float ydenDraw;

	public DrawingPanel(Context context, float xden, float yden) {
		super(context);
		setFocusable(true);
		setFocusableInTouchMode(true);
		this.setOnTouchListener(this);
		
		xs = new ArrayList<Float>();
		ys = new ArrayList<Float>();
		colors = new ArrayList<Integer>();

		this.xden = xden;
		this.yden = yden;
		
		pathsByPaint = new ArrayList<Integer>();
		mPaints = new ArrayList<Paint>();
		mCanvas = new Canvas();
		mCanvas.drawColor(Color.WHITE);
		mContext = context;
		createPaint();
	}

	public DrawingPanel(Context context,
			String colorsString, String xsString, String ysString, float xdenDraw, float ydenDraw, float xden, float yden) {
		super(context);
		xs = stringToFloatList(xsString);
		ys = stringToFloatList(ysString);
		colors = stringToIntList(colorsString);
		
		this.xden = xden;
		this.yden = yden;
		
		this.xdenDraw = xdenDraw;
		this.ydenDraw = ydenDraw;
		pathsByPaint = new ArrayList<Integer>();
		mPaints = new ArrayList<Paint>();
		mCanvas = new Canvas();
		mCanvas.drawColor(Color.WHITE);
		mContext = context;
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
	protected void onDraw(Canvas canvas) {
		canvas.drawColor(Color.WHITE);
		
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

	private void touch_start(float x, float y) {
		// limpa o caminho anterior
		mPath.reset();
		
		// posiciona o primeiro ponto no canvas
		mPath.moveTo(x, y);
		mX = x;
		mY = y;
		
		// desenha um ponto simples no ecr‹
		mPath.quadTo(mX, mY, ((x + 0.1f) + mX) / 2, ((y + 0.1f) + mY) / 2);
		
		// guarda os dados necess‡rios para o replay
		if (toReplay) {
			xs.add(mX);
			ys.add(mY);
			colors.add(mPaint.getColor());
		}
	}

	private void touch_move(float x, float y) {
		
		// Dividir a densidade nossa pela do desenho e aplicar esse valor 1+densidade à multiplicação
		if(!toReplay) {
			x = x*(xden/xdenDraw);
			y = y*(yden/ydenDraw);
		}
	
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
			Matrix m = new Matrix();
			
			mPath.transform(m);
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
		
		// guarda os dados necess‡rios para o replay
		if (toReplay) {
			xs.add(-1.0f);
			ys.add(-1.0f);
			colors.add(mPaint.getColor());
		}
	}

	public boolean onTouch(View arg0, MotionEvent event) {
		
		// entra se estiver em modo de desenho
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
	
	// limpa o canvas atual
	public void cleanCanvas() {
		clean = true;
		this.invalidate();
		
		// adiciona ponto fict’cio para informar que o canvas foi limpo num dado momento
		if (toReplay) {
			xs.add(-2.0f);
			ys.add(-2.0f);
			colors.add(mPaint.getColor());
		}
	}

	public void eraseMode() {
		
		// defini?›es para o efeito de apagar
		currentColor = Color.WHITE;
		strokeWidth = erasestrokewidth;

		// ƒ criado um novo paint com a cor a branco, que serve para apagar
		createPaint();
	}

	public void changeColor(int color) {
		
		// defini?›es para a cor atual
		currentColor = color;
		strokeWidth = defaultstrokewidth;

		// ƒ criado um novo paint com a nova cor seleccionada
		createPaint();
	}
	
	public static ArrayList<Float> stringToFloatList(final String input) {
	    String[] elements = input.substring(1, input.length() - 1).split(", ");
	    ArrayList<Float> result = new ArrayList<Float>(elements.length);
	    for (String item : elements) {
	        result.add(Float.valueOf(item));
	    }
	    return result;
	}
	
	public static ArrayList<Integer> stringToIntList(final String input) {
	    String[] elements = input.substring(1, input.length() - 1).split(", ");
	    ArrayList<Integer> result = new ArrayList<Integer>(elements.length);
	    for (String item : elements) {
	        result.add(Integer.valueOf(item));
	    }
	    return result;
	}

	
	// fun?‹o que mostra o replay de jogo
	public void replay() {
		
		
			toReplay = false;
			
			// inicia o desenho com o ponto inicial guardado
			touch_start(xs.get(0), ys.get(0));
			
			// define a cor desse ponto incial
			mPaint.setColor(colors.get(0));
			
			// thread que vai desenhar tudo, com um dado delay entre opera?›es
			new Thread(new Runnable() {
				public void run() {
					for (int i = 1; i < xs.size(); i++) {
						mPaint.setColor(colors.get(i));
						
						// verfica se est‡ em modo de apagar, para mudar o stroke
						if (colors.get(i) != Color.WHITE)
							mPaint.setStrokeWidth(defaultstrokewidth);
						else
							mPaint.setStrokeWidth(erasestrokewidth);
						
						// se o ponto atual Ž -1.0f, ent‹o acabou o path atual 
						if (xs.get(i) == -1.0f) {

							touch_up();
							if (i != xs.size() - 1) {
								if (colors.get(i + 1) != mPaint.getColor())
									createPaint();
								touch_start(xs.get(i + 1), ys.get(i + 1));
							}
						} else if (xs.get(i) == -2.0f && i != xs.size()-1) { // se for -2.0f, o utilizador limpou o ecr‹ todo
							touch_up();
							clean = true;
							DrawingPanel.this.postInvalidate();
							try {
								Thread.sleep(50);
							} catch (InterruptedException e) {
							}
							if (i != xs.size() - 1)
								touch_start(xs.get(i + 1), ys.get(i + 1));
						} else { // no caso genŽrico, desenha o movimento efetuado
							touch_move(xs.get(i), ys.get(i));
							try {
								Thread.sleep(50);
							} catch (InterruptedException e) {
							}
						}
						DrawingPanel.this.postInvalidate();
					}
					toReplay = true;
				}
			}).start();
			
	}

	

	public void save() {
		
		dialog = ProgressDialog.show(mContext, "", 
                "Sending Draw...", true);
    
    	new SendDraw().execute();
		
	}
	
	private class SendDraw extends AsyncTask<String, Integer, Long> {

		@Override
		protected Long doInBackground(String... string) {
			List<BasicNameValuePair> nameValuePairs = new ArrayList<BasicNameValuePair>();
		 	nameValuePairs.add(new BasicNameValuePair("id_creator", Configurations.id+""));           
	        nameValuePairs.add(new BasicNameValuePair("word_id", "1"));
	        nameValuePairs.add(new BasicNameValuePair("latitude", Configurations.latitude+""));
	        nameValuePairs.add(new BasicNameValuePair("longitude", Configurations.longitude+""));
	        nameValuePairs.add(new BasicNameValuePair("challenge", "false"));
	        nameValuePairs.add(new BasicNameValuePair("description", "Bla"));
	        nameValuePairs.add(new BasicNameValuePair("format", "json"));
	        nameValuePairs.add(new BasicNameValuePair("draw", colors.toString()));
	        nameValuePairs.add(new BasicNameValuePair("drawx", xs.toString()));
	        nameValuePairs.add(new BasicNameValuePair("drawy", ys.toString()));
	        nameValuePairs.add(new BasicNameValuePair("xdensity", xden+""));
	        nameValuePairs.add(new BasicNameValuePair("ydensity", yden+""));
	        
	        String response = Connection.postData("http://" + Configurations.AUTHORITY + Configurations.ADDCHALLENGE, nameValuePairs);
	        JSONObject info;
			try {
				info = new JSONObject(response);
				String status = info.getString("status"); 
				if(status.equals("Draw added.")) {
					//TODO SUCCESS
				} //TODO ELSE
			} catch (JSONException e) {}
			
			return null;
	        
		}
		
		protected void onPostExecute(Long result) {
			dialog.dismiss();
	     }
	}
	
}


