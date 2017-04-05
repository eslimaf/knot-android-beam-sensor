package br.org.cesar.knot.beamsensor.model;

import java.util.List;

/**
 * Created by carlos on 05/04/17.
 */

public interface Subscriber {

    void setError(Exception error);

    Exception getError();

    int getId();

    void ready();

    void notReady();
}
