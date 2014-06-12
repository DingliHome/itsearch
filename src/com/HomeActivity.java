package com;

import src.com.R;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import baidu.BaiduLocation;

import com.baidu.mapapi.SDKInitializer;

/**
 * 
 */

/**
 * @author Lee-PC
 * 
 */
public class HomeActivity extends Activity {

	private static final String LTAG = HomeActivity.class.getSimpleName();
	private String m_home_url = "http://m.iteer.net/modules/xdirectory/index.php";
	private String m_home_url_near = "http://m.iteer.net/modules/xdirectory/env.php?";
	private LocationManager locationManager;
	private String key = "M8f4Re3iiSQ696XQCapAyweh";

	private WebView _webView;
	private SDKReceiver _receiver;

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
				30 * 1000, 0, listener);

		Button button = (Button) findViewById(R.id.locationbtn);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Location location = locationManager
						.getLastKnownLocation(LocationManager.GPS_PROVIDER);
				if (location != null) {
					double lat = location.getLatitude();
					double lng = location.getLongitude();
				}
			}
		});

		// 注册 SDK 广播监听者
		IntentFilter iFilter = new IntentFilter();
		iFilter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR);
		iFilter.addAction(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR);
		_receiver = new SDKReceiver();
		registerReceiver(_receiver, iFilter);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case R.id.locationMap:
			Intent intent = new Intent(HomeActivity.this,BaiduLocation.class);
			startActivity(intent);
			break;

		default:
			break;
		}
		return super.onMenuItemSelected(featureId, item);
	}
	
	@Override
	protected void onDestroy() {
		unregisterReceiver(_receiver);
		super.onDestroy();
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

	/**
	 * 构造广播监听类，监听 SDK key 验证以及网络异常广播
	 */
	public class SDKReceiver extends BroadcastReceiver {
		public void onReceive(Context context, Intent intent) {
			String s = intent.getAction();
			Log.d(LTAG, "action: " + s);

			if (s.equals(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR)) {
				Log.i("", "");
			} else if (s
					.equals(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR)) {
				Log.i("", "");
			}
		}
	}
}
