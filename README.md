## User guide of a distributed system based on the communication protocol HLA, to permit exchange of data between simulations.

#### HLA
HLA (High Level Architecture) is an open international standard, developed by the Simulation Interoperability Standards Organization (SISO) and published by IEEE.
Essentially, HLA is a communicaion protocol used to develop distributed simulation. The simualtions are called federates; each federate is able to excahnge data to each other thnaks to the interoperbility of HLA.
Here the Architecture:
[Architecture] (images in the repository)
The component of HLA are: 
1. **Federates**: simulations, inlcuding a wide types of simulation software and/or normal Java programs developed to be a simulation.
2. **RTI**:(Runtime Infastructure). It's the linker between federates. The data flows from one federate, they pass throw the RTI that knows which type of data is thanks to the FOM. Then the data is redirect to the other federates. This one send a Callback once has received the data, that always pass throw th RTI.    
3. **FOM** (Federation object model) is a ".xml" file, where is descripted the structure of data trasmitted through the RTI. The data can be:  
3.1  *Interactions signals*;
3.2 *Objects data*. 
Therefore the are 2 roots - HLArootInteraction and HLArootObject - to explain the origin of the data. Anyway, during the develop phase they can be ousted. Moreover, the file contains also other information about the federation as well as the federation's name, federates' names and others.
4. **The ambassador**: internal component of each federate. This is the one that allows the CALL and the CALLBACKS from/to RTI and federates. It's the core of the communication protocol. 
5. **Federation**: the federation is the set of components over mentioned.

### Software used:
#### Java:
I've implemented 3 modules in Java code:
- coordinatoor
- hlacore
- master 

Each modules has a specific porpouse and a different way of implementation. Hlacore and coordinator are implemented in pure Java. Master is developed with Spring Boot framework, which allows to build a REST service for a web app. That's why the need of POSTMAN, that I'll discuss in details later.

**Hlacore** is, de facto, a jar library containing the functions that "knows" HLA, that is to say the functions that calls method of hla library on the ambassador. The building of this library is the key of scalability, this module is, indeed, reusable in other projects in a more easy and less hardworking way.

**Coordinator** is a jar library that coordinate the calls and callback from a simulation model and the hlaCore. It's easily re-adaptable to every simulation models. In this case, I used AnyLogic to simulate the route of a car on the scenario starts from Cupertino arrived to Pacifica.

The car, as written in the FOM file, can send and receive interactions, like "load scenario" and "start & stop" simulation. Moreover, it can receive and sends data about the car, like what "type of fuel" the car is refueled or the "fuel level" of it.
This library has to be imported in the software simulation to be used.

**Manager** is a federate (module) that control the other federates. As just said, it's a little web service that works with RESTFUL API calls. No needs to export it as modules in a java library because it's not imported anywhere. The controller handles the HLA functionality, like the connection between federate and RTI in the FedExec.

#### Postman
Postman is API platform used for designing, testing and developing APIs.
In this project, only Master federate is a web service that need to use POSTMAN.
I have built a workspace in which put the API's calls. 
The GET calls are:
* GET init to connect and join the federate to pitch which was activated in advance.
* GET exit to disconnet and shut the federate linked to pitch.
* GET startInteraction that takes also a parameter (TimeScaleFactor) to get the start interaction from pitch.
* GEt stopInteraction to get the staop interaction form pitch.
* POST injectCar that post a Car with 3 attributes (name, license plate and color) into AnyLogic model.


#### AnyLogic
AnyLogic is a tool to create simulation models, developed by The AnyLogic Company. AnyLogic is implemented in Java and use Java language to behave the blocks of the model and the model itself; the blocks are virtuale componenents listed in the AnyLogic's palette (which seems a library of components).
How to link Anylogic to the Master federate? Simply importing the coordinator.jar library to the project model in use. Now, the model can employ and override the methods of the library.

#### Pitch 
PITCH is the HLA environment in  which the federation is created. It mainly let all the components of a federation to join and create the FedExec.
Actually, PITCH is a GUI to visulaize the federation and the interaction exchanged by every federates.

### Installation 
To install PITCH:
* It's available a guide written by authors of PITCH.
Other module to export: 
* As just said the coordinator.jar library is the one who behave the model, so it's should be imported in AnyLogic model in the section dedicated (image);
* The other module to import in AnyLogic model is hlaCore.jar, which is now in a local maven repository. 

## Run
The steps to run the program until now: 
1. Open Pitch
2. Run Master federate: this one has the task to create the federation in Pitch environment and then it join itself.
3. Run AnyLogic simulation with Play button. At this point the federation is completed with all the members.
4. Open POSTMAN: then run the calls with interest. In this case: GET loadScenario to load the Scenario into the federates and then GET startInteraction to start the interaction. In the end launch the simulation with POST injectCar.


