import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateProductFormPageComponent } from './create-product-form-page.component';

describe('CreateProductFormPageComponent', () => {
  let component: CreateProductFormPageComponent;
  let fixture: ComponentFixture<CreateProductFormPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CreateProductFormPageComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CreateProductFormPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
