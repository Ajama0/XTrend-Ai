import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { Observable } from 'rxjs';
import { PodcastResponse } from '../models/PodcastResponse';
import { PodcastRequest } from '../models/PodcastRequest';

@Injectable({
  providedIn: 'root'
})
export class PodcastService {

  api_base = environment.apiBaseUrl;
  constructor(private http:HttpClient) { }


  createPodcast(podcastRequest:PodcastRequest):Observable<PodcastResponse>{
    // This method will call the backend to create a podcast
    // and return an observable of PodcastResponse
    return this.http.post<PodcastResponse>(`${this.api_base}/podcast/generate`, podcastRequest);
  }

    /**
     * 
     * @param podcastId - This method will call the backend to get the status of a podcast
     */
    getPodcastStatus(id: number): Observable<PodcastResponse> {
      return this.http.get<PodcastResponse>(`${this.api_base}/podcast/status/${id}`);
    }
    
    getPresignedUrl(id: string): Observable<PodcastResponse> {
      const endpoint = `${this.api_base}/podcast/download/${id}`;
      console.log(endpoint)
      return this.http.get<PodcastResponse>(endpoint);
  }

  }


