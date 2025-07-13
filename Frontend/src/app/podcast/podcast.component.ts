import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-podcast',
  imports: [],
  templateUrl: './podcast.component.html',
  styleUrl: './podcast.component.css'
})
export class PodcastComponent {
  audioUrl: string = '';
  title = 'SpaceX launches 21 Starlink satellites on Falcon 9 rocket, lands booster on ship at sea - Space.com';
  description = 'It was SpaceXs 41st Falcon 9 mission of the year.';
  duration = 332; // in seconds
  category = 'Technology';
  generatedAt = '2 minutes ago';
  url :string =  "https://cdn.mos.cms.futurecdn.net/u4riJDr7eyvWSGFuk9vsCe-1200-80.jpg"

  currentTime = 0;
  isPlaying = false;
  volume = 75;
  isMuted = false;

  constructor(private route: ActivatedRoute) {}

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      this.audioUrl = params['audio'];
      console.log('Received audio URL:', this.audioUrl);
    });
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
