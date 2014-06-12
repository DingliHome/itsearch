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
	boolean isFirstLoc = true;// �Ƿ��״ζ�λ
	private Marker _marker;
	private LatLng _lastLatLng;
	private boolean _isChangedLocation;
	BitmapDescriptor bd = BitmapDescriptorFactory
			.fromResource(R.drawable.icon_gcoding);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.baidulocation_activity);
		Button setButton = (Button) findViewById(R.id.setLocation);
		Button saveButton = (Button) findViewById(R.id.saveLocation);

		Bundle bundleExtra = getIntent().getExtras();

		double[] doubleArray = bundleExtra.getDoubleArray("location");
		_lastLatLng = new LatLng(doubleArray[0], doubleArray[1]);
		
		InitialMap();
		
		if (_lastLatLng.latitude != 0d) {
			MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(_lastLatLng);
			_baiduMap.animateMapStatus(u);
		}
		saveButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				_isChangedLocation = false;
				finish();
			}
		});
		_baiduMap.setOnMapClickListener(new OnMapClickListener() {

			@Override
			public boolean onMapPoiClick(MapPoi arg0) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public void onMapClick(LatLng arg0) {
				_lastLatLng = new LatLng(arg0.latitude, arg0.longitude);
				_marker.setPosition(_lastLatLng);
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
	 * ��ʼ�������
	 */
	private void InitialMarkerOverlay(double lat, double lng) {

		LatLng llA = new LatLng(lat, lng);
		OverlayOptions ooA = new MarkerOptions().position(llA).icon(bd)
				.zIndex(9);

		_marker = (Marker) (_baiduMap.addOverlay(ooA));
	}

	/**
	 * ��ʼ����ͼ
	 */
	private void InitialMap() {
		_mapView = (MapView) findViewById(R.id.bdmapview);
		// ��ͼ��ʼ��
		_baiduMap = _mapView.getMap();
		// ������λͼ��
		_baiduMap.setMyLocationEnabled(true);
		_baiduMap.setMyLocationConfigeration(new MyLocationConfigeration(
				LocationMode.NORMAL, true, null));
		// ��λ��ʼ��
		_LocationClient = new LocationClient(getApplicationContext());
		_LocationClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();// ��������λ�����ȡ������
		option.setOpenGps(true);// ��gps
		option.setCoorType("bd09ll"); // ������������
		option.setScanSpan(1000);// ��λ��ʱ��������λ��ms
		_LocationClient.setLocOption(option);
		_LocationClient.start();
	}

	/**
	 * ����sdk
	 */
	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation arg0) {
			// TODO Auto-generated method stub
			if (arg0 == null || _mapView == null) {
				return;
			}
			MyLocationData locationData = new MyLocationData.Builder()
					.accuracy(arg0.getRadius())
					// �˴����ÿ����߻�ȡ���ķ�����Ϣ��˳ʱ��0-360
					.direction(100).latitude(arg0.getLatitude())
					.longitude(arg0.getLongitude()).build();
			_baiduMap.setMyLocationData(locationData);
			if (isFirstLoc) {
				LatLng ll = new LatLng(arg0.getLatitude(), arg0.getLongitude());
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
		// �˳�ʱ���ٶ�λ
		_LocationClient.stop();
		// �رն�λͼ��
		_baiduMap.setMyLocationEnabled(false);

		_mapView.onDestroy();
		_mapView = null;
		super.onDestroy();
	}
}
