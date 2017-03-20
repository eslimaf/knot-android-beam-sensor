package br.org.cesar.knot.beamsensor.data.event;

public interface OnThingNewEvent {
    void onNewThingAdded();
    void onPerimeterBreached();
}
