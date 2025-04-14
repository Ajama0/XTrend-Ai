import { Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { HomeComponent } from './home/home.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { NewsComponent } from './news/news.component';

export const routes: Routes = [


    {
        path: 'login',
        component: LoginComponent
    }
    ,
    {
        path: "",
        component : HomeComponent
    },

    {
        path:'dashboard',
        component : DashboardComponent
    },

    {
        path :'news',
        component : NewsComponent
    }
];
