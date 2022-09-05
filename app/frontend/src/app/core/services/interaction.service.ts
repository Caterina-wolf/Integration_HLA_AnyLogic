import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class InteractionService {
  startConfig = environment.masterUrl + "/start";
  stopConfig = environment.masterUrl + "/stop";
  constructor(private http: HttpClient) { }

  getStartInteraction(time: number): Observable<void> {
    return this.http.get<void>(this.startConfig + "/" + time);
  }

  getStopInteraction(): Observable<void> {
    return this.http.get<void>(this.stopConfig);
  }


}
