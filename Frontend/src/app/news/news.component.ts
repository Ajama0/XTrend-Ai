import { Component, OnInit } from '@angular/core';
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
export class NewsComponent implements OnInit{

  
  articleList:Array<Articles> = []
  /**
   * we define the observable that returns the trending news, in which we can subscribe to emit the data
   */
  news$ !:Observable<Array<News>>  

  constructor(private newsService:NewsService){
  }

  ngOnInit(): void {
    console.log("making request")
    this.news$ = this.newsService.getAllTrendingNews()
    
    console.log("about to subscribe")
    this.fetchAllTrendingNews()
  }

  fetchAllTrendingNews(){
    this.news$.subscribe({
      next:(news:Array<News>)=>{
        ///store the value of the return so that we can pass it as an input to the dashboard component
    
        if(news.length == 0){
          console.log("no articles were returned")

        }
        else{
          console.log("the number of articles returned is : ",news.length)
          this.articleList = news.map(n=>n.article)
          console.log(this.articleList)
          
        }  
        
      },
      error:(error:Error)=>{
        throw new Error(error.message)
      }
    })
  }

  
  
}
