package br.org.cesar.knot.beamsensor.communication;


import java.util.List;

import br.org.cesar.knot.beamsensor.model.BeamSensor;
import br.org.cesar.knot.beamsensor.model.BeamSensorFilter;
import br.org.cesar.knot.lib.event.Event;

/**
 * Created by carlos on 09/03/17.
 */

public interface BeamCommunication {

    List<BeamSensor> getSensors(BeamSensorFilter filter);
    void open(String url, int port, String uuid, String token, Event<Boolean> callback) throws Exception;
    boolean close();
}
