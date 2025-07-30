import { Component } from '@angular/core';
import { NewsComponent } from '../news/news.component';
import { Articles } from '../models/Articles';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { NewsDTO } from '../models/newsDTO';

@Component({
  selector: 'app-dashboard',
  imports: [NewsComponent, CommonModule, RouterLink],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css'
})
export class DashboardComponent {

  constructor(private router: Router) {}
  dashboardArticles: Articles[] = [];

  receiveArticles(news: NewsDTO) {
    this.dashboardArticles = news.article
    console.log("event emitted from news component, articles received in dashboard component");
  }



  
  onNewsClick(url: string) {
    console.log('News clicked:', url);
    this.router.navigate(['/article-view'], { queryParams: { url } });
  }

}
