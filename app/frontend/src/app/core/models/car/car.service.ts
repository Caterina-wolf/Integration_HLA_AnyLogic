import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { Car } from './car.model';

@Injectable({
  providedIn: 'root'
})
export class CarService {
carConfig = environment.masterUrl + "/inject";

  constructor(private http: HttpClient) {
   }

   postCar(car: Car): any{
    
    return this.http.post<any>(this.carConfig, car);
   }

  
}
