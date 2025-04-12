import { Component, OnInit } from '@angular/core';
import { UserService } from '../user.service';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { validate } from 'json-schema';
import { Router } from '@angular/router';


@Component({
  selector: 'app-login',
  imports: [ReactiveFormsModule, CommonModule],
  standalone:true,
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent implements OnInit{
  
  loginForm!:FormGroup
  ngOnInit(): void {

    //this runs as soon as the component is instatiated and the constructor is resolved
    this.testerMethod()
    this.formStructure()
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
      'firstname' : ['', Validators.required],
      'lastname' : ['', Validators.required],
      'email' : ['', Validators.required],
      'password' : ['', Validators.required],
       //the interests part will be a checkbox which defines the users interests (only present in the signup section)
    })

  }

  //on form submission this is called. 
  validateForm(form:FormGroup){
   if(form.controls['email'].value === 'harry@example.com' && form.controls['password'].value === 'harry1234'){
    ///user will be routed to the homepage in which they can be begin creating content!
    console.log('successfull login')
    this.router.navigate(['/dashboard'])
   }
    else{
      throw new Error('please use the correct credentials')
    }
    
  }




  



  



}
