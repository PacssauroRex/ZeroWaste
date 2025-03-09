import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UpdateProductFormPageComponent } from './update-product-form-page.component';

describe('UpdateProductFormPageComponent', () => {
  let component: UpdateProductFormPageComponent;
  let fixture: ComponentFixture<UpdateProductFormPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UpdateProductFormPageComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UpdateProductFormPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
