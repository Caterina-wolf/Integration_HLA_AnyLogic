package it.cl.hla.coordinator.interfaces;


import it.cl.hla.coordinator.implementation.FuelTypeImpl;
import it.cl.hla.coordinator.implementation.Position;

public interface Car {

   String getIdentifier();

   /**
    * Get the name of the Car
    *
    * @return the name
    */
   String getName();

   /**
    * Get the license plate number
    *
    * @return the license plate number
    */
   String getLicensePlateNumber();

   /**
    * Get the fuel type
    *
    * @return the fuel type
    */
   FuelTypeImpl getFuelType();

   /**
    * Get the normal speed of the car
    *
    * @return the speed
    */
   int getNormalSpeed();

   /**
    * Get the current speed of the car
    *
    * @return the speed
    */
   double getCurrentSpeed();

   void adjustCurrentSpeed(double change);

   double getLitersPerKm();


   /**
    * Get the current position of the Car
    *
    * @return Position of the Car
    */
   Position getLocation();

   void setLocation(Position location);

   /**
    * Get the current fuel level of the car
    *
    * @return Fuel level of the Car
    */
   double getFuelLevel();

   void setFuelLevel(double fuelLevel);

}
