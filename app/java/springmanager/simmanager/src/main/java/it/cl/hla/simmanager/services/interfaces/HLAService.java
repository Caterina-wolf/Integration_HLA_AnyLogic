package it.cl.hla.simmanager.services.interfaces;

import hla.rti1516e.InteractionClassHandle;
import hla.rti1516e.ParameterHandle;
import hla.rti1516e.ParameterHandleValueMap;
import hla.rti1516e.encoding.DecoderException;
import hla.rti1516e.exceptions.*;


import java.net.URL;


public interface HLAService {
    /**
     * Connect to a CRC and join federation
     *
     * @param localSettingsDesignator The name to load settings for or "" to load default settings
     * @param federationName          Name of the federation to join
     * @param federateName            The name you want for your federate
     */
    void start(String localSettingsDesignator, String federationName, String federateName, URL url)
            throws FederateNotExecutionMember,
            RestoreInProgress,
            SaveInProgress,
            NotConnected,
            FederateServiceInvocationsAreBeingReportedViaMOM,
            RTIinternalError,
            ConnectionFailed;

    /**
     * Resign and disconnect from CRC
     */
    void stop() throws RTIinternalError;

    /**
     *
     * @return value of FederateName
     */
    String getFederateName();

    /*****************************************************************************
     * Interactions
     ****************************************************************************/

    /**
     *
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
     *
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
     *
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
     *
     * @param interactionHandle interazione che sto considerando al momento
     * @param nameParameter nome del parametro da prendere
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
     * @param interactionListener an InteractionListener
     */
    void addInteractionListener(ListenerInteraction interactionListener);

    /*******************************************************
     *  DECODERS e CODER
     *******************************************************/

    /**
     *
     * @param data ByteArray da decodificare
     */
    float decodeFloat32(byte[] data) throws DecoderException;

    /**
     *
     * @param data ByteArray da decodificare
     */
    int decodeInt32(byte[] data) throws DecoderException;
    /**
     *
     * @param data ByteArray da decodificare
     */
    String decodeString32(byte[] data) throws DecoderException;

    /**
     * Codifica una stringa in byteArray
     * @param name stringa da codificare
     * @return byteArray
     */
    byte[] encoderString(String name);
    /**
     * Codifica un intero in byteArray
     * @param fuel intero da codificare
     * @return byteArray
     */

    byte[] encoderInt(int fuel);

    /**
     * Codifica un float in byteArray
     * @param time float da codificare
     * @return byteArray
     */

    byte[] encoderFloat(float time);


    /**
     * Crea una HashMap con i seguenti parametri:
     * @param capacity 2 per parameterHandle e byteArray
     */
    ParameterHandleValueMap createMap(int capacity) throws FederateNotExecutionMember, NotConnected;

    /*******************************************************
     *  CALLBACK
     *******************************************************/
    /**
     * Pubblica sia Scenario Loaded che Scenario Failure
     * @param interactionHandle interaction considered
     * @param mapValue parametro da selezionare per l'interazione
     */
    void sendInteraction(InteractionClassHandle interactionHandle,ParameterHandleValueMap mapValue)
            throws  NotConnected,
            FederateNotExecutionMember,
            RestoreInProgress,
            SaveInProgress,
            RTIinternalError;

    void join(String federateName);


}


