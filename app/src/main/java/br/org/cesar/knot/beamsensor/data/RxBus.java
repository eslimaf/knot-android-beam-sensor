package br.org.cesar.knot.beamsensor.data;


import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class RxBus {
    private static RxBus sInstance;
    private PublishSubject<Object> mBus = PublishSubject.create();

    private RxBus() {
        //Avoid new instance
    }

    public static synchronized RxBus getInstance() {
        if (sInstance == null) {
            sInstance = new RxBus();
        }
        return sInstance;
    }

    public void send(Object o) {
        mBus.onNext(o);
    }

    public Observable<Object> toObservable() {
        return mBus;
    }

    public boolean hasObservers() {
        return mBus.hasObservers();
    }

}
