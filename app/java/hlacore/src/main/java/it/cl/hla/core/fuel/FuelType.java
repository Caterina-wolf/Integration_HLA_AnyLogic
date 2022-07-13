package it.cl.hla.core.fuel;


public enum FuelType {
   GASOLINE("Gasoline"),
   DIESEL("Diesel"),
   ETHANOL_FLEXIBLE_FUEL("EthanolFlexibleFuel"),
   NATURAL_GAS("NaturalGas"),
   UNKNOWN("Unknown");

   private final String name;

   private FuelType(String name) {
      this.name = name;
   }

   public String getName() {
      return name;
   }

   public static FuelType find(String name) {
      for (FuelType fuel : values()) {
         if (fuel.name.equals(name)) {
            return fuel;
         }
      }
      return UNKNOWN;
   }
}
