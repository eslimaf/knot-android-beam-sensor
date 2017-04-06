package br.org.cesar.knot.beamsensor.login;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONException;

import java.util.List;

import br.org.cesar.knot.beamsensor.R;
import br.org.cesar.knot.beamsensor.controller.BeamController;
import br.org.cesar.knot.beamsensor.model.BeamSensor;
import br.org.cesar.knot.beamsensor.model.BeamSensorData;
import br.org.cesar.knot.beamsensor.model.BeamSensorFilter;
import br.org.cesar.knot.beamsensor.model.SubscriberDataListener;
import br.org.cesar.knot.lib.exception.InvalidParametersException;
import br.org.cesar.knot.lib.exception.KnotException;
import br.org.cesar.knot.lib.exception.SocketNotConnected;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity implements SubscriberDataListener {
    private static final String TAG = LoginActivity.class.getSimpleName();

    @BindView(R.id.loginTextInputAddress)
    TextInputEditText mCloudIpEditText;
    @BindView(R.id.loginTextInputPort)
    TextInputEditText mPortEditText;
    @BindView(R.id.loginTextInputUsername)
    TextInputEditText mUsernameEditText;
    @BindView(R.id.loginTextInputPassword)
    TextInputEditText mPasswordEditText;
    @BindView(R.id.loginButton)
    Button mLoginButton;

    //private Communication mKnotSocketCommunication;
    BeamController beamController = BeamController.getInstance();

    private TextWatcher mServerEmptyWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
            checkServerFieldsForEmptyValues();
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    private TextWatcher mCredentialsEmptyWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
            checkCredentialsFieldForEmptyValues();
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        //mKnotSocketCommunication = Communication.getInstance();

                        mCloudIpEditText.addTextChangedListener(mServerEmptyWatcher);
        mPortEditText.addTextChangedListener(mServerEmptyWatcher);

        mUsernameEditText.addTextChangedListener(mCredentialsEmptyWatcher);
        mPasswordEditText.addTextChangedListener(mCredentialsEmptyWatcher);

        mCloudIpEditText.setText("10.211.55.19");
        mPortEditText.setText("3000");
        mUsernameEditText.setText("calberto");
        mPasswordEditText.setText("123456");
    }

    private void enableCredentialsEditText(boolean enable) {
        mUsernameEditText.setEnabled(enable);
        mPasswordEditText.setEnabled(enable);
    }

    private void enableLoginButton(boolean enable) {
        mLoginButton.setEnabled(enable);
    }

    private void checkServerFieldsForEmptyValues() {
        if (!mCloudIpEditText.getText().toString().isEmpty()
                && !mPortEditText.getText().toString().isEmpty()) {
            enableCredentialsEditText(true);
        } else {
            enableCredentialsEditText(false);
        }
    }

    private void checkCredentialsFieldForEmptyValues() {
        if (!mUsernameEditText.getText().toString().isEmpty()
                && !mPasswordEditText.getText().toString().isEmpty()) {
            enableLoginButton(true);
        } else {
            enableLoginButton(false);
        }
    }

    @OnClick(R.id.loginButton)
    void performLogin() {
        Log.d(TAG, "Login button pressed");
        String mCloudIp = mCloudIpEditText.getText().toString();
        int mPort = Integer.parseInt(mPortEditText.getText().toString());
        String mUsername = mUsernameEditText.getText().toString();
        String mPassword = mPasswordEditText.getText().toString();
        try {
            beamController.openCommunication(mCloudIp,mPort,mUsername,mPassword,this);
//            beamController.openCommunication(mCloudIp,mPort,mUsername,mPassword,new Event<Boolean>(){
//                @Override
//                public void onEventFinish(Boolean object) {
//                    if(object.booleanValue()) {
//                        Log.d("Carlos", "Opened");
//                        Intent i = new Intent(getBaseContext(), SensorMapActivity.class);
//                        startActivity(i);
//                        finish();
//                        //Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_SHORT);
//                    }
//                }
//
//                @Override
//                public void onEventError(Exception e) {
//                    Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_SHORT);
//                }
//            });

        } catch (Exception e) {
            Toast.makeText(this.getBaseContext(),e.getMessage(),Toast.LENGTH_SHORT);
        }
    }

    private Exception currentException;
    @Override
    public void setError(Exception error) {
        Log.d("Login",error.getMessage());
        currentException = error;
    }

    @Override
    public Exception getError() {
        return currentException;
    }

    @Override
    public int getId() {
        return 1;
    }

    @Override
    public void ready() {
        Log.d("Login","Subscriber Ready");
        beamController.subscribeDataListener(this);
        try {
            beamController.getSensor(new BeamSensorFilter());
        } catch (InvalidParametersException e) {
                                    e.printStackTrace();
        } catch (SocketNotConnected socketNotConnected) {
            socketNotConnected.printStackTrace();
        } catch (KnotException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void notReady() {
        Log.d("Login","Subscriber Not Ready");
    }

    List<BeamSensor> devices;
    List<BeamSensorData> data;

    @Override
    public void setData(List<BeamSensorData> data) {
        this.data = data;
    }

    @Override
    public List<BeamSensorData> getData() {
        return this.data;
    }

    @Override
    public void setDeviceList(List<BeamSensor> devices) {
        this.devices = devices;
    }

    @Override
    public List<BeamSensor> getDevices() {
        return devices;
    }

}
