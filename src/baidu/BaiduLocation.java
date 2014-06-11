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
	boolean isFirstLoc = true;// 是否首次定位
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		_mapView = (MapView) findViewById(R.id.bdmapview);
		// 地图初始化
		_baiduMap = _mapView.getMap();
		// 开启定位图层
		_baiduMap.setMyLocationEnabled(true);
		// 定位初始化
		_LocationClient = new LocationClient(this);
		_LocationClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();//用来发起定位，添加取消监听
		option.setOpenGps(true);// 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(1000);//定位的时间间隔，单位：ms  
		_LocationClient.setLocOption(option);
		_LocationClient.start();
	}

	/**
	 * 监听sdk
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
					// 此处设置开发者获取到的方向信息，顺时针0-360
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
		// 退出时销毁定位
		_LocationClient.stop();
		// 关闭定位图层
		_baiduMap.setMyLocationEnabled(false);
		_mapView.onDestroy();
		_mapView=null;
		super.onDestroy();
	}
}
