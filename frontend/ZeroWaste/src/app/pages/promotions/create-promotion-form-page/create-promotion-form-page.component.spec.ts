import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreatePromotionFormPageComponent } from './create-promotion-form-page.component';

describe('CreatePromotionFormPageComponent', () => {
  let component: CreatePromotionFormPageComponent;
  let fixture: ComponentFixture<CreatePromotionFormPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CreatePromotionFormPageComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CreatePromotionFormPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
