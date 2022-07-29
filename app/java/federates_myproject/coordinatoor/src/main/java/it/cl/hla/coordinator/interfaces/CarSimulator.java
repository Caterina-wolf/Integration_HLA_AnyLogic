package it.cl.hla.coordinator.interfaces;

public interface CarSimulator {
    //void startSimulation();
    void loadScenario(String nameScenario, int initialFuel);
    void loadCar();
    void injectCar(String name, String licensePlate, String color);

}

