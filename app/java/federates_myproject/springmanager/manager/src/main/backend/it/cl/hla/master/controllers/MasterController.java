package it.cl.hla.master.controllers;

import hla.rti1516e.exceptions.*;

import it.cl.hla.master.DTO.CarDTO;
import it.cl.hla.master.services.interfaces.MasterService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(maxAge = 3600)
@RestController
public class MasterController {

    @Autowired
    private MasterService service;



    //Connect to pitchRTI
    //create the FedExec
    //join the federate to pitch
    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/init")
    void init(){
        service.init();
    }

    //inject a Car
    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("/inject")
    void injectCar(@RequestBody CarDTO carDto) throws FederateNotExecutionMember, RestoreInProgress, NotConnected, RTIinternalError, SaveInProgress {
        service.injectCar(carDto.getName(), carDto.getLicensePlate(), carDto.getColor());
        System.out.println("new Car is: ");
        System.out.println("{ 'name': " + carDto.getName());
        System.out.println("  'license': " + carDto.getLicensePlate());
        System.out.println("  'color': " + carDto.getColor() + " }");
    }

    //Load scenario chosen
    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/scenario/{scenarioName}/{initialFuelLevel}")
    void selectScenario(@PathVariable String scenarioName, @PathVariable Integer initialFuelLevel) throws FederateNotExecutionMember, RestoreInProgress, NotConnected, RTIinternalError, SaveInProgress {
        service.loadScenario(scenarioName, initialFuelLevel);
        System.out.println("You have chosen " + scenarioName + " scenario.");
        System.out.println("The initial level of fuel is " + initialFuelLevel);
    }

    //start simulation
    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/start/{timeScaleFactor}")
    void startSimulation(@PathVariable float timeScaleFactor) throws FederateNotExecutionMember, NotConnected, RestoreInProgress, RTIinternalError, SaveInProgress {
        service.sendStart(timeScaleFactor);
    }

    //stop simulation
    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/stop")
    void stopSimulation() throws FederateNotExecutionMember, RestoreInProgress, NotConnected, RTIinternalError, SaveInProgress {
        service.sendStop();
    }

    //destruction of FedExec and disconnection from pitchRTI
    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/quit")
    void exit(){
        service.exit();
    }

}
