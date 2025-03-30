import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UpdateDonationPageComponent } from './update-donation-page.component';

describe('UpdateDonationPageComponent', () => {
  let component: UpdateDonationPageComponent;
  let fixture: ComponentFixture<UpdateDonationPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UpdateDonationPageComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UpdateDonationPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
