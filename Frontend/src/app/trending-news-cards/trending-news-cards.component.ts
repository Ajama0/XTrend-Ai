import { Component, OnInit, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { PodcastService } from '../services/podcast.service';
import { NewsService } from '../services/news.service';
import { Articles } from '../models/Articles';
import { PodcastRequest } from '../models/PodcastRequest';
import { NewsDTO } from '../models/newsDTO';

@Component({
  selector: 'app-trending-news-cards',
  imports: [CommonModule, RouterLink],
  templateUrl: './trending-news-cards.component.html',
  styleUrl: './trending-news-cards.component.css'
})
export class TrendingNewsCardsComponent implements OnInit {
  @Input() trendingArticles: Articles[] = [];
  
  currentIndex: number = 0;
  generatingPodcast: number | null = null;
  autoSlideInterval: any;
  isCreatingPodcast: boolean = false;

  constructor(
    private podcastService: PodcastService,
    private newsService: NewsService
  ) { }

  ngOnInit(): void {
    if (this.trendingArticles.length === 0) {
      this.loadTrendingNews();
    }
    this.startAutoSlide();
  }

  ngOnDestroy(): void {
    if (this.autoSlideInterval) {
      clearInterval(this.autoSlideInterval);
    }
  }

  loadTrendingNews() {
    this.newsService.getAllTrendingNews().subscribe({
      next: (news:NewsDTO) => {
        // Get first 10 articles for trending cards
        this.trendingArticles = news.article.slice(0, 10);
        console.log("i got the trending articles that are 10", this.trendingArticles)
      },
      error: (error:Error) => {
        console.error('Error loading trending news:', error);
      }
    });
  }

  startAutoSlide() {
    this.autoSlideInterval = setInterval(() => {
      if (this.currentIndex < this.trendingArticles.length - 1) {
        this.nextCard();
      } else {
        this.currentIndex = 0; // Loop back to first card
      }
    }, 5000); // Auto-slide every 5 seconds
  }

  nextCard() {
    if (this.currentIndex < this.trendingArticles.length - 1) {
      this.currentIndex++;
    }
  }

  previousCard() {
    if (this.currentIndex > 0) {
      this.currentIndex--;
    }
  }

  goToCard(index: number) {
    this.currentIndex = index;
  }

  generatePodcast(article: Articles) {
    if (this.generatingPodcast) return;

    if(this.isCreatingPodcast){
      return;
    }

    this.isCreatingPodcast = true;

    this.generatingPodcast = article.newsId;
    
    const podcastRequest: PodcastRequest = {
      email: 'user@example.com', // TODO: Get from authentication service
      newsId: article.newsId,
      contentForm: 'SHORT', // Default to mini podcast for trending news
      podcastType: 'NEWS'
    };

    this.podcastService.createPodcastFromNews(podcastRequest).subscribe({
      next: (response) => {
        console.log('Podcast creation started:', response);
        this.generatingPodcast = null;
        this.isCreatingPodcast = false;
        // Start polling for podcast completion
        this.pollPodcastStatus(response.podcastId);
        
        // Show success message
        this.showPodcastCreationMessage();
      },
      error: (error) => {
        console.error('Error creating podcast:', error);
        this.generatingPodcast = null;
        this.isCreatingPodcast = false;
        alert('Error creating podcast. Please try again.');
      }
    });
  }

  private pollPodcastStatus(podcastId: number) {
    const pollInterval = setInterval(() => {
      this.podcastService.getPodcastStatus(podcastId).subscribe({
        next: (response) => {
          if (response.status === 'COMPLETED') {
            clearInterval(pollInterval);
            this.showPodcastReadyMessage();
          } else if (response.status === 'FAILED') {
            clearInterval(pollInterval);
            alert('Podcast creation failed. Please try again.');
          }
        },
        error: (error) => {
          console.error('Error polling podcast status:', error);
          clearInterval(pollInterval);
        }
      });
    }, 3000); // Poll every 3 seconds
  }

  private showPodcastCreationMessage() {
    // Show a non-intrusive notification
    const notification = document.createElement('div');
    notification.className = 'fixed top-4 right-4 bg-indigo-600 text-white px-6 py-3 rounded-lg shadow-lg z-50 animate-fade-in';
    notification.innerHTML = `
      <div class="flex items-center gap-2">
        <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 11a7 7 0 01-7 7m0 0a7 7 0 01-7-7m7 7v4m0 0H8m4 0h4m-4-8a3 3 0 01-3-3V5a3 3 0 116 0v6a3 3 0 01-3 3z"/>
        </svg>
        <span>Podcast creation started!</span>
      </div>
    `;
    document.body.appendChild(notification);
    
    setTimeout(() => {
      document.body.removeChild(notification);
    }, 3000);
  }

  private showPodcastReadyMessage() {
    if (confirm('Your podcast is ready! Would you like to check it out in My Podcasts?')) {
      // Navigate to My Podcasts - router navigation would be handled by parent component
      window.location.href = '/my-podcasts';
    }
  }

  openArticle(article: Articles) {
    window.open(article.link, '_blank');
  }
}