package pt.drawgeo.map;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class MapCircleOverlay extends Overlay {

private GeoPoint point;
private Paint paint1, paint2;
private float radius;

public MapCircleOverlay(GeoPoint point, float radius, int r, int g, int b, int a) {
    this.point = point;
    this.radius = radius;
    
    paint1 = new Paint();
    paint1.setARGB(128, r, g, b);
    paint1.setStrokeWidth(2);
    paint1.setStrokeCap(Paint.Cap.ROUND);
    paint1.setAntiAlias(true);
    paint1.setDither(false);
    paint1.setStyle(Paint.Style.STROKE);
    
    paint2 = new Paint();
    paint2.setARGB(a, r, g, b);  

}

@Override
public void draw(Canvas canvas, MapView mapView, boolean shadow) {

    Point pt = mapView.getProjection().toPixels(point, null);
    float radius = mapView.getProjection().metersToEquatorPixels(this.radius);
    
    // para não desaparecer totalmente o círculo, pode-se limitr o raio
   /* if(radius < canvas.getHeight()/25){
        radius = canvas.getHeight()/25;
    }*/

    canvas.drawCircle(pt.x, pt.y, radius, paint2);
    canvas.drawCircle(pt.x, pt.y, radius, paint1);

}

}
