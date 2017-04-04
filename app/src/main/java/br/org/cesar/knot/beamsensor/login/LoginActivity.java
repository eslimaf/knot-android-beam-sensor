package br.org.cesar.knot.beamsensor.login;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import br.org.cesar.knot.beamsensor.R;
import br.org.cesar.knot.beamsensor.communication.Communication;
import br.org.cesar.knot.beamsensor.controller.BeamController;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity implements Communication.OpenConnectionListener {
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

    private Communication mKnotSocketCommunication;
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

        mKnotSocketCommunication = Communication.getInstance();

        mCloudIpEditText.addTextChangedListener(mServerEmptyWatcher);
        mPortEditText.addTextChangedListener(mServerEmptyWatcher);

        mUsernameEditText.addTextChangedListener(mCredentialsEmptyWatcher);
        mPasswordEditText.addTextChangedListener(mCredentialsEmptyWatcher);

        mCloudIpEditText.setText("172.17.120.174");
        mPortEditText.setText("3000");
        mUsernameEditText.setText("5477b005-5637-453d-a8c7-4882dc590000");
        mPasswordEditText.setText("0289db9c55b89663b0bf0eb1eabad0e242f79e70");
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

//    @OnClick(R.id.loginButton)
//    void performLogin() {
//        Log.d(TAG, "Login button pressed");
//        String mCloudIp = mCloudIpEditText.getText().toString();
//        int mPort = Integer.parseInt(mPortEditText.getText().toString());
//        String mUsername = mUsernameEditText.getText().toString();
//        String mPassword = mPasswordEditText.getText().toString();
//        try {
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
//
//        } catch (Exception e) {
//            Toast.makeText(this.getBaseContext(),e.getMessage(),Toast.LENGTH_SHORT);
//        }
//    }

    @OnClick(R.id.loginButton)
    void performLogin() {
        Log.d(TAG, "Login button pressed");
        String userName = mUsernameEditText.getText().toString();
        String password = mPasswordEditText.getText().toString();

        //Configuring socket with uuid and socket
        mKnotSocketCommunication.configureSocketWithDeviceInformation(userName,password);

        mKnotSocketCommunication.openConnection("http://172.17.120.174:3000",this);

    }


    @Override
    public void socketConnected() {
        Log.d(TAG, "Socket connected");
    }

    @Override
    public void socketNotConnected() {
        Log.d(TAG, "Socket not connected");
    }
}
