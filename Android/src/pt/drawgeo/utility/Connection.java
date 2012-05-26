package pt.drawgeo.utility;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.net.ParseException;
import android.net.Uri;

public class Connection {

	public static String getJSONLine(Uri uri) throws IOException,	ParseException {
		
		String response = null;
		HttpClient httpclient = null;
		try {
		    HttpGet httpget = new HttpGet(uri.toString());
		    httpclient = new DefaultHttpClient();
		    HttpResponse httpResponse = httpclient.execute(httpget);

		    final int statusCode = httpResponse.getStatusLine().getStatusCode();
		    if (statusCode != HttpStatus.SC_OK) {
		        throw new Exception("Got HTTP " + statusCode 
		            + " (" + httpResponse.getStatusLine().getReasonPhrase() + ')');
		    }

		    response = EntityUtils.toString(httpResponse.getEntity(), HTTP.UTF_8);

		} catch (Exception e) {
		    e.printStackTrace();

		} finally {
		    if (httpclient != null) {
		        httpclient.getConnectionManager().shutdown();
		        httpclient = null;
		    }
		}
		return response;
	}
	
}
