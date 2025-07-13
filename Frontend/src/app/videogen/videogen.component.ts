import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-videogen',
  imports: [CommonModule, FormsModule],
  templateUrl: './videogen.component.html',
  styleUrl: './videogen.component.css'
})
export class VideogenComponent implements OnInit {
  videoUrl !: SafeResourceUrl;
  title:string = "SpaceX launches 21 Starlink satellites on Falcon 9 rocket, lands booster on ship at sea - Space.com";
  description:string = "It was SpaceXs 41st Falcon 9 mission of the year.";
  duration:number = 332; // in seconds
  resolution:string = "1080p";
  currentTime = Date.now();
  isPlaying = false;
  isMuted = false;
  volume = 75;
  progress = 45;


  thumbnailUrl: SafeResourceUrl = "https://files2.heygen.ai/aws_pacific/avatar_tmp/ee91865d738f48c5a7d48493186cd3f0/8114182ba6e94292b9b9cb3db46b7c16.jpeg?Expires=1749016572&Signature=Kesd8yfuyoWVn9Rqn7dkPWSCrpgrIvvJAq71QYl9oS~SguME210cBmjoMUKRNgeN7SQu22kqK6nXSKRji8o5rTrPZ0JD8c75h77VBRb5IknCmHsMDRye8e3dlNBaS5ZE2KpT5M3037ndIXmOnRZH2EHpg3scnFJSaa-yG4oXwBf~ahWEkJpzVXGKZTeIICFwK7dkI188hmh0mvX1NeuRxWNR7NTcd1FrmKhbiloDkDCxWx0u3fBZwdhcMlmuDpHZkMRCyc5EoBXdEcjL66hS8tRub~sOAd9KpfFdo4wTZTgQXEfG8cA99GZbbdQrpFK~suJIYw7fscxYRnIdMeDK6w__&Key-Pair-Id=K38HBHX5LX3X2H"
  constructor(private route: ActivatedRoute, private http: HttpClient, private sanitizer:DomSanitizer) {}




  togglePlay() {
    const video = document.querySelector('video') as HTMLVideoElement;
    this.isPlaying ? video.pause() : video.play();
    this.isPlaying = !this.isPlaying;
  }

  toggleMute() {
    const video = document.querySelector('video') as HTMLVideoElement;
    video.muted = !video.muted;
    this.isMuted = video.muted;
  }
  
  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    this.http.get<{ video_url: string }>(`http://localhost:8067/api/v1/video/fetch/video/${id}`)
      .subscribe({
        next: (res) => {
          console.log("Video URL fetched: ", res.video_url);
          // Trust the video URL explicitly
          this.videoUrl = this.sanitizer.bypassSecurityTrustResourceUrl(res.video_url);
      
        },
        error: (err) => console.error('Video fetch failed', err)
      });
  }



}
