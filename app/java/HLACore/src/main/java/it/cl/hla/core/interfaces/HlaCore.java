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

    /**
     * Federate join the FedExec
     *
     * @param federateName
     */
    void join(String federateName, String federateType);

    /**
     * Resign and disconnect from CRC
     */
    void stop() throws RTIinternalError;

    /**
     * @return value of FederateName
     */
    String getFederateName();

    /**
     * @return value of federation Name
     */
    String getFederationName();

    /*****************************************************************************
     * Interactions
     ****************************************************************************/

    /**
     * @param interactionClassHandle handle per l'interazione publish
     */
    void publishInteractions(InteractionClassHandle interactionClassHandle)
            throws FederateNotExecutionMember,
            RestoreInProgress,
            SaveInProgress,
            NotConnected,
            RTIinternalError,
            FederateServiceInvocationsAreBeingReportedViaMOM;


    /**
     * @param interactionClassHandle handle per l'interazione subscribe
     */
    void subscribeInteractions(InteractionClassHandle interactionClassHandle)
            throws FederateNotExecutionMember,
            RestoreInProgress,
            SaveInProgress,
            NotConnected,
            RTIinternalError,
            FederateServiceInvocationsAreBeingReportedViaMOM;


    /**
     * @param nameHandle nome del handle da selezionare per l'interazione
     */
    InteractionClassHandle getInteractionClassHandle(String nameHandle)
            throws FederateNotExecutionMember,
            RTIinternalError,
            SaveInProgress,
            RestoreInProgress,
            NotConnected,
            FederateServiceInvocationsAreBeingReportedViaMOM;

    /**
     * @param interactionHandle interazione che sto considerando al momento
     * @param nameParameter     nome del parametro da prendere
     */
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

    /**
     * Add an ListenerInteraction to receive notifications when Interactions are received
     *
     * @param interactionListener an InteractionListener
     */
    void addInteractionListener(ListenerInteraction interactionListener);

    /*******************************************************
     *  DECODERS e CODER
     *******************************************************/
    /**
     * @param data ByteArray da decodificare
     */
    float decodeFloat32(byte[] data) throws DecoderException;

    /**
     * @param data ByteArray da decodificare
     */
    int decodeInt32(byte[] data) throws DecoderException;

    /**
     * @param data ByteArray da decodificare
     */
    String decodeString32(byte[] data) throws DecoderException;

    /**
     * Codifica una stringa in byteArray
     *
     * @param name stringa da codificare
     * @return byteArray
     */
    byte[] encoderString(String name);

    /**
     * Codifica un intero in byteArray
     *
     * @param fuel intero da codificare
     * @return byteArray
     */

    byte[] encoderInt(int fuel);

    /**
     * Codifica un float in byteArray
     *
     * @param time float da codificare
     * @return byteArray
     */

    byte[] encoderFloat(float time);

    DataElement encoderFloat64BE(double latLong);

    byte[] encoderDouble(double lat, double longit);

    Iterable<Double> decoderFixedRecord(byte[] bytes) throws DecoderException;

    /**
     * Crea una HashMap con i seguenti parametri:
     *
     * @param capacity 2 per parameterHandle e byteArray
     */
    ParameterHandleValueMap createParameterMap(int capacity) throws FederateNotExecutionMember, NotConnected;

    /*******************************************************
     *  CALLBACK
     *******************************************************/
    /**
     * Pubblica sia Scenario Loaded che Scenario Failure
     *
     * @param interactionHandle interaction considered
     * @param mapValue          parametro da selezionare per l'interazione
     */
    void sendInteraction(InteractionClassHandle interactionHandle, ParameterHandleValueMap mapValue)
            throws NotConnected,
            FederateNotExecutionMember,
            RestoreInProgress,
            SaveInProgress,
            RTIinternalError;
    /*******************************************************
     *  OBJECT
     *******************************************************/


    ObjectClassHandle getObjectClassHandle(String nameOfObject)
            throws NameNotFound,
            FederateNotExecutionMember,
            NotConnected,
            RTIinternalError;

    AttributeHandle getAttributeHandle(ObjectClassHandle objectClassHandle, String objectName)
            throws InvalidObjectClassHandle,
            FederateNotExecutionMember,
            NotConnected,
            RTIinternalError;


    String getNameAttribute(ObjectClassHandle objectClassHandle, AttributeHandle attributeHandle)
            throws FederateNotExecutionMember,
            InvalidAttributeHandle,
            AttributeNotDefined,
            NotConnected,
            InvalidObjectClassHandle,
            RTIinternalError;

    ObjectInstanceHandle registerObject(ObjectClassHandle objectCar)
            throws ObjectClassNotPublished,
            ObjectClassNotDefined,
            SaveInProgress,
            RestoreInProgress,
            FederateNotExecutionMember,
            NotConnected,
            RTIinternalError;


    void updatesAttributes(ObjectInstanceHandle objectHandle, AttributeHandleValueMap mapAttributes, byte[] data)
        throws AttributeNotOwned,
            AttributeNotDefined,
            ObjectInstanceNotKnown,
            SaveInProgress,
            RestoreInProgress,
            FederateNotExecutionMember,
            NotConnected,
            RTIinternalError;
    void publishObjectClass(ObjectClassHandle objectClassCarHandle, AttributeHandleSet attributeHandle)
            throws FederateNotExecutionMember,
            SaveInProgress,
            RestoreInProgress,
            NotConnected,
            RTIinternalError,
            AttributeNotDefined,
            ObjectClassNotDefined,
            FederateServiceInvocationsAreBeingReportedViaMOM;

    void subscribeObjectClass(ObjectClassHandle objectClassHandle, AttributeHandleSet attributeHandle)
        throws FederateNotExecutionMember,
        SaveInProgress,
        RestoreInProgress,
        NotConnected,
        RTIinternalError,
        AttributeNotDefined,
        ObjectClassNotDefined;

    void removeObjectInstance(ObjectInstanceHandle objectHandle, byte[] data)
            throws DeletePrivilegeNotHeld,
            ObjectInstanceNotKnown,
            SaveInProgress,
            RestoreInProgress,
            FederateNotExecutionMember,
            NotConnected,
            RTIinternalError;


    AttributeHandleValueMap createAttributeMap(int capacity)
            throws FederateNotExecutionMember,
            NotConnected;
    void addObjectListener(ListenerObject objectListener);
}