import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ListDonationPointPageComponent } from './list-donation-point-page.component';

describe('ListDonationPointPageComponent', () => {
  let component: ListDonationPointPageComponent;
  let fixture: ComponentFixture<ListDonationPointPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ListDonationPointPageComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ListDonationPointPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
