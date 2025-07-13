import { Injectable } from '@angular/core';
import { environment } from '../environments/environment';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class DashboardService {

  API_BASE = environment.apiBaseUrl

  constructor(private http:HttpClient) { }
}
