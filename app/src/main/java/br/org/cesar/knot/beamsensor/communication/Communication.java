package br.org.cesar.knot.beamsensor.communication;

import android.support.annotation.NonNull;

import br.org.cesar.knot.lib.connection.FacadeConnection;
import br.org.cesar.knot.lib.event.Event;
import br.org.cesar.knot.lib.exception.InvalidParametersException;
import br.org.cesar.knot.lib.exception.SocketNotConnected;

/**
 * Created by usuario on 03/04/17.
 */

public class Communication {

    private static final Object lock = new Object();
    private static Communication sInstance;
    private FacadeConnection mKnotConnection;
    private OpenConnectionListener mListener;


    private Event<Boolean> mConnectEvent = new Event<Boolean>() {
        @Override
        public void onEventFinish(Boolean object) {
            try {

                authenticate();

            } catch (InvalidParametersException e) {
//                e.printStackTrace();
            } catch (SocketNotConnected socketNotConnected) {
//                socketNotConnected.printStackTrace();
            }
        }

        @Override
        public void onEventError(Exception e) {
            if (mListener != null) {
                mListener.socketNotConnected();
            }
        }
    };

    private Event<Boolean> mAuthenticateEvent = new Event<Boolean>() {
        @Override
        public void onEventFinish(Boolean object) {
            if (mListener != null) {
                mListener.socketConnected();
            }
        }

        @Override
        public void onEventError(Exception e) {
            if (mListener != null) {
                mListener.socketNotConnected();
            }
        }
    };


    /**
     * Gets instance.
     *
     * @return the instance
     */
    public static Communication getInstance() {
        synchronized (lock) {
            if (sInstance == null) {
                sInstance = new Communication();
            }
            return sInstance;
        }
    }

    private Communication() {
        mKnotConnection = FacadeConnection.getInstance();
        mKnotConnection.createSocketIo();

    }

    public void configureSocketWithDeviceInformation(@NonNull String uuid, @NonNull String token) {
        mKnotConnection.setupSocketIO(uuid, token);
    }

    public void openConnection(@NonNull String endPoint, OpenConnectionListener listener) {
        mListener = listener;
        try {
            mKnotConnection.connectSocket(endPoint, mConnectEvent);
        } catch (SocketNotConnected socketNotConnected) {
            if (mListener != null) {
                mListener.socketNotConnected();
            }
        }
    }

    private void authenticate() throws InvalidParametersException, SocketNotConnected {
        mKnotConnection.socketIOAuthenticateDevice(mAuthenticateEvent);
    }


    public interface OpenConnectionListener {
        void socketConnected();

        void socketNotConnected();
    }

}
