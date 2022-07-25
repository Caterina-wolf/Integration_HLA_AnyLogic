package it.cl.hla.coordinator.implementation;


public class Position {

   private double latitude;
   private double longitude;

   public Position(){}

   public Position(double latitude, double longitude) {
      this.latitude = latitude;
      this.longitude = longitude;
   }


   public double getLatitude() {
      return this.latitude;
   }

   public double getLongitude() {
      return this.longitude;
   }

   @Override
   public String toString() {
      return "Position[" + getLatitude() + ", " + getLongitude() + "]";
   }

   @Override
   public int hashCode() {
      int returnValue = 37;
      returnValue = returnValue * 17 + new Double(latitude).hashCode();
      returnValue = returnValue * 17 + new Double(longitude).hashCode();
      return returnValue;
   }

   @Override
   public boolean equals(Object obj) {
      if (obj == this) {
         return true;
      }
      if (obj instanceof Position) {
         Position position = (Position)obj;
         return position.latitude == latitude && position.longitude == longitude;
      }
      return false;
   }
}
