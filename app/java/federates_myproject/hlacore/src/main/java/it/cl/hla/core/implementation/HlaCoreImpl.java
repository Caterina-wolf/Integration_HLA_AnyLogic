package it.cl.hla.core.implementation;

import hla.rti1516e.*;
import hla.rti1516e.encoding.*;
import hla.rti1516e.exceptions.*;

import it.cl.hla.core.interfaces.*;

import java.net.URL;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class HlaCoreImpl extends NullFederateAmbassador implements HlaCore {

    //Ambassador della libreria prti1516e.jar
    private RTIambassador ambassador;

    //Nome Federate
    private String federateName = "";

    //Nome Federation
    private String federationName;

    //Risorsa che punta al FOM
    private URL urlFOM;

    //Coder da usare per costruire encoder e decoder dei tipi di dato
    private EncoderFactory coder;

    // Factory della mappa dei parametri delle interazioni
    private ParameterHandleValueMapFactory mapHandleCreator;

    // Factory della mappa degli Attributi di un oggetto
    private AttributeHandleValueMapFactory mapAttributeCreator;

    private AttributeHandleSetFactory setAttributes;

    // Factory di dataElement
    private DataElementFactory dataConvertor;

    // ArrayList dei listener per le interazioni
    private List<ListenerInteraction> listenerInteractions = new CopyOnWriteArrayList<>();

    //ArrayList dei listener per lo/gli oggetto/i
    private List<ListenerObject> listenerObjects = new CopyOnWriteArrayList<>();

    /**
     * Costruttore di default
     */
    public void HLACore() {
    }

    /**
     * @return Federate's name
     */
    @Override
    public String getFederateName() {
        return federateName;
    }

    /**
     *
     * @return coder costruito dalla EncoderFactory
     */
    @Override
    public EncoderFactory getCoder() {
        return coder;
    }

    /**
     *
     * @return Federation's name
     */
    @Override
    public String getFederationName() {
        return federationName;
    }

    /**
     * Connect to RTI and creation of Federation
     * @param localSettingsDesignator The name to load settings for or "" to load default settings
     * @param federationName          Name of the federation to join
     */
    @Override
    public void start(String localSettingsDesignator, String federationName, URL urlFOM) throws RTIinternalError, ConnectionFailed, NotConnected {
        RtiFactory rtiFactory = RtiFactoryFactory.getRtiFactory();
        ambassador = rtiFactory.getRtiAmbassador();
        coder = rtiFactory.getEncoderFactory();


        try {
            ambassador.connect(this, CallbackModel.HLA_IMMEDIATE, localSettingsDesignator);
        } catch (AlreadyConnected ignored) {
        } catch (UnsupportedCallbackModel e) {
            throw new RTIinternalError("HlaInterfaceFailure", e);
        } catch (InvalidLocalSettingsDesignator invalidLocalSettingsDesignator) {
            invalidLocalSettingsDesignator.printStackTrace();
        } catch (CallNotAllowedFromWithinCallback e) {
            throw new RuntimeException(e);
        }

        this.urlFOM = urlFOM;
        this.federationName = federationName;

        if (urlFOM != null) {
            try {
                ambassador.destroyFederationExecution(federationName);
            } catch (FederationExecutionDoesNotExist | FederatesCurrentlyJoined ignored) {
            }


            try {
                ambassador.createFederationExecution(federationName, urlFOM);
            } catch (FederationExecutionAlreadyExists ignored) {
            } catch (ErrorReadingFDD | InconsistentFDD | CouldNotOpenFDD e) {
                throw new RuntimeException(e);
            }

        }
    }

    @Override
    public void start(String localSettingsDesignator, String federationName) throws RTIinternalError, ConnectionFailed, NotConnected {
        this.start(localSettingsDesignator, federationName, null);
    }


    /**
     * Join to Federation
     * @param federateName federate is going to join
     * @param federateType typo of federate is going to join
     */
    @Override
    public void join(String federateName, String federateType) {
        try {
            this.federateName = federateName;
            boolean joined = false;
            while (!joined) {
                try {
                    ambassador.joinFederationExecution(federateName, federateType, federationName);
                    joined = true;
                } catch (RestoreInProgress | NotConnected | RTIinternalError | SaveInProgress e) {
                    System.out.println("[HLA] Federate already in use!");
                    e.printStackTrace();
                }
            }
        } catch (FederateAlreadyExecutionMember ignored) {
        } catch (CouldNotCreateLogicalTimeFactory | FederationExecutionDoesNotExist |
                 CallNotAllowedFromWithinCallback | FederateNameAlreadyInUse e) {
            try {
                throw new RTIinternalError("HlaInterfaceFailure", e);
            } catch (RTIinternalError ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    /**
     * Resign the federation
     * Destroy the federation
     * Disconnect from RTI
     */
    @Override
    public void stop() throws RTIinternalError {
        try {
            try {
                ambassador.resignFederationExecution(ResignAction.CANCEL_THEN_DELETE_THEN_DIVEST);
            } catch (FederateNotExecutionMember ignored) {
            } catch (FederateOwnsAttributes | OwnershipAcquisitionPending | CallNotAllowedFromWithinCallback |
                     InvalidResignAction e) {
                throw new RTIinternalError("HlaInterfaceFailure", e);
            }

            if (federationName != null) {
                try {
                    ambassador.destroyFederationExecution(federationName);
                } catch (FederatesCurrentlyJoined | FederationExecutionDoesNotExist ignored) {
                }
            }

            try {
                ambassador.disconnect();
            } catch (FederateIsExecutionMember | CallNotAllowedFromWithinCallback e) {
                throw new RTIinternalError("HlaInterfaceFailure", e);
            }
        } catch (NotConnected ignored) {
            System.out.println("[HLA] NotConnected ignored");
        }
    }


    /********************************************************
     * DECODERS
     ********************************************************/

    /**
     *
     * @param data ByteArray da decodificare
     * @return float number
     * @throws DecoderException
     */
    @Override
    public float decodeFloat32(byte[] data) throws DecoderException {
        HLAfloat32BE coderFloat = coder.createHLAfloat32BE();
        coderFloat.decode(data);
        return coderFloat.getValue();
    }

    /**
     *
     * @param data ByteArray da decodificare
     * @return int number
     * @throws DecoderException
     */
    @Override
    public int decodeInt32(byte[] data) throws DecoderException {
        HLAinteger32BE coderInt = coder.createHLAinteger32BE();
        coderInt.decode(data);
        return coderInt.getValue();
    }

    /**
     *
     * @param data ByteArray da decodificare
     * @return string line
     * @throws DecoderException
     */
    @Override
    public String decodeString32(byte[] data) throws DecoderException {
        HLAunicodeString coderString = coder.createHLAunicodeString();
        coderString.decode(data);
        return coderString.getValue();
    }

    /*******************************************
     * ENCODERS
     *******************************************/

    /**
     *
     * @param name stringa da codificare
     * @return byte Array
     */
    @Override
    public byte[] encoderString(String name) {
        HLAunicodeString coderString = coder.createHLAunicodeString();
        coderString.setValue(name);
        return coderString.toByteArray();
    }

    /**
     *
     * @param fuel intero da codificare
     * @return byte Array
     */
    @Override
    public byte[] encoderInt(int fuel) {
        HLAinteger32BE coderInt = coder.createHLAinteger32BE();
        coderInt.setValue(fuel);
        return coderInt.toByteArray();
    }

    /**
     *
     * @param time float da codificare
     * @return byte Array
     */
    @Override
    public byte[] encoderFloat(float time) {
        HLAfloat32BE coderFloat = coder.createHLAfloat32BE();
        coderFloat.setValue(time);
        return coderFloat.toByteArray();
    }



    /******************************************
     * INTERACTIONS
     ******************************************/

    /**
     * Crea una Hash map di dimensione uguale a capacity,
     * da usare per allocare i parametri degli handle dello scenario.
     *
     * @return mapHandle HashMap dei parametri
     */
    @Override
    public ParameterHandleValueMap createParameterMap(int capacity) throws FederateNotExecutionMember, NotConnected {
        mapHandleCreator = ambassador.getParameterHandleValueMapFactory();
        return mapHandleCreator.create(capacity);
    }


    /**
     * @param nameHandle interaction class handle per l'interazione
     */
    @Override
    public InteractionClassHandle getInteractionClassHandle(String nameHandle) throws FederateNotExecutionMember, RTIinternalError, NotConnected {
        try {
            return ambassador.getInteractionClassHandle(nameHandle);
        } catch (NameNotFound e) {
            throw new RTIinternalError("HlaInterfaceFailure", e);
        }
    }

    /**
     * @param interactionHandle interaction class handle in questione
     * @param nameParameter     nome del parametro da prendere
     */
    @Override
    public ParameterHandle getParameterHandle(InteractionClassHandle interactionHandle, String nameParameter) throws FederateNotExecutionMember, NotConnected, RTIinternalError {
        try {
            return ambassador.getParameterHandle(interactionHandle, nameParameter);
        } catch (NameNotFound e) {
            throw new RTIinternalError("HlaInterfaceFailure", e);
        } catch (InvalidInteractionClassHandle e) {
            throw new RuntimeException(e);
        }
    }


    /*******************************************
     * LISTENER INTERACTIONS
     *******************************************/
    /**
     * @param interactionListener aggiunge un listenerInteraction alla lista dei listener
     */
    @Override
    public void addInteractionListener(ListenerInteraction interactionListener) {
        listenerInteractions.add(interactionListener);
    }

    /**
     * Dice a tutti i listener che ha ricevuto interazione
     */
    @Override
    public void receiveInteraction(InteractionClassHandle interactionClassHandle, ParameterHandleValueMap mapValue, byte[] userSuppliedTag, OrderType sentOrdering, TransportationTypeHandle theTransport, SupplementalReceiveInfo receiveInfo) {
        for (ListenerInteraction listener : listenerInteractions) {
            try {
                listener.receiveInteraction(interactionClassHandle, mapValue);
            } catch (DecoderException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void receiveInteraction(InteractionClassHandle interactionClassHandle, ParameterHandleValueMap mapValue, byte[] userSuppliedTag, OrderType sentOrdering, TransportationTypeHandle theTransport, LogicalTime theTime, OrderType receivedOrdering, MessageRetractionHandle retractionHandle, SupplementalReceiveInfo receiveInfo) {
        for (ListenerInteraction listener : listenerInteractions) {
            try {
                listener.receiveInteraction(interactionClassHandle, mapValue);
            } catch (DecoderException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void receiveInteraction(InteractionClassHandle interactionClassHandle, ParameterHandleValueMap mapValue, byte[] userSuppliedTag, OrderType sentOrdering, TransportationTypeHandle theTransport, LogicalTime theTime, OrderType receivedOrdering, SupplementalReceiveInfo receiveInfo) {
        for (ListenerInteraction listener : listenerInteractions) {
            try {
                listener.receiveInteraction(interactionClassHandle, mapValue);
            } catch (DecoderException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /******************************************
     *  SUBSCRIBE & PUBLISH
     ******************************************/

    /**
     * Sottoscrive la federate all'interazione del Manager
     * @param interactionClassHandle è l'interazione in questione
     */
    @Override
    public void subscribeInteractions(InteractionClassHandle interactionClassHandle) throws FederateNotExecutionMember, RestoreInProgress, SaveInProgress, NotConnected, RTIinternalError, FederateServiceInvocationsAreBeingReportedViaMOM {
        try {
            ambassador.subscribeInteractionClass(interactionClassHandle);
        } catch (InteractionClassNotDefined e) {
            throw new RTIinternalError("HlaInterfaceFailure", e);
        }
    }

    /**
     * Pubblica l'interazione ricevuta dal Manager
     * @param interactionClassHandle Override del handle per l'interazione
     */
    @Override
    public void publishInteractions(InteractionClassHandle interactionClassHandle) throws FederateNotExecutionMember, RestoreInProgress, SaveInProgress, NotConnected, RTIinternalError {
        try {
            ambassador.publishInteractionClass(interactionClassHandle);
        } catch (InteractionClassNotDefined e) {
            throw new RTIinternalError("HlaInterfaceFailure", e);
        }
    }

    /**
     * Pubblica le interazioni ricevute da AnyLogic sul Manager
     *
     * @param interactionHandle interaction considered
     * @param mapValue          parametro da selezionare per l'interazione
     */
    @Override
    public void sendInteraction(InteractionClassHandle interactionHandle, ParameterHandleValueMap mapValue) throws FederateNotExecutionMember, NotConnected, RestoreInProgress, SaveInProgress, RTIinternalError {
        try {
            ambassador.sendInteraction(interactionHandle, mapValue, null);
        } catch (InteractionClassNotPublished | InteractionClassNotDefined | InteractionParameterNotDefined e) {
            throw new RTIinternalError("HlaInterfaceFailure", e);
        }
    }

    /**********************************
     * OBJECT
     **********************************/


    /**
     * Crea una Hash map di dimensione uguale a capacity,
     * da usare per allocare i parametri degli handle dello scenario.
     * @return Hash Map contenente gli attributi dell'oggetto
     */
    @Override
    public AttributeHandleValueMap createAttributeMap(int capacity) throws FederateNotExecutionMember, NotConnected {
        mapAttributeCreator = ambassador.getAttributeHandleValueMapFactory();
        return mapAttributeCreator.create(capacity);
    }

    @Override
    public AttributeHandleSet createAttributesSet(int capacity) throws FederateNotExecutionMember, NotConnected {
        setAttributes  = ambassador.getAttributeHandleSetFactory();
        return setAttributes.create();
    }
    /**
     * @param nameOfObject nome della class handle che si vuole
     * @return object class handle
     */
    @Override
    public ObjectClassHandle getObjectClassHandle(String nameOfObject) {
        try {
            return ambassador.getObjectClassHandle(nameOfObject);
        } catch (NameNotFound | FederateNotExecutionMember | NotConnected | RTIinternalError e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * @param objectClassHandle tipo di object class handle che si vuole
     * @param attributeName nome dell'attributo
     * @return attribute handle
     */
    @Override
    public AttributeHandle getAttributeHandle(ObjectClassHandle objectClassHandle, String attributeName) {
        try {
            return ambassador.getAttributeHandle(objectClassHandle, attributeName);
        } catch (NameNotFound | InvalidObjectClassHandle | FederateNotExecutionMember | NotConnected |
                 RTIinternalError e) {
            throw new RuntimeException(e);
        }
    }

    /**
     *
     * @param objectClassHandle object class handle che si sta considerando
     * @param attributeHandle attributo che si vuole
     * @return nome dell'attributo
     */
    @Override
    public String getNameAttribute(ObjectClassHandle objectClassHandle, AttributeHandle attributeHandle) {
        String attributeName;
        try {
            attributeName = ambassador.getAttributeName(objectClassHandle, attributeHandle);
        } catch (FederateNotExecutionMember | InvalidAttributeHandle | AttributeNotDefined | NotConnected |
                 InvalidObjectClassHandle | RTIinternalError e) {
            throw new RuntimeException(e);
        }
        return attributeName;
    }

    /**
     * Pubblica un oggetto sul RTI
     * @param objectClassHandle handle oggetto che si vuole pubblicare
     * @param attributeHandleSet set di attributi associato all'oggetto che si sta pubblicando
     */
    @Override
    public void sendObject(ObjectClassHandle objectClassHandle, AttributeHandleSet attributeHandleSet) {
        try {
            ambassador.publishObjectClassAttributes(objectClassHandle, attributeHandleSet);
        } catch (AttributeNotDefined | ObjectClassNotDefined | SaveInProgress | RestoreInProgress |
                 FederateNotExecutionMember |
                 NotConnected | RTIinternalError e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sottoscrizione all'oggetto pubblicato
     * @param objectClassHandle oggetto pubblicato a cui ci si vuole sottoscrivere
     * @param attributeHandleSet set di attributi dell'oggetto in considerazione
     */
    @Override
    public void subscribeObjectClass(ObjectClassHandle objectClassHandle, AttributeHandleSet attributeHandleSet) {
        try {
            ambassador.subscribeObjectClassAttributes(objectClassHandle, attributeHandleSet);
        } catch (ObjectClassNotDefined| AttributeNotDefined| SaveInProgress | RestoreInProgress | FederateNotExecutionMember | NotConnected | RTIinternalError e) {
            throw new RuntimeException(e);
    }
    }


    /**
     * Registrazione di una nuova istanza di un oggetto class handle
     * @param object istanza di un object class handle
     * @return una istanza dell'oggetto
     */
    @Override
    public ObjectInstanceHandle publishObject(ObjectClassHandle object) {
        ObjectInstanceHandle instanceHandle;
        try {
            instanceHandle = ambassador.registerObjectInstance(object);
        } catch (ObjectClassNotPublished | ObjectClassNotDefined | SaveInProgress | RestoreInProgress |
                 FederateNotExecutionMember | NotConnected | RTIinternalError e) {
            throw new RuntimeException(e);
        }
        return instanceHandle;
    }


    /**
     * Update degli attributi dell'oggetto
     * @param objectHandle object class handle
     * @param mapAttributes hash map degli attributi dell'oggetto
     * @param userSuppliedTag byteArray associato
     */
    @Override
    public void updatesAttributes(ObjectInstanceHandle objectHandle, AttributeHandleValueMap mapAttributes, byte[] userSuppliedTag) {
        try {
            ambassador.updateAttributeValues(objectHandle, mapAttributes, userSuppliedTag);
        } catch (AttributeNotOwned | AttributeNotDefined | ObjectInstanceNotKnown | SaveInProgress | RestoreInProgress |
                 FederateNotExecutionMember | NotConnected | RTIinternalError e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Rimozione di una istanza di un oggetto
     * @param objectHandle istanza di un object class handle
     * @param userSuppliedTag byteArray associato
     */
    @Override
    public void removeObjectInstance(ObjectInstanceHandle objectHandle, byte[] userSuppliedTag) {
        try {
            ambassador.deleteObjectInstance(objectHandle, userSuppliedTag);
        } catch (DeletePrivilegeNotHeld | ObjectInstanceNotKnown | SaveInProgress | RestoreInProgress |
                 FederateNotExecutionMember | NotConnected | RTIinternalError e) {
            throw new RuntimeException(e);
        }
    }

    /**************************************
     * LISTENER OBJECT
     **************************************/

    @Override
    public void addObjectListener(ListenerObject objectListener) {
        listenerObjects.add(objectListener);
    }

    /**
     * Observable del Coordinator per gli oggetti
     */

    /*@Override
    public void reflectAttributeValues(ObjectClassHandle objectClassHandle, AttributeHandleValueMap mapAttribute, byte[] userSuppliedTag, OrderType sentOrdering, TransportationTypeHandle theTransport, SupplementalReflectInfo reflectInfo) throws FederateInternalError {
        for (ListenerObject listener : listenerObjects) {
            listener.reflectAttributeValues(objectClassHandle, mapAttribute);
        }
    }


    public void reflectAttributeValues(ObjectClassHandle objectClassHandle, AttributeHandleValueMap mapAttribute, byte[] userSuppliedTag, OrderType sentOrdering, TransportationTypeHandle theTransport, LogicalTime theTime, OrderType receivedOrdering, SupplementalReflectInfo reflectInfo) throws FederateInternalError {
        for (ListenerObject listener : listenerObjects) {
            listener.reflectAttributeValues(objectClassHandle, mapAttribute);
        }
    }


    public void reflectAttributeValues(ObjectClassHandle objectClassHandle, AttributeHandleValueMap mapAttribute, byte[] userSuppliedTag, OrderType sentOrdering, TransportationTypeHandle theTransport, LogicalTime theTime, OrderType receivedOrdering, MessageRetractionHandle retractionHandle, SupplementalReflectInfo reflectInfo) throws FederateInternalError {
        for (ListenerObject listener : listenerObjects) {
            listener.reflectAttributeValues(objectClassHandle, mapAttribute);
        }
    }*/

    //TO DO: discoverObjectInstance (come nella classe HlaInterfaceImpl di mapviewer)


}
