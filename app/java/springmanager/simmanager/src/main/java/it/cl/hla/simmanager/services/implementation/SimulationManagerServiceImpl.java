package it.cl.hla.simmanager.services.implementation;

import hla.rti1516e.InteractionClassHandle;
import hla.rti1516e.ParameterHandle;

import hla.rti1516e.ParameterHandleValueMap;
import hla.rti1516e.encoding.DecoderException;
import hla.rti1516e.exceptions.*;

import it.cl.hla.simmanager.services.interfaces.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URL;




@Service
public class SimulationManagerServiceImpl implements SimulationManagerService {

    @Autowired
    private HLAService hlaService;

    @Autowired
    private HandlesServiceImpl handleService;


    @Value("${fom.xml}")
    Resource fomFile;

    @Value("${federation.name}")
    String federationName;

    @Value("${federate.name}")
    String federateName;

    @Value("${localSettingsDesignator.settings}")
    String localSettingDesignator;

    @Value("${scenario.dir}")
    String scenarioDIR;

    @PostConstruct
    public void init() {
        System.out.println("Welcome to the Master Federate! ");
        System.out.println("****************************************************************");
        System.out.println("Make sure that your desired federates have joined the federation");
        System.out.println("****************************************************************");

        try {
            // Legge le proprietà e il URL del file da application.properties
            URL url = fomFile.getFile().toURI().toURL();
            hlaService.start(localSettingDesignator, federationName, federateName, url);
        } catch (FederateNotExecutionMember | RestoreInProgress | SaveInProgress | NotConnected | RTIinternalError |
                 ConnectionFailed | FederateServiceInvocationsAreBeingReportedViaMOM | IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("FedExec Started");
    }

    public void joint(){
           hlaService.join(federateName);
           System.out.println("Federate joint FedExec");
           getHandles();
    }

    public void exit() {
        try {
            hlaService.stop();
        } catch (RTIinternalError e) {
            throw new RuntimeException(e);
        }
        System.out.println("Federation destruction");
    }

    /*@Override
    public void scenarioPublished(){
        hlaService.addInteractionListener(new ListenerInteraction() {
            @Override
            public void receiveInteraction(InteractionClassHandle interactionHandle, ParameterHandleValueMap mapValue) {
                if(interactionHandle.equals(handleService.getScenarioLoadedInteractionClassHandle())){
                System.out.println("[MASTER]" + federateName + " loaded scenario ");
        }
        }
    });
    }*/

   /* @Override
    public void scenarioNotPublished(String errorMessage){
        hlaService.addInteractionListener(new ListenerInteraction() {
            @Override
            public void receiveInteraction(InteractionClassHandle interactionHandle, ParameterHandleValueMap mapValue){
                if(interactionHandle.equals(handleService.getScenarioLoadFailureInteractionClassHandle())){
                    InteractionClassHandle message = handleService.getScenarioFailureErrorMessage();

                    System.out.println("[MASTER]" + federateName + " failed to load scenario: " + errorMessage);
                }

            }
        });
    }*/
    @Override
    public void loadScenario(String scenarioName, Integer initialFuelLevel) throws FederateNotExecutionMember, NotConnected, RestoreInProgress, SaveInProgress, RTIinternalError {
            ParameterHandleValueMap mapHandle = hlaService.createMap(2);
            byte[] parameterNameFederate = hlaService.encoderString(hlaService.getFederateName());
            mapHandle.put(handleService.getScenarioParameterHandle(), parameterNameFederate);
            byte[] fuel = hlaService.encoderInt(initialFuelLevel);
            mapHandle.put(handleService.getInitialFuelAmountParameterHandle(), fuel);
            hlaService.sendInteraction(handleService.getLoadScenarioClassHandle(), mapHandle);
    }


    @Override
    public void sendStart(float timeScaleFactor) throws FederateNotExecutionMember, NotConnected, RestoreInProgress, RTIinternalError, SaveInProgress {
        ParameterHandleValueMap mapHandle = hlaService.createMap(1);
        byte [] time = hlaService.encoderFloat(timeScaleFactor);
        mapHandle.put(handleService.getStartTimeScaleFactorParameterHandle(), time);
        hlaService.sendInteraction(handleService.getStartInteractionClassHandle(), mapHandle);

    }
    @Override
    public void sendStop() throws FederateNotExecutionMember, NotConnected, RestoreInProgress, RTIinternalError, SaveInProgress {
        ParameterHandleValueMap mapHandle = hlaService.createMap(0);
        hlaService.sendInteraction(handleService.getStopInteractionClassHandle(), mapHandle);

    }

    public void getHandles(){
        try {

            //Start Handle
            InteractionClassHandle startClassHandle = hlaService.getInteractionClassHandle("Start");
            handleService.setStartInteractionClassHandle(startClassHandle);
            hlaService.publishInteractions(startClassHandle);
            ParameterHandle startTimeScaleFactor = hlaService.getParameterHandle(startClassHandle, "TimeScaleFactor");
            handleService.setStartTimeScaleFactorParameterHandle(startTimeScaleFactor);

            //Stop Handle
            InteractionClassHandle stopClassHandle = hlaService.getInteractionClassHandle("Stop");
            handleService.setStopInteractionClassHandle(stopClassHandle);
            hlaService.publishInteractions(stopClassHandle);


            //LoadScenario Handle
            InteractionClassHandle loadScenarioClassHandle = hlaService.getInteractionClassHandle("LoadScenario");
            handleService.setLoadScenarioClassHandle(loadScenarioClassHandle);
            hlaService.publishInteractions(loadScenarioClassHandle);
            ParameterHandle scenarioName = hlaService.getParameterHandle(loadScenarioClassHandle, "ScenarioName");
            handleService.setScenarioParameterHandle(scenarioName);
            ParameterHandle initialFuelLevel = hlaService.getParameterHandle(loadScenarioClassHandle, "InitialFuelAmount");
            handleService.setInitialFuelAmountParameterHandle(initialFuelLevel);

        } catch(FederateNotExecutionMember| RTIinternalError | FederateServiceInvocationsAreBeingReportedViaMOM | RestoreInProgress | SaveInProgress | NotConnected e) {
            System.out.println("Some error just occurs.");
            e.printStackTrace();
        }
    }
}
