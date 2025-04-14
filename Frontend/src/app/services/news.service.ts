import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { News } from '../models/News';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class NewsService {

  API_BASE = environment.apiBaseUrl
  constructor(private http:HttpClient) { }


  getAllTrendingNews():Observable<Array<News>>{
    const allNewsUrl = `${this.API_BASE}/news/all/articles`
    console.log(allNewsUrl)
    return this.http.get<Array<News>>(allNewsUrl);

  }

}
