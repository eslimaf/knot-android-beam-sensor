
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
import br.org.cesar.knot.lib.exception.SocketNotConnected;
import br.org.cesar.knot.lib.model.KnotList;


public class WsBeamCommunication implements BeamCommunication {

    private static FacadeConnection connection;
    private static final String ENDPOINT_SCHEMA = "http";
    private static final String ENDPOINT = "";
    private static final String UUID_OWNER = "";
    private static final String TOKEN_OWNER = "";

    public WsBeamCommunication() {
        if (connection == null)
            connection = FacadeConnection.getInstance();

    }

    public boolean open(String url,int port,String user, String password) throws Exception {
        // Configuring the API
        try {
            String endPoint = getEndpoint(url,port);
            connection.setupSocketIO(endPoint, user, password);
            connection.socketIOAuthenticateDevice(new Event<Boolean>() {
                @Override
                public void onEventFinish(Boolean object) {
                }

                @Override
                public void onEventError(Exception e) {
                }
            });
        } catch (SocketNotConnected socketNotConnected) {
            socketNotConnected.printStackTrace();
        } catch (URISyntaxException e) {
            throw new Exception("Bad formed URI",e);
        }

        return connection.isSocketConnected();
    }

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
