package it.cl.hla.master.services.interfaces;

import hla.rti1516e.exceptions.*;

public interface MasterService {
    void init();

    void exit();


    void loadScenario(String scenarioName, Integer initialFuelLevel) throws
            FederateNotExecutionMember,
            NotConnected,
            RestoreInProgress,
            SaveInProgress,
            RTIinternalError;

    void sendStart(float timeScaleFactor)
            throws FederateNotExecutionMember,
            NotConnected,
            RestoreInProgress,
            RTIinternalError,
            SaveInProgress;

    void sendStop()
            throws FederateNotExecutionMember,
            NotConnected,
            RestoreInProgress,
            RTIinternalError,
            SaveInProgress;


}
