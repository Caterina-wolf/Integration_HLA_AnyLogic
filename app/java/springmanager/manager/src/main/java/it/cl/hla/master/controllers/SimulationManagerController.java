package it.cl.hla.master.controllers;

import hla.rti1516e.exceptions.*;

import it.cl.hla.master.services.interfaces.MasterService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


//funzioni ENDPOINT
@RestController
public class SimulationManagerController {

    @Autowired
    private MasterService simulationManagerService;

    //si connette al RTI e crea la FedExec
    @GetMapping("/init")
    void init(){
        simulationManagerService.init();
    }

    //fa il join alla FedExec
    @GetMapping("/joint")
    void joint(){
        simulationManagerService.joint();
    }

    //Select Scenario
    @GetMapping("/scenario/{scenarioName}/{initialFuelLevel}")
    void selectScenario(@PathVariable String scenarioName, @PathVariable Integer initialFuelLevel) throws FederateNotExecutionMember, RestoreInProgress, NotConnected, RTIinternalError, SaveInProgress {
        simulationManagerService.loadScenario(scenarioName, initialFuelLevel);
        System.out.println("You has chosen " + scenarioName + " scenario.");
        System.out.println("The initial level of fuel is " + initialFuelLevel);
    }

    //start simulation
    @GetMapping("/start/{timeScaleFactor}")
    void startSimulation(@PathVariable float timeScaleFactor) throws FederateNotExecutionMember, NotConnected, RestoreInProgress, RTIinternalError, SaveInProgress {
        simulationManagerService.sendStart(timeScaleFactor);
    }

    @GetMapping("/stop")
    void getHandles() throws FederateNotExecutionMember, RestoreInProgress, NotConnected, RTIinternalError, SaveInProgress {
        simulationManagerService.sendStop();
    }

    //distrugge la FedExec e si disconnette dal RTI
    @GetMapping("/quit")
    void exit(){
        simulationManagerService.exit();
    }

}
