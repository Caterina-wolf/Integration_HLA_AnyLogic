package it.cl.hla.master.beans;

import hla.rti1516e.ParameterHandle;
import hla.rti1516e.InteractionClassHandle;
import org.springframework.stereotype.Component;

@Component
public class HandlesBean {

    private InteractionClassHandle detectCrowdsClassHandle;
    private ParameterHandle peopleHandle;



    public InteractionClassHandle getDetectCrowdsClassHandle() {
        return detectCrowdsClassHandle;
    }

    public void setDetectCrowdsClassHandle(InteractionClassHandle detectCrowdsClassHandle) {
        this.detectCrowdsClassHandle = detectCrowdsClassHandle;
    }



    public ParameterHandle getPeopleHandle() {
        return peopleHandle;
    }

    public void setPeopleHandle(ParameterHandle peopleHandle) {
        this.peopleHandle = peopleHandle;
    }

}