package baidu;

import src.com.R;
import android.app.Activity;
import android.os.Bundle;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;

public class BaiduLocation extends Activity {

	private MapView _mapView;
	private BaiduMap _baiduMap;
	private LocationClient _LocationClient;
	public MyLocationListenner myListener = new MyLocationListenner();
	boolean isFirstLoc = true;// �Ƿ��״ζ�λ
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		_mapView = (MapView) findViewById(R.id.bdmapview);
		// ��ͼ��ʼ��
		_baiduMap = _mapView.getMap();
		// ������λͼ��
		_baiduMap.setMyLocationEnabled(true);
		// ��λ��ʼ��
		_LocationClient = new LocationClient(this);
		_LocationClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();//��������λ�����ȡ������
		option.setOpenGps(true);// ��gps
		option.setCoorType("bd09ll"); // ������������
		option.setScanSpan(1000);//��λ��ʱ��������λ��ms  
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
				LatLng ll = new LatLng(arg0.getLatitude(),
						arg0.getLongitude());
				MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
				_baiduMap.animateMapStatus(u);
				isFirstLoc=false;
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
		_mapView=null;
		super.onDestroy();
	}
}
