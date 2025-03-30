import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DetailDonationPointPageComponent } from './detail-donation-point-page.component';

describe('DetailDonationPointPageComponent', () => {
  let component: DetailDonationPointPageComponent;
  let fixture: ComponentFixture<DetailDonationPointPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DetailDonationPointPageComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DetailDonationPointPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
