package it.cl.hla.master.services.interfaces;

import hla.rti1516e.exceptions.*;

public interface MasterService {
    void init();

    void exit();

    void joint();

    void detectCrowds(Integer people) throws FederateNotExecutionMember, NotConnected, RestoreInProgress, SaveInProgress, RTIinternalError;
}
