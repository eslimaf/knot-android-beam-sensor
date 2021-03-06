package br.org.cesar.knot.beamsensor.data.model;

public class Emitter {
    private int status;
    private final double mLat;
    private final double mLong;

    Emitter(double lat, double aLong) {
        mLat = lat;
        mLong = aLong;
    }

    public double getLat() {
        return mLat;
    }

    public double getLong() {
        return mLong;
    }

    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
    }
}
