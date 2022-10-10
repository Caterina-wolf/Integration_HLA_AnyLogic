package it.cl.hla.core.interfaces;

import hla.rti1516e.*;
import hla.rti1516e.encoding.*;
import hla.rti1516e.exceptions.*;

import java.net.URL;

public interface HlaCore {

    /**
     * Connect to a CRC and join federation
     *
     * @param localSettingsDesignator The name to load settings for or "" to load default settings
     * @param federationName          Name of the federation to join
     */
    void start(String localSettingsDesignator, String federationName, URL url)
            throws FederateNotExecutionMember,
            RestoreInProgress,
            SaveInProgress,
            NotConnected,
            FederateServiceInvocationsAreBeingReportedViaMOM,
            RTIinternalError,
            ConnectionFailed;

    void start(String localSettingDesignator, String federation)
            throws FederateNotExecutionMember,
            RestoreInProgress,
            SaveInProgress,
            NotConnected,
            FederateServiceInvocationsAreBeingReportedViaMOM,
            RTIinternalError,
            ConnectionFailed;

    //Method to join a federation
    void join(String federateName, String federateType);

    //Resign, destroy and disconnect to federation
    void stop() throws RTIinternalError;

    //return Federate Name
    String getFederateName();

    //return coder created by EncoderFactory
    EncoderFactory getCoder();

    //return federation name
    String getFederationName();

    /*****************************************************************************
     * INTERACTIONS
     ****************************************************************************/

    // One federate publish an interaction on RTI
    void publishInteractions(InteractionClassHandle interactionClassHandle)
            throws FederateNotExecutionMember,
            RestoreInProgress,
            SaveInProgress,
            NotConnected,
            RTIinternalError,
            FederateServiceInvocationsAreBeingReportedViaMOM;


    //A federate subscribe to an interaction on RTI
    void subscribeInteractions(InteractionClassHandle interactionClassHandle)
            throws FederateNotExecutionMember,
            RestoreInProgress,
            SaveInProgress,
            NotConnected,
            RTIinternalError,
            FederateServiceInvocationsAreBeingReportedViaMOM;

    //return an handle of interaction type
    InteractionClassHandle getInteractionClassHandle(String nameHandle)
            throws FederateNotExecutionMember,
            RTIinternalError,
            SaveInProgress,
            RestoreInProgress,
            NotConnected,
            FederateServiceInvocationsAreBeingReportedViaMOM;

    //return an handle of parameter type
    ParameterHandle getParameterHandle(InteractionClassHandle interactionHandle, String nameParameter)
            throws FederateNotExecutionMember,
            SaveInProgress,
            RestoreInProgress,
            NotConnected,
            RTIinternalError,
            FederateServiceInvocationsAreBeingReportedViaMOM;



    /*******************************************************
     *  LISTNER
     *******************************************************/

    // add a listener for interaction
    void addInteractionListener(ListenerInteraction interactionListener);

    // add a listener for object
    void addObjectListener(ListenerObject objectListener);


    /*******************************************************
     *  DECODERS e CODER
     *******************************************************/
    // decode a byteArray to a float number
    float decodeFloat32(byte[] data) throws DecoderException;

    // decode a byteArray to an integer number
    int decodeInt32(byte[] data) throws DecoderException;

    // decode a byteArray to a String
    String decodeString32(byte[] data) throws DecoderException;

    // encode a String in a byteArray
    byte[] encoderString(String name);

    // encode an integer number in a byteArray
    byte[] encoderInt(int fuel);

    // encode a float number in a byteArray
    byte[] encoderFloat(float time);

    /*************************************
     * HASH MAPS and SET
     *************************************/

    // create an HashMap of handles of parameter type
    ParameterHandleValueMap createParameterMap(int capacity)
            throws FederateNotExecutionMember,
            NotConnected;

    // create an HashMap of handles of attribute type
    AttributeHandleValueMap createAttributeMap(int capacity)
            throws FederateNotExecutionMember,
            NotConnected;

    // create an Set of handles of attribute type
    AttributeHandleSet createAttributesSet(int capacity)
            throws FederateNotExecutionMember,
            NotConnected;


    /*******************************************************
     *  CALLBACK 
     *******************************************************/

    //Publish on the master that the interaction was received by federates
    void sendInteraction(InteractionClassHandle interactionHandle, ParameterHandleValueMap mapValue)
            throws NotConnected,
            FederateNotExecutionMember,
            RestoreInProgress,
            SaveInProgress,
            RTIinternalError;

    //Publish on the master that the object was received by federates
    void sendObject(ObjectClassHandle objectClassCarHandle, AttributeHandleSet attributeHandle)
            throws FederateNotExecutionMember,
            SaveInProgress,
            RestoreInProgress,
            NotConnected,
            RTIinternalError,
            AttributeNotDefined,
            ObjectClassNotDefined,
            FederateServiceInvocationsAreBeingReportedViaMOM;

    /*******************************************************
     *  OBJECT
     *******************************************************/

    // publish/register an object on RTI
    ObjectInstanceHandle publishObject(ObjectClassHandle objectCar)
            throws ObjectClassNotPublished,
            ObjectClassNotDefined,
            SaveInProgress,
            RestoreInProgress,
            FederateNotExecutionMember,
            NotConnected,
            RTIinternalError;


    // A federate subscribe an object published on RTI
    void subscribeObjectClass(ObjectClassHandle objectClassHandle, AttributeHandleSet attributeHandle)
            throws FederateNotExecutionMember,
            SaveInProgress,
            RestoreInProgress,
            NotConnected,
            RTIinternalError,
            AttributeNotDefined,
            ObjectClassNotDefined;


    // The attributes of an object are updated
    void updatesAttributes(ObjectInstanceHandle objectHandle, AttributeHandleValueMap mapAttributes, byte[] userSuppliedTag)
            throws AttributeNotOwned,
            AttributeNotDefined,
            ObjectInstanceNotKnown,
            SaveInProgress,
            RestoreInProgress,
            FederateNotExecutionMember,
            NotConnected,
            RTIinternalError;

    // The instance of an object is deleted
    void removeObjectInstance(ObjectInstanceHandle objectHandle, byte[] userSuppliedTag)
            throws DeletePrivilegeNotHeld,
            ObjectInstanceNotKnown,
            SaveInProgress,
            RestoreInProgress,
            FederateNotExecutionMember,
            NotConnected,
            RTIinternalError;

    // return an handle of object type
    ObjectClassHandle getObjectClassHandle(String nameOfObject)
            throws NameNotFound,
            FederateNotExecutionMember,
            NotConnected,
            RTIinternalError;

    // return an handle of attribute type
    AttributeHandle getAttributeHandle(ObjectClassHandle objectClassHandle, String objectName)
            throws InvalidObjectClassHandle,
            FederateNotExecutionMember,
            NotConnected,
            RTIinternalError;


    // return a String which is the name of on attribute of object
    String getNameAttribute(ObjectClassHandle objectClassHandle, AttributeHandle attributeHandle)
            throws FederateNotExecutionMember,
            InvalidAttributeHandle,
            AttributeNotDefined,
            NotConnected,
            InvalidObjectClassHandle,
            RTIinternalError;
}