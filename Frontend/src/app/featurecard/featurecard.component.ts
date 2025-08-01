import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-featurecard',
  imports: [CommonModule],
  templateUrl: './featurecard.component.html',
  styleUrl: './featurecard.component.css'
})
export class FeaturecardComponent {


  @Input() title!: string;
  @Input() description!: string;
  @Input() image?: string; 
  @Input() badgeText?: string; 

}
