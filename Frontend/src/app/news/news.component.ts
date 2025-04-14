import { Component } from '@angular/core';
import { NewsService } from '../services/news.service';
import { Observable } from 'rxjs';
import { News } from '../models/News';
import { Articles } from '../models/Articles';

@Component({
  selector: 'app-news',
  imports: [],
  templateUrl: './news.component.html',
  styleUrl: './news.component.css'
})
export class NewsComponent {

  articles!:Array<Articles>
  /**
   * we define the observable that returns the trending news, in which we can subscribe to emit the data
   */
  news$ !:Observable<News>  
  constructor(newsService:NewsService){
    this.news$ = newsService.getAllTrendingNews()
    
  }

  fetchAllTrendingNews(){
    this.news$.subscribe({
      next:(news:News)=>{
        ///store the value of the return so that we can pass it as an input to the dashboard component
        const articlesList = news.articles
        if(articlesList.length == 0){
          console.log("no articles were returned")
        }
        else{
          console.log("the number of articles returned is : ",articlesList.length)
          this.articles = articlesList
        }  
        
      },
      error:(error:Error)=>{
        throw new Error(error.message)
      }
    })
  }

  
  
}
