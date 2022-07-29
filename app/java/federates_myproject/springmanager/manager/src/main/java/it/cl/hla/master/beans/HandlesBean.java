package it.cl.hla.master.beans;

import hla.rti1516e.ParameterHandle;
import hla.rti1516e.InteractionClassHandle;
import org.springframework.stereotype.Component;

@Component
public class HandlesBean {

    private InteractionClassHandle startInteractionClassHandle;
    private InteractionClassHandle stopInteractionClassHandle;
    private InteractionClassHandle loadScenarioClassHandle;
    private InteractionClassHandle addCarClassInteractionHandle;


    private ParameterHandle startTimeScaleFactorParameterHandle;
    private ParameterHandle initialFuelAmountParameterHandle;
    private ParameterHandle scenarioParameterHandle;
    private ParameterHandle carNameParameterHandle;
    private ParameterHandle carColoParameterHandle;
    private ParameterHandle carLicensePlateParameterHandle;




    public InteractionClassHandle getStartInteractionClassHandle() {
        return this.startInteractionClassHandle;
    }

    public void setStartInteractionClassHandle(InteractionClassHandle startInteractionClassHandle) {
        this.startInteractionClassHandle = startInteractionClassHandle;
    }



    public InteractionClassHandle getStopInteractionClassHandle() {
        return this.stopInteractionClassHandle;
    }

    public void setStopInteractionClassHandle(InteractionClassHandle stopInteractionClassHandle) {
        this.stopInteractionClassHandle = stopInteractionClassHandle;
    }



    public InteractionClassHandle getLoadScenarioClassHandle() {
        return this.loadScenarioClassHandle;
    }

    public void setLoadScenarioClassHandle(InteractionClassHandle loadScenarioClassHandle) {
        this.loadScenarioClassHandle = loadScenarioClassHandle;
    }



    public InteractionClassHandle getAddCarClassInteractionHandle() {
        return addCarClassInteractionHandle;
    }

    public void setAddCarClassInteractionHandle(InteractionClassHandle addCarClassInteractionHandle) {
        this.addCarClassInteractionHandle = addCarClassInteractionHandle;
    }




    public ParameterHandle getStartTimeScaleFactorParameterHandle() {
        return this.startTimeScaleFactorParameterHandle;
    }

    public void setStartTimeScaleFactorParameterHandle(ParameterHandle startTimeScaleFactorParameterHandle) {
        this.startTimeScaleFactorParameterHandle = startTimeScaleFactorParameterHandle;
    }



    public ParameterHandle getInitialFuelAmountParameterHandle() {
        return this.initialFuelAmountParameterHandle;
    }

    public void setInitialFuelAmountParameterHandle(ParameterHandle initialFuelAmountParameterHandle) {
        this.initialFuelAmountParameterHandle = initialFuelAmountParameterHandle;
    }



    public ParameterHandle getScenarioParameterHandle() {
        return this.scenarioParameterHandle;
    }

    public void setScenarioParameterHandle(ParameterHandle scenarioParameterHandle) {
        this.scenarioParameterHandle = scenarioParameterHandle;
    }



    public ParameterHandle getCarNameParameterHandle() {
        return carNameParameterHandle;
    }

    public void setCarNameParameterHandle(ParameterHandle carNameParameterHandle) {
        this.carNameParameterHandle = carNameParameterHandle;
    }



    public ParameterHandle getCarColoParameterHandle() {
        return carColoParameterHandle;
    }

    public void setCarColoParameterHandle(ParameterHandle carColoParameterHandle) {
        this.carColoParameterHandle = carColoParameterHandle;
    }



    public ParameterHandle getCarLicensePlateParameterHandle() {
        return carLicensePlateParameterHandle;
    }

    public void setCarLicensePlateParameterHandle(ParameterHandle carLicensePlateParameterHandle) {
        this.carLicensePlateParameterHandle = carLicensePlateParameterHandle;
    }



}
