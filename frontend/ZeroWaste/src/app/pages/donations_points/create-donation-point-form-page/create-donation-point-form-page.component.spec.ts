import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateDonationPointFormPageComponent } from './create-donation-point-form-page.component';

describe('CreateDonationPointFormPageComponent', () => {
  let component: CreateDonationPointFormPageComponent;
  let fixture: ComponentFixture<CreateDonationPointFormPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CreateDonationPointFormPageComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CreateDonationPointFormPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
