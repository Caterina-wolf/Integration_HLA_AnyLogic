package it.cl.hla.coordinator.implementation;


public enum FuelTypeImpl{
   GASOLINE("Gasoline"),
   DIESEL("Diesel"),
   ETHANOL_FLEXIBLE_FUEL("EthanolFlexibleFuel"),
   NATURAL_GAS("NaturalGas"),
   UNKNOWN("Unknown");

   private String name = " ";

   FuelTypeImpl(String name) {
      this.name = name;
   }

  FuelTypeImpl(){}


   public FuelTypeImpl find(String name) {
      for (FuelTypeImpl fuel : values()) {
         if (fuel.name.equals(name)) {
            return fuel;
         }
      }
      return UNKNOWN;
   }
}
