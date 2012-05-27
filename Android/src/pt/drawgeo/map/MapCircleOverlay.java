package pt.drawgeo.map;

import pt.drawgeo.utility.Configurations;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class MapCircleOverlay extends Overlay {

private GeoPoint point;
private Paint paint1, paint2;

public MapCircleOverlay(GeoPoint point) {
    this.point = point;

    paint1 = new Paint();
    paint1.setARGB(128, 0, 0, 255);
    paint1.setStrokeWidth(2);
    paint1.setStrokeCap(Paint.Cap.ROUND);
    paint1.setAntiAlias(true);
    paint1.setDither(false);
    paint1.setStyle(Paint.Style.STROKE);

    paint2 = new Paint();
    paint2.setARGB(64, 0, 0, 255);  

}

@Override
public void draw(Canvas canvas, MapView mapView, boolean shadow) {

    Point pt = mapView.getProjection().toPixels(point, null);
    float radius = mapView.getProjection().metersToEquatorPixels(Configurations.AVALIABLE_RADIUS);
    
    // para não desaparecer totalmente o círculo, pode-se limitr o raio
   /* if(radius < canvas.getHeight()/25){
        radius = canvas.getHeight()/25;
    }*/

    canvas.drawCircle(pt.x, pt.y, radius, paint2);
    canvas.drawCircle(pt.x, pt.y, radius, paint1);

}

}
