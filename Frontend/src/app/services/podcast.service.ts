import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { Observable } from 'rxjs';
import { PodcastResponse } from '../models/PodcastResponse';
import { PodcastRequest } from '../models/PodcastRequest';
import { Router } from '@angular/router';

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


  /**
   * This method will poll the backend for the podcast status
   * we can use setInterval to poll the backend every few seconds
   * 
   * @param podcastResponse  - referes ti the response from the backend
   */
  pollPodcastStatus(podcastResponse: PodcastResponse): void {
    console.log("Polling for podcast status with ID:", podcastResponse.podcastId);
    const intervalId = setInterval(() => {
      this.getPodcastStatus(podcastResponse.podcastId).subscribe({
        next: (response: PodcastResponse) => {
          console.log("Current podcast status:", response.status);
          if (response.status === 'COMPLETED' || response.status === 'FAILED') {
            console.log("Final podcast status:", response.status);
            clearInterval(intervalId);
            if(response.status === 'COMPLETED'){
              // Instead of automatically navigating, show notification
              if (confirm('Your podcast is ready! Would you like to check it out in My Podcasts?')) {
                this.router.navigate(['/my-podcasts']);
              }
            }else if(response.status === 'FAILED'){
              console.error("Podcast generation failed.");
              alert('Podcast generation failed. Please try again.');
            }
          }
        },
        error: (err: Error) => {
          console.error("Error fetching podcast status:", err.message);
          clearInterval(intervalId); // Stop polling on error
        }
      });
    }, 3000); // Poll every 3 seconds (faster polling)
  }



  }


