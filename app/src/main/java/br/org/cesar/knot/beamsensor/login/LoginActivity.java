package br.org.cesar.knot.beamsensor.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import br.org.cesar.knot.beamsensor.R;
import br.org.cesar.knot.beamsensor.controller.BeamController;
import br.org.cesar.knot.beamsensor.map.SensorMapActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {
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

    BeamController beamController =  BeamController.getInstance();

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

        mCloudIpEditText.addTextChangedListener(mServerEmptyWatcher);
        mPortEditText.addTextChangedListener(mServerEmptyWatcher);

        mUsernameEditText.addTextChangedListener(mCredentialsEmptyWatcher);
        mPasswordEditText.addTextChangedListener(mCredentialsEmptyWatcher);
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
            if(beamController.openCommunication(mCloudIp,mPort,mUsername,mPassword)){
                Intent i = new Intent(this, SensorMapActivity.class);
                startActivity(i);
                finish();
            }
        } catch (Exception e) {
            Toast.makeText(this.getBaseContext(),e.getMessage(),Toast.LENGTH_SHORT);
        }
    }
}
