## User guide of a distributed system based on the communication protocol HLA, to permit exchange of data between simulations.

- What is HLA?
- What are federates?
-  What is AnyLogic?
- What is PITCH and what for?
- Scalable hlacore.. what, how and why?
- Postman.. API calls, why?
- Description of libraries 
- Maven project
- Bibliografy

#### HLA
HLA (High Level Architecture) is an open international standard, developed by the Simulation Interoperability Standards Organization (SISO) and published by IEEE.
Essential, HLA is a communicaion protocol used to develop distributed simulation. The simualtions are called federates; each federate is able to excahnge data to each other thnaks to the interoperbility of HLA.
Here the Architecture:
[Architecture] (images in the repository)
The component of HLA are: 
1. **Federates**: simulations, inlcuding a wide kind of simulation software and/or normal Java programs developed to be a simulation.
2. **RTI**:(Runtime Infastructure). It's the linker between federates. The data flows from one federate, they pass throw the RTI that knows which type of data is thanks to the FOM. Then the data is redirect to the other federate. This one send a Callback once has received the data, that always pass throw th RTI.    
3. **FOM** (Federation object model) is a ".xml" file, where is descripted the structure of data trasmitted through the RTI. The data can be:  
3.1  *Interactions signals*;
3.2 *Objects data*. 
Therefore the are 2 roots - HLArootInteraction and HLArootObject - to explain the origin of the dara. Anyway, during the develop, phase they can be ousted. Moreover, the file contains also other information about the federation as wel as the federation's name, federates' names and others.
4. **The ambassador**: internal component of each federate. This is the one that allows the CALL and the CALLBACKS from/to RTI and federates. It's the core of the communication protocol. 
5. **Federation**: the federation is the set of components over mentioned.

### Software used:
#### Java:
I've implemented 3 modules in Java code:
- coordinatoor
- hlacore
- master 

Each modules has a specific porpouse and a different way of implementation. Hlacore and coordinator are implemented in pure Java. Master is developed with Spring Boot framework, which allows to build a REST service for a web app. Here because the necessity of POSTMAN software, that I'll discuss below.

**Hlacore** is, de facto, a jar library containing the functions that "knows" HLA, that is to say the functions that calls method of hla library on the ambassador. The building of this library is the key of scalability, this module is, indeed, reusable in other projects in a more easy and less hardworking way.

**Coordinator** is a jar library that coordinate the calls and callback from a simulation software and the HLA part. It's easily re-adaptable to other simulation with a different porpouse or other simulation software programmed in a different language from Java. In this case, I used AnyLogic to simulate the route of a car on two different scenarios: 
+ Cupertino to Pacifica
+ Redmond to (?)

The car, as written in the FOM file, can send and receive interactions, like "load scenario" and "start & stop" simulation. Moreover, it can receive and sends data about the car, like what "type of fuel" the car is refueled or the "fuel level" of it.
This library has to be imported in the software simulation to be used.

**Manager** is a federate (module) that control the other federates. As just said, it's a little web service that works with RESTFUL API calls. No needs to export it as modules in a java library because it's not imported anywhere. The controller handles the HLA functionality, like the connection of federate to RTI in the federation.

#### Postman
Postman is API platform used for designing, testing and developing APIs.
In this project, only Master federate is a web service that need to use POSTMAN.

#### AnyLogic
AnyLogic is a tool to create simulation models, developed by The AnyLogic Company. AnyLogic is implemented in Java and use Java language to behave the models by a wide variety of virtual componenets listed in the AnyLogic's palette.
How to link Anylogic to the Master federate? Simply importing the coordinator.jar library to the project model in use. Now, the model can employ and override the methods of the library.

#### Pitch 


### Installation 
- moduli da esportare
- comandi per l'installazione
- 
## Run
- comandi manuali x far partire i programmi
- sequenza per farli funzionare

