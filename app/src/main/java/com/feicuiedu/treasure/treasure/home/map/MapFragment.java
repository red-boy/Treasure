package com.feicuiedu.treasure.treasure.home.map;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.feicuiedu.treasure.R;
import com.feicuiedu.treasure.components.TreasureView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 放置在HomeActivity里面，主要展示的就是地图，宝藏展示、宝藏详情、埋藏宝藏都是在这个里面来进行
 */
public class MapFragment extends Fragment {

    @BindView(R.id.map_frame)
    FrameLayout mapFrame;
    @BindView(R.id.centerLayout)
    RelativeLayout centerLayout;
    @BindView(R.id.treasureView)
    TreasureView treasureView;
    @BindView(R.id.layout_bottom)
    FrameLayout layoutBottom;
    @BindView(R.id.hide_treasure)
    RelativeLayout hideTreasure;
    @BindView(R.id.btn_HideHere)
    Button btnHideHere;
    @BindView(R.id.tv_currentLocation)
    TextView tvCurrentLocation;
    @BindView(R.id.iv_located)
    ImageView ivLocated;
    @BindView(R.id.et_treasureTitle)
    EditText etTreasureTitle;

    private MapView mapView;
    private BaiduMap baiduMap;
    private Unbinder bind;
    private LocationClient locationClient;
    private LatLng myLocation;

    private boolean isFirstLocate = true;// 这个主要是用来判断是不是第一进来的时候的定位

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        bind = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 初始化百度地图
        initBaiduMap();

        // 初始化定位相关
        initLocation();
    }

    private void initLocation() {
        /**
         * 1. 开启定位图层
         * 2. 定位类的实例化
         * 3. 定位进行一些相关的设置
         * 4. 设置定位的监听
         * 5. 开始定位（为了处理某些机型初始化定位不成功，需要重新请求定位）
         */
        baiduMap.setMyLocationEnabled(true);
        locationClient = new LocationClient(getContext());
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);// 打开GPS
        option.setCoorType("bd09ll");// 设置百度坐标类型
        option.setIsNeedAddress(true);// 设置需要地址信息
        locationClient.setLocOption(option);// 要把我们做的设置给LocationClient
        locationClient.registerLocationListener(locationListener);// 设置百度地图的监听
        locationClient.start();// 开始定位
        locationClient.requestLocation();// 为了处理某些机型初始化定位不成功，需要重新请求定位
    }

    private BDLocationListener locationListener = new BDLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            // 处理一下定位
            /**
             * 1. 判断有没有定位成功
             * 2. 获得定位信息(经纬度)
             * 3. 定位信息设置到地图上
             * 4. 移动到定位的位置去
             *      第一次进入：一进到项目里面就会移动到定位的位置去
             *      点击定位按钮：其他时候如果需要定位
             */
            if (bdLocation == null) {
                locationClient.requestLocation();
                return;
            }
            double lng = bdLocation.getLongitude();// 经度
            double lat = bdLocation.getLatitude();//纬度

            // 拿到定位的位置
            myLocation = new LatLng(lat, lng);

            MyLocationData myLocationData = new MyLocationData.Builder()
                    .longitude(lng)
                    .latitude(lat)
                    .accuracy(100f)// 精度，定位圈的大小
                    .build();

            baiduMap.setMyLocationData(myLocationData);
            if (isFirstLocate) {
                moveToMyLocation();
                isFirstLocate = false;
            }
        }
    };

    private void initBaiduMap() {

        // 查看百度地图的ＡＰＩ

        // 百度地图状态
        MapStatus mapStatus = new MapStatus.Builder()
                .overlook(0)// 0--(-45) 地图的俯仰角度
                .zoom(15)// 3--21 缩放级别
                .build();

        BaiduMapOptions options = new BaiduMapOptions()
                .mapStatus(mapStatus)// 设置地图的状态
                .compassEnabled(true)// 指南针
                .zoomGesturesEnabled(true)// 设置允许缩放手势
                .rotateGesturesEnabled(true)// 旋转
                .scaleControlEnabled(false)// 不显示比例尺控件
                .zoomControlsEnabled(false);// 不显示缩放控件

        // 创建一个MapView
        mapView = new MapView(getContext(), options);

        // 在当前的Layout上面添加MapView
        mapFrame.addView(mapView, 0);

        // MapView 的控制器
        baiduMap = mapView.getMap();

        // 怎么对地图状态进行监听？
        baiduMap.setOnMapStatusChangeListener(mapStatusChangeListener);

    }

    // 地图类型的切换（普通视图--卫星视图）
    @OnClick(R.id.tv_satellite)
    public void switchMapType() {
        // 先获得当前的类型
        int type = baiduMap.getMapType();
        type = type == BaiduMap.MAP_TYPE_NORMAL ? BaiduMap.MAP_TYPE_SATELLITE : BaiduMap.MAP_TYPE_NORMAL;
        baiduMap.setMapType(type);
    }

    // 定位实现
    @OnClick(R.id.tv_located)
    public void moveToMyLocation() {

        // 将地图位置设置成定位的位置
        MapStatus mapStatus = new MapStatus.Builder()
                .target(myLocation)
                .rotate(0)// 地图位置摆正
                .zoom(19)
                .build();
        // 更新地图状态
        MapStatusUpdate update = MapStatusUpdateFactory.newMapStatus(mapStatus);
        baiduMap.animateMapStatus(update);
    }

    @OnClick(R.id.tv_compass)
    public void switchCompass() {
        /**
         * 指南针是地图视图的一个图标
         */
        boolean isCompass = baiduMap.getUiSettings().isCompassEnabled();
        baiduMap.getUiSettings().setCompassEnabled(!isCompass);
    }

    @OnClick({R.id.iv_scaleUp, R.id.iv_scaleDown})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_scaleUp:
                baiduMap.setMapStatus(MapStatusUpdateFactory.zoomIn());// 放大
                break;
            case R.id.iv_scaleDown:
                baiduMap.setMapStatus(MapStatusUpdateFactory.zoomOut());// 缩小
                break;
        }
    }

    // 百度地图状态的监听
    private BaiduMap.OnMapStatusChangeListener mapStatusChangeListener = new BaiduMap.OnMapStatusChangeListener() {
        @Override
        public void onMapStatusChangeStart(MapStatus mapStatus) {
        }

        @Override
        public void onMapStatusChange(MapStatus mapStatus) {

        }

        @Override
        public void onMapStatusChangeFinish(MapStatus mapStatus) {

        }
    };


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bind.unbind();
    }
}
