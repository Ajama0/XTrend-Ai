import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, Observable, throwError } from 'rxjs';
import { User } from '../models/User';

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

  fetchTestUser():Observable<User> {
    const username = 'harry@example.com'
    const getUser = `${this.Base_URL}/username?name=${username}`
    ///fetch user by the username
    return this.http.get<User>(getUser)
    .pipe(
      catchError(err=>{
        console.error('error finding user', err)
        return throwError(() => new Error('Error fetching user'));

      })
    )
   
  }

}
