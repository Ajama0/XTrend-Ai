import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { NewsService } from '../services/news.service';
import { Observable } from 'rxjs';
import { Articles } from '../models/Articles';
import { CommonModule } from '@angular/common';
import { ActionType } from '../models/ActionType';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { NewsDTO } from '../models/newsDTO';
import { PodcastResponse } from '../models/PodcastResponse';
import { PodcastService } from '../services/podcast.service';
import { PodcastRequest } from '../models/PodcastRequest';
import { switchMap, finalize } from 'rxjs/operators';


@Component({
  selector: 'app-news',
  imports: [CommonModule],
  template: `<!-- News component now serves as data provider only -->`,
  styleUrl: './news.component.css'
})
export class NewsComponent implements OnInit{
  constructor(private newsService:NewsService, private router:Router, private http:HttpClient, private podcastService:PodcastService) { 
    // You can inject other services here if needed
  }
  
  // news.component.ts
  @Input() title!: string;
  @Input() description!: string;
  @Input() image!: string;
  @Input() url!: string; // URL of the news article
  @Input() newsId!:number

  @Output() newsClicked = new EventEmitter<string>();
  @Output() articlesEmitted = new EventEmitter<NewsDTO>();

  podcast$ !: Observable<PodcastResponse>

    /**
   * we define the observable that returns the trending news, in which we can subscribe to emit the data
   */
  news$ !:Observable<NewsDTO>


onCardClick() { 
  this.newsClicked.emit(this.url);  // or emit the full article if needed
}

  handleAction(type: string) {
    if(type == "podcast"){
      //function called here
      this.generateFromNews()

  }

}


  handleClose() {
    //this.action = null;
    
  }

  

  ngOnInit(): void {
    console.log("making request")
    this.news$ = this.newsService.getAllTrendingNews()
    
    console.log("about to subscribe")
    this.fetchAllTrendingNews()
  }

  fetchAllTrendingNews(){
    this.news$.subscribe({
      next:(news:NewsDTO)=>{
        ///store the value of the return so that we can pass it as an input to the dashboard component
        console.log("the news object returned is : ",news)
        console.log(news.article.length)
    
        if(news.article.length == 0){
          console.log("no articles were returned")

        }
        else{
          console.log("the number of articles returned is : ",news.article.length)
          console.log(news)
          this.articlesEmitted.emit(news); 
          
          
        }  
        
      },
      error:(error:Error)=>{
        throw new Error(error.message)
      }
    })
  }




  generateFromNews():void{
    //**
    // we call podcast service to generate a podcast
    // we return a podcast response which contains the information for polling
    // we use this response to poll the backend for the podcast status
    // */
    
    const podcastRequest :PodcastRequest = 
    {
      newsId : this.newsId,
      email :"harry@example.com", //TODO: get the email from the user
      contentForm: "SHORT",
      podcastType: "NEWS"
    }
  
    this.podcast$ = this.podcastService.createPodcastFromNews(podcastRequest);

    this.podcastService.createPodcastFromNews(podcastRequest).pipe(
          switchMap(res => this.podcastService.pollPodcastStatus$(res.podcastId)),
          ).subscribe({
            next:(response:PodcastResponse)=>{
              console.log("podcast completed!")
              if(response.status === 'COMPLETED'){
                alert("podcast ready!, check my podcasts to view yours")
              }else{
                alert("Podcast creation failed")
              }
            }, 
            error: (err:Error)=>{
              throw Error("error whilst creating / polling")
            }
          })
    
        }
    }

  
  
  


  





