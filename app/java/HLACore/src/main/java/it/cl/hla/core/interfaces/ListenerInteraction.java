package it.cl.hla.core.interfaces;

import hla.rti1516e.*;
import hla.rti1516e.encoding.*;

public interface ListenerInteraction {
    void receiveInteraction(InteractionClassHandle interactionHandle, ParameterHandleValueMap mapValue) throws DecoderException;
}
