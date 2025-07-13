import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { NewsService } from '../services/news.service';
import { Observable } from 'rxjs';
import { News } from '../models/News';
import { Articles } from '../models/Articles';
import { CommonModule } from '@angular/common';
import { ActionType } from '../models/ActionType';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';


@Component({
  selector: 'app-news',
  imports: [CommonModule],
  templateUrl: './news.component.html',
  styleUrl: './news.component.css'
})
export class NewsComponent implements OnInit{
  constructor(private newsService:NewsService, private router:Router, private http:HttpClient) {
  }
  @Output() articlesEmitted = new EventEmitter<Articles[]>();

  podcastURL:string  = "http://localhost:8000/audio/podcast_208bb0fa6ab44ec8b8bec5d23d4417db.mp3"
  // news.component.ts
  @Input() title!: string;
  @Input() description!: string;
  @Input() image!: string;
  @Input() url!: string; // URL of the news article

  @Output() newsClicked = new EventEmitter<string>();

onCardClick() { 
  this.newsClicked.emit(this.url);  // or emit the full article if needed
}

  handleAction(type: string) {
    if(type == "video"){
      this.router.navigate(['/generate-video', 14]); // hardcoded ID for demonstration, replace with actual ID as needed
    }else if(type == "blog"){
      this.router.navigate(['/generate-blog', 14]); // hardcoded ID for demonstration, replace with actual ID as needed   
    }
    else{
     const podcastURL = "http://localhost:8000/audio/podcast_208bb0fa6ab44ec8b8bec5d23d4417db.mp3";
    this.router.navigate(['/podcast-player'], {
      queryParams: { audio: podcastURL }
    })

  }
    /**
    else if (type === 'podcast') {
      this.http.post<{ audio: string }>('http://localhost:8000/generate-podcast', {
        data: { url: "https://www.space.com/space-exploration/launches-spacecraft/spacex-starlink-12-17-b1083-kennedy-space-center"}
      }).subscribe({
        next: (res) => {
          const audioUrl = res.audio;
          // Now route to a podcast-player page and pass the audio URL
          this.router.navigate(['/podcast-player'], {
            queryParams: { audio: audioUrl }
          });
        },
        error: (err) => console.error('Podcast generation failed', err)
      });
    }
      **/

  }


  handleClose() {
    //this.action = null;
    
  }
  
  articleList: Array<Articles> = [];
  /**
   * we define the observable that returns the trending news, in which we can subscribe to emit the data
   */
  news$ !:Observable<Array<News>>  

  

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
          this.articlesEmitted.emit(this.articleList); 
          
          
        }  
        
      },
      error:(error:Error)=>{
        throw new Error(error.message)
      }
    })
  }

  //https://chicago.suntimes.com/horoscopes/2025/04/13/horoscopes-today-sunday-april-13-2025
  
}


