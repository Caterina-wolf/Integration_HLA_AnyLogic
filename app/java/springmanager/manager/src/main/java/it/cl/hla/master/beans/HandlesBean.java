package it.cl.hla.master.beans;

import hla.rti1516e.ParameterHandle;
import hla.rti1516e.InteractionClassHandle;
import org.springframework.stereotype.Component;

@Component
public class HandlesBean {

    private InteractionClassHandle startInteractionClassHandle;
    private InteractionClassHandle stopInteractionClassHandle;
    private InteractionClassHandle loadScenarioClassHandle;
    private InteractionClassHandle scenarioLoadedInteractionClassHandle;
    private InteractionClassHandle scenarioLoadFailureInteractionClassHandle;
    private InteractionClassHandle scenarioFailureErrorMessage;

    private ParameterHandle startTimeScaleFactorParameterHandle;
    private ParameterHandle initialFuelAmountParameterHandle;
    private ParameterHandle scenarioParameterHandle;



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



    public InteractionClassHandle getScenarioLoadedInteractionClassHandle() {
        return scenarioLoadedInteractionClassHandle;
    }

    public void setScenarioLoadedInteractionClassHandle(InteractionClassHandle scenarioLoadedInteractionClassHandle) {
        this.scenarioLoadedInteractionClassHandle = scenarioLoadedInteractionClassHandle;
    }



    public InteractionClassHandle getScenarioLoadFailureInteractionClassHandle() {
        return scenarioLoadFailureInteractionClassHandle;
    }

    public void setScenarioLoadFailureInteractionClassHandle(InteractionClassHandle scenarioLoadedFailureInteractionClassHandle) {
        this.scenarioLoadFailureInteractionClassHandle = scenarioLoadedFailureInteractionClassHandle;
    }



    public InteractionClassHandle getScenarioFailureErrorMessage() {
        return scenarioFailureErrorMessage;
    }

    public void setScenarioFailureErrorMessage(InteractionClassHandle scenarioFailureErrorMessage) {
        this.scenarioFailureErrorMessage = scenarioFailureErrorMessage;
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




}
