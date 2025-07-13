import { Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { HomeComponent } from './home/home.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { NewsComponent } from './news/news.component';
import { VideogenComponent } from './videogen/videogen.component';
import { PodcastComponent } from './podcast/podcast.component';
import { BlogComponent } from './blog/blog.component';

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
    },
    
        { path: 'generate-video/:id', 
        component: VideogenComponent },

        {
            path: 'podcast-player',
            component:PodcastComponent
        },
        {
            path: 'generate-blog/:id',
            component:BlogComponent
        }

        
    
];
