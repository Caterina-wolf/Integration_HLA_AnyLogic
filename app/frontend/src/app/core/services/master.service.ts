import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class MasterService {
connectionConfig= environment.masterUrl + "/init";
disconnectionConfig = environment.masterUrl + "/quit";

  constructor(private http: HttpClient) { }

  getMasterConnection():Observable<void>{
    return this.http.get<void>(this.connectionConfig);
  }

  getMasterDisconnection():Observable<void>{
    return this.http.get<void>(this.disconnectionConfig);
  }
}
