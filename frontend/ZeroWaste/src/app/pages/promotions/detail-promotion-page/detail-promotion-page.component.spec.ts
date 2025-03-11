import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DetailPromotionPageComponent } from './detail-promotion-page.component';

describe('DetailPromotionPageComponent', () => {
  let component: DetailPromotionPageComponent;
  let fixture: ComponentFixture<DetailPromotionPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DetailPromotionPageComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DetailPromotionPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
