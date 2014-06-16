package baidu;

import src.com.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfigeration;
import com.baidu.mapapi.map.MyLocationConfigeration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;

public class BaiduLocation extends Activity {

	private MapView _mapView;
	private BaiduMap _baiduMap;
	private LocationClient _LocationClient;
	public MyLocationListenner myListener = new MyLocationListenner();
	boolean isFirstLoc = true;// 是否首次定位
	private Marker _marker;
	private LatLng _lastLatLng;
	private boolean _isChangedLocation;
	private String _locationAddr;
	BitmapDescriptor bd = BitmapDescriptorFactory
			.fromResource(R.drawable.icon_gcoding);

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.baidulocation_activity);
		Button setButton = (Button) findViewById(R.id.setLocation);
		Button saveButton = (Button) findViewById(R.id.saveLocation);

		Bundle bundleExtra = getIntent().getExtras();

		double[] doubleArray = bundleExtra.getDoubleArray("location");
		if (doubleArray.length > 1) {
			_lastLatLng = new LatLng(doubleArray[0], doubleArray[1]);
		}

		InitialMap();

		saveButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				_isChangedLocation = false;
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putDoubleArray("resultDatas", new double[] {
						_lastLatLng.latitude, _lastLatLng.longitude });
				bundle.putString("location", _locationAddr);
				intent.putExtras(bundle);
				setResult(1, intent);
				
				finish();
			}
		});

		_baiduMap.setOnMapClickListener(new OnMapClickListener() {

			@Override
			public boolean onMapPoiClick(MapPoi arg0) {

				return false;
			}

			@Override
			public void onMapClick(LatLng arg0) {
				if (_isChangedLocation) {
					_lastLatLng = new LatLng(arg0.latitude, arg0.longitude);
					_marker.setPosition(_lastLatLng);
					
				}
			}
		});

		setButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				_isChangedLocation = true;
			}
		});

	}

	/**
	 * 初始化坐标点
	 */
	private void InitialMarkerOverlay(double lat, double lng) {

		LatLng llA = new LatLng(lat, lng);
		OverlayOptions ooA = new MarkerOptions().position(llA).icon(bd)
				.zIndex(9);
		
		_marker = (Marker) (_baiduMap.addOverlay(ooA));
	}

	/**
	 * 初始化地图
	 */
	private void InitialMap() {
		_mapView = (MapView) findViewById(R.id.bdmapview);
		// 地图初始化
		_baiduMap = _mapView.getMap();
		// 开启定位图层
		_baiduMap.setMyLocationEnabled(true);
		_baiduMap.setMyLocationConfigeration(new MyLocationConfigeration(
				LocationMode.NORMAL, true, null));
		// 定位初始化
		_LocationClient = new LocationClient(getApplicationContext());
		_LocationClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();// 用来发起定位，添加取消监听
		option.setLocationMode(com.baidu.location.LocationClientOption.LocationMode.Hight_Accuracy);// 设置定位模式
		option.setIsNeedAddress(true);// 返回的定位结果包含地址信息
		option.setNeedDeviceDirect(true);// 返回的定位结果包含手机机头的方向
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(60000);// 定位的时间间隔，单位：ms
		_LocationClient.setLocOption(option);
		_LocationClient.start();
		
	}

	/**
	 * 监听sdk
	 */
	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			
			if (location == null || _mapView == null) {
				return;
			}
			_locationAddr = location.getAddrStr();

			MyLocationData locationData = new MyLocationData.Builder()
					.accuracy(location.getRadius())
					// 此处设置开发者获取到的方向信息，顺时针0-360
					.direction(100).latitude(location.getLatitude())
					.longitude(location.getLongitude()).build();
			_baiduMap.setMyLocationData(locationData);

			if (isFirstLoc) {
				LatLng ll = null;
				if (_lastLatLng != null && _lastLatLng.latitude != 0d) {
					ll = new LatLng(_lastLatLng.latitude, _lastLatLng.longitude);
				} else {
					ll = new LatLng(location.getLatitude(),
							location.getLongitude());
				}

				InitialMarkerOverlay(ll.latitude, ll.longitude);
				MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
				_baiduMap.animateMapStatus(u);
				isFirstLoc = false;
			}

		}

		@Override
		public void onReceivePoi(BDLocation arg0) {
			// TODO Auto-generated method stub

		}

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		_mapView.onResume();
		super.onResume();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		_mapView.onPause();
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		// 退出时销毁定位
		_LocationClient.stop();
		// 关闭定位图层
		_baiduMap.setMyLocationEnabled(false);

		_mapView.onDestroy();
		_mapView = null;
		super.onDestroy();
	}
}
