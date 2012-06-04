package pt.drawgeo.utility;

import java.io.IOException;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
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
	
	public static String postData(String site, List<BasicNameValuePair> nameValuePairs) {
	    // Create a new HttpClient and Post Header
	    HttpClient httpclient = new DefaultHttpClient();
	    HttpPost httppost = new HttpPost(site);

	    try {
	        // Add your data
	        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,"UTF-8"));

	        // Execute HTTP Post Request
	        HttpResponse response = httpclient.execute(httppost);
	        
	        return EntityUtils.toString(response.getEntity(), HTTP.UTF_8);

	    } catch (ClientProtocolException e) {
	        // TODO Auto-generated catch block
	    } catch (IOException e) {
	        System.out.println(e.getMessage());
	    }
	    return null;
	} 
	
}
