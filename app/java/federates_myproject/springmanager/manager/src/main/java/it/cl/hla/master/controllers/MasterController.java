package it.cl.hla.master.controllers;

import hla.rti1516e.exceptions.*;

import it.cl.hla.master.services.interfaces.MasterService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class MasterController {

    @Autowired
    private MasterService service;

    //connection to pitchRTI and creation of FedExec
    @GetMapping("/init")
    void init(){
        service.init();
    }

    //join to FedExec
    @GetMapping("/join")
    void joint(){
        service.join();
    }

    //Load scenario chosen
    @GetMapping("/scenario/{scenarioName}/{initialFuelLevel}")
    void selectScenario(@PathVariable String scenarioName, @PathVariable Integer initialFuelLevel) throws FederateNotExecutionMember, RestoreInProgress, NotConnected, RTIinternalError, SaveInProgress {
        service.loadScenario(scenarioName, initialFuelLevel);
        System.out.println("You has chosen " + scenarioName + " scenario.");
        System.out.println("The initial level of fuel is " + initialFuelLevel);
    }

    //start simulation
    @GetMapping("/start/{timeScaleFactor}")
    void startSimulation(@PathVariable float timeScaleFactor) throws FederateNotExecutionMember, NotConnected, RestoreInProgress, RTIinternalError, SaveInProgress {
        service.sendStart(timeScaleFactor);
    }

    //stop simulation
    @GetMapping("/stop")
    void stopSimulation() throws FederateNotExecutionMember, RestoreInProgress, NotConnected, RTIinternalError, SaveInProgress {
        service.sendStop();
    }

    //destruction of FedExec and disconnection from pitchRTI
    @GetMapping("/quit")
    void exit(){
        service.exit();
    }

}
