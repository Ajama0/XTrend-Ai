import { TestBed } from '@angular/core/testing';

import { VideogenService } from './videogen.service';

describe('VideogenService', () => {
  let service: VideogenService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(VideogenService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
