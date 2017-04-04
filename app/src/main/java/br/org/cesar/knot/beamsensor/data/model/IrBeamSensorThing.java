package br.org.cesar.knot.beamsensor.data.model;

public class IrBeamSensorThing {
    private final String mId;
    private final Receiver mReceiver;
    private final Emitter mLeftEmitter;
    private final Emitter mRightEmitter;

    private IrBeamSensorThing(Builder builder) {
        mId = builder.mId;
        mReceiver = builder.mReceiver;
        mLeftEmitter = builder.mLeftEmitter;
        mRightEmitter = builder.mRightEmitter;
    }

    public Receiver getReceiver() {
        return mReceiver;
    }

    public Emitter getLeftEmitter() {
        return mLeftEmitter;
    }

    public Emitter getRightEmitter() {
        return mRightEmitter;
    }

    public String getId() {
        return mId;
    }

    public IrBeamSensorThing create() {
        return this;
    }

    public static class Builder {
        private String mId;
        private Receiver mReceiver;
        private Emitter mLeftEmitter;
        private Emitter mRightEmitter;

        public Builder(){}

        public Builder withId(String id){
            mId = id;
            return this;
        }

        public Builder receiverWithLocation(double latitude, double longitude) {
            mReceiver = new Receiver(latitude,longitude);
            return this;
        }

        public Builder rightEmitterWithLocation(double latitude, double longitude) {
            mRightEmitter = new Emitter(latitude,longitude);
            return this;
        }

        public Builder leftEmitterWithLocation(double latitude, double longitude) {
            mLeftEmitter = new Emitter(latitude,longitude);
            return this;
        }

        public IrBeamSensorThing create(){
            return new IrBeamSensorThing(this);
        }
    }
}
