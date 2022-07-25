package it.cl.hla.coordinator.encoders;

import hla.rti1516e.encoding.*;
import it.cl.hla.coordinator.implementation.Position;


public class PositionRecordCoder {

   public HLAfixedRecord createFixedRecord(EncoderFactory coder){
      HLAfloat64BE latCoder = createLatCoder(coder);
      HLAfloat64BE longCoder = createLongCoder(coder);
      HLAfixedRecord positionRecordCoder = coder.createHLAfixedRecord();
      positionRecordCoder.add(latCoder);
      positionRecordCoder.add(longCoder);
      return positionRecordCoder;

   }

   public HLAfloat64BE createLatCoder(EncoderFactory coder){
      return coder.createHLAfloat64BE();
   }

   public HLAfloat64BE createLongCoder(EncoderFactory coder){
      return coder.createHLAfloat64BE();
   }


   public Position decode(byte[] bytes, EncoderFactory coder) throws DecoderException {
      HLAfixedRecord positionCoder = createFixedRecord(coder);
      positionCoder.decode(bytes);
      return new Position(createLatCoder(coder).getValue(), createLongCoder(coder).getValue());
   }

   public byte[] encode(Position position, EncoderFactory coder) {
      HLAfixedRecord positionCoder = createFixedRecord(coder);
      ((HLAfloat64BE)positionCoder.get(0)).setValue(position.getLatitude());
      ((HLAfloat64BE)positionCoder.get(1)).setValue(position.getLongitude());
      return positionCoder.toByteArray();
   }
}
