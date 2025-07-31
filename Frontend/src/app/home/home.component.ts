import { Component, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import { NavbarComponent } from '../navbar/navbar.component';
import { HerosectionComponent } from '../herosection/herosection.component';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-home',
  imports: [NavbarComponent, HerosectionComponent, CommonModule],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent implements OnInit{
  ngOnInit(): void {
   console.log("in home")
  }




    examplePodcasts = [
    {
      title: 'The Future of AI',
      description: 'Exploring the next wave of artificial intelligence.',
      duration: '6 mins',
      image: 'https://placehold.co/60x60',
      audio: 'https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3'
    },
     {
      title: 'The Future of AI',
      description: 'Exploring the next wave of artificial intelligence.',
      duration: '6 mins',
      image: 'https://placehold.co/60x60',
      audio: 'https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3'
    },
     {
      title: 'The Future of AI',
      description: 'Exploring the next wave of artificial intelligence.',
      duration: '6 mins',
      image: 'https://placehold.co/60x60',
      audio: 'https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3'
    },
     {
      title: 'The Future of AI',
      description: 'Exploring the next wave of artificial intelligence.',
      duration: '6 mins',
      image: 'https://placehold.co/60x60',
      audio: 'https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3'
    },
     {
      title: 'The Future of AI',
      description: 'Exploring the next wave of artificial intelligence.',
      duration: '6 mins',
      image: 'https://placehold.co/60x60',
      audio: 'https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3'
    },
     {
      title: 'The Future of AI',
      description: 'Exploring the next wave of artificial intelligence.',
      duration: '6 mins',
      image: 'https://placehold.co/60x60',
      audio: 'https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3'
    },
    // ...add 5 more podcast objects
  ];

   services = [
    { title: 'Podcast from News', description: 'Generate podcasts from trending news.' },
    { title: 'Podcast from Images', description: 'Turn images into audio stories.' },
    { title: 'Podcast from Text', description: 'Convert any text into a podcast.' },
    { title: 'Podcast from PDFs', description: 'Upload PDFs and get instant podcasts.' },
    { title: 'Podcast from Transcripts', description: 'Transform transcripts into audio.' }
  ];

  currentPodcast: any = null;
  isPlaying = false;

  playPodcast(podcast: any) {
    this.currentPodcast = podcast;
    this.isPlaying = true;
    // You would use an <audio> element and control playback here
  }

  togglePlay() {
    this.isPlaying = !this.isPlaying;
    // Add logic to play/pause the audio element
  }


}
