import { Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { HomeComponent } from './home/home.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { NewsComponent } from './news/news.component';
import { PodcastComponent } from './podcast/podcast.component';
import { BlogComponent } from './blog/blog.component';
import { TempComponent } from './temp/temp.component';
import { ExploreComponent } from './explore/explore.component';

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
    {
            path: 'podcast-player/:id',
            component:PodcastComponent
        },
        {
            path: 'generate-blog/:id',
            component:BlogComponent
        },        {
            path: 'temp/audio',
            component: TempComponent
        },
        {
            path: 'explore',
            component: ExploreComponent
        },
        {
            path: 'my/podcasts',
            component: PodcastComponent
        }
];
