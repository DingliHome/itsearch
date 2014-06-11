package com;

import src.com.R;
import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

/**
 * 
 */

/**
 * @author Lee-PC
 * 
 */
public class HomeActivity extends Activity {

	private String m_home_url = "http://m.iteer.net/modules/xdirectory/index.php";
	private String m_home_url_near = "http://m.iteer.net/modules/xdirectory/env.php?";
	private LocationManager locationManager;
	private String key="M8f4Re3iiSQ696XQCapAyweh";
	
	private WebView _webView;

	public HomeActivity() {

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_activity);

		LocationListener listener = new LocationListener() {

			@Override
			public void onStatusChanged(String provider, int status,
					Bundle extras) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProviderEnabled(String provider) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProviderDisabled(String provider) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onLocationChanged(Location location) {
				// TODO Auto-generated method stub
				if (location != null) {
					Log.i("location", "Lat:" + location.getLatitude() + " Lng:"
							+ location.getLongitude());
				}
			}
		};
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				30*1000, 0, listener);

		Button button = (Button) findViewById(R.id.locationbtn);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Location location = locationManager
						.getLastKnownLocation(LocationManager.GPS_PROVIDER);

				double lat = location.getLatitude();
				double lng = location.getLongitude();
			}
		});
	}

	public void initWebView() {
		_webView = (WebView) findViewById(R.id.webView);
		_webView.getSettings().setJavaScriptEnabled(true);
		_webView.getSettings().setDomStorageEnabled(true);
		_webView.getSettings().setCacheMode(-1);
		_webView.getSettings().setAppCacheMaxSize(83889608L);
		_webView.getSettings().setAllowFileAccess(true);
		_webView.getSettings().setAppCacheEnabled(true);
		_webView.setScrollBarStyle(0);
		_webView.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageFinished(WebView view, String url) {
				// TODO Auto-generated method stub
				super.onPageFinished(view, url);
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				// TODO Auto-generated method stub
				super.onPageStarted(view, url, favicon);
			}

			@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				// TODO Auto-generated method stub
				super.onReceivedError(view, errorCode, description, failingUrl);
			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// TODO Auto-generated method stub
				return super.shouldOverrideUrlLoading(view, url);
			}
		});
	}

}
