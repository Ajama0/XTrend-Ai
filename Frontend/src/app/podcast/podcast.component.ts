import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { PodcastService } from '../services/podcast.service';
import { Observable } from 'rxjs';
import { PodcastResponse } from '../models/PodcastResponse';

@Component({
  selector: 'app-podcast',
  imports: [],
  templateUrl: './podcast.component.html',
  styleUrl: './podcast.component.css'
})
export class PodcastComponent {
  audioUrl!:string;
  title = 'SpaceX launches 21 Starlink satellites on Falcon 9 rocket, lands booster on ship at sea - Space.com';
  description = 'It was SpaceXs 41st Falcon 9 mission of the year.';
  duration = 332; // in seconds
  category = 'Technology';
  generatedAt = '2 minutes ago';
  imageUrl!:String

  currentTime = 0;
  isPlaying = false;
  volume = 75;
  isMuted = false;

  podcast$!:Observable<PodcastResponse>

  constructor(private route: ActivatedRoute, private podcastService:PodcastService) {}

  ngOnInit(): void {
    const podcastId = this.route.snapshot.paramMap.get("id")
    console.log("Podcast ID from route:", podcastId);
    if(podcastId){
      this.podcast$ = this.podcastService.getPresignedUrl(podcastId)
      this.recievePodcast();
    }
  }


  recievePodcast():void{
    this.podcast$.subscribe({
      next:(response:PodcastResponse)=>{
        if(response !== null && response !== undefined){
          console.log("response from s3 download:", response);
          this.audioUrl = response.url ?? '';
          console.log("Podcast URL:", this.audioUrl);
          this.imageUrl = response.imageUrl ?? '';

        }else{
          throw new Error("Podcast response is null or undefined")
        }
      },
      error:(err:Error)=>{
        console.error("Error fetching podcast:", err);
      }
    })
  }









  formatTime(seconds: number): string {
    const mins = Math.floor(seconds / 60);
    const secs = seconds % 60;
    return `${mins}:${secs.toString().padStart(2, '0')}`;
  }

  togglePlay(audio: HTMLAudioElement) {
    this.isPlaying = !this.isPlaying;
    this.isPlaying ? audio.play() : audio.pause();
    
  }

  skipForward(audio: HTMLAudioElement) {
    audio.currentTime += 15;
  }

  skipBackward(audio: HTMLAudioElement) {
    audio.currentTime -= 15;
  }

  toggleMute(audio: HTMLAudioElement) {
    this.isMuted = !this.isMuted;
    audio.muted = this.isMuted;
  }

  updateVolume(audio: HTMLAudioElement, value: number) {
    this.volume = value;
    audio.volume = value / 100;
  }

  onTimeUpdate(audio: HTMLAudioElement) {
    this.currentTime = audio.currentTime;
  }



}
