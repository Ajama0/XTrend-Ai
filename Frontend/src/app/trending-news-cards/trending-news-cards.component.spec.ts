import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TrendingNewsCardsComponent } from './trending-news-cards.component';

describe('TrendingNewsCardsComponent', () => {
  let component: TrendingNewsCardsComponent;
  let fixture: ComponentFixture<TrendingNewsCardsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TrendingNewsCardsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TrendingNewsCardsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
