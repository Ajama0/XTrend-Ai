import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { Observable } from 'rxjs';
import { PodcastResponse } from '../models/PodcastResponse';
import { PodcastRequest } from '../models/PodcastRequest';
import { Router } from '@angular/router';
import { interval, exhaustMap, takeWhile, first } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class PodcastService {

  api_base = environment.apiBaseUrl;
  constructor(private http:HttpClient, private router:Router) { }


  createPodcastFromNews(podcastRequest:PodcastRequest):Observable<PodcastResponse>{
    // This method will call the backend to create a podcast
    // and return an observable of PodcastResponse
    return this.http.post<PodcastResponse>(`${this.api_base}/podcast/create/from/news`, podcastRequest);
  }

  createPodcastFromPdf(podcastRequest:FormData):Observable<PodcastResponse>{
    return this.http.post<PodcastResponse>(`${this.api_base}/podcast/create/from/file`, podcastRequest);
  }

  createPodcastFromInput(podcastRequest:PodcastRequest):Observable<PodcastResponse>{
    return this.http.post<PodcastResponse>(`${this.api_base}/podcast/create/from/input`, podcastRequest);
  }

    /**
     * 
     * @param podcastId - This method will call the backend to get the status of a podcast
     */
    getPodcastStatus(id: number): Observable<PodcastResponse> {
      return this.http.get<PodcastResponse>(`${this.api_base}/podcast/status/${id}`);
    }
    
    /**
     * 
     * @param id - this represents the podcast id
     * @returns  - returns the presigned url for the podcast
     */
    getPresignedUrl(id: string): Observable<PodcastResponse> {
      const endpoint = `${this.api_base}/podcast/download/${id}`;
      console.log(endpoint)
      return this.http.get<PodcastResponse>(endpoint);
  }

  pollPodcastStatus$(podcastId: number): Observable<PodcastResponse> {
    return interval(3000).pipe(
      exhaustMap(() => this.getPodcastStatus(podcastId)),
      takeWhile(r => r.status !== 'COMPLETED' && r.status !== 'FAILED', true),
      first() // emit the terminal response and complete
    );
  }
  
  }


