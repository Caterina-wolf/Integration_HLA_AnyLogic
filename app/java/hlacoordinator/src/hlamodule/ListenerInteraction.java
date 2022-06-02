package src.hlamodule;

import hla.rti1516e.InteractionClassHandle;
import hla.rti1516e.ParameterHandleValueMap;
import hla.rti1516e.encoding.DecoderException;

public interface ListenerInteraction {
    void receiveInteraction(InteractionClassHandle interactionHandle, ParameterHandleValueMap mapValue) throws DecoderException;
}
