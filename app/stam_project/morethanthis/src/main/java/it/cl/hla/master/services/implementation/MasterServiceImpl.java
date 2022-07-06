package it.cl.hla.master.services.implementation;

import hla.rti1516e.InteractionClassHandle;
import hla.rti1516e.ParameterHandle;
import hla.rti1516e.ParameterHandleValueMap;
import hla.rti1516e.exceptions.*;

import it.cl.hla.core.interfaces.HlaCore;
import it.cl.hla.master.services.interfaces.MasterService;
import it.cl.hla.master.beans.HandlesBean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URL;




@Service
public class MasterServiceImpl implements MasterService {


    @Autowired
    private HlaCore hlaCore;

    @Autowired
    private HandlesBean handlesBean;

    @Value("${fom.xml}")
    Resource fomFile;

    @Value("${federation.name}")
    String federationName;

    @Value("${federate.name}")
    String federateName;
    @Value("${federate.type}")
    String federateType;

    @Value("${CRC.address}")
    String address;

    @Value("${CRC.port}")
    int port;

  /*  @Value("${localSettingsDesignator.settings}")
    String localSettingDesignator;*/


    @PostConstruct
    public void init() {
        System.out.println("Welcome to the Master Federate! ");
        System.out.println("****************************************************************");
        System.out.println("Make sure that your desired federates have joined the federation");
        System.out.println("****************************************************************");

        String settingDesignator = "CRCAddress=" + address + ":" + Integer.toString(port);
        try {
            // Legge le proprietà e il URL del file da application.properties
            URL url = fomFile.getFile().toURI().toURL();
            hlaCore.start(settingDesignator, federationName, url);
        } catch (FederateNotExecutionMember | RestoreInProgress | SaveInProgress | NotConnected | RTIinternalError |
                 ConnectionFailed | FederateServiceInvocationsAreBeingReportedViaMOM | IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("FedExec Started");
    }

    @Override
    public void joint(){
           hlaCore.join(federateName,federateType);
           System.out.println("Federate joint FedExec");
           getHandles();
    }

    @Override
    public void exit() {
        try {
            hlaCore.stop();
        } catch (RTIinternalError e) {
            throw new RuntimeException(e);
        }
        System.out.println("Federation destruction");
    }

    @Override
    public void detectCrowds(Integer people) throws FederateNotExecutionMember, NotConnected, RestoreInProgress, SaveInProgress, RTIinternalError {
            ParameterHandleValueMap mapHandle = hlaCore.createMap(1);
            byte[] bytearrayPeople = hlaCore.encoderInt(people);
            mapHandle.put(handlesBean.getPeopleHandle(), bytearrayPeople);
    }


    public void getHandles(){
        try {

            //detect people handle
            InteractionClassHandle detectedPeople = hlaCore.getInteractionClassHandle("DetectPeople");
            handlesBean.setDetectCrowdsClassHandle(detectedPeople);
            hlaCore.publishInteractions(detectedPeople);
            ParameterHandle peopleParameter = hlaCore.getParameterHandle(detectedPeople, "NumberOfPeople");
            handlesBean.setPeopleHandle(peopleParameter);

        } catch(FederateNotExecutionMember| RTIinternalError | FederateServiceInvocationsAreBeingReportedViaMOM | RestoreInProgress | SaveInProgress | NotConnected e) {
            System.out.println("Some error just occurs.");
            e.printStackTrace();
        }
    }
}
