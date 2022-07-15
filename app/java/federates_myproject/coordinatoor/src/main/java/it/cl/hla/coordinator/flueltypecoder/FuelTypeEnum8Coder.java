package it.cl.hla.coordinator.flueltypecoder;

import hla.rti1516e.encoding.DecoderException;
import hla.rti1516e.encoding.EncoderFactory;
import hla.rti1516e.encoding.HLAinteger32BE;
import it.cl.hla.coordinator.implementation.FuelType;

public class FuelTypeEnum8Coder {

   private enum FuelTypeEnum8 {
      UNKNOWN(0), GASOLINE(1), DIESEL(2), ETHANOL_FLEXIBLE_FUEL(3), NATURAL_GAS(4);

      private final int _value;

      private FuelTypeEnum8(int value) {
         _value = value;
      }

      public int getValue() {
         return _value;
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

   private final HLAinteger32BE _coder;

   FuelTypeEnum8Coder(EncoderFactory encoderFactory) {
      _coder = encoderFactory.createHLAinteger32BE();
   }

   public byte[] encode(FuelType fuelType) {
      _coder.setValue(translate(fuelType).getValue());
      return _coder.toByteArray();
   }

   public FuelType decode(byte[] bytes) throws DecoderException {
      _coder.decode(bytes);
      return translate(FuelTypeEnum8.find(_coder.getValue()));
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
