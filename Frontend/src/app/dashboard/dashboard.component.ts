import { Component } from '@angular/core';
import { NewsComponent } from '../news/news.component';
import { Articles } from '../models/Articles';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';

@Component({
  selector: 'app-dashboard',
  imports: [NewsComponent, CommonModule, RouterLink],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css'
})
export class DashboardComponent {

  constructor(private router: Router) {}
  dashboardArticles: Articles[] = [];

  receiveArticles(articles: Articles[]) {
    this.dashboardArticles = articles;
    console.log("Received articles in dashboard:", articles);
  }



  
  onNewsClick(url: string) {
    console.log('News clicked:', url);
    this.router.navigate(['/article-view'], { queryParams: { url } });
  }

}
