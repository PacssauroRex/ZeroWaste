import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ListDonationPageComponent } from './list-donation-page.component';

describe('ListDonationPageComponent', () => {
  let component: ListDonationPageComponent;
  let fixture: ComponentFixture<ListDonationPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ListDonationPageComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ListDonationPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
