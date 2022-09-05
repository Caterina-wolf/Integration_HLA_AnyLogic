import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { Scenario } from './scenario.model';

@Injectable({
  providedIn: 'root'
})
export class ScenarioService {
scenarioConfig= environment.masterUrl + "/scenario";

  constructor(private http: HttpClient) { }

  getScenario(scenarioName:string, initialFuelLevel:number): Observable<void> {
    return this.http.get<void>(this.scenarioConfig + "/" + scenarioName + "/" + initialFuelLevel);
  }

}
