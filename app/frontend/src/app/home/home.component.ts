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
  car: Car = new Car();
  scenario: Scenario = new Scenario();
  timeScaleFactor: number;
  form: FormGroup;
  formSelect : FormControl = new FormControl('');
  form2 : FormGroup;


  constructor(public service: CarService, public masterService: MasterService, public scenarioService: ScenarioService, public interactionService: InteractionService) { }

  ngOnInit(): void {
    this.form = new FormGroup({
      name: new FormControl(''),
      licensePlate: new FormControl(''),
      color: new FormControl('')
    });
    this.form2 = new FormGroup({
      fuel : new FormControl(''),
      time : new FormControl('')
    });
    this.form.valueChanges.subscribe((input) => {
      console.log(input);

    });
    this.form2.valueChanges.subscribe((input2) => {
      console.log(input2);
    });
    this.formSelect.valueChanges.subscribe((input3) => {
      console.log(input3);
    })
  }

  connectEvent(e:Event) {
    this.msg = "Connected";
    this.masterService.getMasterConnection().subscribe();
    return this.msg;
  }

  disconnectEvent(e:Event) {
    this.msg = "Disconnected";
    this.masterService.getMasterDisconnection().subscribe();
    return this.msg;
  }

  loadScenario(scenario: Scenario, formSelect: FormControl) {
    console.log(this.scenario);
    this.scenario.scenarioName = this.formSelect.value;
    this.scenarioService.getScenario(this.scenario.scenarioName, this.scenario.initialFuelLevel)
      .subscribe(() => this.scenario = scenario);
  }

  sendTimeScaleFactor() {
    console.log(this.timeScaleFactor);
    return this.interactionService.getStartInteraction(this.timeScaleFactor)
      .subscribe()

  }

  stop(e:Event) {
    this.msg1= "The simulation has been stopped, so the values are all reset!";
    this.interactionService.getStopInteraction().subscribe();
    return this.msg1;
  }

  showCar(form: FormGroup) {
    this.car.name = this.form.controls['name'].value;
    this.car.licensePlate= this.form.controls['licensePlate'].value; 
    this.car.color = this.form.controls['color'].value;
    console.log(this.car);
    this.service.postCar(this.car)
      .subscribe((car: Car) => this.car = car);
  }


  submit(form2:FormGroup, formSelect:FormControl) {
    this.scenario.initialFuelLevel = this.form2.controls['fuel'].value;
    this.timeScaleFactor = this.form2.controls['time'].value;
    this.loadScenario(this.scenario, this.formSelect.value);
    this.sendTimeScaleFactor()
  }


}