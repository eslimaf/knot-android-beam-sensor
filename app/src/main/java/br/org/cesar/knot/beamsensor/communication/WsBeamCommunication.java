
/**
 * Created by carlos on 14/03/17.
 */

package br.org.cesar.knot.beamsensor.communication;


import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import br.org.cesar.knot.beamsensor.model.BeamSensor;
import br.org.cesar.knot.beamsensor.model.BeamSensorFilter;
import br.org.cesar.knot.lib.connection.FacadeConnection;
import br.org.cesar.knot.lib.event.Event;
import br.org.cesar.knot.lib.model.KnotList;
import java.lang.Boolean;

public class WsBeamCommunication implements BeamCommunication {

    private static FacadeConnection connection;
    private static final String ENDPOINT_SCHEMA = "http";

    public WsBeamCommunication() {
        if (connection == null)
            connection = FacadeConnection.getInstance();

    }

    public void open(String url,int port,String user, String password,Event<Boolean> callback) throws Exception {
        // Configuring the API
        String endPoint = getEndpoint(url, port);
//        connection.setupSocketIO(endPoint, user, password);
//        connection.socketIOAuthenticateDevice(callback);
    }

    @Override
    public boolean close(){
        connection.disconnectSocket();
        return connection.isSocketConnected();
    }


    @Override
    public List<BeamSensor> getSensors(BeamSensorFilter filter) {
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

    private String getEndpoint(String url,int port) throws URISyntaxException {
        URI uri = new URI(ENDPOINT_SCHEMA,null,url,port,null,null,null);
        return uri.toASCIIString();
    }
}
