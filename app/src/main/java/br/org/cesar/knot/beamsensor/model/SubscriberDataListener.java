package br.org.cesar.knot.beamsensor.model;

import java.util.List;

/**
 * Created by carlos on 05/04/17.
 */

public interface SubscriberDataListener extends Subscriber{

    void setData(List<BeamSensorData> data);

    List<BeamSensorData> getData();

    void setDeviceList(List<BeamSensor> devices);

    List<BeamSensor> getDevices();
}
