import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { NewsService } from '../services/news.service';
import { PodcastService } from '../services/podcast.service';
import { Articles } from '../models/Articles';
import { PodcastRequest } from '../models/PodcastRequest';
import { NewsDTO } from '../models/newsDTO';

@Component({
  selector: 'app-explore',
  imports: [CommonModule, FormsModule],
  templateUrl: './explore.component.html',
  styleUrl: './explore.component.css'
})
export class ExploreComponent implements OnInit {
  // Data
  allArticles: Articles[] = [];
  filteredArticles: Articles[] = [];
  paginatedArticles: Articles[] = [];
  
  // Filtering & Search
  searchTerm: string = '';
  selectedCategory: string = '';
  availableCategories: string[] = [];
  sortBy: 'newest' | 'oldest' | 'title' = 'newest';
  
  // Pagination
  currentPage: number = 1;
  articlesPerPage: number = 12;
  totalPages: number = 0;
  
  // Loading States
  isLoading: boolean = true;
  generatingPodcast: number | null = null;

  constructor(
    private newsService: NewsService,
    private podcastService: PodcastService
  ) { }

  ngOnInit(): void {
    this.loadAllArticles();
  }

  loadAllArticles() {
    this.isLoading = true;
    this.newsService.getAllTrendingNews().subscribe({
      next: (news: NewsDTO) => {
        this.allArticles =news.article ;
        this.filteredArticles = [...news.article];
        this.extractCategories();
        this.sortArticles();
        this.updatePagination();
        this.isLoading = false;
      },
      error: (error) => {
        console.error('Error loading articles:', error);
        this.isLoading = false;
      }
    });
  }

  extractCategories() {
    const categories = new Set<string[]>();
    this.allArticles.forEach(article => {
      if (article.category) {
        categories.add(article.category);
      }
    });
    this.availableCategories = Array.from(categories).sort().flat();
  }

  filterArticles() {
    this.filteredArticles = this.allArticles.filter(article => {
      // Search filter
      const matchesSearch = this.searchTerm === '' || 
        article.title.toLowerCase().includes(this.searchTerm.toLowerCase()) ||
        (article.description && article.description.toLowerCase().includes(this.searchTerm.toLowerCase()));
      
      // Category filter
      const matchesCategory = this.selectedCategory === '' || 
        article.category.flat().toString() === this.selectedCategory;
      
      return matchesSearch && matchesCategory;
    });

    this.sortArticles();
    this.currentPage = 1; // Reset to first page
    this.updatePagination();
  }

  sortArticles() {
    switch (this.sortBy) {
      case 'newest':
        this.filteredArticles.sort((a, b) => 
          new Date(b.pubDate || '').getTime() - new Date(a.pubDate || '').getTime()
        );
        break;
      case 'oldest':
        this.filteredArticles.sort((a, b) => 
          new Date(a.pubDate || '').getTime() - new Date(b.pubDate || '').getTime()
        );
        break;
      case 'title':
        this.filteredArticles.sort((a, b) => a.title.localeCompare(b.title));
        break;
    }
    this.updatePagination();
  }

  updatePagination() {
    this.totalPages = Math.ceil(this.filteredArticles.length / this.articlesPerPage);
    const startIndex = (this.currentPage - 1) * this.articlesPerPage;
    const endIndex = startIndex + this.articlesPerPage;
    this.paginatedArticles = this.filteredArticles.slice(startIndex, endIndex);
  }

  nextPage() {
    if (this.currentPage < this.totalPages) {
      this.currentPage++;
      this.updatePagination();
      this.scrollToTop();
    }
  }

  previousPage() {
    if (this.currentPage > 1) {
      this.currentPage--;
      this.updatePagination();
      this.scrollToTop();
    }
  }

  private scrollToTop() {
    window.scrollTo({ top: 0, behavior: 'smooth' });
  }

  generatePodcast(article: Articles, contentForm: 'SHORT' | 'LONG') {
    if (this.generatingPodcast) return;

    this.generatingPodcast = article.newsId;
    
    const podcastRequest: PodcastRequest = {
      email: 'user@example.com', // TODO: Get from authentication service
      newsId: article.newsId,
      contentForm: contentForm,
      podcastType: 'NEWS'
    };

    this.podcastService.createPodcastFromNews(podcastRequest).subscribe({
      next: (response) => {
        console.log('Podcast creation started:', response);
        this.generatingPodcast = null;
        
        // Start polling for podcast completion
        this.pollPodcastStatus(response.podcastId);
        
        // Show success message
        this.showPodcastCreationMessage(contentForm);
      },
      error: (error) => {
        console.error('Error creating podcast:', error);
        this.generatingPodcast = null;
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

  private showPodcastCreationMessage(type: 'SHORT' | 'LONG') {
    const duration = type === 'SHORT' ? '2-5 minute' : '7-20 minute';
    
    // Show a non-intrusive notification
    const notification = document.createElement('div');
    notification.className = 'fixed top-4 right-4 bg-indigo-600 text-white px-6 py-3 rounded-lg shadow-lg z-50 animate-slide-in max-w-sm';
    notification.innerHTML = `
      <div class="flex items-center gap-3">
        <svg class="w-5 h-5 flex-shrink-0" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 11a7 7 0 01-7 7m0 0a7 7 0 01-7-7m7 7v4m0 0H8m4 0h4m-4-8a3 3 0 01-3-3V5a3 3 0 116 0v6a3 3 0 01-3 3z"/>
        </svg>
        <div>
          <div class="font-semibold">${duration} podcast started!</div>
          <div class="text-sm opacity-90">We'll notify you when it's ready</div>
        </div>
      </div>
    `;
    document.body.appendChild(notification);
    
    setTimeout(() => {
      if (document.body.contains(notification)) {
        document.body.removeChild(notification);
      }
    }, 4000);
  }

  private showPodcastReadyMessage() {
    if (confirm('Your podcast is ready! Would you like to check it out in My Podcasts?')) {
      // Navigate to My Podcasts
      window.location.href = '/my-podcasts';
    }
  }

  openArticle(article: Articles) {
    window.open(article.link, '_blank');
  }

  formatDate(dateString: string | undefined): string {
    if (!dateString) return '';
    
    const date = new Date(dateString);
    const now = new Date();
    const diffInMinutes = Math.floor((now.getTime() - date.getTime()) / (1000 * 60));
    
    if (diffInMinutes < 60) {
      return `${diffInMinutes}m ago`;
    } else if (diffInMinutes < 1440) {
      return `${Math.floor(diffInMinutes / 60)}h ago`;
    } else {
      return `${Math.floor(diffInMinutes / 1440)}d ago`;
    }
  }

  trackByArticleId(index: number, article: Articles): number {
    return article.newsId;
  }
}