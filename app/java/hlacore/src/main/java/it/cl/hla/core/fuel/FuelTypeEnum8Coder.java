
package it.cl.hla.core.fuel;

import hla.rti1516e.encoding.DecoderException;
import hla.rti1516e.encoding.EncoderFactory;
import hla.rti1516e.encoding.HLAinteger32BE;



class FuelTypeEnum8Coder {
   private enum FuelTypeEnum8 {
      UNKNOWN(0), GASOLINE(1), DIESEL(2), ETHANOL_FLEXIBLE_FUEL(3), NATURAL_GAS(4);

      private final int value;

      private FuelTypeEnum8(int value) {
         this.value = value;
      }

      public int getValue() {
         return value;
      }

      public static FuelTypeEnum8 find(int value) {
         for (FuelTypeEnum8 fuelType : values()) {
            if (fuelType.getValue() == value) {
               return fuelType;
            }
         }
         return FuelTypeEnum8.UNKNOWN;
      }
   }

   private final HLAinteger32BE coderInt;

   FuelTypeEnum8Coder(EncoderFactory encoderFactory) {
      coderInt = encoderFactory.createHLAinteger32BE();
   }

   byte[] encode(FuelType fuelType) {
      coderInt.setValue(translate(fuelType).getValue());
      return coderInt.toByteArray();
   }

   FuelType decode(byte[] bytes) throws DecoderException {
      coderInt.decode(bytes);
      return translate(FuelTypeEnum8.find(coderInt.getValue()));
   }

   private static FuelType translate(FuelTypeEnum8 fuelType) {
      switch (fuelType) {
         case GASOLINE:
            return FuelType.GASOLINE;
         case DIESEL:
            return FuelType.DIESEL;
         case ETHANOL_FLEXIBLE_FUEL:
            return FuelType.ETHANOL_FLEXIBLE_FUEL;
         case NATURAL_GAS:
            return FuelType.NATURAL_GAS;
      }
      return FuelType.UNKNOWN;
   }

   private static FuelTypeEnum8 translate(FuelType fuelType) {
      switch (fuelType) {
         case GASOLINE:
            return FuelTypeEnum8.GASOLINE;
         case DIESEL:
            return FuelTypeEnum8.DIESEL;
         case ETHANOL_FLEXIBLE_FUEL:
            return FuelTypeEnum8.ETHANOL_FLEXIBLE_FUEL;
         case NATURAL_GAS:
            return FuelTypeEnum8.NATURAL_GAS;
      }
      return FuelTypeEnum8.UNKNOWN;
   }
}
