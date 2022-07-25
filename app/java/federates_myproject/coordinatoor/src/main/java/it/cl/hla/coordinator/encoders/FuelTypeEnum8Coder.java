package it.cl.hla.coordinator.encoders;

import hla.rti1516e.encoding.DecoderException;
import hla.rti1516e.encoding.EncoderFactory;
import hla.rti1516e.encoding.HLAinteger32BE;
import it.cl.hla.coordinator.implementation.FuelTypeImpl;

public class FuelTypeEnum8Coder {

   private enum FuelTypeEnum8 {
      UNKNOWN(0), GASOLINE(1), DIESEL(2), ETHANOL_FLEXIBLE_FUEL(3), NATURAL_GAS(4);

      private final int value;

      private FuelTypeEnum8(int value) {
         this.value = value;
      }

      public int getValue() {
         return this.value;
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


   public byte[] encode(FuelTypeImpl fuelTypeImpl, EncoderFactory coder) {
      HLAinteger32BE fuelTypeEnum8Coder = coder.createHLAinteger32BE();
      fuelTypeEnum8Coder.setValue(translate(fuelTypeImpl).getValue());
      return fuelTypeEnum8Coder.toByteArray();
   }

   public FuelTypeImpl decode(byte[] bytes, EncoderFactory coder) throws DecoderException {
      HLAinteger32BE fuelTypeEnum8Coder = coder.createHLAinteger32BE();
      fuelTypeEnum8Coder.decode(bytes);
      return translate(FuelTypeEnum8.find(fuelTypeEnum8Coder.getValue()));
   }

   private static FuelTypeImpl translate(FuelTypeEnum8 fuelType) {
      switch (fuelType) {
         case GASOLINE:
            return FuelTypeImpl.GASOLINE;
         case DIESEL:
            return FuelTypeImpl.DIESEL;
         case ETHANOL_FLEXIBLE_FUEL:
            return FuelTypeImpl.ETHANOL_FLEXIBLE_FUEL;
         case NATURAL_GAS:
            return FuelTypeImpl.NATURAL_GAS;
      }
      return FuelTypeImpl.UNKNOWN;
   }

   private static FuelTypeEnum8 translate(FuelTypeImpl fuelTypeImpl) {
      switch (fuelTypeImpl) {
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
