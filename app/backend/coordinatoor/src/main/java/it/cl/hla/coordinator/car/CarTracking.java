package it.cl.hla.coordinator.car;

import hla.rti1516e.ObjectInstanceHandle;

import java.util.HashMap;
import java.util.Map;

public class CarTracking {

    private Map<ObjectInstanceHandle, String> map1 = new HashMap<>();
    private Map<String, ObjectInstanceHandle> map2 = new HashMap<>();



    public CarTracking(){}

    public void put(ObjectInstanceHandle handle, String id) {
        map1.put(handle, id);
        map2.put(id, handle);
    }

    public String translate(ObjectInstanceHandle objectInstanceHandle) {
        return map1.get(objectInstanceHandle);
    }

    public ObjectInstanceHandle translate(String id) {
        return map2.get(id);
    }

    public void remove(ObjectInstanceHandle objectInstanceHandle) {
        map2.remove(map1.get(objectInstanceHandle));
        map1.remove(objectInstanceHandle);
    }

    public void remove(String identifier) {
        map1.remove(map2.get(identifier));
        map2.remove(identifier);
    }
}
