package com;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import src.com.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import baidu.BaiduLocation;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.model.LatLng;

/**
 * 
 */

/**
 * @author Lee-PC
 * 
 */
public class HomeActivity extends Activity {

	private static final String LTAG = HomeActivity.class.getSimpleName();
	private String m_home_url = "http://m.iteer.net/";
	private String m_home_url_near = "http://m.iteer.net/modules/xdirectory/env.php?";
	private LatLng _latLng;
	private WebView _webView;
	private SDKReceiver _receiver;
	private int _baiduViewRequest = 1;
	private Handler _handler = new Handler(){
		
		@Override
		public void handleMessage(Message msg) {
			
			super.handleMessage(msg);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_activity);

		setTitle("当前位置是：");

		// 注册 SDK 广播监听者
		IntentFilter iFilter = new IntentFilter();
		iFilter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR);
		iFilter.addAction(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR);
		_receiver = new SDKReceiver();
		registerReceiver(_receiver, iFilter);

		initWebView();

		LoadUrl(_webView, this.m_home_url);
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
			Intent intent = new Intent(HomeActivity.this, BaiduLocation.class);
			Bundle bundle = new Bundle();
			double[] data;
			if (_latLng == null || _latLng == null) {
				data = new double[] {};
			} else {
				data = new double[] { _latLng.latitude, _latLng.longitude };
			}

			bundle.putDoubleArray("location", data);
			intent.putExtras(bundle);
			startActivityForResult(intent, _baiduViewRequest);
			break;

		default:
			break;
		}
		return super.onMenuItemSelected(featureId, item);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == _baiduViewRequest) {
			double[] doubleArrayExtra = data.getDoubleArrayExtra("resultDatas");
			_latLng = new LatLng(doubleArrayExtra[0], doubleArrayExtra[1]);
			Geocoder geocoder = new Geocoder(getBaseContext(),
					Locale.getDefault());
			String addString = "当前位置是：";
			try {
				List<Address> addresslist = geocoder.getFromLocation(
						_latLng.latitude, _latLng.longitude, 5);
				if (!addresslist.isEmpty()) {
					Address address = addresslist.get(0);
					int lineIndex = address.getMaxAddressLineIndex();
					if (lineIndex >= 2) {
						addString += address.getAddressLine(1)
								+ address.getAddressLine(2);
					} else {
						addString += address.getAddressLine(1);
					}
				}
				setTitle(addString.toString());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				LoadLocationUrl();
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (this._webView != null) {
			if ((keyCode == KeyEvent.KEYCODE_BACK)
					&& (this._webView.canGoBack())) {
				this._webView.goBack();
				return true;
			}
			if (keyCode == KeyEvent.KEYCODE_BACK) {
				ConfirmExit();
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 退出确认
	 */
	private void ConfirmExit() {
		AlertDialog.Builder localBuilder = new AlertDialog.Builder(this);
		localBuilder.setTitle("退出");
		localBuilder.setMessage("是否退出IT人手册?");
		localBuilder.setPositiveButton("是",
				new DialogInterface.OnClickListener() {
					public void onClick(
							DialogInterface paramAnonymousDialogInterface,
							int paramAnonymousInt) {
						finish();
					}
				});
		localBuilder.setNegativeButton("否",
				new DialogInterface.OnClickListener() {
					public void onClick(
							DialogInterface paramAnonymousDialogInterface,
							int paramAnonymousInt) {
					}
				});
		localBuilder.show();
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

	private void LoadLocationUrl() {
		double d1 = _latLng.latitude;
		double d2 = _latLng.longitude;
		if (d1 == 0d || d2 == 0d) {
			return;
		}
		this.m_home_url = (this.m_home_url_near + "lat=" + String.valueOf(d1)
				+ "&lng=" + String.valueOf(d2));
		LoadUrl(_webView, this.m_home_url);
	}

	/**
	 * 加载地址
	 * 
	 * @param view
	 * @param paramString
	 */
	private void LoadUrl(WebView view, String paramString) {
		if (view != null && paramString != null) {
			view.loadUrl(paramString);
		}
	}

	/**
	 * 构造广播监听类，监听 SDK key 验证以及网络异常广播
	 */
	public class SDKReceiver extends BroadcastReceiver {
		public void onReceive(Context context, Intent intent) {
			String s = intent.getAction();
			Log.d(LTAG, "action: " + s);

			if (s.equals(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR)) {
				Log.i("key", "key error");
			} else if (s
					.equals(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR)) {
				Log.i("network", "no network works");
			}
		}
	}
}
