package br.org.cesar.knot.beamsensor.model;


import br.org.cesar.knot.lib.model.AbstractThingDevice;
/**
 * Created by carlos on 09/03/17.
 */

public class BeamSensor extends AbstractThingDevice {

    private boolean online;
    private String id;
    private String ipAddress;
    private BeamSensorItem receiver;
    private BeamSensorItem rightEmitter;
    private BeamSensorItem leftEmitter;


    public Boolean getOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public BeamSensorItem getReceiver() {
        return receiver;
    }

    public void setReceiver(BeamSensorItem receiver) {
        this.receiver = receiver;
    }

    public BeamSensorItem getRightEmitter() {
        return rightEmitter;
    }

    public void setRightEmitter(BeamSensorItem rightEmitter) {
        this.rightEmitter = rightEmitter;
    }

    public BeamSensorItem getLeftEmitter() {
        return leftEmitter;
    }

    public void setLeftEmitter(BeamSensorItem leftEmitter) {
        this.leftEmitter = leftEmitter;
    }

}
