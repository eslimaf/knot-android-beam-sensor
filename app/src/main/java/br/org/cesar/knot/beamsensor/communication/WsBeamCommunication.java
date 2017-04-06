
/**
 * Created by carlos on 14/03/17.
 */

package br.org.cesar.knot.beamsensor.communication;


import org.json.JSONException;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;

import br.org.cesar.knot.beamsensor.controller.BeamController;
import br.org.cesar.knot.beamsensor.model.BeamSensor;
import br.org.cesar.knot.beamsensor.model.BeamSensorData;
import br.org.cesar.knot.beamsensor.model.BeamSensorFilter;
import br.org.cesar.knot.beamsensor.model.Subscriber;
import br.org.cesar.knot.beamsensor.model.SubscriberDataListener;
import br.org.cesar.knot.lib.connection.FacadeConnection;
import br.org.cesar.knot.lib.model.AbstractThingData;
import br.org.cesar.knot.lib.event.Event;
import br.org.cesar.knot.lib.exception.InvalidParametersException;
import br.org.cesar.knot.lib.exception.KnotException;
import br.org.cesar.knot.lib.exception.SocketNotConnected;
import br.org.cesar.knot.lib.model.KnotList;
import br.org.cesar.knot.lib.model.KnotQueryData;

import java.lang.Boolean;

public class WsBeamCommunication implements BeamCommunication, Communication.OpenConnectionListener {

    private static FacadeConnection connection;
    private static final String ENDPOINT_SCHEMA = "http";
    private HashMap<Integer,SubscriberDataListener> subscribers = new HashMap<>();
    private Subscriber subscriber;

    public WsBeamCommunication() {
        if (connection == null)
            connection = FacadeConnection.getInstance();

        connection.createSocketIo();

    }

    public void subscriberListener(SubscriberDataListener subscribeDataListener){
        getSubscribers().put(new Integer(subscribeDataListener.getId()),subscribeDataListener);
    }

    public void open(String url,int port,String user, String password,Subscriber subscriber) throws Exception {
        setSubscriber(subscriber);
        String endPoint = getEndpoint(url,port);
        connection.setupSocketIO(user, password);
        connection.connectSocket(endPoint, new Event<Boolean>() {
            @Override
            public void onEventFinish(Boolean object) {
                try {
                    authenticate();
                } catch (InvalidParametersException e) {
                    e.printStackTrace();
                } catch (SocketNotConnected socketNotConnected) {
                    socketNotConnected.printStackTrace();
                }
            }

            @Override
            public void onEventError(Exception e) {

            }
        });

    }

    @Override
    public boolean close(){
        connection.disconnectSocket();
        return connection.isSocketConnected();
    }

  /*  @Override
    public void getSensors(BeamSensorFilter filter) {
        final List<BeamSensor> result = new ArrayList<>();
        try{
            connection.socketIOGetDeviceList(new KnotList<>(BeamSensor.class), filter.getQuery(), new Event<List<BeamSensor>>() {
                @Override
                public void onEventFinish(List<BeamSensor> beamSensors) {
                    for (BeamSensor b :
                            beamSensors) {
                        result.add(b);
                    }
                }

                @Override
                public void onEventError(Exception e) {
                    e.printStackTrace();
                }
            });
        }
        catch (Exception ex){
            ex.printStackTrace();
        }

        return result;
    }

    */

    public void getSensors(BeamSensorFilter filter) throws JSONException, InvalidParametersException,
            KnotException, SocketNotConnected {
        KnotList<BeamSensor> list = new KnotList<>(BeamSensor.class);
        connection.socketIOGetDeviceList(list, filter.getQuery(), new Event<List<BeamSensor>>() {
            @Override
            public void onEventFinish(List<BeamSensor> object) {
                for (SubscriberDataListener listener :
                        getSubscribers().values()) {
                    listener.setDeviceList(object);
                }
            }
            @Override
            public void onEventError(Exception e) {
                for (SubscriberDataListener listener :
                        getSubscribers().values()) {
                    listener.setError(e);
                }
            }
        });
    }

    public void getData(KnotQueryData filter,String uuid,String token) throws JSONException, InvalidParametersException,
            KnotException, SocketNotConnected {
        KnotList<BeamSensorData> list = new KnotList<>(BeamSensorData.class);
        connection.socketIOGetData(list, uuid, token, filter, new Event<List<BeamSensorData>>() {
            @Override
            public void onEventFinish(List<BeamSensorData> object) {
                for (SubscriberDataListener listener :
                        getSubscribers().values()) {
                    listener.setData(object);
                }
            }

            @Override
            public void onEventError(Exception e) {
                for (SubscriberDataListener listener :
                        getSubscribers().values()) {
                    listener.setError(e);
                }
            }
        });
    }

    private String getEndpoint(String url,int port) throws URISyntaxException {
        URI uri = new URI(ENDPOINT_SCHEMA,null,url,port,null,null,null);
        return uri.toASCIIString();
    }

    private void authenticate() throws InvalidParametersException, SocketNotConnected {
        final WsBeamCommunication that = this;
        connection.socketIOAuthenticateDevice(new Event<Boolean>() {

            @Override
            public void onEventFinish(Boolean object) {
                if(object.booleanValue())
                {
                    that.socketConnected();
                }
            }

            @Override
            public void onEventError(Exception e) {

                that.socketNotConnected();
            }
        });
    }


    @Override
    public void socketConnected() {
        getSubscriber().ready();
    }

    @Override
    public void socketNotConnected() {

    }


    public Subscriber getSubscriber() {
        return subscriber;
    }

    public void setSubscriber(Subscriber subscriber) {
        this.subscriber = subscriber;
    }

    public HashMap<Integer, SubscriberDataListener> getSubscribers() {
        return subscribers;
    }

    public void setSubscribers(HashMap<Integer, SubscriberDataListener> subscribers) {
        this.subscribers = subscribers;
    }
}
