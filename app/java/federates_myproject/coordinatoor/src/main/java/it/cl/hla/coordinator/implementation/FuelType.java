package it.cl.hla.coordinator.implementation;


public enum FuelType {
   GASOLINE("Gasoline"),
   DIESEL("Diesel"),
   ETHANOL_FLEXIBLE_FUEL("EthanolFlexibleFuel"),
   NATURAL_GAS("NaturalGas"),
   UNKNOWN("Unknown");

   private final String _name;

   private FuelType(String name) {
      _name = name;
   }

   public static FuelType find(String name) {
      for (FuelType fuel : values()) {
         if (fuel._name.equals(name)) {
            return fuel;
         }
      }
      return UNKNOWN;
   }
}
