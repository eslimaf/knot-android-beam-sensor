package br.org.cesar.knot.beamsensor.communication;


import java.util.List;

import br.org.cesar.knot.beamsensor.model.BeamSensor;
import br.org.cesar.knot.beamsensor.model.BeamSensorFilter;

/**
 * Created by carlos on 09/03/17.
 */

public interface BeamCommunication {

    List<BeamSensor> getSensors(BeamSensorFilter filter);
    boolean open(String url,int port,String uuid, String token) throws Exception;
    boolean close();
}
