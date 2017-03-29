package br.org.cesar.knot.beamsensor.map;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import br.org.cesar.knot.beamsensor.R;
import br.org.cesar.knot.beamsensor.controller.BeamController;
import br.org.cesar.knot.beamsensor.model.BeamSensor;
import br.org.cesar.knot.beamsensor.model.BeamSensorFilter;

public class SensorMapActivity extends AppCompatActivity implements OnMapReadyCallback {

    BeamController beamController =  BeamController.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_map);
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng cesar = new LatLng(-8.0582517, -34.8741831);
        googleMap.addMarker(new MarkerOptions().position(cesar).title("CESAR"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(cesar,15));
        BeamSensorFilter filter = new BeamSensorFilter();
        List<BeamSensor> sensors = beamController.getSensor(filter);
        int count = sensors.size();
    }
}
