import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { Observable, interval } from 'rxjs';
import { exhaustMap, takeWhile, last, distinctUntilChanged, switchMap } from 'rxjs/operators';
import { PodcastResponse } from '../models/PodcastResponse';
import { PodcastRequest } from '../models/PodcastRequest';

@Injectable({
  providedIn: 'root'
})
export class PodcastService {

  api_base = environment.apiBaseUrl;
  constructor(private http:HttpClient) { }


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

  /**
   * Get all podcasts for the current user
   * @returns Observable<PodcastResponse[]>
   */
  getMyPodcasts(): Observable<PodcastResponse[]> {
    return this.http.get<PodcastResponse[]>(`${this.api_base}/podcast/my-podcasts`);
  }

  // Removed pollPodcastStatus$ - polling now handled in components with more flexibility
}


