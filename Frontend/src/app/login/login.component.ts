import { Component, OnInit } from '@angular/core';
import { UserService } from '../user.service';

@Component({
  selector: 'app-login',
  imports: [],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent implements OnInit{
  
  
  ngOnInit(): void {

    //this runs as soon as the component is instatiated and the constructor is resolved
    this.testerMethod()
  }

  constructor(private user:UserService){}


  testerMethod(){
    this.user.testerMethod().subscribe({
      next:(data:string)=>{
        console.log('data returned')
        console.log(data)
      },

      error:(err:Error)=>{
        console.log(err.message)
      }
    })

  }



}
