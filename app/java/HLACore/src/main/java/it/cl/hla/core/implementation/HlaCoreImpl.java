package it.cl.hla.core.implementation;

import hla.rti1516e.*;
import hla.rti1516e.encoding.*;
import hla.rti1516e.exceptions.*;

import it.cl.hla.core.interfaces.*;

import java.net.URL;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class HlaCoreImpl extends NullFederateAmbassador implements HlaCore {
        /**
         * Ambassador per comunicare con la libreria hla.rti1516e
         */
        private RTIambassador ambassador;

        /**
         * Nome della federate
         */
        private String federateName = "";

    /**
         * Nome della FedeExec
         */
        private String federationName;
        private URL urlFOM;
        private EncoderFactory coder;
        private ParameterHandleValueMapFactory mapHandleCreator;

        /**
         * Listener usato nei metodi di Coordinator per ricevere le interaction
         */
        private List<ListenerInteraction> Listeners = new CopyOnWriteArrayList<>() {};
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

        @Override
        public String getFederationName() {
            return federationName;
        }

    /**
         * @param settingDesignator//localSettingsDesignator The name to load settings for or "" to load default settings
         * @param federationName      Name of the federation to join
         */
    @Override
    public void start(String settingDesignator/*localSettingsDesignator*/, String federationName, URL urlFOM) throws RTIinternalError, ConnectionFailed, NotConnected {
            RtiFactory rtiFactory = RtiFactoryFactory.getRtiFactory();
            ambassador = rtiFactory.getRtiAmbassador();
            coder = rtiFactory.getEncoderFactory();


            try {
                ambassador.connect(this, CallbackModel.HLA_IMMEDIATE, settingDesignator/*localSettingsDesignator*/);
            } catch (AlreadyConnected ignored) {
            } catch (UnsupportedCallbackModel e) {
                throw new RTIinternalError("HlaInterfaceFailure", e);
            } catch ( InvalidLocalSettingsDesignator invalidLocalSettingsDesignator) {
                invalidLocalSettingsDesignator.printStackTrace();
            } catch (CallNotAllowedFromWithinCallback e) {
                throw new RuntimeException(e);
            }

        this.urlFOM = urlFOM;
        this.federationName = federationName;

        if(urlFOM != null) {
            try {
                ambassador.destroyFederationExecution(federationName);
            } catch (FederationExecutionDoesNotExist | FederatesCurrentlyJoined ignored) {
            }


            try {
                ambassador.createFederationExecution(federationName, urlFOM);
            } catch (FederationExecutionAlreadyExists ignored) {
                ignored.printStackTrace();
            } catch (ErrorReadingFDD | InconsistentFDD | CouldNotOpenFDD e) {
                throw new RuntimeException(e);
            }

        }
    }

        @Override
        public void start(String settingDesignator/*localSettingsDesignator*/, String federationName) throws RTIinternalError, ConnectionFailed, NotConnected {
            this.start(settingDesignator/*localSettingsDesignator*/, federationName, null);
        }

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
         * Terminate the communication with FedExec
         */
        @Override
        public void stop() throws RTIinternalError {
            try {
                try {
                    ambassador.resignFederationExecution(ResignAction.CANCEL_THEN_DELETE_THEN_DIVEST);
                } catch (FederateNotExecutionMember ignored) {
                } catch (FederateOwnsAttributes | OwnershipAcquisitionPending | CallNotAllowedFromWithinCallback| InvalidResignAction e) {
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


    @Override
    public float decodeFloat32(byte[] data) throws DecoderException {
        HLAfloat32BE coderFloat = coder.createHLAfloat32BE();
        coderFloat.decode(data);
        return coderFloat.getValue();
    }

    @Override
    public int decodeInt32(byte[] data) throws DecoderException {
        HLAinteger32BE coderInt = coder.createHLAinteger32BE();
        coderInt.decode(data);
        return coderInt.getValue();
    }
    @Override
    public String decodeString32(byte[] data) throws DecoderException {
        HLAunicodeString coderString = coder.createHLAunicodeString();
        coderString.decode(data);
        return coderString.getValue();
    }

    @Override
    public byte[] encoderString(String name) {
        HLAunicodeString coderString = coder.createHLAunicodeString();
        coderString.setValue(name);
        return coderString.toByteArray();
    }
    @Override
    public byte[] encoderInt(int fuel){
        HLAinteger32BE coderInt = coder.createHLAinteger32BE();
        coderInt.setValue(fuel);
        return coderInt.toByteArray();
    }


    @Override
    public byte[] encoderFloat(float time){
        HLAfloat32BE coderFloat= coder.createHLAfloat32BE();
        coderFloat.setValue(time);
        return coderFloat.toByteArray();
    }



        /**
         * Crea una Hash map di dimensione uguale a capacity,
         * da usare per allocare gli handle dello scenario.
         * @return mapHandle HashMap degli handle
         */
        @Override
        public ParameterHandleValueMap createMap(int capacity) throws FederateNotExecutionMember, NotConnected {
            mapHandleCreator = ambassador.getParameterHandleValueMapFactory();
            return mapHandleCreator.create(capacity);
        }
        /**
         * @param nameHandle nome del handle da selezionare per l'interazione
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
         *
         * @param interactionHandle interazione che sto considerando al momento
         * @param nameParameter nome del parametro da prendere
         */
        @Override
        public ParameterHandle getParameterHandle(InteractionClassHandle interactionHandle, String nameParameter)throws FederateNotExecutionMember,NotConnected, RTIinternalError{
            try{
                return ambassador.getParameterHandle(interactionHandle,nameParameter);
            } catch (NameNotFound e){
                throw  new RTIinternalError("HlaInterfaceFailure",e);
            } catch (InvalidInteractionClassHandle e) {
                throw new RuntimeException(e);
            }
        }

        /**
         * @param interactionListener aggiunge un listener alla lista dei listener
         */
        @Override
        public void addInteractionListener(ListenerInteraction interactionListener) {
            Listeners.add(interactionListener);
        }

        /**
         * Observable del Coordinator per le interazioni
         */
        @Override
        public void receiveInteraction(InteractionClassHandle interactionClassHandle, ParameterHandleValueMap mapValue, byte[] userSuppliedTag, OrderType sentOrdering, TransportationTypeHandle theTransport, SupplementalReceiveInfo receiveInfo) {
            for (ListenerInteraction listener : Listeners) {
                try {
                    listener.receiveInteraction(interactionClassHandle, mapValue);
                } catch (DecoderException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        @Override
        public void receiveInteraction(InteractionClassHandle interactionClassHandle, ParameterHandleValueMap mapValue, byte[] userSuppliedTag, OrderType sentOrdering, TransportationTypeHandle theTransport, LogicalTime theTime, OrderType receivedOrdering, MessageRetractionHandle retractionHandle, SupplementalReceiveInfo receiveInfo) {
            for (ListenerInteraction listener : Listeners) {
                try {
                    listener.receiveInteraction(interactionClassHandle, mapValue);
                } catch (DecoderException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        @Override
        public void receiveInteraction(InteractionClassHandle interactionClassHandle, ParameterHandleValueMap mapValue, byte[] userSuppliedTag, OrderType sentOrdering, TransportationTypeHandle theTransport, LogicalTime theTime, OrderType receivedOrdering, SupplementalReceiveInfo receiveInfo) {
            for (ListenerInteraction listener : Listeners) {
                try {
                    listener.receiveInteraction(interactionClassHandle, mapValue);
                } catch (DecoderException e) {
                    throw new RuntimeException(e);
                }
            }
        }
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
         * @param interactionHandle interaction considered
         * @param mapValue parametro da selezionare per l'interazione
         */
        @Override
        public void sendInteraction(InteractionClassHandle interactionHandle, ParameterHandleValueMap mapValue) throws FederateNotExecutionMember, NotConnected, RestoreInProgress, SaveInProgress, RTIinternalError {
            try {
                ambassador.sendInteraction(interactionHandle, mapValue, null);
            } catch (InteractionClassNotPublished | InteractionClassNotDefined | InteractionParameterNotDefined e) {
                throw new RTIinternalError("HlaInterfaceFailure", e);
            }
        }


}
