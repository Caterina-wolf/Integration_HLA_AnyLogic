import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Car } from '../core/models/car/car.model';
import { CarService } from '../core/models/car/car.service';
import { Scenario } from '../core/models/scenario/scenario.model';
import { ScenarioService } from '../core/models/scenario/scenario.service';
import { InteractionService } from '../core/services/interaction.service';
import { MasterService } from '../core/services/master.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})


export class HomeComponent implements OnInit {
msgConnection: string;
msgStopSimulation: string;
scenario: Scenario = new Scenario();
timeScaleFactor: number;
CarForm: FormGroup;
ScenarioForm: FormGroup;
formSelect: FormControl = new FormControl('');
toggled: boolean = false;
submitSuccess = '';
carInjectSuccess = '';


constructor(public service: CarService, 
  public masterService: MasterService, 
  public scenarioService: ScenarioService, 
  public interactionService: InteractionService) { }


ngOnInit(): void {
  this.CarForm = new FormGroup({
    name: new FormControl('',
      [Validators.required,
      Validators.maxLength(12)]),
    licensePlate: new FormControl('',
      [Validators.required,
      Validators.minLength(7),
      Validators.maxLength(7)]),
    color: new FormControl('',
      [Validators.required,
      Validators.pattern('#[0-9a-fA-F]+')])
  });
  this.ScenarioForm = new FormGroup({
    fuel: new FormControl('',
      [Validators.required,
      Validators.max(100),
      Validators.pattern("^[0-9]*$"),]),
    time: new FormControl('',
      [Validators.required,
      Validators.maxLength(1)])
  });
  this.CarForm.valueChanges.subscribe((carParameters) => {
    console.log(carParameters);

  });
  this.ScenarioForm.valueChanges.subscribe((scenarioParameters) => {
    console.log(scenarioParameters);
  });

  this.formSelect.valueChanges.subscribe((nameScanario) => {
    console.log(nameScanario);
  });

}

connectEvent(e: MouseEvent){
  this.masterService.getMasterConnection().subscribe(() => {
    this.msgConnection = "Connected";
    setTimeout(() => this.msgConnection = null, 8000);
  });
}

disconnectEvent(e: MouseEvent){
  this.masterService.getMasterDisconnection().subscribe(()=> {
    this.msgConnection = "Disconnected";
    setTimeout(()=> this.msgConnection = null, 8000);
});
}

toggle(e: MouseEvent){
  if (!this.toggled) {
    this.disconnectEvent(e);
  } else {
    this.connectEvent(e);
  }
  this.toggled = !this.toggled;
}


loadScenario(scenario: Scenario, formSelect: FormControl) {
  console.log(this.scenario);
  this.scenario.scenarioName = this.formSelect.value;
  this.scenarioService.getScenario(this.scenario.scenarioName, this.scenario.initialFuelLevel)
    .subscribe(() => {
      this.scenario = scenario;
      this.submitSuccess = "Submit sucessful!";
      setTimeout(() => this.submitSuccess = null, 5000);
    });
}

sendTimeScaleFactor() {
  console.log(this.timeScaleFactor);
  return this.interactionService.getStartInteraction(this.timeScaleFactor)
    .subscribe()

}

stop(e: Event) {
  this.interactionService.getStopInteraction().subscribe(()=>{
    this.msgStopSimulation = "Parameters are all reset!";
    setTimeout(()=> this.msgStopSimulation = null,8000);
  });
  this.resetForms();
}

showCar(CarForm: FormGroup) {
  const car = new Car();
  car.name = this.CarForm.controls['name'].value;
  car.licensePlate = this.CarForm.controls['licensePlate'].value;
  car.color = this.CarForm.controls['color'].value;
  console.log(car);
  this.service.postCar(car)
    .subscribe((car1: Car) => {
      car1 = car;
      this.carInjectSuccess = "Car injected...waiting for another car!"
      setTimeout(() => this.carInjectSuccess = null, 5000);
    });
  this.resetForms();

}


submit(ScenarioForm: FormGroup, formSelect: FormControl) {
  this.scenario.initialFuelLevel = this.ScenarioForm.controls['fuel'].value;
  this.timeScaleFactor = this.ScenarioForm.controls['time'].value;
  this.loadScenario(this.scenario, this.formSelect.value);
  this.sendTimeScaleFactor();
}

resetForms(){
  this.CarForm.reset();
  this.ScenarioForm.reset();
  this.formSelect.reset();
} 

  
}