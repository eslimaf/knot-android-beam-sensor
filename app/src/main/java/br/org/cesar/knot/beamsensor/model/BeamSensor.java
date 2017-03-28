package br.org.cesar.knot.beamsensor.model;


import android.location.Location;
import br.org.cesar.knot.lib.model.AbstractThingDevice;
/**
 * Created by carlos on 09/03/17.
 */

public class BeamSensor extends AbstractThingDevice {

    private Location location;

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
