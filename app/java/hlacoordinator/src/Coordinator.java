package src;

import hla.rti1516e.*;
import hla.rti1516e.encoding.DecoderException;
import hla.rti1516e.exceptions.*;
import src.hlamodule.*;

public class Coordinator{
    private HLAInterface hlaInterface = new HLAImpl();
    private InteractionClassHandle startInteractionClassHandle;
    private InteractionClassHandle stopInteractionClassHandle;
    private InteractionClassHandle loadScenarioClassHandle;
    private ParameterHandle startTimeScaleFactorParameterHandle;
    private InteractionClassHandle scenarioLoadedInteractionClassHandle;
    private InteractionClassHandle scenarioLoadFailureInteractionClassHandle;
    private ParameterHandle scenarioLoadedFederateNameParameterHandle;
    private ParameterHandle initialFuelAmountParameterHandle;
    private ParameterHandle scenarioParameterHandle;
    private ParameterHandle scenarioFailureFederateNameParameterHandle;
    private ParameterHandle scenarioFailureErrorMessage;
    private CarSimulator anyLogic ;
    public boolean running;
    public float timeScale;
    public int initialFuel;

    /**
     * Costruttore di default
     */
    public Coordinator(){
    }

    /**
     * Costruttore con parametro
     * @param anylogic la variabile che comunica con il software AnyLogic grazie all'interfaccia CarSimulator
     */
    public Coordinator(CarSimulator anylogic){
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
                hlaInterface.stop();
            } catch (RTIinternalError exception) {
                exception.printStackTrace();
            }
    }
    /**
     * chiama l'interfaccia per fare il join della FedExec inoltre gestisce i due handle per lo start e lo stop
    */
    public void Start() throws FederateNotExecutionMember, RestoreInProgress, FederateServiceInvocationsAreBeingReportedViaMOM, NotConnected, SaveInProgress, ConnectionFailed {

        hlaInterface.addInteractionListener(new ListenerInteraction() {
            @Override
            public void receiveInteraction(InteractionClassHandle interactionHandle, ParameterHandleValueMap mapValue){
                if (interactionHandle.equals(startInteractionClassHandle)) {
                    try {
                        float timeScaleFactor = hlaInterface.decodeFloat32(mapValue.get(startTimeScaleFactorParameterHandle));
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
            hlaInterface.start("Tutorial", "HLA Tutorial", "AnyLogicCar");
            startInteractionClassHandle = hlaInterface.getInteractionClassHandle("Start");
            hlaInterface.subscribeInteractions(startInteractionClassHandle);
            startTimeScaleFactorParameterHandle = hlaInterface.getParameterHandle(startInteractionClassHandle, "TimeScaleFactor");

            stopInteractionClassHandle = hlaInterface.getInteractionClassHandle("Stop");
            hlaInterface.subscribeInteractions(stopInteractionClassHandle);

            scenarioHandling();

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
        hlaInterface.addInteractionListener(new ListenerInteraction() {
            @Override
            public void receiveInteraction(InteractionClassHandle interactionHandle, ParameterHandleValueMap mapValue){
                if (interactionHandle.equals(loadScenarioClassHandle)) {
                  try {
                      String scenarioName = hlaInterface.decodeString32(mapValue.get(scenarioParameterHandle));
                      System.out.println("[COORD] Scenario chosen: " + scenarioName);

                      int fuelInitial = hlaInterface.decodeInt32(mapValue.get(initialFuelAmountParameterHandle));
                      setInitialFuel(fuelInitial);

                      anyLogic.loadScenario();



                  } catch (DecoderException e) {
                      e.printStackTrace();
                  }
                } else if (interactionHandle.equals(scenarioLoadedInteractionClassHandle)) {

                  try {
                      hlaInterface.decodeString32(mapValue.get(scenarioLoadedFederateNameParameterHandle));
                  } catch (DecoderException e) {
                      throw new RuntimeException(e);
                  }
                } else if (interactionHandle.equals(scenarioLoadFailureInteractionClassHandle)) {
                    try {
                        hlaInterface.decodeString32(mapValue.get(scenarioFailureFederateNameParameterHandle));
                        hlaInterface.decodeString32(mapValue.get(scenarioFailureErrorMessage));
                    } catch (DecoderException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });

        try {
            loadScenarioClassHandle = hlaInterface.getInteractionClassHandle("LoadScenario");
            hlaInterface.subscribeInteractions(loadScenarioClassHandle);
            scenarioParameterHandle = hlaInterface.getParameterHandle(loadScenarioClassHandle, "ScenarioName");
            initialFuelAmountParameterHandle = hlaInterface.getParameterHandle(loadScenarioClassHandle, "InitialFuelAmount");

            scenarioLoadedInteractionClassHandle = hlaInterface.getInteractionClassHandle("ScenarioLoaded");
            hlaInterface.publishInteractions(scenarioLoadedInteractionClassHandle);
            scenarioLoadedFederateNameParameterHandle = hlaInterface.getParameterHandle(scenarioLoadedInteractionClassHandle, "FederateName");

            scenarioLoadFailureInteractionClassHandle = hlaInterface.getInteractionClassHandle("ScenarioLoadFailure");
            hlaInterface.publishInteractions(scenarioLoadFailureInteractionClassHandle);
            scenarioFailureFederateNameParameterHandle = hlaInterface.getParameterHandle(scenarioLoadFailureInteractionClassHandle, "FederateName");
            scenarioFailureErrorMessage =  hlaInterface.getParameterHandle(scenarioLoadFailureInteractionClassHandle, "ErrorMessage");

        } catch(RTIinternalError e) {
            System.out.println("Could not connect to the RTI with local settings designator");
            e.printStackTrace();
        }

    }
    /**
     * callback del Coordinator sul Manager per il loading dello Scenario
     */
    public void publishScenarioLoaded() throws FederateNotExecutionMember, RestoreInProgress, NotConnected, RTIinternalError, SaveInProgress {
        try {
            ParameterHandleValueMap mapHandle = hlaInterface.createMap(2);
            byte [] nameParameter = hlaInterface.encoder(hlaInterface.getFederateName());
            mapHandle.put(scenarioLoadedFederateNameParameterHandle,nameParameter);
            hlaInterface.sendInteraction(scenarioLoadedInteractionClassHandle, mapHandle);
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
           ParameterHandleValueMap mapHandle = hlaInterface.createMap(2);
           byte [] nameParameter = hlaInterface.encoder(hlaInterface.getFederateName());
           byte [] error = hlaInterface.encoder(errorMessage);
           mapHandle.put(scenarioFailureFederateNameParameterHandle, nameParameter);
           mapHandle.put(scenarioFailureErrorMessage, error);
           hlaInterface.sendInteraction(scenarioLoadFailureInteractionClassHandle,mapHandle);
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



