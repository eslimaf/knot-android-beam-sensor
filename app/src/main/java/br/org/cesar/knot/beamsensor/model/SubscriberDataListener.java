package br.org.cesar.knot.beamsensor.model;

import java.util.List;

/**
 * Created by carlos on 05/04/17.
 */

public interface SubscriberDataListener extends Subscriber{

    void setData(List<BeamSensor> data);

    List<BeamSensor> getData();
}
