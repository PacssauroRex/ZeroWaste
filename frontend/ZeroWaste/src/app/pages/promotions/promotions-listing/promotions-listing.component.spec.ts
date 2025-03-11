import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PromotionsListingComponent } from './promotions-listing.component';

describe('PromotionsListingComponent', () => {
  let component: PromotionsListingComponent;
  let fixture: ComponentFixture<PromotionsListingComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PromotionsListingComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PromotionsListingComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
