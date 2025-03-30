import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DetailDonationPageComponent } from './detail-donation-page.component';

describe('DetailDonationPageComponent', () => {
  let component: DetailDonationPageComponent;
  let fixture: ComponentFixture<DetailDonationPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DetailDonationPageComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DetailDonationPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
