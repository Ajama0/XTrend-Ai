import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { PodcastService } from '../services/podcast.service';
import { Observable, Subscription, interval } from 'rxjs';
import { switchMap, distinctUntilChanged } from 'rxjs/operators';
import { PodcastResponse } from '../models/PodcastResponse';
import { tap } from 'rxjs/operators';
import { signedUrlResponse } from '../models/signedUrlResponse';

@Component({
  selector: 'app-podcast',
  imports: [CommonModule, RouterLink],
  templateUrl: './podcast.component.html',
  styleUrl: './podcast.component.css'
})
export class PodcastComponent implements OnInit, OnDestroy {
  podcasts: PodcastResponse[] = [];
  isLoading: boolean = true;
  currentlyPlaying: PodcastResponse | null = null;
  private pollingSubscription?: Subscription;
  private currentAudio: HTMLAudioElement | null = null;
  
  // Audio player state
  isPlaying: boolean = false;
  currentTime: number = 0;
  duration: number = 0;
  playbackRate: number = 1;
  volume: number = 1;
  
  // Notification state
  showNotification: boolean = false;
  notificationMessage: string = '';
  notificationPodcast: PodcastResponse | null = null;

  // Placeholder data for demo (since you removed it but HTML still needs it)
  placeholderPodcasts: any[] = [];

  // Math helper for templates
  Math = Math;

  /**
   * Track by function for ngFor performance
   */
  trackByPodcastId(index: number, podcast: PodcastResponse): number {
    return podcast.podcastId;
  }

  constructor(
    private podcastService: PodcastService
  ) {}

  ngOnInit(): void {
    this.loadMyPodcasts();
    // Polling will be started conditionally in loadMyPodcasts if needed
  }

  ngOnDestroy(): void {
    // Clean up polling when component is destroyed
    this.stopPolling();
    // Clean up audio and event listeners
    this.stopPodcast();
  }

  /**
   * Stop polling and clean up subscription
   */
  private stopPolling(): void {
    if (this.pollingSubscription) {
      this.pollingSubscription.unsubscribe();
      this.pollingSubscription = undefined;
      console.log('Polling stopped');
    }
  }

  /**
   * Load all user's podcasts initially
   */
  private loadMyPodcasts(): void {
    this.isLoading = true;
    this.podcastService.getMyPodcasts().subscribe({
      next: (podcasts: PodcastResponse[]) => {
        this.podcasts = podcasts;
        this.isLoading = false;
        console.log('Loaded podcasts:', podcasts);
        
        // Only start polling if there are PROCESSING podcasts
        const hasProcessingPodcasts = podcasts.some(p => p.status === 'PROCESSING');
        if (hasProcessingPodcasts) {
          console.log('Found processing podcasts, starting polling...');
          this.startPollingForProcessingPodcasts();
        } else {
          console.log('No processing podcasts found, no polling needed');
        }
      },
      error: (error) => {
        console.error('Error loading podcasts:', error);
        this.isLoading = false;
      }
    });
  }

  /**
   * Start polling for any PROCESSING podcasts
   * Updates the podcasts list when status changes
   */
  private startPollingForProcessingPodcasts(): void {
    this.pollingSubscription = interval(5000).pipe( 
      tap(() => console.log("polling again")),
      switchMap(() => this.podcastService.getMyPodcasts()),
      distinctUntilChanged((prev, curr) => 
        JSON.stringify(prev.map(p => ({id: p.podcastId, status: p.status}))) === 
        JSON.stringify(curr.map(p => ({id: p.podcastId, status: p.status})))
      )
    ).subscribe({
      next: (newPodcasts: PodcastResponse[]) => {
        console.log("status changed!")
        // Check if any podcast changed from PROCESSING to COMPLETED
        const oldProcessingPodcasts = this.podcasts.filter(p => p.status === 'PROCESSING');
        
        // Since only one podcast processes at a time, check if it completed
        if (oldProcessingPodcasts.length > 0) {
          const processingPodcast = oldProcessingPodcasts[0]; // Only one processing at a time
          const nowCompleted = newPodcasts.find(p => 
            p.podcastId === processingPodcast.podcastId && 
            (p.status === 'COMPLETED')
          );
          
          if (nowCompleted) {
            // Show notification and stop polling since processing is done
            this.showPodcastCompletedNotification(processingPodcast);
            this.stopPolling();
          }
        }

        // Update the podcasts list (this happens AFTER the comparison above)
        this.podcasts = newPodcasts;
        
      },
      error: (error) => {
        console.error('Error polling podcasts:', error);
      }
    });
  }

  /**
   * Show notification when a podcast is completed (Angular way)
   */
  private showPodcastCompletedNotification(podcast: PodcastResponse): void {
    this.notificationPodcast = podcast;
    this.notificationMessage = `Podcast "${podcast.podcastTagline || 'Untitled'}" is ready!`;
    this.showNotification = true;
    
    // Auto-hide notification after 4 seconds
    setTimeout(() => {
      this.hideNotification();
    }, 4000);
  }

  /**
   * Hide the notification
   */
  hideNotification(): void {
    this.showNotification = false;
    this.notificationMessage = '';
    this.notificationPodcast = null;
  }

  /**
   * Play a podcast using cached/new signed URL
   */
  playPodcast(podcast: PodcastResponse): void {
    if (podcast.status !== 'COMPLETED') {
      alert('Podcast is still processing. Please wait...');
      return;
    }

    // Check cached signed URL or get new one
    this.podcastService.checkIfSignedUrlIsExpired(podcast.podcastId).subscribe({
      next: (response:signedUrlResponse) => {
        if (response.url) {
          this.currentlyPlaying = podcast;
          this.playAudio(response.url);
          console.log('Playing podcast:', podcast.podcastTagline, 'URL:', response.url);
        } else {
          console.error('No URL received from backend');
          alert('Error: Unable to get podcast URL. Please try again.');
        }
      },
      error: (error) => {
        console.error('Error getting podcast URL:', error);
        alert('Error loading podcast. Please try again.');
      }
    });
  }

  /**
   * Play audio using HTML5 audio element with full controls
   */
  private playAudio(url: string): void {
    // Stop any currently playing audio
    if (this.currentAudio) {
      this.currentAudio.pause();
      this.currentAudio.removeEventListener('timeupdate', this.onTimeUpdate.bind(this));
      this.currentAudio.removeEventListener('loadedmetadata', this.onLoadedMetadata.bind(this));
      this.currentAudio.removeEventListener('ended', this.onAudioEnded.bind(this));
      this.currentAudio = null;
    }

    // Create new audio element
    this.currentAudio = new Audio(url);
    this.currentAudio.volume = this.volume;
    this.currentAudio.playbackRate = this.playbackRate;

    // Add event listeners
    this.currentAudio.addEventListener('timeupdate', this.onTimeUpdate.bind(this));
    this.currentAudio.addEventListener('loadedmetadata', this.onLoadedMetadata.bind(this));
    this.currentAudio.addEventListener('ended', this.onAudioEnded.bind(this));

    // Play audio
    this.currentAudio.play().then(() => {
      this.isPlaying = true;
    }).catch(error => {
      console.error('Error playing audio:', error);
      alert('Error playing podcast. Please try again.');
    });
  }

  /**
   * Audio event handlers
   */
  private onTimeUpdate(): void {
    if (this.currentAudio) {
      // Use setTimeout to avoid ExpressionChangedAfterItHasBeenCheckedError
      setTimeout(() => {
        this.currentTime = this.currentAudio?.currentTime || 0;
      }, 0);
    }
  }

  private onLoadedMetadata(): void {
    if (this.currentAudio) {
      // Use setTimeout to avoid ExpressionChangedAfterItHasBeenCheckedError
      setTimeout(() => {
        this.duration = this.currentAudio?.duration || 0;
      }, 0);
    }
  }

  private onAudioEnded(): void {
    this.isPlaying = false;
    this.currentTime = 0;
    this.stopPodcast();
  }

  /**
   * Audio control methods
   */
  togglePlayPause(): void {
    if (!this.currentAudio) return;

    if (this.isPlaying) {
      this.currentAudio.pause();
      this.isPlaying = false;
    } else {
      this.currentAudio.play().then(() => {
        this.isPlaying = true;
      }).catch(error => {
        console.error('Error playing audio:', error);
      });
    }
  }

  seekTo(time: number): void {
    if (this.currentAudio) {
      this.currentAudio.currentTime = time;
      this.currentTime = time;
    }
  }

  onSeekChange(event: Event): void {
    const target = event.target as HTMLInputElement;
    this.seekTo(+target.value);
  }

  setPlaybackRate(rate: number): void {
    this.playbackRate = rate;
    if (this.currentAudio) {
      this.currentAudio.playbackRate = rate;
    }
  }

  onPlaybackRateChange(event: Event): void {
    const target = event.target as HTMLSelectElement;
    this.setPlaybackRate(+target.value);
  }

  setVolume(volume: number): void {
    this.volume = volume;
    if (this.currentAudio) {
      this.currentAudio.volume = volume;
    }
  }

  onVolumeChange(event: Event): void {
    const target = event.target as HTMLInputElement;
    this.setVolume(+target.value);
  }

  /**
   * Format time for display
   */
  formatTime(seconds: number): string {
    if (isNaN(seconds)) return '0:00';
    
    const minutes = Math.floor(seconds / 60);
    const remainingSeconds = Math.floor(seconds % 60);
    return `${minutes}:${remainingSeconds.toString().padStart(2, '0')}`;
  }



  /**
   * Stop currently playing podcast
   */
  stopPodcast(): void {
    if (this.currentAudio) {
      this.currentAudio.pause();
      this.currentAudio.removeEventListener('timeupdate', this.onTimeUpdate.bind(this));
      this.currentAudio.removeEventListener('loadedmetadata', this.onLoadedMetadata.bind(this));
      this.currentAudio.removeEventListener('ended', this.onAudioEnded.bind(this));
      this.currentAudio = null;
    }
    this.currentlyPlaying = null;
    this.isPlaying = false;
    this.currentTime = 0;
    this.duration = 0;
  }

  /**
   * Delete a podcast
   */
  deletePodcast(podcast: PodcastResponse): void {
    if (confirm(`Are you sure you want to delete "${podcast.podcastTagline || 'this podcast'}"?`)) {
      // Implement delete logic here
      console.log('Deleting podcast:', podcast.podcastId);
    }
  }

  /**
   * Get formatted duration for display
   */
  getFormattedDuration(podcast: PodcastResponse): string {
    // Since duration is not in the model, return a placeholder
    return 'placeholder';
  }

  /**
   * Get podcast creation date for display
   */
  getCreatedDate(podcast: PodcastResponse): string | null {
    // Since createdAt is not in the model, return a placeholder
    return podcast.createdAt || '';
  }

  /**
   * Check if podcast is currently processing
   */
  isProcessing(podcast: PodcastResponse): boolean {
    return podcast.status === 'PROCESSING';
  }

  /**
   * Check if podcast is completed and playable
   */
  isPlayable(podcast: PodcastResponse): boolean {
    return podcast.status === 'COMPLETED';
  }

  /**
   * Check if podcast failed
   */
  isFailed(podcast: PodcastResponse): boolean {
    return podcast.status === 'FAILED';
  }
}