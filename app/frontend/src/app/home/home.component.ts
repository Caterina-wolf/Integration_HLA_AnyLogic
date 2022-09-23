import { Component, OnInit, ViewChild } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Subject, timeout } from 'rxjs';
import { Car } from '../core/models/car/car.model';
import { CarService } from '../core/models/car/car.service';
import { Scenario } from '../core/models/scenario/scenario.model';
import { ScenarioService } from '../core/models/scenario/scenario.service';
import { InteractionService } from '../core/services/interaction.service';
import { MasterService } from '../core/services/master.service';
import { NgbAlert } from '@ng-bootstrap/ng-bootstrap';
import { debounceTime } from 'rxjs/operators';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  msg0:string;
  msg1:string;
  //msg2:string;
  //msg3:string;
  scenario: Scenario = new Scenario();
  timeScaleFactor: number;
  CarForm: FormGroup;
  ScenarioForm : FormGroup;
  formSelect : FormControl = new FormControl('');
  toggled : boolean = false;
  // private success0 = new Subject<string>();
  // private success1= new Subject<string>();
  submitSuccess = '';
  carInjectSuccess = '';
  //Quando  submitSucces e CarInjectSuccess vengono riempiti allora ngIf è a True
  //!!!IMPORTANTE!!!! Approfondire ngIf logica!!!! 

  // @ViewChild('selfClosingAlert', {static: false}) selfClosingAlert: NgbAlert;


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
        Validators.minLength(7),
        Validators.maxLength(7),
        Validators.pattern('#[0-9a-fA-F]+')])
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
    });

    // this.success0.subscribe(message => this.successMessage0 = message);
    // this.success0.pipe(debounceTime(4000)).subscribe(() => {
    //   if (this.selfClosingAlert) {
    //     this.selfClosingAlert.close();
    //   }
    // });
    // this.success1.subscribe(message => this.successMessage1 = message);
    // this.success1.pipe(debounceTime(3000)).subscribe(() => {
    //   if (this.selfClosingAlert) {
    //     this.selfClosingAlert.close();
    //   }
    // });
  }


  connectEvent(e:MouseEvent){
    this.msg0 = "Connected";
    this.masterService.getMasterConnection().subscribe();
    return this.msg0;
  }

  disconnectEvent(e:MouseEvent){
    this.msg0 = "Disconnected";
    this.masterService.getMasterDisconnection().subscribe();
    this.msg1 = null;
    return this.msg0;
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
    this.resetMessage();
    return this.msg1;
  }

  showCar(CarForm: FormGroup) {
    //this.msg2="Car Injected... post another car."
    const car = new Car();
    car.name = this.CarForm.controls['name'].value;
    car.licensePlate= this.CarForm.controls['licensePlate'].value; 
    car.color = this.CarForm.controls['color'].value;
    console.log(car);
    this.service.postCar(car)
      .subscribe((car: Car) => {
        car = car;
        setTimeout(() => this.successMessage0 = null, 2000);
      });
    this.success0.next(this.msg2);
    this.resetForms();
  
  }


  submit(ScenarioForm:FormGroup, formSelect:FormControl) {
    this.msg3= "  Submit succesfull!!";
    this.scenario.initialFuelLevel = this.ScenarioForm.controls['fuel'].value;
    this.timeScaleFactor = this.ScenarioForm.controls['time'].value;
    this.loadScenario(this.scenario, this.formSelect.value);
    this.sendTimeScaleFactor();
    this.success1.next(this.msg3); //success non esistono
  }

  resetForms(){
    this.CarForm.reset();
    this.ScenarioForm.reset();

  } 

  resetMessage(){

    this.msg2=null;
    this.msg3=null;
  }
  
}