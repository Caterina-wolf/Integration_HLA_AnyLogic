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


    @Value("${CRC.address}")
    String address;

    @Value("${CRC.port}")
    int port;

    @Value("${localSettingsDesignator.settings}")
    String localSettingDesignator;

    @Value("${scenario.dir}")
    String scenarioDIR;

    @Value("${federate.type}")
    String federateType;


    @PostConstruct
    public void init() {
        System.out.println("Welcome to the Master Federate! ");
        System.out.println("****************************************************************");
        System.out.println("Make sure that your desired federates have joined the federation");
        System.out.println("****************************************************************");

        String settingDesignator = "crcAddress=" + address + ":" + Integer.toString(port);
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

    public void joint(){
           hlaCore.join(federateName,federateType);
           System.out.println("Federate joint FedExec");
           getHandles();
    }

    public void exit() {
        try {
            hlaCore.stop();
        } catch (RTIinternalError e) {
            throw new RuntimeException(e);
        }
        System.out.println("Federation destruction");
    }

    @Override
    public void loadScenario(String scenarioName, Integer initialFuelLevel) throws FederateNotExecutionMember, NotConnected, RestoreInProgress, SaveInProgress, RTIinternalError {
            ParameterHandleValueMap mapHandle = hlaCore.createMap(2);
            byte[] parameterScenarioName = hlaCore.encoderString(scenarioName);
            mapHandle.put(handlesBean.getScenarioParameterHandle(), parameterScenarioName);
            byte[] fuel = hlaCore.encoderInt(initialFuelLevel);
            mapHandle.put(handlesBean.getInitialFuelAmountParameterHandle(), fuel);
            hlaCore.sendInteraction(handlesBean.getLoadScenarioClassHandle(), mapHandle);
    }


    @Override
    public void sendStart(float timeScaleFactor) throws FederateNotExecutionMember, NotConnected, RestoreInProgress, RTIinternalError, SaveInProgress {
        ParameterHandleValueMap mapHandle = hlaCore.createMap(1);
        byte [] time = hlaCore.encoderFloat(timeScaleFactor);
        mapHandle.put(handlesBean.getStartTimeScaleFactorParameterHandle(), time);
        hlaCore.sendInteraction(handlesBean.getStartInteractionClassHandle(), mapHandle);

    }
    @Override
    public void sendStop() throws FederateNotExecutionMember, NotConnected, RestoreInProgress, RTIinternalError, SaveInProgress {
        ParameterHandleValueMap mapHandle = hlaCore.createMap(0);
        hlaCore.sendInteraction(handlesBean.getStopInteractionClassHandle(), mapHandle);

    }

    public void getHandles(){
        try {

            //Start Handle
            InteractionClassHandle startClassHandle = hlaCore.getInteractionClassHandle("Start");
            handlesBean.setStartInteractionClassHandle(startClassHandle);
            hlaCore.publishInteractions(startClassHandle);
            ParameterHandle startTimeScaleFactor = hlaCore.getParameterHandle(startClassHandle, "TimeScaleFactor");
            handlesBean.setStartTimeScaleFactorParameterHandle(startTimeScaleFactor);

            //Stop Handle
            InteractionClassHandle stopClassHandle = hlaCore.getInteractionClassHandle("Stop");
            handlesBean.setStopInteractionClassHandle(stopClassHandle);
            hlaCore.publishInteractions(stopClassHandle);


            //LoadScenario Handle
            InteractionClassHandle loadScenarioClassHandle = hlaCore.getInteractionClassHandle("LoadScenario");
            handlesBean.setLoadScenarioClassHandle(loadScenarioClassHandle);
            hlaCore.publishInteractions(loadScenarioClassHandle);
            ParameterHandle scenarioName = hlaCore.getParameterHandle(loadScenarioClassHandle, "ScenarioName");
            handlesBean.setScenarioParameterHandle(scenarioName);
            ParameterHandle initialFuelLevel = hlaCore.getParameterHandle(loadScenarioClassHandle, "InitialFuelAmount");
            handlesBean.setInitialFuelAmountParameterHandle(initialFuelLevel);

        } catch(FederateNotExecutionMember| RTIinternalError | FederateServiceInvocationsAreBeingReportedViaMOM | RestoreInProgress | SaveInProgress | NotConnected e) {
            System.out.println("Some error just occurs.");
            e.printStackTrace();
        }
    }
}
