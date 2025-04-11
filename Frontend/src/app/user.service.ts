import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UserService {


  Base_URL = "http://localhost:8066/api/v1/XTrend"
  constructor(private http:HttpClient) { }



  testerMethod():Observable<string>{
    const tester_endpoint= `${this.Base_URL}/test`
    return this.http.get(tester_endpoint, {responseType: 'text'})

  }
}
