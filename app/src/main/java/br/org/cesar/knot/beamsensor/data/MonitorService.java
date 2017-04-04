package br.org.cesar.knot.beamsensor.data;


import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MonitorService extends Service {
    private final String TAG = MonitorService.class.getSimpleName();
    private MonitorBinder mMonitorBinder = new MonitorBinder();
    @Nullable
    private Disposable mAutoEventDisposable;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mMonitorBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_STICKY;
    }

    public void startMonitoring() {
        Log.d(TAG, "START: Monitoring Beam events");
        mAutoEventDisposable = Observable.interval(10, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        RxBus.getInstance().send(new Object());
                    }
                });
    }

    public void stopMonitoring() {
        Log.d(TAG, "STOP: Monitoring Beam events");
        if (mAutoEventDisposable != null) {
            mAutoEventDisposable.dispose();
        }
    }

    public class MonitorBinder extends Binder {
        public MonitorService getMonitorServiceInstance() {
            return MonitorService.this;
        }
    }
}
