package it.cl.hla.coordinator.implementation;

import hla.rti1516e.*;
import hla.rti1516e.encoding.DecoderException;
import hla.rti1516e.exceptions.*;
import it.cl.hla.coordinator.interfaces.CarSimulator;
import it.cl.hla.core.implementation.*;
import it.cl.hla.coordinator.flueltypecoder.FuelTypeEnum8Coder;
import it.cl.hla.core.interfaces.HlaCore;
import it.cl.hla.core.interfaces.ListenerInteraction;

public class AnyLogicCoordinator {
    private HlaCore hlaCore = new HlaCoreImpl();


    private InteractionClassHandle startInteractionClassHandle;
    private InteractionClassHandle stopInteractionClassHandle;
    private InteractionClassHandle loadScenarioClassHandle;
    private InteractionClassHandle scenarioLoadedInteractionClassHandle;
    private InteractionClassHandle scenarioLoadFailureInteractionClassHandle;


    private ParameterHandle startTimeScaleFactorParameterHandle;
    private ParameterHandle initialFuelAmountParameterHandle;
    private ParameterHandle scenarioParameterHandle;
    private ParameterHandle scenarioLoadedFederateNameParameterHandle;
    private ParameterHandle scenarioFailureFederateNameParameterHandle;
    private ParameterHandle scenarioFailureErrorMessage;

    private ObjectClassHandle objectClassCarHandle;

    private AttributeHandle attributePositionHandle;
    private AttributeHandle attributeFuelLevelHandle;
    private AttributeHandle nameAttributeHandle;
    private AttributeHandle licensePlateNumberAttributeHandle;
    private AttributeHandle fuelTypeAttributeHandle;

    private FuelTypeEnum8Coder fuelTypeEnumCoder;
    private FuelType fuelType;
    private CarSimulator anyLogic ;
    public boolean running;
    public float timeScale;
    public int initialFuel;

    /**
     * Costruttore di default
     */
    public AnyLogicCoordinator(){
    }

    /**
     * Costruttore con parametro
     * @param anylogic la variabile che comunica con il software AnyLogic grazie all'interfaccia CarSimulator
     */
    public AnyLogicCoordinator(CarSimulator anylogic){
        anyLogic = anylogic;
    }


    /**
     * Getter della variabile is Running
     * -> controlla che l'interaction startInteractionClassHandle sia stata ricevuta
     * @return boolean value
     */
    public boolean isRunning() {
        return this.running;
    }

    /**
     * Setter della variabile isRunning
     * @param Running boolean value
     */
    public void setRunning(boolean Running) {
        running = Running;
    }


    /**
     * Getter della var TimeScale
     * -> parametro da inserire dopo che la startInteractionClassHandle è stata ricevuta
     * stabilisce tempo della simulazione
     * @return time scale factor
     */
    public float getTimeScale() {
        return timeScale;
    }

    /**
     * Setter della var timeScale
     * @param timeScale float value
     */
    public void setTimeScale(float timeScale) {
        this.timeScale = timeScale;
    }


    /**
     * Getter della var initialFuel
     * -> stabilisce quantità di gasolio a inizio simulazione
     * @return initial fuel level
     */
    public int getInitialFuel() {
        return initialFuel;
    }

    /**
     * Setter della var initialFuel
     * @param initialFuel int value
     */
    public void setInitialFuel(int initialFuel) {
        this.initialFuel = initialFuel;
    }


    /**
     *  chiama l'interfaccia per uscire dalla FedExec
     */
    public void Stop() throws RTIinternalError {
            try {
                hlaCore.stop();
            } catch (RTIinternalError exception) {
                exception.printStackTrace();
            }
    }


    /**
     * chiama l'interfaccia per fare il join della FedExec,
     * inoltre gestisce i due handle per lo start e lo stop
    */
    public void Start(String settingsDesignator, String federationName, String federateName, String federateType) throws FederateNotExecutionMember, RestoreInProgress, FederateServiceInvocationsAreBeingReportedViaMOM, NotConnected, SaveInProgress, ConnectionFailed {

        hlaCore.addInteractionListener(new ListenerInteraction() {
            @Override
            public void receiveInteraction(InteractionClassHandle interactionHandle, ParameterHandleValueMap mapValue){
                if (interactionHandle.equals(startInteractionClassHandle)) {
                    try {
                        float timeScaleFactor = hlaCore.decodeFloat32(mapValue.get(startTimeScaleFactorParameterHandle));
                        setRunning(true);
                        System.out.println("[COORD] Federate is running: " + isRunning());
                        setTimeScale(timeScaleFactor);
                        System.out.println("[COORD] Received Start interaction");
                        anyLogic.startSimulation();
                    } catch (DecoderException e) {
                        throw new RuntimeException(e);
                    }
                } else if (interactionHandle.equals(stopInteractionClassHandle)) {
                    System.out.println("[COORD] Federate is running: " + isRunning());
                    System.out.println("[COORD] Received Stop interaction");
                    setAllParameters();
                }
            }
        });
            try {
                    hlaCore.start("Tutorial", "HLA Tutorial", null);

                    hlaCore.join("AnyLogicCar","CarSimulator");

                    startInteractionClassHandle = hlaCore.getInteractionClassHandle("Start");
                    hlaCore.subscribeInteractions(startInteractionClassHandle);
                    startTimeScaleFactorParameterHandle = hlaCore.getParameterHandle(startInteractionClassHandle, "TimeScaleFactor");

                    stopInteractionClassHandle = hlaCore.getInteractionClassHandle("Stop");
                    hlaCore.subscribeInteractions(stopInteractionClassHandle);

                    scenarioHandling();

                    objectHandling();

                    System.out.println("[COORD] Car Published");


            } catch (RTIinternalError e) {
                System.out.println("[COORD] Could not connect to the RTI with local settings designator");
                e.printStackTrace();
            }
    }


    /**
     * ScenarioHandling aggiunge un listener per la gestione delle interaction dello scenario
     * e viene richiamata in Start().
     */
    public void scenarioHandling() throws FederateNotExecutionMember, RestoreInProgress, FederateServiceInvocationsAreBeingReportedViaMOM, NotConnected, RTIinternalError, SaveInProgress {
        hlaCore.addInteractionListener(new ListenerInteraction() {

            @Override
            public void receiveInteraction(InteractionClassHandle interactionHandle, ParameterHandleValueMap mapValue){
                if (interactionHandle.equals(loadScenarioClassHandle)) {
                    try {
                      String scenarioName = hlaCore.decodeString32(mapValue.get(scenarioParameterHandle));
                      System.out.println("[COORD] Scenario chosen: " + scenarioName);

                      int fuelInitial = hlaCore.decodeInt32(mapValue.get(initialFuelAmountParameterHandle));
                      setInitialFuel(fuelInitial);

                      anyLogic.loadScenario();



                  } catch (DecoderException e) {
                      e.printStackTrace();
                  }
                } else if (interactionHandle.equals(scenarioLoadedInteractionClassHandle)) {

                  try {
                      hlaCore.decodeString32(mapValue.get(scenarioLoadedFederateNameParameterHandle));
                  } catch (DecoderException e) {
                      throw new RuntimeException(e);
                  }
                } else if (interactionHandle.equals(scenarioLoadFailureInteractionClassHandle)) {
                    try {
                        hlaCore.decodeString32(mapValue.get(scenarioFailureFederateNameParameterHandle));
                        hlaCore.decodeString32(mapValue.get(scenarioFailureErrorMessage));
                    } catch (DecoderException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });

        try {
            loadScenarioClassHandle = hlaCore.getInteractionClassHandle("LoadScenario");
            hlaCore.subscribeInteractions(loadScenarioClassHandle);
            scenarioParameterHandle = hlaCore.getParameterHandle(loadScenarioClassHandle, "ScenarioName");
            initialFuelAmountParameterHandle = hlaCore.getParameterHandle(loadScenarioClassHandle, "InitialFuelAmount");

            scenarioLoadedInteractionClassHandle = hlaCore.getInteractionClassHandle("ScenarioLoaded");
            hlaCore.publishInteractions(scenarioLoadedInteractionClassHandle);
            scenarioLoadedFederateNameParameterHandle = hlaCore.getParameterHandle(scenarioLoadedInteractionClassHandle, "FederateName");

            scenarioLoadFailureInteractionClassHandle = hlaCore.getInteractionClassHandle("ScenarioLoadFailure");
            hlaCore.publishInteractions(scenarioLoadFailureInteractionClassHandle);
            scenarioFailureFederateNameParameterHandle = hlaCore.getParameterHandle(scenarioLoadFailureInteractionClassHandle, "FederateName");
            scenarioFailureErrorMessage =  hlaCore.getParameterHandle(scenarioLoadFailureInteractionClassHandle, "ErrorMessage");

        } catch(RTIinternalError e) {
            System.out.println("Could not connect to the RTI with local settings designator");
            e.printStackTrace();
        }

    }


    public void objectHandling(){

        try {
            objectClassCarHandle=hlaCore.getObjectClassHandle("Car");

            attributePositionHandle=hlaCore.getAttributeHandle(objectClassCarHandle, "Position");
            attributeFuelLevelHandle=hlaCore.getAttributeHandle(objectClassCarHandle, "FuelLevel");
            nameAttributeHandle=hlaCore.getAttributeHandle(objectClassCarHandle, "Name");
            licensePlateNumberAttributeHandle=hlaCore.getAttributeHandle(objectClassCarHandle, "LicensePlateNumber");;
            fuelTypeAttributeHandle=hlaCore.getAttributeHandle(objectClassCarHandle, "FuelType");


            anyLogic.loadCar();
        } catch (NameNotFound | NotConnected | FederateNotExecutionMember| InvalidObjectClassHandle | RTIinternalError e) {
            throw new RuntimeException(e);
        }
    }

    public ObjectInstanceHandle createCar() {
        ObjectInstanceHandle carInstance;
        try {
            carInstance = hlaCore.publishObject(objectClassCarHandle);
            AttributeHandleValueMap mapAttributes = hlaCore.createAttributeMap(2);
            byte [] nameAttribute = hlaCore.encoderString(hlaCore.getNameAttribute(objectClassCarHandle, nameAttributeHandle));
            mapAttributes.put(nameAttributeHandle,nameAttribute);
            byte [] licenseAttribute = hlaCore.encoderString(hlaCore.getNameAttribute(objectClassCarHandle, licensePlateNumberAttributeHandle));
            mapAttributes.put(licensePlateNumberAttributeHandle, licenseAttribute);
            //byte [] fuelTypeEnum = fuelTypeEnumCoder.encode(fuelType); //Non prende nessun tipo di fuel in particolare
            //mapAttributes.put(fuelTypeAttributeHandle, fuelTypeEnum);
        } catch (ObjectClassNotPublished | ObjectClassNotDefined | SaveInProgress | RestoreInProgress |
                 FederateNotExecutionMember | NotConnected | RTIinternalError | InvalidAttributeHandle |
                 AttributeNotDefined | InvalidObjectClassHandle e) {
            throw new RuntimeException(e);
        }
        return carInstance;
    }


    public void publishCar() throws FederateNotExecutionMember, NotConnected, RestoreInProgress, SaveInProgress, RTIinternalError {
        try {
            AttributeHandleSet attributes = hlaCore.createAttributesSet(5);
            attributes.add(nameAttributeHandle);
            attributes.add(attributePositionHandle);
            attributes.add(fuelTypeAttributeHandle);
            attributes.add(licensePlateNumberAttributeHandle);
            attributes.add(attributeFuelLevelHandle);
            hlaCore.sendObject(objectClassCarHandle,attributes);
        } catch (AttributeNotDefined e) {
            throw new RTIinternalError("HlaInterfaceFailure", e);
        } catch (ObjectClassNotDefined e) {
            throw new RTIinternalError("HlaInterfaceFailure", e);
        } catch (FederateServiceInvocationsAreBeingReportedViaMOM e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * callback del Coordinator sul Manager per il loading dello Scenario
     */
    public void publishScenarioLoaded() throws FederateNotExecutionMember, RestoreInProgress, NotConnected, RTIinternalError, SaveInProgress {
        try {
            ParameterHandleValueMap mapHandle = hlaCore.createParameterMap(2);
            byte [] nameParameter = hlaCore.encoderString(hlaCore.getFederateName());
            mapHandle.put(scenarioLoadedFederateNameParameterHandle,nameParameter);
            hlaCore.sendInteraction(scenarioLoadedInteractionClassHandle, mapHandle);
            System.out.println("[COORD] Scenario Loading SUCCEED in AnyLogic");
        } catch(NullPointerException e){
            e.printStackTrace();
        }
    }

    /**
     * Callback del Coordinator sul Manager per il Failure nel loading dello Scenario
     * @param errorMessage stringa di Errore
     */
    public void publishScenarioFailed(String errorMessage) throws FederateNotExecutionMember, RestoreInProgress, NotConnected, RTIinternalError, SaveInProgress {
       try {
           ParameterHandleValueMap mapHandle = hlaCore.createParameterMap(2);
           byte [] nameParameter = hlaCore.encoderString(hlaCore.getFederateName());
           mapHandle.put(scenarioFailureFederateNameParameterHandle, nameParameter);
           byte [] error = hlaCore.encoderString(errorMessage);
           mapHandle.put(scenarioFailureErrorMessage, error);
           hlaCore.sendInteraction(scenarioLoadFailureInteractionClassHandle,mapHandle);
           System.out.println("[COORD] Scenario Loading FAILED in AnyLogic");
       } catch(NullPointerException e){
           e.printStackTrace();
       }

    }
    public void setAllParameters(){
        setRunning(false);
        setInitialFuel(0);
        setTimeScale(0);
    }

}