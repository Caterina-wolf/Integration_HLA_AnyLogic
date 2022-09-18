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
  msg: string;
  msg1: string;
  msg2:string;
  car: Car = new Car();
  scenario: Scenario = new Scenario();
  timeScaleFactor: number;
  CarForm: FormGroup;
  ScenarioForm : FormGroup;
  formSelect : FormControl = new FormControl('');
  toggled : boolean = false;
  


  constructor(public service: CarService, public masterService: MasterService, public scenarioService: ScenarioService, public interactionService: InteractionService) { }

  ngOnInit(): void {
    this.CarForm = new FormGroup({
      name: new FormControl('',
      [ Validators.required,
        Validators.maxLength(12)]),
      licensePlate: new FormControl('',
      [ Validators.required,
        Validators.minLength(7),
        Validators.maxLength(7)]),
      color: new FormControl('',
      [ Validators.required,
        Validators.maxLength(7),
        Validators.minLength(7)])
    });
    this.ScenarioForm = new FormGroup({
      fuel : new FormControl('', 
      [ Validators.required,
        Validators.max(100),
        Validators.pattern("^[0-9]*$"),]),
      time : new FormControl('', 
      [ Validators.required,
        Validators.maxLength(1)])
    });
    this.CarForm.valueChanges.subscribe((input) => {
      console.log(input);

    });
    this.ScenarioForm.valueChanges.subscribe((input2) => {
      console.log(input2);
    });
    this.formSelect.valueChanges.subscribe((input3) => {
      console.log(input3);
    })
  }

  connectEvent(e:MouseEvent){
    this.msg = "Connected";
    this.masterService.getMasterConnection().subscribe();
    return this.msg;
  }

  disconnectEvent(e:MouseEvent){
    this.msg = "Disconnected";
    this.masterService.getMasterDisconnection().subscribe();
    this.msg1 = null;
    return this.msg;
  }

  toggle(e: MouseEvent){
    if(!this.toggled){
      this.disconnectEvent(e);
    }else{
      this.connectEvent(e);
    }
    this.toggled = !this.toggled;
  }


  loadScenario(scenario: Scenario, formSelect: FormControl) {
    console.log(this.scenario);
    this.scenario.scenarioName = this.formSelect.value;
    this.scenarioService.getScenario(this.scenario.scenarioName, this.scenario.initialFuelLevel)
      .subscribe(() => this.scenario = scenario); //TODO:controllo valore initial Fuel level (<100)
  }

  sendTimeScaleFactor() {
    console.log(this.timeScaleFactor);
    return this.interactionService.getStartInteraction(this.timeScaleFactor)
      .subscribe()

  }

  stop(e:Event) {
    this.msg1= "Parameters are all reset!";
    this.interactionService.getStopInteraction().subscribe();
    this.resetForms();
    return this.msg1;
  }

  showCar(CarForm: FormGroup) {
    this.msg2="Car Injected...     I am waiting for another car."
    this.car.name = this.CarForm.controls['name'].value;
    this.car.licensePlate= this.CarForm.controls['licensePlate'].value; 
    this.car.color = this.CarForm.controls['color'].value;
    console.log(this.car);
    this.service.postCar(this.car)
      .subscribe((car: Car) => this.car = car);
    this.resetForms();
    this.msg2;
  }


  submit(ScenarioForm:FormGroup, formSelect:FormControl) {
    this.scenario.initialFuelLevel = this.ScenarioForm.controls['fuel'].value;
    this.timeScaleFactor = this.ScenarioForm.controls['time'].value;
    this.loadScenario(this.scenario, this.formSelect.value);
    this.sendTimeScaleFactor();

  }

  resetForms(){
    this.CarForm.reset();
    this.ScenarioForm.reset();
  }

  

}