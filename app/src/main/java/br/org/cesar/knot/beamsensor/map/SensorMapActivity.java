package br.org.cesar.knot.beamsensor.map;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

import br.org.cesar.knot.beamsensor.R;
import br.org.cesar.knot.beamsensor.data.MonitorService;
import br.org.cesar.knot.beamsensor.data.RxBus;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class SensorMapActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static final String TAG = SensorMapActivity.class.getSimpleName();
    @Nullable
    private GoogleMap mMap;
    @NonNull
    private List<Polyline> mPolylineList = new ArrayList<>();
    @Nullable
    private MonitorService mMonitorService;
    private boolean mIsMonitorServiceBound = false;

    private CompositeDisposable mDisposable = new CompositeDisposable();

    private ServiceConnection mMonitorConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.d(TAG, "MonitorService Up");
            mMonitorService = ((MonitorService.MonitorBinder) iBinder).getMonitorServiceInstance();
            mMonitorService.startMonitoring();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.d(TAG, "MonitorService Down");
            mMonitorService = null;
        }
    };

    private Consumer<Object> mObjectConsumer = new Consumer<Object>() {
        @Override
        public void accept(Object o) throws Exception {
            Log.d(TAG, "new event happened");
        }
    };

    private void doBindService() {
        mIsMonitorServiceBound = bindService(new Intent(this, MonitorService.class)
                , mMonitorConnection, BIND_AUTO_CREATE);
    }

    private void doUnbindService() {
        if (mIsMonitorServiceBound) {
            unbindService(mMonitorConnection);
            mIsMonitorServiceBound = false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_map);
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mDisposable.add(RxBus.getInstance().toObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mObjectConsumer));
        mapFragment.getMapAsync(this);
        doBindService();
        //TODO: Remove auto event
        RxBus.getInstance().send(new Object());
    }

    @Override
    protected void onDestroy() {
        doUnbindService();
        mDisposable.clear();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mMonitorService != null) {
            mMonitorService.startMonitoring();
        }
    }

    @Override
    protected void onPause() {
        if (mMonitorService != null) {
            mMonitorService.stopMonitoring();
        }
        super.onPause();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    private void addBeamMarker(LatLng position) {
        if (mMap != null) {
            mMap.addMarker(new MarkerOptions()
                    .position(position));
        }
    }

    private void drawLines(LatLng startPos, LatLng endPos) {
        PolylineOptions options = new PolylineOptions();
        options.add(startPos);
        options.add(endPos);
        if (mMap != null) {
            mPolylineList.add(mMap.addPolyline(options));
        }
    }

    private void changeLineColor(int color, int lineIndex) {
        Polyline polyline = mPolylineList.get(0);
        polyline.setColor(Color.BLUE);
    }
}
