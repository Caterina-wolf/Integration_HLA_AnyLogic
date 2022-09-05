import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
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
  car: Car;
  scenario: Scenario;
  timeScaleFactor: number;
  form: FormGroup;


  constructor(public service: CarService, public masterService: MasterService, public scenarioService: ScenarioService, public interactionService: InteractionService) { }

  connectEvent() {
    this.msg = "Connected";
    this.masterService.getMasterConnection().subscribe();
    return this.msg;
  }

  disconnectEvent() {
    this.msg = "Disconnected";
    this.masterService.getMasterDisconnection().subscribe();
    return this.msg;
  }

  showCar() {
    console.log(this.car);
    this.service.postCar(this.car)
      .subscribe((car: Car) => this.car = car);
    //  this.form
    // voglio prendere da input cioè il form 
    //la car che deve essere mandata sul modello di AnyLogic
    }

  loadScenario(scenario: Scenario) {
    console.log(this.scenario);
    this.scenarioService.getScenario(this.scenario.scenarioName, this.scenario.initialFuelLevel)
      .subscribe(() => this.scenario = scenario);
  }

  sendTimeScaleFactor() {
    console.log(this.timeScaleFactor);
    return this.interactionService.getStartInteraction(this.timeScaleFactor)
      .subscribe()

  }

  stop() {
    return this.interactionService.getStopInteraction().subscribe()
  }

   submit(){
    this.loadScenario(this.scenario);
    this.sendTimeScaleFactor()
 }

  ngOnInit(): void { 
    this.form = new FormGroup ({
      name : new FormControl(''),
      licensePlate : new FormControl(''),
      color : new FormControl('')
    });
    this.form.valueChanges.subscribe((input)=>{
      console.log(input);
        
    })
  }
}