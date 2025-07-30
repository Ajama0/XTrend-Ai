import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { Observable } from 'rxjs';
import { NewsDTO } from '../models/newsDTO';

@Injectable({
  providedIn: 'root'
})
export class NewsService {

  API_BASE = environment.apiBaseUrl
  constructor(private http:HttpClient) { }


  getAllTrendingNews():Observable<NewsDTO>{
    const allNewsUrl = `${this.API_BASE}/news/all/articles`
    console.log(allNewsUrl)
    return this.http.get<NewsDTO>(allNewsUrl);

  }

}
