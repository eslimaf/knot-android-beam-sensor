package br.org.cesar.knot.beamsensor.communication;


import org.json.JSONException;

import br.org.cesar.knot.beamsensor.model.BeamSensorFilter;
import br.org.cesar.knot.beamsensor.model.Subscriber;
import br.org.cesar.knot.beamsensor.model.SubscriberDataListener;
import br.org.cesar.knot.lib.exception.InvalidParametersException;
import br.org.cesar.knot.lib.exception.KnotException;
import br.org.cesar.knot.lib.exception.SocketNotConnected;
import br.org.cesar.knot.lib.model.KnotQueryData;

/**
 * Created by carlos on 09/03/17.
 */

public interface BeamCommunication {

    void getSensors(BeamSensorFilter filter) throws JSONException, InvalidParametersException, KnotException, SocketNotConnected;
    void getData(KnotQueryData filter,String uuid,String token) throws JSONException, InvalidParametersException,
            KnotException, SocketNotConnected;
    void open(String url, int port, String uuid, String token, Subscriber subscriber) throws Exception;
    void subscriberListener(SubscriberDataListener subscribeDataListener);
    boolean close();
}
