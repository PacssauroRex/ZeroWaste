import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UpdateDonationPointPageComponent } from './update-donation-point-page.component';

describe('UpdateDonationPointPageComponent', () => {
  let component: UpdateDonationPointPageComponent;
  let fixture: ComponentFixture<UpdateDonationPointPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UpdateDonationPointPageComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UpdateDonationPointPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
