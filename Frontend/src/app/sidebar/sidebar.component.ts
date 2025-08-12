import { Component, OnInit } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-sidebar',
  imports: [RouterLink, RouterLinkActive, CommonModule],
  templateUrl: './sidebar.component.html',
  styleUrl: './sidebar.component.css'
})
export class SidebarComponent implements OnInit {
  podcastCount: number = 0;
  isMobileMenuOpen: boolean = false;

  constructor() { }

  ngOnInit(): void {
    // TODO: Fetch user's podcast count from service
    this.loadPodcastCount();
  }

  toggleMobileMenu() {
    this.isMobileMenuOpen = !this.isMobileMenuOpen;
  }

  private loadPodcastCount() {
    // TODO: Implement service call to get user's podcast count
    // For now, mock data
    this.podcastCount = 0;
  }
}