import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateDonationFormPageComponent } from './create-donation-form-page.component';

describe('CreateDonationFormPageComponent', () => {
  let component: CreateDonationFormPageComponent;
  let fixture: ComponentFixture<CreateDonationFormPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CreateDonationFormPageComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CreateDonationFormPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
