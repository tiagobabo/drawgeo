package pt.drawgeo.map;

import java.util.ArrayList;

import pt.drawgeo.canvas.ReplayCanvasActivity;
import pt.drawgeo.utility.Configurations;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;
import com.main.R;

@SuppressWarnings("rawtypes")
public class MapChallenge extends ItemizedOverlay {

	private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
	private ArrayList<String> ids = new ArrayList<String>();
	private Context mContext;
	
	public MapChallenge(Drawable defaultMarker, Context context) {
		super(boundCenterBottom(defaultMarker));
		this.mContext = context;
		// TODO Auto-generated constructor stub
	}

	@Override
	protected OverlayItem createItem(int i) {
		return mOverlays.get(i);
	}

	@Override
	public int size() {
		return mOverlays.size();
	}
	
	public void addOverlay(OverlayItem overlay) {
	    mOverlays.add(overlay);
	    populate();
	}

	@Override
	protected boolean onTap(final int index) {
	  OverlayItem item = mOverlays.get(index);
	  final Dialog dialog = new Dialog(mContext);
	  dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	  
	  dialog.setContentView(R.layout.challengedialog);

	  
	  TextView title = (TextView) dialog.findViewById(R.id.title);
	  title.setText(item.getTitle());
	  
	  final ImageView pButton = (ImageView) dialog.findViewById(R.id.playnow);
		pButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(),
						ReplayCanvasActivity.class);
				
				Configurations.drawidreplay = ids.get(index);
				dialog.dismiss();
				mContext.startActivity(intent);
			}
		});
	  
	  dialog.show();
	  return true;
	}

	public void addItem(String string) {
		ids.add(string);
	}
	
}
