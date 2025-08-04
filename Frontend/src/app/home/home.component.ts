import { Component, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import { NavbarComponent } from '../navbar/navbar.component';
import { HerosectionComponent } from '../herosection/herosection.component';
import { CommonModule } from '@angular/common';
import { FeaturecardComponent } from '../featurecard/featurecard.component';

@Component({
  selector: 'app-home',
  imports: [NavbarComponent, HerosectionComponent, CommonModule, FeaturecardComponent],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent implements OnInit{
  ngOnInit(): void {
   console.log("in home")
  }


  features = [
    
    {
      title : "Explore with Rela AI",
      description : "Create dynamic Multi-lingual podcasts that features a conversation between two voices, making it perfect for research, discussions, and storytelling. We offer different durations ranging from 3-5 minutes for shorter podcasts, and 15-30 minutes for more in-depth conversations.",
      image : "https://cdn.pixabay.com/photo/2024/01/25/07/57/ai-generated-8531273_1280.jpg",
      badgeText : "PODCAST"
    }

]



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
    { title: 'Podcast from News', description: 'Generate podcasts from the latest trending news.' },
    { title: 'Podcast from Images', description: 'Turn images into audio stories.' },
    { title: 'Podcast from Text', description: 'Convert any text into a podcast.' },
    { title: 'Podcast from PDFs', description: 'Upload PDFs and get instant podcasts.' },
    { title: 'Podcast from Transcripts', description: 'Transform lengthy transcripts into podcasts.' },
     { title: 'Podcast from Urls', description: 'Turn any url into a podcast by passing in just a url to the input.' }
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
