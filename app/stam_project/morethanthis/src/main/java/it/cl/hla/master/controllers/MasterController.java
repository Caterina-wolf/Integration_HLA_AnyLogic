package it.cl.hla.master.controllers;

import hla.rti1516e.exceptions.*;

import it.cl.hla.master.services.interfaces.MasterService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


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
    @GetMapping("/joint")
    void joint(){
        service.joint();
    }

    //DetectPeople
    @PostMapping("/people")
    void detectPeople(@RequestBody Integer people)throws FederateNotExecutionMember, RestoreInProgress, NotConnected, RTIinternalError, SaveInProgress {
        service.detectCrowds(people);
        System.out.println("People detected: " + people);
    }

    //destruction of FedExec and disconnection from pitchRTI
    @GetMapping("/quit")
    void exit(){
        service.exit();
    }

}
