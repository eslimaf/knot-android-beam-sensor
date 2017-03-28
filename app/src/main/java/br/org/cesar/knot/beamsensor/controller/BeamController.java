package br.org.cesar.knot.beamsensor.controller;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import br.org.cesar.knot.beamsensor.communication.BeamCommunication;
import br.org.cesar.knot.beamsensor.communication.BeamCommunicationFactory;
import br.org.cesar.knot.beamsensor.model.BeamSensor;
import br.org.cesar.knot.beamsensor.model.BeamSensorFilter;

/**
 * Created by carlos on 14/03/17.
 */

public class BeamController {
    private BeamCommunication communication;
    private static BeamController instance = new BeamController();


    private BeamController(){
        try {
            communication = BeamCommunicationFactory.getBeamCommunication(BeamCommunicationFactory.BeamCommunicationProtocol.WSS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static BeamController getInstance(){
        return instance;
    }

    public List<BeamSensor> getSensor(BeamSensorFilter filter){
       return communication.getSensors(filter);
    }

    public boolean openCommunication(String url,int port,String user, String password) throws Exception {
        return communication.open(url,port
                ,user,password);
    }

    /*
     BeamSensorFilter f = new BeamSensorFilter();
        ArrayList range = new ArrayList();
        range.add("10");
        range.add("13");
        range.add("1");
        f.build("value1",10, BeamSensorFilter.FilterCompareValueMode.GreatThan);
        f.build("range",range, BeamSensorFilter.FilterCompareValueMode.In);
        f.build("name","car", BeamSensorFilter.FilterCompareValueMode.Like);
        f.build("age",10, BeamSensorFilter.FilterCompareValueMode.GreatThan, BeamSensorFilter.FilterLinkType.Or,"index",1, BeamSensorFilter.FilterCompareValueMode.Equal);
        f.build("rate",2, BeamSensorFilter.FilterCompareValueMode.LessThan, BeamSensorFilter.FilterLinkType.And,"item",1, BeamSensorFilter.FilterCompareValueMode.Equal);
        try {
            JSONObject query = f.getQuery();
            System.out.println(query);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        */

}
