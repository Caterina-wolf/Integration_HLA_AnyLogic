package it.cl.hla.coordinator.car;


import it.cl.hla.coordinator.implementation.FuelTypeImpl;
import it.cl.hla.coordinator.implementation.Position;
import it.cl.hla.coordinator.interfaces.Car;

public class CarImpl implements Car {

   private static int carCount = 0;

   private String identifier;

   private String name;
   private String licensePlateNumber;
   private FuelTypeImpl fuelTypeImpl;

   private int normalSpeed; // km/h
   private double currentSpeed; // km/h

   private double litersPerKm; // First minute of driving

   private Position location;

   private double fuelLevel;

   public CarImpl(String name,
                  String licensePlateNumber,
                  FuelTypeImpl fuelTypeImpl,
                  int normalSpeed,
                  double litersPerKm) {
      identifier = "Car" + carCount;
      carCount++;
      this.name = name;
      this.licensePlateNumber = licensePlateNumber;
      this.fuelTypeImpl = fuelTypeImpl;
      this.normalSpeed = normalSpeed;
      currentSpeed = normalSpeed;
      this.litersPerKm = litersPerKm;
      this.location = new Position();
   }

   public CarImpl(){
   }

   public String getIdentifier() {
      return this.identifier;
   }
   public String getName() {
      return this.name;
   }

   public String getLicensePlateNumber() {
      return this.licensePlateNumber;
   }

   public FuelTypeImpl getFuelType() {
      return this.fuelTypeImpl;
   }


   public int getNormalSpeed() {
      return this.normalSpeed;
   }

   public double getLitersPerKm() {
      return this.litersPerKm;
   }

   public Position getLocation() {
      return location;
   }

   public double getFuelLevel() {
      return fuelLevel;
   }

   public void setLocation(Position location) {
      this.location = location;
   }

   public void setFuelLevel(double fuelLevel) {
      this.fuelLevel = fuelLevel;
   }

   public double getCurrentSpeed() {
      return this.currentSpeed;
   }

   public void adjustCurrentSpeed(double change) {
      currentSpeed = currentSpeed + change;
   }
}
