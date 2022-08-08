package it.cl.hla.coordinator.implementation;

import hla.rti1516e.*;
import hla.rti1516e.encoding.DecoderException;
import hla.rti1516e.exceptions.*;
import it.cl.hla.coordinator.interfaces.Car;
import it.cl.hla.coordinator.interfaces.CarSimulator;
import it.cl.hla.core.implementation.*;
import it.cl.hla.coordinator.encoders.FuelTypeEnum8Coder;
import it.cl.hla.core.interfaces.HlaCore;
import it.cl.hla.core.interfaces.ListenerInteraction;
import it.cl.hla.coordinator.encoders.*;
import it.cl.hla.coordinator.car.*;



public class AnyLogicCoordinator {
    private HlaCore hlaCore = new HlaCoreImpl();


    private InteractionClassHandle startInteractionClassHandle;
    private InteractionClassHandle stopInteractionClassHandle;
    private InteractionClassHandle loadScenarioClassHandle;
    private InteractionClassHandle scenarioLoadedInteractionClassHandle;
    private InteractionClassHandle scenarioLoadFailureInteractionClassHandle;
    private InteractionClassHandle addCarClassInteractionHandle;



    private ParameterHandle startTimeScaleFactorParameterHandle;
    private ParameterHandle initialFuelAmountParameterHandle;
    private ParameterHandle scenarioParameterHandle;
    private ParameterHandle scenarioLoadedFederateNameParameterHandle;
    private ParameterHandle scenarioFailureFederateNameParameterHandle;
    private ParameterHandle scenarioFailureErrorMessage;
    private ParameterHandle carNameParameterHandle;
    private ParameterHandle carColoParameterHandle;
    private ParameterHandle carLicensePlateParameterHandle;




    private ObjectClassHandle objectClassCarHandle;


    private AttributeHandle attributePositionHandle;
    private AttributeHandle attributeFuelLevelHandle;
    private AttributeHandle nameAttributeHandle;
    private AttributeHandle licensePlateNumberAttributeHandle;
    private AttributeHandle fuelTypeAttributeHandle;


    private FuelTypeEnum8Coder fuelTypeEnumCoder= new FuelTypeEnum8Coder();
    private PositionRecordCoder coderPosition = new PositionRecordCoder();


    private CarTracking carTracking= new CarTracking();


    private CarSimulator anyLogic ;


    public boolean running;
    public float timeScale;
    public int initialFuel;


    /**
     * Costruttore di default
     */
    public AnyLogicCoordinator(){}

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
                        System.out.println("[COORD] Federate is running");

                        setTimeScale(timeScaleFactor);
                        System.out.println("[COORD] StartInteraction received");
                    } catch (DecoderException e) {
                        throw new RuntimeException(e);
                    }
                } else if (interactionHandle.equals(stopInteractionClassHandle)) {
                    System.out.println("[COORD] Stop interaction");
                    setAllParameters();
                } else if(interactionHandle.equals(addCarClassInteractionHandle)){
                    try {

                        String carName = hlaCore.decodeString32(mapValue.get(carNameParameterHandle));
                        String license= hlaCore.decodeString32(mapValue.get(carLicensePlateParameterHandle));
                        String color= hlaCore.decodeString32(mapValue.get(carColoParameterHandle));

                        anyLogic.injectCar(carName, license, color);

                        System.out.println("[COORD] Car injected");

                    } catch (DecoderException e) {
                        throw new RuntimeException(e);
                    }


                }
            }
        });
            try {
                    hlaCore.start(settingsDesignator, federationName, null);

                    hlaCore.join(federateName,federateType);

                    startInteractionClassHandle = hlaCore.getInteractionClassHandle("Start");
                    hlaCore.subscribeInteractions(startInteractionClassHandle);
                    startTimeScaleFactorParameterHandle = hlaCore.getParameterHandle(startInteractionClassHandle, "TimeScaleFactor");

                    stopInteractionClassHandle = hlaCore.getInteractionClassHandle("Stop");
                    hlaCore.subscribeInteractions(stopInteractionClassHandle);

                    addCarClassInteractionHandle = hlaCore.getInteractionClassHandle("addCar");
                    hlaCore.subscribeInteractions(addCarClassInteractionHandle);
                    carNameParameterHandle = hlaCore.getParameterHandle(addCarClassInteractionHandle, "CarName");
                    carLicensePlateParameterHandle = hlaCore.getParameterHandle(addCarClassInteractionHandle, "LicensePlateCar");
                    carColoParameterHandle = hlaCore.getParameterHandle(addCarClassInteractionHandle, "ColorCar");

                    addCarHandling();

                    scenarioHandling();

                    objectHandling();

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

                      anyLogic.loadScenario(scenarioName, fuelInitial);
                  } catch (DecoderException e) {
                      e.printStackTrace();
                  }
                } else if (interactionHandle.equals(scenarioLoadedInteractionClassHandle)) {

                  try {
                     String scenarioLoadedInFederate = hlaCore.decodeString32(mapValue.get(scenarioLoadedFederateNameParameterHandle));
                      System.out.println("The federate" + scenarioLoadedInFederate + "has loaded the scenario chosen." );
                  } catch (DecoderException e) {
                      throw new RuntimeException(e);
                  }
                } else if (interactionHandle.equals(scenarioLoadFailureInteractionClassHandle)) {
                    try {
                        String scenarioFailureInFederate = hlaCore.decodeString32(mapValue.get(scenarioFailureFederateNameParameterHandle));
                        String error = hlaCore.decodeString32(mapValue.get(scenarioFailureErrorMessage));
                        System.out.println("The federate" + scenarioFailureInFederate + "has not loaded the scenario chosen, because" + error + "error.");
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

    public void addCarHandling(){
        try {
            addCarClassInteractionHandle = hlaCore.getInteractionClassHandle("addCar");

            carNameParameterHandle = hlaCore.getParameterHandle(addCarClassInteractionHandle, "CarName");
            carLicensePlateParameterHandle = hlaCore.getParameterHandle(addCarClassInteractionHandle, "LicensePlateCar");
            carColoParameterHandle = hlaCore.getParameterHandle(addCarClassInteractionHandle, "ColorCar");

        } catch (SaveInProgress | RestoreInProgress | NotConnected| RTIinternalError | FederateNotExecutionMember | FederateServiceInvocationsAreBeingReportedViaMOM e) {
            throw new RuntimeException(e);
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
            System.out.println("[COORD] Car Published");
        } catch (NameNotFound | NotConnected | FederateNotExecutionMember| InvalidObjectClassHandle | RTIinternalError e) {
            throw new RuntimeException(e);
        }
    }

    public void addCar(String nameCar, String licensePlate, String colorCar){
        try {
            ParameterHandleValueMap mapHandle = hlaCore.createParameterMap(3);
            byte [] nameCarParameter = hlaCore.encoderString(nameCar);
            mapHandle.put(carNameParameterHandle,nameCarParameter);
            byte [] license = hlaCore.encoderString(licensePlate);
            mapHandle.put(carLicensePlateParameterHandle,license);
            byte [] color = hlaCore.encoderString(colorCar);
            mapHandle.put(carColoParameterHandle,color);
            hlaCore.sendInteraction(addCarClassInteractionHandle, mapHandle);
        } catch(NullPointerException  | FederateNotExecutionMember | RestoreInProgress | NotConnected | RTIinternalError | SaveInProgress e){
            e.printStackTrace();}
    }



    public void createCar(Car car) {
        try {
            ObjectInstanceHandle carInstance = hlaCore.publishObject(objectClassCarHandle);
            carTracking.put(carInstance, car.getIdentifier());
            AttributeHandleValueMap mapAttributes = hlaCore.createAttributeMap(3);
            byte [] nameAttribute = hlaCore.encoderString(car.getName());
            mapAttributes.put(nameAttributeHandle,nameAttribute);
            byte [] licenseAttribute = hlaCore.encoderString(car.getLicensePlateNumber());
            mapAttributes.put(licensePlateNumberAttributeHandle, licenseAttribute);
            byte [] fuelTypeEnum = fuelTypeEnumCoder.encode(car.getFuelType(), hlaCore.getCoder());
            mapAttributes.put(fuelTypeAttributeHandle, fuelTypeEnum);
            hlaCore.updatesAttributes(carInstance, mapAttributes,null);
        } catch (ObjectClassNotPublished | ObjectClassNotDefined | SaveInProgress | RestoreInProgress |
                 FederateNotExecutionMember | NotConnected | RTIinternalError |
                 AttributeNotDefined | AttributeNotOwned | ObjectInstanceNotKnown e) {
            throw new RuntimeException(e);
        }
    }


   public void updateCar(Car car){
       try {
           AttributeHandleValueMap mapAttribute = hlaCore.createAttributeMap(2);
           byte [] positionAttribute = coderPosition.encode(car.getLocation(), hlaCore.getCoder());
           mapAttribute.put(attributePositionHandle, positionAttribute);
           byte [] fuelLevel = hlaCore.encoderInt((int)Math.round(car.getFuelLevel()));
           mapAttribute.put(attributeFuelLevelHandle,fuelLevel);
           hlaCore.updatesAttributes(carTracking.translate(car.getIdentifier()),mapAttribute,null);
       } catch (FederateNotExecutionMember | NotConnected | AttributeNotOwned | AttributeNotDefined | ObjectInstanceNotKnown | SaveInProgress |
                RestoreInProgress | RTIinternalError e) {
           throw new RuntimeException(e);
       }

    }


    public void removeCar(Car car){
        try {
            hlaCore.removeObjectInstance(carTracking.translate(car.getIdentifier()),null);
            carTracking.remove(car.getIdentifier());
        } catch (NotConnected | DeletePrivilegeNotHeld |ObjectInstanceNotKnown | SaveInProgress | RestoreInProgress | FederateNotExecutionMember | RTIinternalError e) {
            throw new RuntimeException(e);
        }
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
        } catch (FederateServiceInvocationsAreBeingReportedViaMOM| ObjectClassNotDefined| AttributeNotDefined e) {
            throw new RTIinternalError("HlaInterfaceFailure", e);
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