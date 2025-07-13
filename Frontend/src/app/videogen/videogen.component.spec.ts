import { ComponentFixture, TestBed } from '@angular/core/testing';

import { VideogenComponent } from './videogen.component';

describe('VideogenComponent', () => {
  let component: VideogenComponent;
  let fixture: ComponentFixture<VideogenComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [VideogenComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(VideogenComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
