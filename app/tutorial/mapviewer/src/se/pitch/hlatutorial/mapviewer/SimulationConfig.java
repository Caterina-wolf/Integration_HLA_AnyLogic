/*
 * Copyright (C) 2012  Pitch Technologies
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package mapviewer.src.se.pitch.hlatutorial.mapviewer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class SimulationConfig {

   private static final String CRC_ADDRESS = "crcAddress=";
   private String ip = "192.168.100.65";

   private static final String LOCAL_SETTINGS_DESIGNATOR = "localSettingsDesignator";
   private static final String FEDERATION_NAME = "federationName";
   private static final String FEDERATE_NAME = "federateName";

   private static final String SCENARIO_DIR = "scenarioDir";
   private static final String FOM = "fom";

   private final String _localSettingsDesignator;
   private final String _federationName;
   private final String _federateName;

   private final String _scenarioDir;
   private final String _fom;

   private final String crc_Address;


   public SimulationConfig(String fileName) throws IOException {
      this(new File(fileName));
   }

   public SimulationConfig(File file) throws IOException {
      Properties properties = new Properties();
      properties.load(new FileInputStream(file));

      crc_Address = properties.getProperty(CRC_ADDRESS,"crcAddress=" + ip + ":" + Integer.toString(8989));
      _localSettingsDesignator = properties.getProperty(LOCAL_SETTINGS_DESIGNATOR, " ");
      _federationName = properties.getProperty(FEDERATION_NAME, "HLA Tutorial");
      _federateName = properties.getProperty(FEDERATE_NAME, "Mapviewer");

      _scenarioDir = properties.getProperty(SCENARIO_DIR, ".");
      _fom = properties.getProperty(FOM, "FuelEconomyBase2.xml");

   }

   public String getLocalSettingsDesignator() {
      return _localSettingsDesignator;
   }

   public String getFederationName() {
      return _federationName;
   }

   public String getFederateName() {
      return _federateName;
   }

   public String getScenarioDir() {
      return _scenarioDir;
   }

   public String getFom() {
      return _fom;
   }

   public String getCrcAddress(){
      return crc_Address;
   }
}
