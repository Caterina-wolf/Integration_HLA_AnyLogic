package it.cl.hla.core.interfaces;

import hla.rti1516e.AttributeHandleValueMap;
import hla.rti1516e.ObjectClassHandle;

public interface ListenerObject {

    void reflectAttributeValues(ObjectClassHandle objectClassHandle, AttributeHandleValueMap mapAttribute);
}
