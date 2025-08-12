import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { PodcastService } from '../services/podcast.service';
import { PodcastRequest } from '../models/PodcastRequest';
import { PodcastResponse } from '../models/PodcastResponse';

type ContentType = 'website' | 'pdf' | 'ai-summarizer';
type PodcastStyle = 'mini' | 'deep-dive';

@Component({
  selector: 'app-hero-section',
  imports: [CommonModule, FormsModule],
  templateUrl: './herosection.component.html',
  styleUrl: './herosection.component.css'
})
export class HerosectionComponent {
  selectedContentType: ContentType = 'ai-summarizer';
  selectedPodcastStyle: PodcastStyle = 'mini';
  inputValue: string = '';
  selectedFile: File | null = null;
  isDragOver: boolean = false;
  isCreating: boolean = false;

  constructor(private podcastService: PodcastService) {}

  contentTypes = [
    {
      id: 'website' as ContentType,
      name: 'Website',
      icon: '🌐',
      placeholder: 'Enter a URL here...'
    },
    {
      id: 'pdf' as ContentType,
      name: 'PDF',
      icon: '📄',
      placeholder: 'Upload your PDF file'
    },
    {
      id: 'ai-summarizer' as ContentType,
      name: 'AI Summarizer',
      icon: '🤖',
      placeholder: 'Create podcasts from text or any topic...'
    }
  ];

  podcastStyles = [
    {
      id: 'mini' as PodcastStyle,
      name: 'Mini Podcast',
      duration: '2-5 minute podcast',
      description: 'Quick insights and key points'
    },
    {
      id: 'deep-dive' as PodcastStyle,
      name: 'Deep Dive',
      duration: '7-20 minute podcast',
      description: 'Comprehensive analysis and discussion'
    }
  ];

  selectContentType(type: ContentType) {
    this.selectedContentType = type;
    this.inputValue = '';
    this.selectedFile = null;
  }

  // Computed flag used to enable/disable the Create button without showing alerts
  get isReadyToCreate(): boolean {
    switch (this.selectedContentType) {
      case 'website':
        return this.inputValue.trim() !== '' && this.toHttpUrl(this.inputValue) !== null;
      case 'pdf':
        return this.selectedFile !== null;
      case 'ai-summarizer':
        return this.inputValue.trim() !== '' && this.inputValue.trim().length >= 200;
      default:
        return false;
    }
  }

  selectPodcastStyle(style: PodcastStyle) {
    this.selectedPodcastStyle = style;
  }

  getCurrentPlaceholder(): string {
    const contentType = this.contentTypes.find(ct => ct.id === this.selectedContentType);
    return contentType?.placeholder || '';
  }

  onFileSelected(event: any) {
    const file = event.target.files[0];
    if (file && file.type === 'application/pdf') {
      this.selectedFile = file;
    }
  }

  onDragOver(event: DragEvent) {
    event.preventDefault();
    this.isDragOver = true;
  }

  onDragLeave(event: DragEvent) {
    event.preventDefault();
    this.isDragOver = false;
  }

  onDrop(event: DragEvent) {
    event.preventDefault();
    this.isDragOver = false;
    
    const files = event.dataTransfer?.files;
    if (files && files.length > 0) {
      const file = files[0];
      if (file.type === 'application/pdf') {
        this.selectedFile = file;
      }
    }
  }

  onCreatePodcast() {
    if (this.isCreating) return;

    // Validate input based on content type
    if (!this.validateInput()) {
      alert('Please provide valid input for the selected content type.');
      return;
    }

    this.isCreating = true;

   
    if(this.selectedContentType == "pdf"){
      const formDataRequest = this.buildFormdata();
  
    this.podcastService.createPodcastFromPdf(formDataRequest).subscribe({
      next: (response: PodcastResponse) => {
        console.log('Podcast creation started:', response);
        this.isCreating = false;
        alert('Podcast creation started! Check your email for updates.');
      },
      error: (error: any) => {
        console.error('Error creating podcast:', error);
        this.isCreating = false;
        alert('Error creating podcast. Please try again.');
      }
    });
  } else if(this.selectedContentType == "website" || this.selectedContentType == "ai-summarizer"){
    const [contentForm, podcastType] = this.setContentFormAndPodcastType();
    
    // Build different JSON structure based on content type
    const podcastRequest: PodcastRequest = {
      email: 'harry@example.com', // TODO: get the email from the user
      contentForm: contentForm,
      podcastType: podcastType
    };

    // Add content-specific field
    if (this.selectedContentType === "website") {
      podcastRequest.url = this.toHttpUrl(this.inputValue) || ''
      ;
    } else if (this.selectedContentType === "ai-summarizer") {
      podcastRequest.text = this.inputValue;
    }

    console.log('Sending request:', podcastRequest);

    this.podcastService.createPodcastFromInput(podcastRequest).subscribe({
      next: (response: PodcastResponse) => {
        console.log('Podcast creation started:', response);
        this.isCreating = false;
        alert('Podcast creation started! Check your email for updates.');
      },
      error: (error: any) => {
        console.error('Error creating podcast:', error);
        this.isCreating = false;
        alert('Error creating podcast. Please try again.');
      }
    });
  }
}

  private validateInput(): boolean {
    switch (this.selectedContentType) {
      case 'website':
        return this.inputValue.trim() !== '' && this.toHttpUrl(this.inputValue) !== null;
      case 'pdf':
        return this.selectedFile !== null;
      case 'ai-summarizer':
        return this.inputValue.trim() !== '' && this.inputValue.trim().length > 200;;
      default:
        return false;
    }
  }

  private toHttpUrl(input: string): string | null {
    const trimmed = input.trim();
    const candidate = /^https?:\/\//i.test(trimmed) ? trimmed : `https://${trimmed}`;
    try {
      const url = new URL(candidate);
      if (!['http:', 'https:'].includes(url.protocol)) return null;
      if (!url.hostname) return null; // guard against bare schemes
      return url.toString(); // normalized URL (with scheme)
    } catch {
      return null;
    }
  }




  private buildFormdata(): FormData {
    const formDataRequest: FormData = new FormData();
    const [contentForm, podcastType] = this.setContentFormAndPodcastType();
    
    const request: PodcastRequest = {
      email: 'harry@example.com', // TODO: get the email from the user
      contentForm: contentForm,
      podcastType: podcastType
    };
    
    formDataRequest.append(
      'request',
      new Blob([JSON.stringify(request)], { type: 'application/json' })
    );
    
    if (this.selectedFile) {
      formDataRequest.append('file', this.selectedFile);
    }
    
    return formDataRequest;
  }

  private setContentFormAndPodcastType(): [string, string] {
    let contentForm: string = "";
    if (this.selectedPodcastStyle === "mini") {
      contentForm = "SHORT";
    } else {
      contentForm = "LONG";
    }
      
    let podcastType: string = "";
    if (this.selectedContentType === "website" || this.selectedContentType === "ai-summarizer") {
      podcastType = "INPUT";
    } else {
      podcastType = "FILE";
    }
    
    return [contentForm, podcastType];
  }
}
