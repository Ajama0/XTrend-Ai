import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { validate } from 'json-schema';
import { Router } from '@angular/router';
import { User } from '../models/User';
import { UserService } from '../services/user.service';



@Component({
  selector: 'app-login',
  imports: [ReactiveFormsModule, CommonModule],
  standalone:true,
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent implements OnInit{
  
  loginForm!:FormGroup
  testUser!:User
  userReturned : boolean = false
  ngOnInit(): void {
   

    //this runs as soon as the component is instatiated and the constructor is resolved
    //this.testerMethod()
    this.formStructure()
    this.fetchTestUserCredentials()
  }

  constructor(private user:UserService, private formBuilder:FormBuilder, private router:Router){}


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

  formStructure(){
    this.loginForm = this.formBuilder.group({
      'email' : ['', Validators.required],
      'password' : ['', Validators.required],
       //the interests part will be a checkbox which defines the users interests (only present in the signup section)
    })

  }

  fetchTestUserCredentials(){
    this.user.fetchTestUser().subscribe({
      next:(data:User)=>{
        console.log("data returned ", data)
        this.testUser = data
        this.userReturned = true
      }

    })
  }

  //on form submission this is called. 
  validateForm(){
    if(this.userReturned && this.loginForm.valid){
      /** 
      console.log(this.loginForm.controls['email'].value)
      console.log(this.loginForm.controls['password'])
      console.log(this.testUser)
      */
      if(this.loginForm.controls['email'].value === this.testUser.email && this.loginForm.controls['password'].value === this.testUser.password){
        ///user will be routed to the homepage in which they can be begin creating content!
        console.log('successfull login')
        this.router.navigate(['/dashboard'])
       }
    }

    else{
      throw new Error('please use the correct credentials')
    }
    
  }




  



  



}
