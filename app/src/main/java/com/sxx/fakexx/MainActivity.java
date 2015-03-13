package com.sxx.fakexx;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMapDoubleClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMapLongClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMapStatusChangeListener;
import com.baidu.mapapi.map.BaiduMap.OnMapTouchListener;
import com.baidu.mapapi.map.BaiduMap.SnapshotReadyCallback;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 演示地图缩放，旋转，视角控制
 */
public class MainActivity extends Activity {

    /**
     * MapView 是地图主控件
     */
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private SettingsHelper mSettings;

    /**
     * 当前地点击点
     */
    private LatLng currentPt;
    /**
     * 控制按钮
     */
    private Button saveLocationButton;

    /**
     * 用于显示地图状态的面板
     */
    private TextView mStateBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapcontrol);

        mSettings = new SettingsHelper(this);

        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        mStateBar = (TextView) findViewById(R.id.state);
        initListener();
    }

    private void initListener() {
        mBaiduMap.setOnMapTouchListener(new OnMapTouchListener() {

            @Override
            public void onTouch(MotionEvent event) {

            }
        });

        mBaiduMap.setOnMapClickListener(new OnMapClickListener() {
            public void onMapClick(LatLng point) {
                currentPt = point;
                updateMapState();
            }

            public boolean onMapPoiClick(MapPoi poi) {
                return false;
            }
        });
        mBaiduMap.setOnMapLongClickListener(new OnMapLongClickListener() {
            public void onMapLongClick(LatLng point) {
                currentPt = point;
                updateMapState();
            }
        });
        mBaiduMap.setOnMapDoubleClickListener(new OnMapDoubleClickListener() {
            public void onMapDoubleClick(LatLng point) {
                currentPt = point;
                updateMapState();
            }
        });
        mBaiduMap.setOnMapStatusChangeListener(new OnMapStatusChangeListener() {
            public void onMapStatusChangeStart(MapStatus status) {
                updateMapState();
            }

            public void onMapStatusChangeFinish(MapStatus status) {
                updateMapState();
            }

            public void onMapStatusChange(MapStatus status) {
                updateMapState();
            }
        });
        saveLocationButton = (Button) findViewById(R.id.save_location);

        OnClickListener onClickListener = new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.equals(saveLocationButton) && currentPt != null) {
                    mSettings.setString("latitude",  "" + currentPt.latitude);
                    mSettings.setString("longitude", "" + currentPt.longitude);
                    Toast.makeText(getApplicationContext(), getString(R.string.save_location) + ": (" + currentPt.longitude + ", " + currentPt.latitude + ")", Toast.LENGTH_SHORT).show();
                }
            }

        };
        saveLocationButton.setOnClickListener(onClickListener);
    }

    /**
     * 更新地图状态显示面板
     */
    private void updateMapState() {
        if (mStateBar == null) {
            return;
        }
        String state = "";
        if (currentPt == null) {
            state = getString(R.string.tip);
        } else {
            state = String.format(getString(R.string.current_location) + "(%f, %f)",
                    currentPt.longitude, currentPt.latitude);
        }
        mStateBar.setText(state);
    }

    @Override
    protected void onPause() {
        // MapView的生命周期与Activity同步，当activity挂起时需调用MapView.onPause()
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        // MapView的生命周期与Activity同步，当activity恢复时需调用MapView.onResume()
        mMapView.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        // MapView的生命周期与Activity同步，当activity销毁时需调用MapView.destroy()
        mMapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
