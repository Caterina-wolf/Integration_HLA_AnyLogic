package it.cl.hla.simmanager.services.interfaces;
//logica hla in master.main e master.java

import hla.rti1516e.exceptions.*;

public interface SimulationManagerService {

    void init();

    void exit();

    void joint();

    void loadScenario(String scenarioName, Integer initialFuelLevel) throws
            FederateNotExecutionMember,
            NotConnected,
            RestoreInProgress,
            SaveInProgress,
            RTIinternalError;

   // void scenarioPublished();
   // void scenarioNotPublished(String errorMessage);

    void sendStart(float timeScaleFactor) throws FederateNotExecutionMember, NotConnected, RestoreInProgress, RTIinternalError, SaveInProgress;
    void sendStop() throws FederateNotExecutionMember, NotConnected, RestoreInProgress, RTIinternalError, SaveInProgress;


}
